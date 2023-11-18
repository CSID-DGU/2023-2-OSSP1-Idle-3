import json
from geopy.distance import geodesic

with open('result1.json', 'r', encoding='utf-8') as transfor_file:
    transfor_data = json.load(transfor_file)

# `bus_node.json` 파일을 읽어옵니다. 인코딩을 'utf-8'로 지정합니다.
with open('data/bus_stop_node_with_transfer.json', 'r', encoding='utf-8') as bus_node_file:
    bus_data = json.load(bus_node_file)

# `step_node.json` 파일을 읽어옵니다. 인코딩을 'utf-8'로 지정합니다.
with open('data/step_node.json', 'r', encoding='utf-8') as step_node_file:
    step_data = json.load(step_node_file)

result_data = []

total_items = len(transfor_data)
for i, transfor_item in enumerate(transfor_data):
    bus_id = transfor_item['bus_id']
    step_node = transfor_item['nearest_step_node']

    bus_node = next(
        (node for node in bus_data if node['id'] == bus_id), None)
    if bus_node and step_node:
        bus_location = (
            bus_node['position']['latitude'], bus_node['position']['longitude'])
        step_location = (step_node['position']['latitude'],
                         step_node['position']['longitude'])

        # 두 지점 간 거리 계산 (미터로 변환)
        distance = geodesic(bus_location, step_location).meters

        # 결과 데이터 생성
        result_data.append({
            'bus_id': bus_id,
            'step_id': step_node['id'],
            'distance': distance
        })

    # 진행 상황 출력
    print(f'Processing item {i + 1}/{total_items}', end='\r')

with open('result2.json', 'w', encoding='utf-8') as result_file:
    json.dump(result_data, result_file, indent=2)

print('Processing completed.')  # 처리 완료 메시지 출력
