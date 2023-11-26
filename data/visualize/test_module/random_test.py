# 랜덤 노드를 선택하여 서버로부터 더 나은 지점의 개수를 받음
# 테스트 1000번을 진행하여 개수가 가장 작은 케이스가 best case, 가장 큰 경우가 worst case


import json
import random
import requests
import folium
import time
import os

# def getRandNodes(n):
#   with open("step_node.json", "r", encoding="UTF-8") as file:
#     nodes = json.load(file)
#   nodeIds = []
#   for node in nodes:
#     nodeIds.append(node["id"])

#   randomIds = []

#   # 시작 노드 랜덤 선택
#   for _ in range(n):
#     randId = 0
#     while (randId not in nodeIds and randId in randomIds) or randId == 0:
#       randId = random.randrange(900001, 1115570)
#     randomIds.append(randId)
  
#   # 시작 노드 좌표 설정
#   randNodes = []
#   for node in nodes:
#     for i in range(len(randomIds)):
#       if node["id"] == randomIds[i]:
#         randNodes.append({
#           "id": randomIds[i],
#           "latitude": node["position"]["latitude"],
#           "longitude": node["position"]["longitude"]
#         })
#   # 시작 노드 예시
#   return randNodes


# SEND REQUEST
def postPosition(randomIds):

  #url
  url = "https://fb86-210-94-220-228.ngrok-free.app/middleSpace/testTimeCenter"
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
    folium.Circle(position, color="green", radius=200, popup=popupstr).add_to(map)
    return
  for p in ps:
    if type == "missing":
      position = []
      position.append(p["position"]["latitude"])
      position.append(p["position"]["longitude"])
      normGap = p["norm_gap"]
      normSum = p["norm_sum"]
    # sum 정규화한 값을 radius(반지름)에 반영
    # gap 정규화한 값을 fill_opacity(불투명도)에 반영
      popupstr = str(position) + "gapdiff: " + str(p["gapDifference"])
      folium.Circle(position, radius=100*normSum, fill_opacity=100*normGap, color="red", fill="red",popup=popupstr).add_to(map)
      return
    elif type == "start":
      position = []
      position.append(p["latitude"])
      position.append(p["longitude"])
      folium.Circle(position, color = "black", fill="black", radius=200, popup=position).add_to(map)



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

# print("사용자 수를 입력하세요: ")
# n = int(input())
n = 6

for j in range(TESTCASE):
  # randNodes = getRandNodes(n)

  # SEND REQUEST
  
  start_nodes = [
    {"latitude": 37.51441651126843, "longitude": 127.04199825118819},
    {"latitude": 37.49322813001188, "longitude": 127.03149261531696},
    {"latitude": 37.48657972901505, "longitude": 127.0707475116464},
    {"latitude": 37.50687804281888, "longitude": 127.1082253695799},]
  response = postPosition(start_nodes)

  # reponse 파일 읽어오기
  file_path = "C:/Users/cjm95/Downloads/server_response.json"
  # with open(file_path, "r", encoding="UTF-8") as file:
  #   response = json.load(file)
  time.sleep(1.5)
  
  missing_points = response["missingPoints"]
  count = len(missing_points)
  answer = response["answer"]
  
  # 정규화
  normGaps = []
  normSums = []
  if len(missing_points) == 0:
    print("missing point가 없습니다.")
  else:
    for mp in missing_points:
      normGaps.append(mp["gapDifference"])
      normSums.append(mp["sumDifference"])
    print("normGaps: ", normGaps)
    print("normSums: ", normSums)
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
    "start_nodes": start_nodes, 
    "answer_node": answer,
    "missing_points": missing_points,
    "count": count
  }

  map = folium.Map(location = [37.544129, 127.054357],zoom_start = 14)
  
  # for node in randNodes:
  #   start_nodes.append({
  #     "position":{
  #       "latitude": node["latitude"],
  #       "longitude": node["longitude"]
  #     }
  #   })
  visualize(start_nodes, map, "start")
  answer_node = answer
  visualize(answer_node, map, "answer")
  missing_points = missing_points
  visualize(missing_points, map, "missing")
  title = "maps/select_test.html"
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
try:
  os.remove(file_path)
  print("파일 삭제 성공")
except OSError as e:
  print("파일 삭제 에러")
with open("result.json", 'w', encoding = 'utf-8') as json_file:
  json.dump(datas, json_file)
# 좌표, 편차의 차이, 이동시간의 차이
# 차이에 따라서 다르게 지도에 표현
# position, gap, sum, missing points([poisiton(latitude, longitude), gap, sum])

# 시작노드: 검은색, 중간지점: 초록색
# sumDifference 원의 크기
# gapDifference 불투명도