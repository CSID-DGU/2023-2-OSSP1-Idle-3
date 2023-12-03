const fs = require("fs");
const axios = require("axios");
const { plot } = require("nodeplotlib");

const inputFilePath = "input.json";

async function fetchDataFromServer() {
  try {
    const response = await axios.get("서버의_데이터_엔드포인트_URL");
    const serverData = response.data;

    fs.writeFileSync(
      inputFilePath,
      JSON.stringify(serverData, null, 2),
      "utf-8"
    );
  } catch (error) {
    console.error("서버에서 데이터를 받아오는 도중 오류 발생:", error.message);
  }
}

function processData(inputData) {
  const resultData = {};

  for (const entry of inputData) {
    const userTotalCnt = entry.userTotalCnt;
    const userInShapeCnt = entry.userInShapeCnt;
    const userSum = entry.userSum;

    // userTotalCnt로 그룹화
    if (!resultData[userTotalCnt]) {
      resultData[userTotalCnt] = {};
    }

    // userInShapeCnt로 그룹화
    if (!resultData[userTotalCnt][userInShapeCnt]) {
      resultData[userTotalCnt][userInShapeCnt] = {
        sum: 0,
        count: 0,
      };
    }

    // userSum을 더하고, 해당 그룹의 개수 증가
    resultData[userTotalCnt][userInShapeCnt].sum += userSum;
    resultData[userTotalCnt][userInShapeCnt].count += 1;
  }

  return resultData;
}

function generateGraph(data) {
  // userTotalCnt별로 그래프 생성
  for (const userTotalCnt in data) {
    const plotData = [];

    // userInShapeCnt에 따른 그래프 데이터 설정
    for (const userInShapeCnt in data[userTotalCnt]) {
      const avgUserSum =
        data[userTotalCnt][userInShapeCnt].sum /
        data[userTotalCnt][userInShapeCnt].count;
      plotData.push({
        x: userInShapeCnt,
        y: avgUserSum,
        type: "bar",
        name: `userInShapeCnt=${userInShapeCnt}`,
      });
    }

    plot(plotData, {
      xaxis: { title: "userInShapeCnt" },
      yaxis: { title: "Average userSum" },
      title: `userTotalCnt=${userTotalCnt}`,
    });
  }
}

async function main() {
  await fetchDataFromServer();
  const inputData = JSON.parse(fs.readFileSync(inputFilePath, "utf-8"));
  const processedData = processData(inputData);
  generateGraph(processedData);
}

main();
