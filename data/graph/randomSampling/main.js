const {ConvexInsidePointGenerator} = require("./generator/ConvexInsidePointGenerator");

const minLat = 37.413294, maxLat = 37.715133; // 위도 범위
const minLng = 126.734086, maxLng = 127.269311; // 경도 범위

const supplier = new ConvexInsidePointGenerator(minLat, maxLat, minLng, maxLng);

console.log(supplier.generate(4, 2));