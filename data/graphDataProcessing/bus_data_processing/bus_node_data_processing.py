import json
import csv

def getBusStopNodeJson():
    bus_stop_filePath = "rawData/bus_stop_info.csv" 

    with open(bus_stop_filePath, 'r', encoding='utf-8-sig') as csv_file:
        reader = csv.DictReader(csv_file)
        bus_stops_list = list(reader)

    # 버스 정류장(bus stop) 노드
    node_bus_stop_json_list = [
        {
            "id": int(bus_stop["\ufeff정류장ID"].replace("\ufeff", "")),
            "position": {
                "latitude": float(bus_stop["좌표Y"].replace("\ufeff", "")),
                "longitude": float(bus_stop["좌표X"].replace("\ufeff", ""))
            },
            "name": bus_stop["정류장명"].replace("\ufeff", "")
        }
        for bus_stop in bus_stops_list
    ]

    with open('bus_stop_node.json', 'w', encoding='utf-8') as f:
        json.dump(node_bus_stop_json_list, f, indent=4, ensure_ascii=False)



getBusStopNodeJson()

