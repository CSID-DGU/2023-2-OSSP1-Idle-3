import json

with open("raw_data/subway_duration_time.json", "r", encoding="utf-8") as file:
    input_json = json.load(file)

output_json = []

for i in range(len(input_json["DATA"]) - 1):
    start_station = input_json["DATA"][i]["statn_nm"]
    end_station = input_json["DATA"][i + 1]["statn_nm"]
    time_minutes = int(input_json["DATA"][i + 1]["mnt"].split(":")[0]) * \
        60 + int(input_json["DATA"][i + 1]["mnt"].split(":")[1])
    line = input_json["DATA"][i]["line"]

    if time_minutes > 0:
        edge_info = {
            "start": start_station,
            "end": end_station,
            "cost": time_minutes,  # 초 단위로 변환
            "type": "SUBWAY",
            "line": line
        }
        output_json.append(edge_info)

with open("preprocessed_data/subway_edge_name.json", "w", encoding="utf-8") as outfile:
    json.dump(output_json, outfile, ensure_ascii=False, indent=4)

print("success")
