import csv
import json

def getStepEdgeJson():
  data = []
  with open("raw_data/step_raw_data.csv", 'r') as csv_file:
    reader = csv.DictReader(csv_file)
    data = list(reader)

  link_reader, link_data = [], []

  for i in range(len(data)):
      if data[i]['NodeLink'] == 'LINK':
        link_reader.append(data[i])

  # 링크 변환
  for i in range(len(link_reader)):
    l = {
      'id': link_reader[i]['Link ID'],
      'start': link_reader[i]['Start Node ID'],
      'end': link_reader[i]['End Node ID'],
      'cost': round(float(link_reader[i]['Link Length'])/1.29, 2),
      'subway_network': link_reader[i]['Subway Network']
    }
    link_data.append(l)

  print(link_data[0])

  # 링크 json 쓰기
  with open("step_link.json", 'w', encoding = 'utf-8') as json_file:
    json.dump(link_data, json_file)

getStepEdgeJson()