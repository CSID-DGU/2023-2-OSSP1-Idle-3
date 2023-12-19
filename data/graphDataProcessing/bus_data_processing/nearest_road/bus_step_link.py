import json

with open('result2.json', 'r', encoding='utf-8') as result_file:
    result_data = json.load(result_file)

result2_data = []

total_items = len(result_data)
for i, result_item in enumerate(result_data):
    bus_id = result_item['bus_id']
    step_id = result_item['step_id']
    distance_km = result_item['distance']

    # 거리를 초당 1.29km로 변환하여 cost 계산 (소수 네 번째 자리까지)
    cost = round(distance_km / 1.29, 4)

    # cost가 600 이상인 항목은 제외
    if cost < 600:
        # start와 end 값을 교환하여 데이터 추가
        result2_data.append({
            'start': bus_id,
            'end': step_id,
            'cost': cost
        })

        # 다시 반대로 start와 end 값을 교환하여 데이터 추가
        result2_data.append({
            'start': step_id,
            'end': bus_id,
            'cost': cost
        })

    # 진행 상황 출력
    print(f'Processing item {i + 1}/{total_items}', end='\r')

with open('result3.json', 'w', encoding='utf-8') as result2_file:
    json.dump(result2_data, result2_file, indent=2)

print('Processing completed.')  # 처리 완료 메시지 출력
