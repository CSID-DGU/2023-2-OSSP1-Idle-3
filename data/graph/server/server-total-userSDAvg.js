const fs = require("fs");
const { plot } = require("nodeplotlib");
const axios = require("axios");

// 입력 파일 경로
const inputFilePath = "input.json";

async function fetchDataFromServer() {
  try {
    const response = await axios.get("서버의_데이터_엔드포인트_URL");
    const serverData = response.data;

    // 받은 데이터를 input.json 파일에 저장
    fs.writeFileSync(
      inputFilePath,
      JSON.stringify(serverData, null, 2),
      "utf-8"
    );
  } catch (error) {
    console.error("서버에서 데이터를 받아오는 도중 오류 발생:", error.message);
  }
}

// 데이터 처리 함수
function processData(inputData) {
  const resultData = {};
  const maxData = { y: -Infinity, data: null };
  const minData = { y: Infinity, data: null };
  const avgData = { y: 0, count: 0, data: null };

  for (const entry of inputData) {
    const userTotalCnt = entry.userTotalCnt;
    const userSDAvg = entry.userSDAvg;

    if (!resultData[userTotalCnt]) {
      resultData[userTotalCnt] = { sum: 0, count: 0 };
    }

    resultData[userTotalCnt].sum += userSDAvg;
    resultData[userTotalCnt].count += 1;

    if (userSDAvg > maxData.y) {
      maxData.y = userSDAvg;
      maxData.data = entry;
    }

    if (userSDAvg < minData.y) {
      minData.y = userSDAvg;
      minData.data = entry;
    }

    avgData.y += userSDAvg;
    avgData.count += 1;
  }

  avgData.y /= avgData.count;

  let minDiff = Infinity;
  for (const userTotalCnt in resultData) {
    const avgDiff = Math.abs(
      resultData[userTotalCnt].sum / resultData[userTotalCnt].count - avgData.y
    );
    if (avgDiff < minDiff) {
      minDiff = avgDiff;
      avgData.data = resultData[userTotalCnt];
    }
  }

  return { resultData, maxData, minData, avgData };
}

// 그래프 생성 함수
function generateGraph(data) {
  const xValues = Object.keys(data.resultData);
  const yValues = xValues.map(
    (userTotalCnt) =>
      data.resultData[userTotalCnt].sum / data.resultData[userTotalCnt].count
  );

  const plotData = [
    {
      x: xValues,
      y: yValues,
      type: "line",
      name: "Average userSDAvg",
    },
  ];

  plot(plotData, {
    xaxis: { title: "userTotalCnt" },
    yaxis: { title: "Average userSDAvg" },
    title: "Average userSDAvg vs userTotalCnt",
  });
}

async function main() {
  await fetchDataFromServer();
  const inputData = JSON.parse(fs.readFileSync(inputFilePath, "utf-8"));
  const processedData = processData(inputData);

  // 결과 데이터 파일로 저장
  fs.writeFileSync(
    "total_userSDAvg.json",
    JSON.stringify(processedData.resultData, null, 2)
  );
  fs.writeFileSync(
    "result_max_total_userSDAvg.json",
    JSON.stringify(processedData.maxData.data, null, 2)
  );
  fs.writeFileSync(
    "result_min_total_userSDAvg.json",
    JSON.stringify(processedData.minData.data, null, 2)
  );
  fs.writeFileSync(
    "result_avg_total_userSDAvg.json",
    JSON.stringify(processedData.avgData.data, null, 2)
  );

  // 그래프 생성
  generateGraph(processedData);
}

main();
