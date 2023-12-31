import json

def getBusRouterEdgeJson():
    bus_route_filePath = "rawData/nosun_data_final.json"
    bus_route_line_filePath = "rawData/bus_route_line.json"

    bus_average_speed = 5.028

    with open(bus_route_filePath, 'r', encoding='utf-8') as f:
        bus_route_list = json.load(f)

    with open(bus_route_line_filePath, 'r', encoding='utf-8') as f:
        bus_route_line_list = json.load(f)

    #route_id를 기준으로 그룹화
    route_group = {}   
    for route in bus_route_list:
        if route['route_id'] not in route_group:
            route_group[route['route_id']] = []
        route_group[route['route_id']].append(route)

    # 각 노선에 대해 sttn_ord에 따라 정렬하고 출발지와 도착지 결정
    edge_bus_router_json_list = []
    for route_id, bus_stops in route_group.items():
        line_name = [item['route_nm'] for item in bus_route_line_list['DATA'] if item['route_id'] == route_id][0]
        sorted_bus_stops = sorted(bus_stops, key=lambda x: x['sttn_ord'])
        for i in range(len(sorted_bus_stops)-1):
            start = sorted_bus_stops[i]['sttn_id']
            end = sorted_bus_stops[i+1]['sttn_id']
            cost = sorted_bus_stops[i+1]['sttn_dstnc_mtr'] / bus_average_speed
            edge_bus_router_json_list.append({"start": start, "end": end, "cost" : cost , "type" : "BUS", "route_id" : route_id,
            "line" : line_name
             })
        
    with open('bus_router_edge.json', 'w', encoding='utf-8') as f:
        json.dump(edge_bus_router_json_list, f, indent=4, ensure_ascii=False)


getBusRouterEdgeJson()




# def count():
#     path = 'bus_stop_node_with_transfer.json'

#     with open(path, 'r', encoding='utf-8') as f:
#         bus_stop_list = json.load(f)

#     print(len(bus_stop_list))


