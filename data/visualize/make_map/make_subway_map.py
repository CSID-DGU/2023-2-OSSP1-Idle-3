import folium
import json

def readSubwayLinks():
  with open('data_processing/result_data/subway_edge_for_vis.json', 'r', encoding='utf-8') as f:
    subwayLinks = json.load(f)
  return subwayLinks

def makeSubwayMap(subwayLinks, map, line):
  for link in subwayLinks:
    if line == link["line"]:
      startPosition = []
      endPosition = []
      startPosition.append(link["start"]["latitude"])
      startPosition.append(link["start"]["longitude"])
      endPosition.append(link["end"]["latitude"])
      endPosition.append(link["end"]["longitude"])

      startName = link["start"]["name"]

      locationData = [startPosition, endPosition]
      popup = folium.Popup(str(line) + "호선 " + startName, max_width=200)
      folium.Marker(location = startPosition, popup=popup).add_to(map)
      folium.PolyLine(locations=locationData, tooltip='Polyline').add_to(map)

map = folium.Map(location = [37.544129, 127.054357],zoom_start = 12)
print("몇호선?(1~8 정수)")
line = int(input())

subwayLinks = readSubwayLinks()
makeSubwayMap(subwayLinks, map, line)

map.save("maps/" + str(line) + "호선.html")