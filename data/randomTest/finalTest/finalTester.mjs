import {APISender, HTTPResponseError} from "../APISender.mjs"
import ConvexInsidePointGenerator from "../randomSampling/generator/ConvexInsidePointGenerator.mjs";
import wait from "waait";
import fs from "fs";

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
        // this.drawer = new Drawer(500, 500, minLat, maxLat, minLng, maxLng);
    }

    async testRegularPolygon(n, inside, uri, testCase) {
        let promises = [];
        let result = [];
        let completed = 0;

        for (let i = 0 ; i < testCase ; i++) {
            try {
                await wait(300);
                const dots = this.supplier.generateRegularPolygon(n, inside, this.deltaLatitude, this.deltaLongitude);
                let promise = this.sender.requestTest(uri, dots)
                .then( response => {
                    result.push({
                        "index" : i,
                        "n": 3,
                        "inside" : inside,
                        "start": dots,
                        ...response
                    });
                    completed++;
                    console.log(`Progress: ${completed}\n`);
                });
                promises.push(promise);
            }catch (err) {
                i--;
                console.log(err);
            }
        }
        await Promise.all(promises);
        fs.writeFileSync(`regularPolygon.json` ,JSON.stringify(result), 'utf8');
        return result;
    }

    testFarwayPolygon(n) {
        const result = this.supplier.generateOneFromAnother(n, this.deltaLatitude, this.deltaLongitude,
            this.deltaLatitude  * 7, this.deltaLongitude * 7,
            this.deltaLatitude * 3, this.deltaLongitude * 3
        );
        return result;
    }

    testIsoscelesTriangleAngles() {
        const result = this.supplier.generateIsoscelesTriangle();
        return result;
    }
}