const fs = require("fs");

const input = [];

const saveDataToArray = (requestData, responseData, iteration) => {
  const data = {
    iteration,
    userTotalCnt: requestData.userTotalCnt,
    userInShapeCnt: requestData.userInShapeCnt,
    userPosition: requestData.userPosition,
    serverResponse: responseData,
  };

  input.push(data);
};

// 클라이언트에서 서버로 데이터를 전송하는 함수
const sendDataToServer = async (iteration) => {
  const url = "http://localhost:3000/calculateData";

  // 클라이언트에서 전송할 데이터 가져오기
  // const userTotalCnt =
  // const userInShapeCnt =
  // const userPosition =

  const requestData = {
    userTotalCnt,
    userInShapeCnt,
    userPosition,
  };

  try {
    const response = await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    });

    if (!response.ok) {
      throw new Error("Network response was not ok");
    }

    const responseData = await response.json();
    console.log(responseData);

    saveDataToArray(requestData, responseData, iteration);
  } catch (error) {
    console.error(
      `There was a problem with the fetch operation (iteration ${iteration}):`,
      error.message
    );
  }
};

for (let i = 1; i <= 100; i++) {
  sendDataToServer(i);
}

const jsonData = JSON.stringify(input, null, 2);
fs.writeFile("input.json", jsonData, "utf8", (err) => {
  if (err) {
    console.error("Error writing to input.json:", err);
  } else {
    console.log("Data has been written to input.json");
  }
});

module.exports = function get_result() {
  
}
