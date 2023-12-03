const fs = require("fs");
const { plot } = require("nodeplotlib");

const inputFilePath = "input.json";

function processData(inputData) {
  const resultData = {};

  for (const entry of inputData) {
    const userTotalCnt = entry.userTotalCnt;
    const userInShapeCnt = entry.userInShapeCnt;
    const serverResponse = entry.serverResponse;

    if (!resultData[userTotalCnt]) {
      resultData[userTotalCnt] = {};
    }

    if (!resultData[userTotalCnt][userInShapeCnt]) {
      resultData[userTotalCnt][userInShapeCnt] = {
        sum: 0,
        count: 0,
      };
    }

    resultData[userTotalCnt][userInShapeCnt].sum += serverResponse.sum;
    resultData[userTotalCnt][userInShapeCnt].count += 1;
  }

  return resultData;
}

function generateGraph(data) {
  for (const userTotalCnt in data) {
    const plotData = [];

    for (const userInShapeCnt in data[userTotalCnt]) {
      const avgServerResponseSum =
        data[userTotalCnt][userInShapeCnt].sum /
        data[userTotalCnt][userInShapeCnt].count;
      plotData.push({
        x: userInShapeCnt,
        y: avgServerResponseSum,
        type: "bar",
        name: `userInShapeCnt=${userInShapeCnt}`,
      });
    }

    plot(plotData, {
      xaxis: { title: "userInShapeCnt" },
      yaxis: { title: "Average serverResponse.sum" },
      title: `userTotalCnt=${userTotalCnt}`,
    });
  }
}

function userSum() {
  const inputData = JSON.parse(fs.readFileSync(inputFilePath, "utf-8"));
  const processedData = processData(inputData);
  generateGraph(processedData);
}

module.exports = userSum;