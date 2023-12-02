const express = require('express');
const axios = require('axios');
const app = express();
const path = require('path');
const nunjucks = require('nunjucks');
const fs = require('fs');
const port = 3000; // 사용할 포트 번호

// 넌적스 템플릿 설정
nunjucks.configure('views', {
  express: app,
  watch: true,
});
app.set('view engine', 'html');
app.set('views', path.join(__dirname, 'views'));
// 바디 파싱 용도
app.use(express.json());

// 다른 서버로 요청을 보내는 함수
function fetchDataFromOtherServer(){
  const data = [
    {"latitude": 37.525461, "longitude": 126.887562},
    {"latitude": 37.545322, "longitude": 127.055797},
    {"latitude": 37.514366, "longitude": 127.110661}
  ];
  axios.post('http://localhost:8080/middleSpace/test', JSON.stringify(data), {
    headers: {'Content-Type': 'application/json'}
  })
  .then(response => {
    console.log(response.data)
    // 응답 데이터에서 위도 경도 추출
    return response.data;
  })
  .catch(error => {
    console.error('데이터를 가져오는 중 에러 발생:', error);
  });
}


app.post('/save/startPos', async (req, res)=>{
  try{
    console.log(req.body);
    saveData = req.body;
    // 시작 지점, answer, 무게중심, gap, sum 파일로 저장
    fs.writeFile('./results_json/file.json', saveData, (err) => {
      if(err) throw err;
      console.log('JSON 데이터가 저장되었습니다.')
    })
    res.json({ message: '노드 서버 처리 완료' });
  } catch(error){
    res.status(500).json({ error: error.message }); // 에러 처리
  }
});

// 루트 경로에 대한 요청 처리
app.get('/', async (req, res) => {
  try {
    //const data = await fetchDataFromOtherServer(); // Almost There 서버에서 데이터 가져오기
    
    // HTML 파일 전송
    res.render('index')
  } catch (error) {
    res.status(500).json({ error: error.message }); // 에러 처리
  }
});


// 서버 시작
app.listen(port, () => {
  console.log(`서버가 http://localhost:${port} 에서 실행 중입니다!`);
});
