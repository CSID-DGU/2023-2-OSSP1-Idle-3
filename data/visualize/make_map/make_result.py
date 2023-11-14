import folium
import json
import requests

#서버에 좌표 post하여 response 받기
def postPosition(posArr, index):
  positionData = [

  ]
  url = "https://b98e-218-48-52-82.ngrok.io"
  header = {
    "Content-Type": "application/json"
  }
  
  for pos in posArr:
    positionData.append({
      "latitude": float(pos[0]),
      "longitude": float(pos[1])
    })
  
  requestBody = {
    "index" : index,
    "startPoints" : positionData
  }
  response = requests.post(url, data = json.dumps(
    requestBody
  ), headers = header)
  print("response status:", response.status_code)
  return response.json()

#읽어온 데이터 map에 표현
def makeMap(links, map):
  endLat = links["endLatitude"]
  endLon = links["endLongitude"]
  deviationcost = "도착지" + str(links["cost"]) + "\n위도: " + str(endLat) + "\n경도: " + str(endLon)
  popup = folium.Popup(deviationcost, max_width=200, color="red")
  folium.Marker(location = [endLat, endLon], popup=popup).add_to(map)
  
  for i in range(len(links["paths"])):
    link = links["paths"][i]
    startLat = link["startLatitude"]
    startLon = link["startLongitude"]
    cost = str(link["cost"])

    # startPosition = "\n위도: " + str(startLat) + "\n경도: " + str(startLon)
    popup = folium.Popup(cost, max_width=200)
    folium.Marker(location = [startLat, startLon], popup = popup).add_to(map)

    for r in link["route"]:
      type = r["type"]
      startPosition = []
      endPosition = []
      startPosition.append(r["start"]["latitude"])
      startPosition.append(r["start"]["longitude"])
      endPosition.append(r["end"]["latitude"])
      endPosition.append(r["end"]["longitude"])

      locationData = [startPosition, endPosition]
      if type is "WALK":
        folium.PolyLine(locations=locationData, tooltip='Polyline', color="brown").add_to(map)  
      elif type is "BUS":
        folium.PolyLine(locations=locationData, tooltip='Polyline', color="green").add_to(map)
      else:
        folium.PolyLine(locations=locationData, tooltip='Polyline', color="blue").add_to(map)


map = folium.Map(location = [37.544129, 127.054357],zoom_start = 14)

#만나는 사람 수 입력
print("만나는 사람 수")
n = int(input())
postArr = []

print("사용자 좌표")
#사용자 좌표 입력
for _ in range(n):
  postArr.append(list(input().split()))

print("몇 번째 경로를 출력?")
index = int(input())

#요청 보내기(response는 json)
response = postPosition(postArr, index)

#map 만들기
makeMap(response, map)

#map 저장
map.save("Maps/result.html")