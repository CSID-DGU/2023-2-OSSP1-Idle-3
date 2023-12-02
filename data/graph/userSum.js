const fs = require("fs");
const { plot } = require("nodeplotlib");

function process_data(data) {
  const results = {};

  for (const entry of data) {
    const userTotalCnt = entry.userTotalCnt;
    const userInShapeCnt = entry.userInShapeCnt;
    const userSum = entry.userSum;

    if (!results[userTotalCnt]) {
      results[userTotalCnt] = {};
    }

    if (!results[userTotalCnt][userInShapeCnt]) {
      results[userTotalCnt][userInShapeCnt] = { sum: 0, count: 0 };
    }

    results[userTotalCnt][userInShapeCnt].sum += userSum;
    results[userTotalCnt][userInShapeCnt].count += 1;
  }

  const averages = {};
  for (const userTotalCnt in results) {
    averages[userTotalCnt] = {};
    for (const userInShapeCnt in results[userTotalCnt]) {
      averages[userTotalCnt][userInShapeCnt] =
        results[userTotalCnt][userInShapeCnt].sum /
        results[userTotalCnt][userInShapeCnt].count;
    }
  }

  return averages;
}

function generate_graph(data) {
  const plots = [];

  for (const userTotalCnt in data) {
    const x_values = Object.keys(data[userTotalCnt]);
    const y_values = Object.values(data[userTotalCnt]);

    plots.push({
      x: x_values,
      y: y_values,
      type: "line",
      name: `userTotalCnt=${userTotalCnt}`,
    });
  }

  plot(plots, {
    xaxis: { title: "userInShapeCnt" },
    yaxis: { title: "Average userSum" },
    title: "Average userSum vs userInShapeCnt for different userTotalCnt",
  });
}

function main() {
  const input_data = JSON.parse(fs.readFileSync("input.json", "utf-8"));

  const processed_data = process_data(input_data);

  fs.writeFileSync(
    "userSum.json",
    JSON.stringify({ userTotalCntGroups: processed_data }, null, 2)
  );

  generate_graph(processed_data);
}

main();
