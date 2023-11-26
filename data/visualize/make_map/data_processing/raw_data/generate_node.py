import json

def readSubwayStationJson():
  with open("subway_station.json", "r", encoding="utf-8") as file:
    station_json = json.load(file)
  return station_json


def generate(stationData):
  nodeJson = []
  for data in stationData:
    id = int(data["statn_id"])
    name = data["statn_nm"]
    latitude = float(data["crdnt_y"])
    longitude = float(data["crdnt_x"])
    line = data["route"]
    item = {
      "id": id,
      "name": name,
      "latitude": latitude,
      "longitude": longitude,
      "line": line
    }
    nodeJson.append(item)
  return nodeJson

def writeSubwayNode(outputData):
  with open("subway_node.json", "w", encoding="utf-8") as outfile:
    json.dump(outputData, outfile, ensure_ascii=False, indent=4)

stationData = readSubwayStationJson()
outputData = generate(stationData["DATA"])
writeSubwayNode(outputData)