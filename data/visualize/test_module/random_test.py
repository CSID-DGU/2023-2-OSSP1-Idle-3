# 랜덤 노드를 선택하여 서버로부터 더 나은 지점의 개수를 받음
# 테스트 1000번을 진행하여 개수가 가장 작은 케이스가 best case, 가장 큰 경우가 worst case


import json
import random
import requests
import folium

def getRandomIds():
  with open("step_node.json", "r", encoding="UTF-8") as file:
    nodes = json.load(file)
  nodeIds = []
  for node in nodes:
    nodeIds.append(node["id"])

  print("사용자 수를 입력하세요: ")
  n = int(input())
  randomIds = []

  for _ in range(n):
    randId = 0
    while (randId not in nodeIds and randId in randomIds) or randId == 0:
      randId = random.randrange(900001, 1115570)
    randomIds.append(randId)
  
  # 시작 노드 좌표 설정
  randNodes = []
  for node in nodes:
    for i in range(len(randomIds)):
      if node["id"] == randomIds[i]:
        randNodes[i] = {
          "id": randomIds[i],
          "position":{
            "latitude": node["position"]["latitude"],
            "longitude": node["position"]["longitude"]
          }
        }

  return randomIds



# SEND REQUEST
def postPosition(randomIds):

  #url
  url = "https://b98e-218-48-52-82.ngrok.io"
  # header
  header = {
    "Content-Type": "application/json"
  }
  # body
  requestBody = {
    "randomIds" : randomIds
  }
  # response
  response = requests.post(url, data = json.dumps(requestBody), headers = header)
  print("response status:", response.status_code)
  return response.json()


def visualize(ps, map, type):
  for p in ps:
    position = []
    position.append(p["position"]["latitude"])
    position.append(p["position"]["longitude"])

    if type == "missing":
      normGap = p["norm_gap"]
      normSum = p["norm_sum"]
    # sum 정규화한 값을 radius(반지름)에 반영
    # gap 정규화한 값을 fill_opacity(불투명도)에 반영
      folium.Circle(position, radius=100*normSum, fill_opacity=100*normGap).add_to(map) 
    else:
      folium.Circle(position).add_to(map)



# =================================================#
# ====================== MAIN =====================#
# =================================================#
worst = {
  "count": 0
}
best = {
  "count": 1000000
}

# 테스트 케이스별 시작지점과 도착 지점 저장
datas = []

for j in range(1000):
  randomIds = getRandomIds()

  # SEND REQUEST
  response = postPosition(randomIds)

  missing_points = response["missing_points"]
  count = len(missing_points)
  answer = response["answer"]
  
  # 정규화
  normGaps = []
  normSums = []
  for mp in missing_points:
    normGaps.append(mp["gap_difference"])
    normSums.append(mp["sum_difference"])
  max_gap = max(normGaps)
  max_sum = max(normSums)
  for i in len(missing_points):
    normGaps[i] = normGaps[i]/max_gap
    normSums[i] = normSums[i]/max_sum
  for i in len(missing_points):
    missing_points[i]["norm_gap"] = normGaps[i]
    missing_points[i]["norm_sum"] = normSums[i]

  # response data 가공
  data = {
    "test_id": j,
    "start_nodes": randomIds, 
    "answer_node": answer,
    "missing_points": missing_points,
    "count": count
  }
  datas.append(data)


  # worst 갱신
  if count > worst["count"]:
    worst = data
  # best 갱신
  if count < best["count"]:
    best = data

print("worst case:", worst)
print("best case:", best)

# 시각화
for i in len(datas):
  map = folium.Map(location = [37.544129, 127.054357],zoom_start = 14)
  start_nodes = datas[i]["start_nodes"]
  visualize(start_nodes, map)
  answer_node = datas[i]["answer"]
  visualize(answer_node, map)
  missing_points = datas[i]["missing_points"]
  visualize(missing_points, map)
  map.save("test", i, ".html")


# 좌표, 편차의 차이, 이동시간의 차이
# 차이에 따라서 다르게 지도에 표현
# position, gap, sum, missing points([poisiton(latitude, longitude), gap, sum])

# 시작노드: 검은색, 중간지점: 초록색
# sum_difference는 원의 크기
# gap_difference는 불투명도