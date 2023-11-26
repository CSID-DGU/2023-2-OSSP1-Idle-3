import requests
import json
from urllib.parse import urlencode, quote_plus

def getBusInterval() :
    api_key = 'g2B7EooEAgEwa++yErKYAhIk93i7tdYXP/3i5nOrRMN0Fmt78AnTzkaJUGqdsIUcqd7ITge5nUX0dAK/luCmFg=='

    serviceKey = requests.utils.unquote(api_key)

    api_url = 'http://ws.bus.go.kr/api/rest/busRouteInfo/getRouteInfo'

    params ={'serviceKey' : api_key, 'busRouteId' : '100100124' }

    response = requests.get(api_url, params=params)
    print(response.content)



def countRouteId():
    with open('bus_router_edge_with_transfer.json', 'r', encoding='utf-8') as f:
        bus_route_list = json.load(f)

    unique_route_ids = set(edge['route_id'] for edge in bus_route_list)

    print("고유한 route_id의 개수:", len(unique_route_ids))

countRouteId()