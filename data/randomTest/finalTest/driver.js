import FinalTester from "./finalTester.mjs";

// 서울 위도 경도
const minLat = 37.413294, maxLat = 37.715133; // 위도 범위
const minLng = 126.734086, maxLng = 127.269311; // 경도 범위

const tester = new FinalTester(minLat, maxLat, minLng, maxLng);


tester.testRegularPolygon(3, 0, 'testAll', 3);

console.log('done');