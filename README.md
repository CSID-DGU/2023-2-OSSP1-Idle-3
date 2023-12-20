# 2023-2-OSSP1-Idle-3
2023년, 2학기, 공개SW프로젝트, 01분반, Idle팀, 3조

## 🧑🏻‍💻 팀 소개
|최정민|송지웅|장은세|남선우|
|:-:|:-:|:-:|:-:|
|2019112104|2019112167|2021112073|2019112090|
|<img src="https://avatars.githubusercontent.com/u/89333351?v=4" width="100px" />|<img src="https://avatars.githubusercontent.com/u/55657581?v=4" width="100px" />|<img src="https://avatars.githubusercontent.com/u/113760409?v=4" width="100px" />|<img src="https://avatars.githubusercontent.com/u/74304338?v=4" width="100px" />|
|[@likerhythm](https://github.com/likerhythm)|[@shortboy7](https://github.com/shortboy7)|[@EunseJang](https://github.com/EunseJang)|[@seonwoonam](https://github.com/seonwoonam)|


## 💻 Almost There
```
기존 프로젝트 [Almost There](https://github.com/JiKangHun/AlmostThere/tree/develop) 는 사용자가 모임을 편리하게 관리할 수 있도록 다양한 기능을 제공하는 서비스입니다.
모임을 위해 '만나기 전', '만났을 때', '만나고 난 후' 각 상황에 맞춰 다양한 기능을 제공합니다.

 🔸 만나기 전에는 카페, 영화관, 음식점 카테고리로 만날 장소를 추천해주고 모임 멤버 구성 및 채팅 기능을 제공합니다.
 🔸 만났을 때에는 모임 구성원의 현재 위치를 확인해 모임 장소까지 어느 정도 남았는지 알려주는 기능, 지각자 파악 및 지각비 계산 기능을 제공합니다.
 🔸 만난 후에는 영수증 정보를 사용하여 금액 내역을 정산하는 기능을 제공합니다.

이외에도 카카오톡으로 친구를 모임에 초대하는 기능 등 모임 관리를 위한 다양한 기능을 제공하는 서비스입니다.
```

## 🛠️ 기술 스택
### Backend

- <img src="https://img.shields.io/badge/Spring Boot 2.7.9-6DB33F?style=flat-square&logo=SpringBoot&logoColor=white"/> <img src="https://img.shields.io/badge/Spring Data JPA-6DB33F?style=flat-square&logo=&logoColor=white"/>
- <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=SpringSecurity&logoColor=white"/> <img src="https://img.shields.io/badge/OAuth-000000?style=flat-square&logo=&logoColor=white"/> <img src="https://img.shields.io/badge/JWT-000000?style=flat-square&logo=&logoColor=white"/>
- <img src="https://img.shields.io/badge/WebSocket-FF6C37?style=flat-square&logo=WebSocke" />
- <img src="https://img.shields.io/badge/Intellij IDEA-0052CC?style=flat-square&logo=Intellij IDEA&logoColor=white"/>
- <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat-square&logo=Postman&logoColor=white"/>

### Frontend

- <img src="https://img.shields.io/badge/Vue.js 2.6.14-4FC08D?style=flat-square&logo=Vue.js&logoColor=white"/> <img src="https://img.shields.io/badge/Vuex-4FC08D?style=flat-square&logo=&logoColor=white"/>
- <img src="https://img.shields.io/badge/Vuetify 2.6.14-8041D9?style=flat-square&logo=Vuetify&logoColor=white"/>
- <img src="https://img.shields.io/badge/Visual Studio Code-1867C0?style=flat-square&logo=Visual Studio Code&logoColor=white"/>

### Database

- <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat-square&logo=MySQL&logoColor=white"/> <img src="https://img.shields.io/badge/Redis-DC382D?style=flat-square&logo=&logoColor=white"/>


## 개선점

기존 프로젝트는 사용자의 위치 좌표의 평균을 계산하여 모임 장소를 추천한다.
이러한 방식으로 모임 장소를 결정하는 경우 다음과 같은 상황에서 일부 참여자의 이동시간이 더 길어지는 상황이 발생한다.

 - 사용자들의 위치가 이등변삼각형 등 중심 좌표가 한쪽으로 치우치는 모양인 경우
 - 교통망이 잘 구축되지 않은 곳에 위치한 참여자가 모임에 있을 경우
 - 사용자 사이에 산과 같이 모임으로 적절하지 않은 장소가 존재하는 경우
   
이러한 문제를 개선하고자 좌표의 평균을 구하는 기존 방법 대신 참여자들의 이동시간의 편차와 총 이동시간을 고려하는 알고리즘을 적용한다.
또한 사용자 간의 모임 상황과 선호하는 중간 지점에 대한 기준이 다를 수 있기 때문에 세 가지 종류의 알고리즘을 도입하여 사용자에게 선택지를 제공한다.

## 구현 방법

사용자의 이동 시간을 구하기 위해 길찾기 API를 사용하게 되면 모든 목적지마다 API를 호출해야 하므로 지도 그래프를 만들고 다익스트라 알고리즘을 적용하여 이동시간을 구한다.
그래프의 노드는 도보 노드, 지하철 노드, 버스 노드와 각각을 연결하는 엣지로 이루어져 있다(데이터 출처: 서울열린데이터광장)
사용자의 출발지 좌표와 가장 가까운 도보 노드를 다익스트라 알고리즘의 출발 노드로 설정하여 모든 노드에 대한 이동 시간을 구한다.
사용자별 이동 시간을 바탕으로 이동 시간 고려 알고리즘, 편차 고려 알고리즘, 이동 시간과 편차에 가중치를 적용한 알고리즘 결과를 구하여 사용자에게 제시한다.

## 평가 방법

🔵 출발지들을 중간 지점 선정 알고리즘의 입력값으로 전달한다. 
🔵 그 결과 출력값으로 도출된 목적지와 해당 목적지까지 가는데 걸리는 이동시간의 평균과 편차의 평균을 구하여 기존 알고리즘과 비교한다.
🔵 출발지들 선정은 일반적인 상황을 가정하는 랜덤추출과, 특이케이스로 나누어 추출하였다.
🔵 특이케이스는 출발지 분포 모양에 따라 정다각형, 정다각형 내부 추가 출발지 분포, 하나의 출발지만 멀리 떨어진 경우, 출발지의 위도가 비슷한 경우, 이등변 삼각형인 케이스로 나누었다.
🔵 해당 과정을 자동화한 평가 모듈을 구현하여 평가를 진행하였다.

## 정량적 평가 지표

- 평가는 사용자가 선택할 수 있는 3가지 알고리즘인, 편차를 고려하는 알고리즘, 이동 시간 및 편차를 모두 고려하여 구한 가중치를 이용하는 알고리즘, 이동시간을 고려하는 알고리즘으로 나누어 진행하였다.
- 평가 지표는 편차와 이동시간으로 나누어 평가를 진행하였고, 막대 그래프의 y축은 (개선 알고리즘의 편차또는 이동시간) / (기존알고리즘의 편차 또는 이동시간) * 100이다. 이는 기존알고리즘의 편차 또는 이동시간을 100이라 했을 때 개선알고리즘의 결과값의 비율을 나타낸다.
- 통계상 편차 및 이동시간은 낮을 수록 지표가 좋은 상태를 나타내므로 막대 그래프가 작을 수록 더 적은 이동시간의 차이 혹은 더 적은 총 이동시간을 나타낸다.
- 각각의 평가 케이스 별 500번씩 평가를 진행하였고, 최종 통계를 산출하는 케이스는 랜덤추출 케이스 + 특이케이스를 모두 합쳐 도합 총 3000번 평가의 통계이다.


## 개선 결과

## 결과 화면

## 시연영상
<p align="center">
  <img src = "https://github.com/CSID-DGU/2023-2-OSSP1-Idle-3/assets/55657581/411b5370-b75e-4f0a-8e0e-1bc34386005a">  
</p>
