import folium
import json


def makeMap(map, latitude, longitude):
  folium.CircleMarker([latitude, longitude],
                      radius=2,
                      color='blue',
                      fill_color='skyblue').add_to(map)
  
def readStepLinkJson():
  with open('data_processing/raw_data/step_link.json', 'r', encoding='utf-8') as f:
    stepLinks = json.load(f)
  return stepLinks
  

def readStepNodeInJoonguJson():
  with open('data_processing/result_data/step_node_in_Joongu.json', 'r', encoding='utf-8') as f:
    stepNodes = json.load(f)
  return stepNodes

def readStepLinkInJoonguJson():
  with open('data_processing/result_data/step_link_in_Joongu.json', 'r', encoding='utf-8') as f:
    stepLinks = json.load(f)
  return stepLinks

def setStepLinkPosition(stepLinks, map):
  for link in stepLinks:
    startPosition = []
    endPosition = []
    startPosition.append(link["start"]["position"]["latitude"])
    startPosition.append(link["start"]["position"]["longitude"])
    endPosition.append(link["end"]["position"]["latitude"])
    endPosition.append(link["end"]["position"]["longitude"])

    locationData = [startPosition, endPosition]
    folium.PolyLine(locations=locationData, tooltip='Polyline').add_to(map)

def readSubwayLinkPositionJson():
  with open('data_processing/result_data/subway_link_position.json', 'r', encoding='utf-8') as f:
    subwayLinks = json.load(f)
  return subwayLinks



map = folium.Map(location = [37.544129, 127.054357],zoom_start = 14)

stepLinks = readStepLinkInJoonguJson()
setStepLinkPosition(stepLinks, map)



map.save("Maps/JoonguStepMap.html")