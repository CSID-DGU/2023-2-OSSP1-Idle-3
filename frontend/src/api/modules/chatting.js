import { apiInstance } from "../index";

// api instance 가져오기
const api = apiInstance();

async function getChatting(meetingId) {
  var result = null;
  await api
    .get(`/chat/${meetingId}`)
    .then((res) => {
      result = res;
    })
    .catch((e) => {
      e;
    });
  return await Promise.resolve(result);
}

async function getChattingLog(meetingId, lastNumber) {
  var result = null;
  await api
    .get(`/chat/${meetingId}/${lastNumber}`)
    .then((res) => {
      result = res;
    })
    .catch((e) => {
      e;
    });
  return await Promise.resolve(result);
}

export { getChatting, getChattingLog };
