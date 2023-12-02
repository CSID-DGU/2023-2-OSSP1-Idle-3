const fs = require("fs");
const { plot } = require("nodeplotlib");

function processData(inputData) {
  const resultData = {};

  for (const entry of inputData) {
    const userTotalCnt = entry.userTotalCnt;
    const userSum = entry.userSum;

    if (!resultData[userTotalCnt]) {
      resultData[userTotalCnt] = { sum: 0, count: 0 };
    }

    resultData[userTotalCnt].sum += userSum;
    resultData[userTotalCnt].count += 1;
  }

  const averages = {};

  for (const userTotalCnt in resultData) {
    averages[userTotalCnt] =
      resultData[userTotalCnt].sum / resultData[userTotalCnt].count;
  }

  return averages;
}

function generateGraph(data) {
  const xValues = Object.keys(data);
  const yValues = Object.values(data);

  const plotData = [
    {
      x: xValues,
      y: yValues,
      type: "line",
      name: "Average userSum",
    },
  ];

  plot(plotData, {
    xaxis: { title: "userTotalCnt" },
    yaxis: { title: "Average userSum" },
    title: "Average userSum vs userTotalCnt",
  });
}

function main() {
  const inputData = JSON.parse(fs.readFileSync("input.json", "utf-8"));

  const processedData = processData(inputData);

  fs.writeFileSync(
    "total_userSUM.json",
    JSON.stringify(processedData, null, 2)
  );

  generateGraph(processedData);
}

main();
