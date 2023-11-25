import json
import random
import requests

def getRandomIds():
  with open("step_node.json", "r", encoding="UTF-8") as file:
    nodes = json.load(file)

  nodeIds = []
  for node in nodes:
    nodeIds.append(node["id"])

  print("사용자 수를 입력하세요: ")
  n = int(input())
  randomIds = []

  for _ in range(n):
    randId = 0
    while (randId not in nodeIds and randId in randomIds) or randId == 0:
      randId = random.randrange(900001, 1115570)
    randomIds.append(randId)
  return randomIds


# def getBestCase():


# def getWorstCase():


def postPosition(randomIds):
  url = "https://b98e-218-48-52-82.ngrok.io"
  header = {
    "Content-Type": "application/json"
  }
  
  # 요청 바디
  requestBody = {
    "randomIds" : randomIds
  }
  print(requestBody)

  # 응답 받기
  # response = requests.post(url, data = json.dumps(requestBody), headers = header)
  # print("response status:", response.status_code)
  # return response.json()


randomIds = getRandomIds()
postPosition(randomIds)
