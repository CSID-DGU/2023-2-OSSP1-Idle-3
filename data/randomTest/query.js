import {APISender, HTTPResponseError} from "./APISender.mjs"
import ConvexInsidePointGenerator from "./randomSampling/generator/ConvexInsidePointGenerator.mjs";
import fs from "fs"
import wait from "waait";

const hostname = "localhost";
const protocol = "http";
const port = "8080";

async function test() {
    // 서울 위도 경도
    const minLat = 37.413294, maxLat = 37.715133; // 위도 범위
    const minLng = 126.734086, maxLng = 127.269311; // 경도 범위

    // API 전송 객체
    const sender =  new APISender(protocol, hostname, port);
    // 현재는 서울에서 뽑게 하였음.
    const suplier = new ConvexInsidePointGenerator(minLat, maxLat, minLng, maxLng);

    // 첫번쨰 입력 인자로 보낼 uri 받음
    const type = process.argv[2];
    // 두번쨰 입력 인자로 몇 각형 테스트 할지 입력
    const n = parseInt(process.argv[3]);
    // 세번째 입력 인자로 내부 점의 개수 입력
    const insideDots = parseInt(process.argv[4]); 
    // 네번쨰 입력 인자로 몇 개 테스트 할지 입력
    const batchSizes = parseInt(process.argv[5]);

    let completed = 0;
    let result = [];
    let requestPromises = [];
    for (let i = 0 ; i < batchSizes ; i++) {
        await wait(300);
        try {
            let data = suplier.generate(n, insideDots);
            let promise = sender.requestTest(type, data)
            .then( response => {
                result.push({
                    "inputPoints": data,
                    ...response
                });
                completed++;
                console.log(`Progress: ${completed}\r`);
            });
            requestPromises.push(promise);
        }catch (err) {
            i--;
            console.log(err);
        }
    }

    Promise.all(requestPromises)
    .then(() => {
        // let minGap = result[0].answer.gap,  maxGap = result[0].answer.gap;
        // let minSum = result[0].answer.sum, maxSum = result[0].answer.sum;
        // for (let item of result) {
        //     if (minGap > item.min)
        // }



    fs.writeFileSync(`./testResult/${type}_${n}_${insideDots}_${batchSizes}.json`, JSON.stringify({
        'algorithm': type,
        'polygon': n,
        'insideDots': insideDots,
        'result' : [...result]
        }));
    })
    .catch(err => {
        console.log(err);
    })
}

test();