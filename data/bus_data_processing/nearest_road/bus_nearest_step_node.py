import json
import time
import threading

# `step_node.json` 데이터와 `bus_node.json` 데이터 간의 차이가 가장 적은 노드를 찾습니다.


def find_nearest_step_node(bus_node, step_data):
    nearest_step_node = None
    min_difference = float('inf')

    bus_lat = bus_node['position']['latitude']
    bus_lon = bus_node['position']['longitude']

    for step_node in step_data:
        step_lat = step_node['position']['latitude']
        step_lon = step_node['position']['longitude']

        # 두 점 사이의 차이 계산
        lat_difference = abs(step_lat - bus_lat)
        lon_difference = abs(step_lon - bus_lon)
        total_difference = lat_difference + lon_difference

        if total_difference < min_difference:
            min_difference = total_difference
            nearest_step_node = step_node

    return nearest_step_node


# # `transfor.json` 파일에 저장할 데이터 생성
# transfor_data = []

# for i, bus_node in enumerate(bus_data):
#     nearest_step_node = find_nearest_step_node(bus_node, step_data)
#     bus_id = bus_node['id']
#     transfor_data.append({
#         'bus_id': bus_id,
#         'nearest_step_node': nearest_step_node
#     })

# print('Processing completed.')  # 처리 완료 메시지 출력


def process_data(transfor_data, bus_node, step_data, lock):
    # 여기에 데이터 조각을 처리하는 로직을 작성합니다.
    nearest_step_node = find_nearest_step_node(bus_node, step_data)
    bus_id = bus_node['id']

    with lock:
        transfor_data.append({
            'bus_id': bus_id,
            'nearest_step_node': nearest_step_node
        })


def print_data(transfor_data, length, lock):
    while (True):
        with lock:
            if (len(transfor_data) >= length):
                break
            print(len(transfor_data) / length)
            time.sleep(1000 / 1000)


def main():
    thread_count = 4
    # `bus_node.json` 파일을 읽어옵니다. 인코딩을 'utf-8'로 지정합니다.
    with open('data/bus_stop_node_with_transfer.json', 'r', encoding='utf-8') as bus_node_file:
        bus_data = json.load(bus_node_file)

    # `step_node.json` 파일을 읽어옵니다. 인코딩을 'utf-8'로 지정합니다.
    with open('data/step_node.json', 'r', encoding='utf-8') as step_node_file:
        step_data = json.load(step_node_file)
    # 데이터를 스레드 수에 맞게 분할

    transfor_data = []
    chunk_size = len(bus_data) // thread_count
    threads = []

    for i in range(thread_count):
        # 데이터 조각을 만듭니다.
        start = i * chunk_size
        end = None if i+1 == thread_count else (i+1) * chunk_size
        data_slice = bus_data[start:end]
        lock = threading.Lock()  # Lock 객체 생성

        # 스레드 생성 및 시작
        thread = threading.Thread(target=process_data,
                                  args=(transfor_data, data_slice, step_data, lock))
        thread.start()
        threads.append(thread)

    print_thread = threading.Thread(
        print_data, args=(transfor_data, len(bus_data), lock))
    threads.append(print_data)

    # 모든 스레드가 완료될 때까지 기다립니다.
    for thread in threads:
        thread.join()

    with open('result1.json', 'w', encoding='utf-8') as transfor_file:
        json.dump(transfor_data, transfor_file, indent=2)


if __name__ == "__main__":
    main()
