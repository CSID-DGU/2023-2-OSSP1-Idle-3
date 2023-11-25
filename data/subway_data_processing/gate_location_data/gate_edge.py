"""
플랫폼 - 출입구 edge 데이터 (양방향 edge로 설정)

  {
    "start": 70001, // 출입구 id
    "end": 4208, // 플랫폼 id
    "cost": 16.8602, // 초당 1.29m 이동시간
    "type": "SUBWAY", // 지하철
    "name": "계양", // 역 이름
    "gate": "1" // 출입구 번호
  },

최종 생성 파일명 : gate_edge.json
"""

import json
from geopy.distance import geodesic

# 파일 경로 수정 필요
gate_location_filename = 'gate_location_in_seoul_ver2.json'
subway_node_filename = 'subway_node.json'
output_filename = 'gate_edge.json'

with open(gate_location_filename, 'r', encoding='utf-8') as gate_file:
    gate_data = json.load(gate_file)

with open(subway_node_filename, 'r', encoding='utf-8') as node_file:
    node_data = json.load(node_file)

# 지점 간 거리에 대한 이동 시간 계산 (초당 1.29m 로 계산)


def calculate_travel_time(coord1, coord2, speed=1.29):
    distance = geodesic(coord1, coord2).meters
    time_seconds = distance / speed
    return round(time_seconds, 4)


# 변수 설정 및 edge 데이터 생성 (양방향)
edge_data = []
for gate_item in gate_data:
    for node_item in node_data:
        if gate_item['name'] == node_item['name']:
            edge_AB = {
                'start': gate_item['id'],
                'end': node_item['id'],
                'cost': calculate_travel_time((gate_item['latitude'], gate_item['longitude']),
                                              (node_item['position']['latitude'], node_item['position']['longitude'])),
                'type': 'SUBWAY',
                'name': gate_item['name'],
                'gate': gate_item['gate']
            }
            edge_BA = {
                'start': node_item['id'],
                'end': gate_item['id'],
                'cost': calculate_travel_time((node_item['position']['latitude'], node_item['position']['longitude']),
                                              (gate_item['latitude'], gate_item['longitude'])),
                'type': 'SUBWAY',
                'name': gate_item['name'],
                'gate': gate_item['gate']
            }
            edge_data.extend([edge_AB, edge_BA])

with open(output_filename, 'w', encoding='utf-8') as output_file:
    json.dump(edge_data, output_file, ensure_ascii=False, indent=2)

print(f'Edge data saved to {output_filename}')
