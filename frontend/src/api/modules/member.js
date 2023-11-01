import { apiInstance } from "../index";

const api = apiInstance();

// [GET] my-page 필요한 회원정보 모두 조회
async function getMemberInfo(success, fail) {
  await api.get(`/member/info`).then(success).catch(fail);
}

async function getLoginMember(id, success, fail) {
  await api.get(`/member/${id}`).then(success).catch(fail);
}

async function logout(id, success, fail) {
  await api.post(`/member/logout/${id}`).then(success).catch(fail);
}

export { getMemberInfo, getLoginMember, logout };
