// const createCanvas  = require('canvas');
// const fs = require('fs');
import {createCanvas} from 'canvas'
import fs from 'fs';

/**
 * 만들어진 점의 위치 관계를 테스트하기 위한 객체
 */
class Drawer {
    constructor(canvasWidth, canvasHeight, minLat, maxLat, minLng, maxLng) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        // 위도 범위, 경도 범위
        this.minLat = minLat;
        this.maxLat = maxLat;
        this.minLng = minLng;
        this.maxLng = maxLng; 
        // 범위 계산
        this.latRange = this.maxLat - this.minLat;
        this.lngRange = this.maxLng - this.minLng;
        this.id = 1;
    }
    /**
     * 다각형안에 점이 있는 모습을 확인하는 함수
     * insideConvex{Id} 파일에 결과를 저장함
     * @param {*} polygon 
     * @param {*} insideDots 
     */
    async drawInsideConvex(polygon, insideDots) {    
        const canvas = createCanvas(this.canvasWidth, this.canvasHeight);
        const ctx = canvas.getContext('2d');
        // 배경을 흰색으로 설정
        ctx.fillStyle = 'white';
        ctx.fillRect(0, 0, canvas.width, canvas.height);

        // polygon 에 있는 점들 inside에 있는 점들 모두 canvas에 맞게 바꾸기
        let polygonCanvas = polygon.map(dot => {
            let lat = (dot.lat - this.minLat) / this.latRange * this.canvasHeight;
            let lng = (dot.lng - this.minLng) / this.lngRange * this.canvasWidth;
    
            return { lat, lng };
        });
    
        let insideDotsCanvas = insideDots.map(dot => {
            let lat = (dot.lat - this.minLat) / this.latRange * this.canvasHeight;
            let lng = (dot.lng - this.minLng) / this.lngRange * this.canvasWidth;
    
            return { lat, lng };
        });
        console.log('after convereteed')

        // 다각형 그리기
        ctx.beginPath(); // 이 부분이 추가되어야 합니다
        ctx.moveTo(polygonCanvas[0].lng, polygonCanvas[0].lat);
        polygonCanvas.forEach(point => ctx.lineTo(point.lng, point.lat));
        ctx.closePath();
        ctx.stroke();

        console.log('after draw out')

        // 내부 점 표현
        insideDotsCanvas.forEach(dot => {
            ctx.beginPath();
            ctx.arc(dot.lng, dot.lat, 1, 0, 2 * Math.PI); // 작은 원으로 점 표현
            ctx.fillStyle = 'black'; // 원의 채우기 색상을 검은색으로 설정
            ctx.fill();
        });

        console.log('after draw in')
        
        const out = fs.createWriteStream(`C:/intellij/idle_almost_there/data/randomTest/finalTest/result/insideConvex${this.id}.png`);
        const stream = canvas.createPNGStream();
        stream.pipe(out);
        out.on('finish', () => console.log('The PNG file was created.'));
    }
};

module.exports = Drawer;

