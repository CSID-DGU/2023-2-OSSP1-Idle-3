import json

# `bus_node.json` 파일을 읽어옵니다. 인코딩을 'utf-8'로 지정합니다.
with open('bus_stop_node.json', 'r', encoding='utf-8') as bus_node_file:
    bus_data = json.load(bus_node_file)

# `step_node.json` 파일을 읽어옵니다. 인코딩을 'utf-8'로 지정합니다.
with open('step_node.json', 'r', encoding='utf-8') as step_node_file:
    step_data = json.load(step_node_file)

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


# `transfor.json` 파일에 저장할 데이터 생성
transfor_data = []

for i, bus_node in enumerate(bus_data):
    nearest_step_node = find_nearest_step_node(bus_node, step_data)
    bus_id = bus_node['id']
    transfor_data.append({
        'bus_id': bus_id,
        'nearest_step_node': nearest_step_node
    })

    # 진행 상황 출력
    print(f'Processing bus_node {i + 1}/{len(bus_data)}', end='\r')

with open('result1.json', 'w', encoding='utf-8') as transfor_file:
    json.dump(transfor_data, transfor_file, indent=2)

print('Processing completed.')  # 처리 완료 메시지 출력
