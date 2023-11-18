import folium
import json
from enum import Enum

class color(Enum):
  line1 = "navy"
  line2 = "green"
  line3 = "orange"
  line4 = "blue"
  line5 = "purple"
  line6 = "#EF6C00"
  line7 = "#C0CA33"
  line8 = "pink"


def readSubwayLinks():
  with open('data_processing/result_data/subway_edge_for_vis.json', 'r', encoding='utf-8') as f:
    subwayLinks = json.load(f)
  return subwayLinks

def makeSubwayMap(subwayLinks, map, line):
  color = ["navy", "#4CAF50", "orange", "blue", "#C2185B", "#EF6C00", "#AFB42B", "#F06292"]
  for link in subwayLinks:
    if line == link["line"] or line == 0:
      startPosition = []
      endPosition = []
      startPosition.append(link["start"]["latitude"])
      startPosition.append(link["start"]["longitude"])
      endPosition.append(link["end"]["latitude"])
      endPosition.append(link["end"]["longitude"])
      startName = link["start"]["name"]

      locationData = [startPosition, endPosition]
      
      # 일부 호선을 그릴 때만 노드 표시
      if line != 0:
        popup = folium.Popup(str(line) + "호선 " + startName, max_width=200)
        folium.Marker(location = startPosition, popup=popup).add_to(map)
      folium.PolyLine(locations=locationData, tooltip=str(link["line"])+"호선", color=color[link["line"]-1], weight=4).add_to(map)

map = folium.Map(location = [37.544129, 127.054357],zoom_start = 12)
print("몇호선?(1~8 정수)")
line = int(input())

subwayLinks = readSubwayLinks()
makeSubwayMap(subwayLinks, map, line)

map.save("maps/" + str(line) + "호선.html")