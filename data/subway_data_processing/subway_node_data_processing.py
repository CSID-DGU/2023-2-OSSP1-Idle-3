import json

with open("raw_data/subway_station.json", "r", encoding="utf-8") as file:
    input_json = json.load(file)

output_json = []

for item in input_json["DATA"]:
    id = int(item["statn_id"])
    latitude = float(item["crdnt_y"])
    longitude = float(item["crdnt_x"])
    name = item["statn_nm"]

    station_info = {
        "id": id,
        "position": {
            "latitude": latitude,
            "longitude": longitude
        },
        "name": name
    }

    output_json.append(station_info)

with open("subway_node.json", "w", encoding="utf-8") as outfile:
    json.dump(output_json, outfile, ensure_ascii=False, indent=4)

print("success")
