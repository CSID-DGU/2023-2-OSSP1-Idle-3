const fs = require("fs");
const { plot } = require("nodeplotlib");

const inputFilePath = "input.json";

function processData(inputData) {
  const resultData = {};
  const maxData = { y: -Infinity, data: null };
  const minData = { y: Infinity, data: null };
  const avgData = { y: 0, count: 0, data: null };

  for (const entry of inputData) {
    const userTotalCnt = entry.userTotalCnt;
    const serverResponse = entry.serverResponse;

    if (!resultData[userTotalCnt]) {
      resultData[userTotalCnt] = { sum: 0, count: 0 };
    }

    resultData[userTotalCnt].sum += serverResponse.sum;
    resultData[userTotalCnt].count += 1;

    const userSum = serverResponse.sum;
    if (userSum > maxData.y) {
      maxData.y = userSum;
      maxData.data = entry;
    }

    if (userSum < minData.y) {
      minData.y = userSum;
      minData.data = entry;
    }

    avgData.y += userSum;
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
      name: "Average userSum",
    },
  ];

  plot(plotData, {
    xaxis: { title: "userTotalCnt" },
    yaxis: { title: "Average userSum" },
    title: "Average userSum vs userTotalCnt",
  });
}

function total_userSum() {
  const inputData = JSON.parse(fs.readFileSync(inputFilePath, "utf-8"));
  const processedData = processData(inputData);

  fs.writeFileSync(
    "total_userSum.json",
    JSON.stringify(processedData.resultData, null, 2)
  );
  fs.writeFileSync(
    "result_max_total_userSum.json",
    JSON.stringify(processedData.maxData.data, null, 2)
  );
  fs.writeFileSync(
    "result_min_total_userSum.json",
    JSON.stringify(processedData.minData.data, null, 2)
  );
  fs.writeFileSync(
    "result_avg_total_userSum.json",
    JSON.stringify(processedData.avgData.data, null, 2)
  );

  generateGraph(processedData);
}

module.exports = total_userSum;
