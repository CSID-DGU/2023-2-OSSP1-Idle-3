import folium
import json

def readSubwayLinkPositionJson():
  with open('data_processing/result_data/subway_link_position.json', 'r', encoding='utf-8') as f:
    subwayLinks = json.load(f)
  return subwayLinks

def setSubwayLinkPosition(subwayLinks, map):
  for link in subwayLinks:
    startPosition = []
    endPosition = []
    startPosition.append(link["start"]["position"]["latitude"])
    startPosition.append(link["start"]["position"]["longitude"])
    endPosition.append(link["end"]["position"]["latitude"])
    endPosition.append(link["end"]["position"]["longitude"])

    startName = link["start"]["name"]

    locationData = [startPosition, endPosition]
    print(locationData)
    popup = folium.Popup(startName, max_width=200)
    # folium.Marker(location = startPosition, popup=popup).add_to(map)
    folium.PolyLine(locations=locationData, tooltip='Polyline').add_to(map)


map = folium.Map(location = [37.544129, 127.054357],zoom_start = 14)
subwayLinkPosition = readSubwayLinkPositionJson()
print(subwayLinkPosition[0])

setSubwayLinkPosition(subwayLinkPosition, map)

map.save("Maps/SubwayMapWithoutName.html")