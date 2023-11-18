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

    # 서로서로 연결해주는 edge 생성
    for subway_node_name, subway_nodes in subway_node_group.items():
        for i in range(len(subway_nodes)-1):
            for j in range(i+1, len(subway_nodes)):
                subway_edge_list.append({
                    "start" : subway_nodes[i]['id'],
                    "end" : subway_nodes[j]['id'],
                    "cost" : 20.0,
                    "type" : "SUBWAY",
                    "line" : "환승",
                })

                # 반대 방향의 엣지도 추가
                subway_edge_list.append({
                    "start": subway_nodes[j]['id'],
                    "end":  subway_nodes[i]['id'],
                    "cost" : 20.0,
                    "type" : "SUBWAY",
                    "line" : "환승",
                })
    
    with open('subway_edge_id.json', 'w', encoding='utf-8') as f:
        json.dump(subway_edge_list, f, indent=4, ensure_ascii=False)


generateTransferSubwayEdge()