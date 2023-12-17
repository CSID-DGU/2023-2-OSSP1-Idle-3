import { apiInstance } from "../index";

const api = apiInstance();

async function meetingRegister(
  id,
  name,
  date_time,
  place,
  address,
  lat,
  lng,
  success,
  fail
) {
  await api
    .post(`/meeting`, {
      hostId: id,
      meetingName: name,
      meetingTime: date_time,
      meetingPlace: place,
      meetingAddress: address,
      meetingLat: lat,
      meetingLng: lng,
      roomCode: "",
    })
    .then(success)
    .catch(fail);
}
async function modifyMeeting(
  meeitng_id,
  host_id,
  name,
  date_time,
  place,
  address,
  lat,
  lng,
  amount,
  success,
  fail
) {
  await api
    .put(`/meeting`, {
      meetingId: meeitng_id,
      hostId: host_id,
      meetingName: name,
      meetingTime: date_time,
      meetingPlace: place,
      meetingAddress: address,
      meetingLat: lat,
      meetingLng: lng,
      lateAmount: amount,
    })
    .then(success)
    .catch(fail);
}

async function outMeeting(member_id, meeting_id, success, fail) {
  await api
    .put("/meeting/exit", {
      memberId: member_id,
      meetingId: meeting_id,
    })
    .then(success)
    .catch(fail);
}

async function deleteMeeting(meetingId) {
  var result = null;
  await api
    .delete(`/meeting/${meetingId}`)
    .then((res) => {
      result = res.data.data;
    })
    .catch((err) => {
      err;
    });
  return await Promise.resolve(result);
}

async function getMeeting(meetingId) {
  var result = null;
  await api.get(`/meeting/detail/${meetingId}`).then((res) => {
    if (res.data.statusCode == "404") {
      result = null;
    }
    result = res.data.data;
  });
  return await Promise.resolve(result);
}

async function getTodayMeetings() {
  var result = null;
  await api
    .get("/meeting/today")
    .then((res) => {
      result = res.data.data;
    })
    .catch((err) => {
      err;
    });
  return await Promise.resolve(result);
}

async function getUpcomingMeetings() {
  var result = null;
  await api
    .get("/meeting/upcoming")
    .then((res) => {
      result = res.data.data;
    })
    .catch((err) => {
      err;
    });
  return await Promise.resolve(result);
}

function getMostRecentMeeting() {
  return new Promise((resolve, reject) => {
    api
      .get("/meeting/most-recent")
      .then((response) => {
        resolve(response.data.data);
      })
      .catch((error) => {
        reject(error);
      });
  });
}

// [GET] 현재 시각으로부터 지난 가장 최근의 모임
function getRecentPastMeeting() {
  return new Promise((resolve, reject) => {
    api
      .get("/meeting/past-recent")
      .then((response) => {
        resolve(response.data.data);
      })
      .catch((error) => {
        reject(error);
      });
  });
}

async function getBestMember() {
  var result = null;
  await api
    .get("/member/best-member")
    .then((res) => {
      result = res.data.data;
    })
    .catch((err) => {
      err;
    });
  return await Promise.resolve(result);
}

async function postReceiptInfo(receipt) {
  var result = null;

  var formData = new FormData();
  formData.append("receipt", receipt);

  await api
    .post(`/meeting-calculate/receipt`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    })
    .then(async (res) => {
      if (res.data.statusCode == 200) {
        result = res.data.data;
      } else {
        result = null;
      }
    })
    .catch();
  return await Promise.resolve(result);
}

async function saveCalculateDetail(meetingId, receipt, storeName, totalPrice) {
  var result = false;
  var formData = new FormData();
  formData.append("meetingId", meetingId);
  formData.append("receipt", receipt);
  formData.append("storeName", storeName);
  formData.append("price", totalPrice);
  await api
    .post(`/meeting-calculate/detail`, formData, {
      headers: { "Content-Type": "multipart/form-data" },
    })
    .then(async () => {
      result = true;
    });
  return await Promise.resolve(result);
}

async function deleteCalculateDetail(calculateDetailId) {
  var result = false;
  await api
    .delete(`/meeting-calculate/${calculateDetailId}`, {})
    .then(async () => {
      result = true;
    });
  return await Promise.resolve(result);
}

// [POST] room-id, member에 따른 출발장소 저장
async function saveMemberStartPlace(startPlaceInfo) {
  var result = null;

  await api
    .post(`/meeting/start-place`, JSON.stringify(startPlaceInfo))
    .then((res) => {
      // console.log("#[meeting]# api - response 확인: ", res);
      result = res;
    })
    .catch((error) => {
      error
      // console.log("#[meeting]# 출발장소 저장 error: ", error);
    });

  return await Promise.resolve(result);
}

// input : 
async function getCenterWay(startPlaceInfo){
  var result = null;

  await api.post('/middleSpace/testCenterTimeDistance',JSON.stringify(startPlaceInfo))
  .then((res) => {
    // console.log("#[meeting]# api - response 확인: ", res);
    result = res;
  })
  .catch((error) => {
    error
    // console.log("#[meeting]# 출발장소 저장 error: ", error);
  });

  return await Promise.resolve(result);
}


async function getIntervalWay(startPlaceInfo){

}

async function getTotalTimeWay(startPlaceInfo){
  var result = null;

}

export {
  meetingRegister,
  getMeeting,
  getTodayMeetings,
  getUpcomingMeetings,
  getMostRecentMeeting,
  getBestMember,
  postReceiptInfo,
  saveCalculateDetail,
  deleteCalculateDetail,
  modifyMeeting,
  saveMemberStartPlace,
  outMeeting,
  getRecentPastMeeting,
  deleteMeeting,
  getCenterWay,
  getIntervalWay,
  getTotalTimeWay
};
