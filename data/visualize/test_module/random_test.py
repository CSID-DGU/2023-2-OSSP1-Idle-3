# 랜덤 노드를 선택하여 서버로부터 더 나은 지점의 개수를 받음
# 테스트 1000번을 진행하여 개수가 가장 작은 케이스가 best case, 가장 큰 경우가 worst case


import json
import random
import requests
import folium
import time

def getRandNodes(n):
  with open("step_node.json", "r", encoding="UTF-8") as file:
    nodes = json.load(file)
  nodeIds = []
  for node in nodes:
    nodeIds.append(node["id"])

  randomIds = []

  # 시작 노드 랜덤 선택
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
        randNodes.append({
          "id": randomIds[i],
          "latitude": node["position"]["latitude"],
          "longitude": node["position"]["longitude"]
        })
  # 시작 노드 예시
  # randNodes = [
  #   {
  #     "latitude": 37.525461,
  #     "longitude": 126.887562
  #   },
  #   {
  #     "latitude": 37.545322,
  #     "longitude": 127.055797
  #   },
  #   {
  #     "latitude": 37.514366,
  #     "longitude": 127.110661
  #   }]
  return randNodes



# SEND REQUEST
def postPosition(randomIds):

  #url
  url = " {ngrok 주소}"
  # header
  header = {
    "Content-Type": "application/json"
  }
  # body
  requestBody = randomIds
  # response
  response = requests.post(url, data = json.dumps(requestBody), headers = header)
  print("response status:", response.status_code)
  return response.json()


def visualize(ps, map, type):
  if type == "answer":
    position = []
    position.append(ps["position"]["latitude"])
    position.append(ps["position"]["longitude"])
    popupstr = str(position)+"gap: "+ str(ps["gap"])
    folium.Circle(position, color="green", radius=300, popup=popupstr).add_to(map)
    return
  for p in ps:
    position = []
    position.append(p["position"]["latitude"])
    position.append(p["position"]["longitude"])

    if type == "missing":
      normGap = p["norm_gap"]
      normSum = p["norm_sum"]
    # sum 정규화한 값을 radius(반지름)에 반영
    # gap 정규화한 값을 fill_opacity(불투명도)에 반영
      popupstr = str(position) + "gapdiff: " + str(p["gapDifference"])
      folium.Circle(position, radius=100*normSum, fill_opacity=100*normGap, color="red", fill="red",popup=popupstr).add_to(map)
      return
    elif type == "start":
      folium.Circle(position, color = "black", fill="black", radius=300, popup=position).add_to(map)



# =================================================#
# ====================== MAIN =====================#
# =================================================#
TESTCASE = 1
worst = {
  "count": 0
}
best = {
  "count": 1000000
}

# 테스트 케이스별 시작지점과 도착 지점 저장
datas = []

print("사용자 수를 입력하세요: ")
n = int(input())

for j in range(TESTCASE):
  randNodes = getRandNodes(n)

  # SEND REQUEST
  response = postPosition(randNodes)
  time.sleep(1.5)
  
  missing_points = response["missingPoints"]
  count = len(missing_points)
  answer = response["answer"]
  
  # 정규화
  normGaps = []
  normSums = []
  if len(missing_points) == 0:
    print("missing point가 없습니다.")
    continue
  for mp in missing_points:
    normGaps.append(mp["gapDifference"])
    normSums.append(mp["sumDifference"])
  # print("normGaps: ", normGaps)
  # print("normSums: ", normSums)
  max_gap = max(normGaps)
  max_sum = max(normSums)
  for i in range(len(missing_points)):
    normGaps[i] = normGaps[i]/max_gap
    normSums[i] = normSums[i]/max_sum
  for i in range(len(missing_points)):
    missing_points[i]["norm_gap"] = normGaps[i]
    missing_points[i]["norm_sum"] = normSums[i]

  # response data 가공
  data = {
    "test_id": j,
    "start_nodes": randNodes, 
    "answer_node": answer,
    "missing_points": missing_points,
    "count": count
  }

  map = folium.Map(location = [37.544129, 127.054357],zoom_start = 14)
  start_nodes = []
  for node in randNodes:
    start_nodes.append({
      "position":{
        "latitude": node["latitude"],
        "longitude": node["longitude"]
      }
    })
  visualize(start_nodes, map, "start")
  answer_node = answer
  visualize(answer_node, map, "answer")
  missing_points = missing_points
  visualize(missing_points, map, "missing")
  title = "maps/"+str(n)+"persons/"+"test"+str(j)+".html"
  map.save(title)

  datas.append(data)

  # worst 갱신
  if count > worst["count"]:
    worst = data
  # best 갱신
  if count < best["count"]:
    best = data

print("worst case:", worst)
print("best case:", best)
with open("maps/"+str(n)+"persons/result.json", 'w', encoding = 'utf-8') as json_file:
  json.dump(datas, json_file)
# 좌표, 편차의 차이, 이동시간의 차이
# 차이에 따라서 다르게 지도에 표현
# position, gap, sum, missing points([poisiton(latitude, longitude), gap, sum])

# 시작노드: 검은색, 중간지점: 초록색
# sumDifference 원의 크기
# gapDifference 불투명도