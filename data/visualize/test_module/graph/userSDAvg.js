const fs = require("fs");
const { plot } = require("nodeplotlib");

function processData(inputData) {
  const resultData = {};

  for (const entry of inputData) {
    const userTotalCnt = entry.userTotalCnt;
    const userInShapeCnt = entry.userInShapeCnt;
    const userSDAvg = entry.userSDAvg;

    if (!resultData[userTotalCnt]) {
      resultData[userTotalCnt] = {};
    }

    if (!resultData[userTotalCnt][userInShapeCnt]) {
      resultData[userTotalCnt][userInShapeCnt] = { sum: 0, count: 0 };
    }

    resultData[userTotalCnt][userInShapeCnt].sum += userSDAvg;
    resultData[userTotalCnt][userInShapeCnt].count += 1;
  }

  const averages = {};

  for (const userTotalCnt in resultData) {
    averages[userTotalCnt] = {};

    for (const userInShapeCnt in resultData[userTotalCnt]) {
      averages[userTotalCnt][userInShapeCnt] =
        resultData[userTotalCnt][userInShapeCnt].sum /
        resultData[userTotalCnt][userInShapeCnt].count;
    }
  }

  return averages;
}

function generateGraph(data) {
  const plots = [];

  for (const userTotalCnt in data) {
    const xValues = Object.keys(data[userTotalCnt]);
    const yValues = Object.values(data[userTotalCnt]);

    plots.push({
      x: xValues,
      y: yValues,
      type: "line",
      name: `userTotalCnt=${userTotalCnt}`,
    });
  }

  plot(plots, {
    xaxis: { title: "userInShapeCnt" },
    yaxis: { title: "Average userSDAvg" },
    title: "Average userSDAvg vs userInShapeCnt for different userTotalCnt",
  });
}

function main() {
  const inputData = JSON.parse(fs.readFileSync("input.json", "utf-8"));

  const processedData = processData(inputData);

  fs.writeFileSync("userSDAvg.json", JSON.stringify(processedData, null, 2));

  generateGraph(processedData);
}

main();
