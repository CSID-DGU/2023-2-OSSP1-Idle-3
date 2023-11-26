'''
지하철 출입구와 가장 가까운 도보노드 구하기

  {
    "subway_gate_id": 70001, // 지하철 id
    "nearest_step_node": { 
      "id": 1062558,// 가장 가까운 도보노드 id
      "crosswalk": 0,
      "position": { // 위치
        "latitude": 37.57782764054916,
        "longitude": 126.79591348830094
      }
    }
  }

* py 실행이 늦어서 코랩에서 다시 실행하였음 *

최종 생성 파일명 : step_nearest_list.json
'''

import json
from haversine import haversine

# 위경도 거리 계산 함수


def calculate_distance(coord1, coord2):
    return haversine(coord1, coord2)

# 가장 가까운 값을 찾는 함수


def find_nearest_step_node(gate, step_nodes):
    gate_coords = (gate["latitude"], gate["longitude"])
    nearest_node = min(step_nodes, key=lambda x: calculate_distance(
        gate_coords, (x["position"]["latitude"], x["position"]["longitude"])))
    return nearest_node


# 파일 읽기
with open("gate_location_in_seoul_ver2.json", "r", encoding="utf-8") as f:
    gate_data = json.load(f)

with open("step_node.json", "r", encoding="utf-8") as f:
    step_node_data = json.load(f)

# 모든 게이트에 대해 가장 가까운 노드 찾기
total_gates = len(gate_data)
print(f"Processing {total_gates} gates:")
nearest_list = []
for i, gate in enumerate(gate_data, start=1):
    nearest_node = find_nearest_step_node(gate, step_node_data)

    # 생성할 JSON 데이터 구성
    data = {
        "subway_gate_id": gate["id"],
        "nearest_step_node": {
            "id": nearest_node["id"],
            "crosswalk": nearest_node["crosswalk"],
            "position": {
                "latitude": nearest_node["position"]["latitude"],
                "longitude": nearest_node["position"]["longitude"]
            }
        }
    }

    nearest_list.append(data)

    # 진행 상황 출력
    print(f"\rProgress: {i}/{total_gates}", end="", flush=True)

# 결과를 파일에 쓰기
with open("step_nearest_list.json", "w", encoding="utf-8") as f:
    json.dump(nearest_list, f, ensure_ascii=False, indent=2)

print("\nProcessing complete. Check 'step_nearest_list.json' for the results.")
