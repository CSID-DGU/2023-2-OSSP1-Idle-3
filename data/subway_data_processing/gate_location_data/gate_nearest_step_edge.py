import json
from geopy.distance import geodesic

with open('step_nearest_list.json', 'r', encoding='utf-8') as transfor_file:
    transfor_data = json.load(transfor_file)

with open('gate_location_in_seoul_ver2.json', 'r', encoding='utf-8') as gate_node_file:
    gate_data = json.load(gate_node_file)

with open('step_node.json', 'r', encoding='utf-8') as step_node_file:
    step_data = json.load(step_node_file)

result_data = []

total_items = len(transfor_data)
for i, transfor_item in enumerate(transfor_data):
    subway_id = transfor_item['subway_gate_id']
    step_node = transfor_item['nearest_step_node']

    gate_node = next(
        (node for node in gate_data if node['id'] == subway_id), None)
    if gate_node and step_node:
        gate_location = (
            gate_node['latitude'], gate_node['longitude'])
        step_location = (step_node['position']['latitude'],
                         step_node['position']['longitude'])

        # 두 지점 간 거리 계산 (미터로 변환)
        distance = geodesic(gate_location, step_location).meters

        # 거리를 이동 시간으로 변환 (초당 1.29m로 가정)
        speed_m_per_sec = 1.29
        travel_time = distance / speed_m_per_sec

        travel_time = round(travel_time, 4)

        # cost가 600 미만인 경우만 결과 데이터 생성
        if travel_time < 600:
            # 양방향 추가
            result_data.append({
                'start': subway_id,
                'end': step_node['id'],
                'cost': travel_time
            })
            result_data.append({
                'start': step_node['id'],
                'end': subway_id,
                'cost': travel_time
            })

    print(f'Processing item {i + 1}/{total_items}', end='\r')

with open('gate_nearest_step_edge.json', 'w', encoding='utf-8') as result_file:
    json.dump(result_data, result_file, indent=2)

print('Processing completed.')
