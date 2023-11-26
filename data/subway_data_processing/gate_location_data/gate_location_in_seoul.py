"""
gate_location_in_seoul.json 파일 형식을 아래와 같이 변경

  {
    "id": 70001, // 70001번대 부터 id 설정
    "name": "계양", // 지하철 역 명
    "gate": "1", // 지하철 출입구 번호
    "latitude": 37.5714782745416, // 위도
    "longitude": 126.73638564196 // 경도
  }

최종 생성 파일명 : gate_location_in_seoul_ver2.json
"""

import json

# 입력 파일명과 출력 파일명 지정
input_filename = 'gate_location_in_seoul.json'
output_filename = 'gate_location_in_seoul_ver2.json'

# 입력 파일 열기
with open(input_filename, 'r', encoding='utf-8') as file:
    data = json.load(file)

# 변수명 및 id 설정
for i, item in enumerate(data, start=70001):
    item['id'] = i
    item['name'] = item.pop('지하철역명')
    item['gate'] = item.pop('출입구번호')
    item['latitude'] = item.pop('위도')
    item['longitude'] = item.pop('경도')

# 출력 파일에 쓰기
with open(output_filename, 'w', encoding='utf-8') as output_file:
    json.dump(data, output_file, ensure_ascii=False, indent=2)

print(f'Modified data saved to {output_filename}')
