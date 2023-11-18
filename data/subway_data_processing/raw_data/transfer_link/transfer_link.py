import json;

def generateTransferSubwayEdge():

    subway_node_filePath = 'subway_node.json'
    subway_edge_filePath = 'subway_edge_id.json'

    with open(subway_node_filePath, 'r', encoding='utf-8') as f:
        subway_node_list = json.load(f)

    with open(subway_edge_filePath, 'r', encoding='utf-8') as f:
        subway_edge_list = json.load(f) 
    
    # 역의 이름을 기준으로 그룹화
    subway_node_group = {}
    for subway_node in subway_node_list:
        if subway_node['name'] not in subway_node_group:
            subway_node_group[subway_node['name']] = []
        subway_node_group[subway_node['name']].append(subway_node)


    # 지하철별 평균 배차 간격을 저장할 변수 1호선~9호선
    # cost는 배차간격의 절반
    # 1호선 5분
    # 2호선 6분
    # 3호선 8분
    # 4호선 7분
    # 5호선 6분
    # 6호선 7분
    # 7호선 6분
    # 8호선 8분
    # 9호선 12분

    subway_interval_cost = {
        "1" : 150.0,
        "2" : 180.0,
        "3" : 240.0, 
        "4" : 210.0,
        "5" : 180.0,
        "6" : 210.0,
        "7" : 180.0,
        "8" : 240.0,
        "9" : 360.0,
    }

    transfer_subway_edge_list = []

    # 서로서로 연결해주는 edge 생성
    for subway_node_name, subway_nodes in subway_node_group.items():
        for i in range(len(subway_nodes)-1):
            transfer_cost_to_i = subway_interval_cost[getSubwayEdgeIndex(subway_edge_list, subway_nodes[i]['id'])]
            for j in range(i+1, len(subway_nodes)):
                transfer_cost_to_j = subway_interval_cost[getSubwayEdgeIndex(subway_edge_list, subway_nodes[j]['id'])]

                transfer_subway_edge_list.append({
                    "start" : subway_nodes[i]['id'],
                    "end" : subway_nodes[j]['id'],
                    "cost" : transfer_cost_to_j,
                    "type" : "SUBWAY",
                    "line" : "환승",
                })

                # 반대 방향의 엣지도 추가
                transfer_subway_edge_list.append({
                    "start": subway_nodes[j]['id'],
                    "end":  subway_nodes[i]['id'],
                    "cost" : transfer_cost_to_i,
                    "type" : "SUBWAY",
                    "line" : "환승",
                })
    
    
    # subway_edge_list 뒷부분에 transfer_subway_edge_list를 추가
    subway_edge_list.extend(transfer_subway_edge_list)

    with open('subway_edge_id.json', 'w', encoding='utf-8') as f:
        json.dump(subway_edge_list, f, indent=4, ensure_ascii=False)


# subway_nodes[j]['id'] 가 subway_edge_list의 데이터 중 end에 존재하는 데이터 목록중 0번 불러오기
def getSubwayEdgeIndex(subway_edge_list, subway_nodes_id):
    for i in range(len(subway_edge_list)):
        if subway_edge_list[i]['end'] == subway_nodes_id:
            return subway_edge_list[i]['line']
    return None

generateTransferSubwayEdge()