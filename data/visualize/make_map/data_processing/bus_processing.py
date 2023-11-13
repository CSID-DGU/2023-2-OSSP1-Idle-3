import json

def readBusLinkJson():
  with open('raw_data/bus3Link.json', 'r', encoding='utf-8') as f:
    busLink = json.load(f)
  return busLink

def readBusNodeJson():
  with open('raw_data/bus_stop_node.json', 'r', encoding='utf-8') as f:
    busNode = json.load(f)
  return busNode

def setLinkPosition(links, nodes):
  result = []
  for link in links:
    startId = link["start"]
    endId = link["end"]

    startLatitude = 0
    startLongitude = 0
    endLatitude = 0
    endLongitude = 0
    startName = ""
    endName = ""


    for node in nodes:
      nodeId = node["id"]
      if nodeId == startId:
        startName = node["name"]
        startLatitude = node["position"]["latitude"]
        startLongitude = node["position"]["longitude"]
        break

    for node in nodes:
      nodeId = node["id"]
      if nodeId == endId:
        endName = node["name"]
        endLatitude = node["position"]["latitude"]
        endLongitude = node["position"]["longitude"]
        break
        
    l = {
          'start': {
            'id': startId,
            'name': startName,
            'position': {
              'latitude': startLatitude,
              'longitude': startLongitude
            }
          },
          'end': {
            'id': endId,
            'name': endName,
            'position': {
              'latitude': endLatitude,
              'longitude': endLongitude
            }
          }
    }
    result.append(l)
  return result

link = readBusLinkJson()
node = readBusNodeJson()
result = setLinkPosition(link, node)

# 링크 json 쓰기
with open("result_data/bus3.json", 'w', encoding = 'utf-8') as json_file:
  json.dump(result, json_file, ensure_ascii=False)