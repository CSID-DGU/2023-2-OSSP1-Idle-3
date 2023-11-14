import csv
import json



def readStepEdgeJson():
  with open('raw_data/step_link.json', 'r', encoding='utf-8') as f:
    stepLinks = json.load(f)
  return stepLinks

def readStepNodeInJoonguJson():
  with open('result_data/step_node_in_Joongu.json', 'r', encoding='utf-8') as f:
    stepNodes = json.load(f)
  return stepNodes

def getLinksInJoongu(stepNodesJoongu, stepLinks):
  linksInJoongu = []
  for link in stepLinks:

    startId = link["start"]
    endId = link["end"]

    startLatitude = 0
    startLongitude = 0
    endLatitude = 0
    endLongitude = 0

    foundStart = False
    foundEnd = False

    for node in stepNodesJoongu:
      nodeId = node["id"]
      if startId == nodeId:
        startLatitude = node['position']['latitude']
        startLongitude = node['position']['longitude']
        foundStart = True
      elif endId == nodeId:
        endLatitude = node['position']['latitude']
        endLongitude = node['position']['longitude']
        foundEnd = True
      if foundStart and foundEnd:
        l = {
          'id': link['id'],
          'start': {
            'id': link['start'],
            'position': {
              'latitude': startLatitude,
              'longitude': startLongitude
            }
          },
          'end': {
            'id': link['end'],
            'position': {
              'latitude': endLatitude,
              'longitude': endLongitude
            }
          }
        }
        linksInJoongu.append(l)
        break
  return linksInJoongu


stepLinks = readStepEdgeJson() # 링크 28만개
stepNodesInJoongu = readStepNodeInJoonguJson() # 중구 노드 5825개
# => for루프 16억번...

# 링크에서 중구 링크만 뽑아내기
linksInJoongu = getLinksInJoongu(stepNodesInJoongu, stepLinks)

# 링크 json 쓰기
with open("result_data/step_link_in_Joongu.json", 'w', encoding = 'utf-8') as json_file:
  json.dump(linksInJoongu, json_file)