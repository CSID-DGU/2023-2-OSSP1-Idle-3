const fs = require('fs');

// 기존 JSON 파일 읽기
const fileName = process.argv[2];
fs.readFile(`C:/Users/cjm95/Desktop/2023-2-OSSP1-Idle-3/data/randomTest/testResult/${fileName}`, 'utf8', (err, data) => {
  if (err) {
    console.error('파일을 읽는 중 에러 발생:', err);
    return;
  }

  try {
    const jsonData = JSON.parse(data); // JSON 데이터 파싱


    // json을 배열로 변환하여 result 데이터만 추출
    const resultData = Object.values(jsonData)[3];
    console.log(resultData);
    let graphData = [];

    resultData.forEach((element, index) => {
      data = {
        index: index, 
        sum: element.answer.sum, 
        gap: element.answer.gap,
        alpha: element.alpha
      }
      graphData.push(data);
    });
    console.log(graphData)

    // 새로운 JSON 파일로 작성
    fs.writeFile(`${fileName}`, JSON.stringify(graphData, null, 2), 'utf8', (err) => {
      if (err) {
        console.error('파일을 쓰는 중 에러 발생:', err);
        return;
      }
      console.log('새로운 JSON 파일이 성공적으로 작성되었습니다.');
    });
  } catch (err) {
    console.error('JSON 데이터를 파싱하는 중 에러 발생:', err);
  }
});
