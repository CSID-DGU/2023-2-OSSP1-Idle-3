import json
import time
from time import sleep

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.common.keys import Keys

# --크롬창을 숨기고 실행-- driver에 options를 추가해주면된다
options = webdriver.ChromeOptions()
options.add_argument('headless')
with open('regularPolygon.json', 'r') as file:
  algorResult = json.load(file)

algorithms = ["stdOnlyAlgorithm", "weightAlgorithm", "distanceOnlyAlgorithm"]
output = []
chrome_options = Options()
chrome_options.add_experimental_option("detach", True)

url = 'https://www.google.co.kr/maps/?hl=ko'
driver = webdriver.Chrome(options=chrome_options)  # 드라이버 경로

testCase = len(algorResult)

try:
  for T in range(testCase):
    cost_sum = 0
    cost_list = []
    caseT = algorResult[T]
    start = []
    # output 배열의 요소
    caseResult = {
      "index": T,
      "start": [],
      "stdOnlyAlgorithm":{},
      "weightAlgorithm": {},
      "distanceOnlyAlgorithm": {}
    }
    for i in range(len(caseT["start"])):
      start.append(
        str(caseT["start"][i]["latitude"]) + ', ' + str(caseT["start"][i]["longitude"])
      )
    # caseResult에 알고리즘별 start 추가
    caseResult["start"].append(st)
    for i in range(len(algorithms)): 
      end = str(caseT[algorithms[i]]["end"]["latitude"]) + ', ' + str(caseT[algorithms[i]]["end"]["longitude"])
      print('end ' + end)
      # caseResult에 알고리즘별 end 추가
      caseResult[algorithms[i]]["end"] = end
      for st in start:
        print('start ' + st) 
        driver.implicitly_wait(5) # 페이지 로딩 완료될 때가지 3초 wait
        # driver = webdriver.Chrome('./chromedriver',chrome_options=options) # 크롬창 숨기기
        driver.get(url)

        # 검색창 클릭
        search_box = driver.find_element(By.XPATH,'//*[@id="searchboxinput"]')
        search_box.click()
        driver.implicitly_wait(6)
        # 좌표값 입력
        search_box.send_keys(st)
        driver.implicitly_wait(6)
        #검색버튼 클릭
        # driver.find_element(By.XPATH,'//*[@id="searchbox-searchbutton"]').click()
        # 길찾기 버튼 클릭
        driver.find_element(By.XPATH,'//*[@id="hArJGc"]').click()
        driver.implicitly_wait(6)
        # 출발지 클릭
        start_place = driver.find_element(By.XPATH,'//*[@id="sb_ifc50"]/input')
        driver.implicitly_wait(6)
        # 출발지 입력
        start_place.send_keys(end)
        driver.implicitly_wait(6)
        # 검색 클릭
        start_place.send_keys(Keys.RETURN)
        # driver.find_element(By.XPATH,'//*[@id="directions-searchbox-0"]/button[1]')
        driver.implicitly_wait(6)
        # cost 가져오기
        cost = driver.find_element(By.XPATH,'//*[@id="section-directions-trip-0"]/div[1]/div/div[1]/div').text
        if len(cost) > 0:  
          if len(cost) > 3:
            # 문자열에서 시간과 분을 추출하여 숫자로 변환
            split_time = cost.split()
            hours = int(split_time[0][:-2]) if '시간' in split_time[0] else 0
            minutes = int(split_time[1][:-1]) if '분' in split_time[1] else 0

            # 시간을 분으로 변환하고 분과 합산하여 총 분으로 계산
            cost = hours * 60 + minutes
          elif cost[-1] == '분':  
            cost = int(cost[:-1])
          else:
            cost = int(cost[0])
          cost_sum += int(cost)

          cost_list.append(cost)

      mean = cost_sum/len(start)
      # print(cost_list)
      for j in range(len(cost_list)):
        cost_list[j] -= mean
        cost_list[j] = abs(cost_list[i])
      gap = sum(cost_list)
      caseResult[algorithms[i]]["gap"] = gap
      caseResult[algorithms[i]]["sum"] = cost_sum
      # print('gap: ' + str(gap))
      # print('sum: ' + str(cost_sum))
      # print(result)
    output.append(caseResult)
    print(caseResult)
except:
  with open('caseResult.json', 'w') as file:
      json.dump(output, file, indent=4)
