const ConvexPointsGenerater = require('./ConvexPointsGenerater');
const Drawer = require('./Drawer');

// import Drawer from './Drawer';

const minLat = 37.413294, maxLat = 37.715133; // 위도 범위
const minLng = 126.734086, maxLng = 127.269311; // 경도 범위


const supplier = new ConvexPointsGenerater(minLat, maxLat, minLng, maxLng);
const drawer = new Drawer(400, 400, minLat, maxLat, minLng, maxLng);

drawer.drawInsideConvex(supplier.generate(3), []);