import json

def getBusStopNodeJson():
    bus_stop_filePath = "rawData/bus_stop.json" 

    with open(bus_stop_filePath, 'r', encoding='utf-8') as f:
        bus_stops_list = json.load(f)

    # 버스 정류장(bus stop) 노드
    node_bus_stop_json_list = [
        {
            "id": bus_stop["sttn_id"],
            "position": {
                "latitude": bus_stop["crdnt_y"],
                "longitude": bus_stop["crdnt_x"]
            },
            "name": bus_stop["sttn_nm"]
        }
        for bus_stop in bus_stops_list['DATA']
    ]

    with open('bus_stop_node.json', 'w', encoding='utf-8') as f:
        json.dump(node_bus_stop_json_list, f, indent=4, ensure_ascii=False)



getBusStopNodeJson()