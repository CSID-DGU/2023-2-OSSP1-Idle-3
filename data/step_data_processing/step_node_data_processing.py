import csv
import json

def getStepNodeJson():
  data = []
  with open("raw_data/step_raw_data.csv", 'r') as csv_file:
    reader = csv.DictReader(csv_file)
    data = list(reader)

  node_reader, node_data = [], []

  for i in range(len(data)):
    if data[i]['NodeLink'] == 'NODE':
      node_reader.append(data[i])

  # 노드 변환
  for i in range(len(node_reader)):
    longitude, latitude = node_reader[i]['Node WKT'][6:-1].split()
    n = {
      'id': int(node_reader[i]['Node ID']) + 900000, # 지하철과 노드 id와 겹치지 않도록 90만 더하기
      'crosswalk': int(node_reader[i]['Crosswalk']),
      'position': {
        'latitude': float(latitude),  # 위도
        'longitude': float(longitude) # 경도
      }
    }
    node_data.append(n)

  print(node_data[0])
  
  # 노드 json 쓰기
  with open("step_node.json", 'w', encoding = 'utf-8') as json_file:
    json.dump(node_data, json_file)

getStepNodeJson()