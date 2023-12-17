// import wait from './wait.js';
// import XMLHttpRequest from 'xhr2';

// async function findTransportationTime() {
//     // input 
//     // 1. 출발지들 위도 경도
//     // 2. 도착지 한 곳의 위도 경도
    
//     // output
//     // 각 출발지에서 도착지까지의 걸리는 시간을 리스트로 반환(출발지 순서대로)
// }



import axios from 'axios';

async function searchPubTransPath() {
  try {
    const response = await axios.get('https://api.odsay.com/v1/api/searchPubTransPathT', {
      params: {
        SX: 126.9027279,
        SY: 37.5349277,
        EX: 126.9145430,
        EY: 37.5499421,
        apiKey: '48R0%2FBMZARHvGB1wQWk1nw'
      }
    });

    console.log(response.data); // API 응답 데이터

    // 여기서 응답 데이터를 활용하여 원하는 작업을 수행할 수 있습니다.

  } catch (error) {
    console.error('API 호출 중 오류 발생:', error);
  }
}

// 함수 호출
searchPubTransPath();