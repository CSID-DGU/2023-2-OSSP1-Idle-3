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
        l1 = {
            'id': int(link_reader[i]['Link ID']),
            # 지하철과 노드 id와 겹치지 않도록 90만 더하기
            'start': int(link_reader[i]['Start Node ID']) + 900000,
            'end': int(link_reader[i]['End Node ID']) + 900000,
            # 소요시간 첫째 자리에서 반올림
            'cost': round(float(link_reader[i]['Link Length'])/1.29),
            'subway_network': int(link_reader[i]['Subway Network'])
        }
        l2 = {
            'id': int(link_reader[i]['Link ID']),
            # 지하철과 노드 id와 겹치지 않도록 90만 더하기
            'start': int(link_reader[i]['End Node ID']) + 900000,
            'end': int(link_reader[i]['Start Node ID']) + 900000,
            # 소요시간 첫째 자리에서 반올림
            'cost': round(float(link_reader[i]['Link Length'])/1.29),
            'subway_network': int(link_reader[i]['Subway Network'])
        }
        link_data.append(l1)
        link_data.append(l2)

    print(link_data[0])

    # 링크 json 쓰기
    with open("step_link.json", 'w', encoding='utf-8') as json_file:
        json.dump(link_data, json_file)


getStepEdgeJson()
