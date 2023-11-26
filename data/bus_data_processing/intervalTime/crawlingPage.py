from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from time import sleep
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
import json
import re

def main():
    driver = webdriver.Chrome()  # ChromeDriver 경로 지정이 필요한 경우 괄호 안에 추가
    base_url = 'https://topis.seoul.go.kr/map/openBusMap.do#'  # 대상 사이트 URL

    driver.get(base_url)

    sleep(5)
    driver.execute_script("schTabs(this, 'bus');")

    progress_idx = 0

    data = []

    for page in range(1, 252):
        driver.execute_script("fn_search({});".format(page))
        sleep(1)

        elements = driver.find_elements(By.CSS_SELECTOR, "#busList #resultList .bustit strong")

        second_tds = driver.find_elements(By.CSS_SELECTOR, "#busList #resultList .tby03.min.txtC tr:first-of-type td:nth-of-type(2)")

        # 해당 요소 찾기 (CSS 선택자, XPath 등을 적절하게 사용)
        route_id_elements = driver.find_elements(By.CSS_SELECTOR, "#busList #resultList .bustit a:first-of-type") 

        # onclick 속성 값 가져오기
        onclick_attrs = [element.get_attribute("onclick") for element in route_id_elements]
          
        # elements와 second_tds를 조합하여 JSON 데이터 생성
        for element, second_td, onclick_attr in zip(elements, second_tds, onclick_attrs):
            match = re.search(r'fn_searchMap\("(\d+)"', onclick_attr)

            data.append({
                "route_id": match.group(1),
                "line": element.text,
                "intervalTime": int(second_td.text.replace("분", "")) * 60
            })

        page += 1
        progress_idx += 1

        print("진행도: {} / 251".format(progress_idx), end='\r')

    with open('bus_interval.json', 'w', encoding='utf-8') as f:
        json.dump(data, f, indent=4, ensure_ascii=False)

    driver.quit()


def new():
    caps = DesiredCapabilities.CHROME
    caps['goog:loggingPrefs'] = {'browser': 'ALL'}

    driver = webdriver.Chrome()

    driver.get("https://topis.seoul.go.kr/map/openBusMap.do#")
    driver.execute_script("fn_search(1);")

    # 콘솔 로그 가져오기
    logs = driver.get_log("browser")
    for log in logs:
        print(log)



if __name__ == '__main__':
    main()