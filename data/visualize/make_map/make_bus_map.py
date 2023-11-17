import folium
import json

def readBusLinkPositionJson():
  with open('data_processing/result_data/bus3.json', 'r', encoding='utf-8') as f:
    busLinks = json.load(f)
  return busLinks

def setBusLinkPosition(busLinks, map):
  for link in busLinks:
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
subwayLinkPosition = readBusLinkPositionJson()
print(subwayLinkPosition[0])

setBusLinkPosition(subwayLinkPosition, map)

map.save("Maps/bus3Map.html")