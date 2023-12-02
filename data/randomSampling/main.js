const ConvexPointsGenerater = require('./ConvexPointsGenerater');
const InsidePointsGenerator = require('./InsidePointsGenerator');
const Drawer = require('./Drawer');

// import Drawer from './Drawer';

const minLat = 37.413294, maxLat = 37.715133; // 위도 범위
const minLng = 126.734086, maxLng = 127.269311; // 경도 범위


const polygonSupplier = new ConvexPointsGenerater(minLat, maxLat, minLng, maxLng);
const insideDotSupplier = new InsidePointsGenerator(minLat, maxLat, minLng, maxLng);

const drawer = new Drawer(400, 400, minLat, maxLat, minLng, maxLng);

function testGenerator(n, insidePoints) {
    polygon = polygonSupplier.generate(n);
    insidePoints = insideDotSupplier.generate(polygon, insidePoints);
    drawer.drawInsideConvex(polygon, insidePoints);
}

testGenerator(4, 2);