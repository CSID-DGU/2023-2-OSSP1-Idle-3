import {APISender, HTTPResponseError} from "../APISender.mjs"
import ConvexInsidePointGenerator from "../randomSampling/generator/ConvexInsidePointGenerator.mjs";
// import Drawer from "../randomSampling/Drawer";
// const Drawer = require("../randomSampling/Drawer");

const hostname = "localhost";
const protocol = "http";
const port = "8080";    

/* 서울 위도가 37라고 가정 */
const deltaLongitude = 0.01126;
const deltaLatitude = 0.00901;


export default class FinalTester {
    constructor(minLat, maxLat, minLng, maxLng) {
        this.minLat = minLat; 
        this.maxLat = maxLat;
        this.minLng = minLng;
        this.maxLng = maxLng;
        /* 서울 위도 37도 가정 */
        this.deltaLongitude = 0.01125;
        this.deltaLatitude = 0.00901;

        this.supplier = new ConvexInsidePointGenerator(minLat, maxLat, minLng, maxLng);
        this.sender = new APISender(protocol, hostname, port);
        this.drawer = new Drawer(500, 500, minLat, maxLat, minLng, maxLng);
    }

    async testRegularPolygon(n, inside) {
        const result = this.supplier.generateRegularPolygon(n, inside, this.deltaLatitude, this.deltaLongitude);
    }

    async testFarwayPolygon(n) {
        const result = this.supplier.generateOneFromAnother(n, this.deltaLatitude, this.deltaLongitude,
            this.deltaLatitude  * 10, this.deltaLongitude * 10,
            this.deltaLatitude * 5, this.deltaLongitude * 5
        );
        return result;
    }

    async testFlatHorizonalPolygon(n, inside) {
        const result = this.supplier.generate(n, 0);
    }
}