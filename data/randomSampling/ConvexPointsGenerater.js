/**
 * 볼록 n각형의 점들을 만들어내는 생성기
 * 위도, 경도의 최대 최소 범위를 조절하여
 * 해당 범위 내에 점이 생길 수 있도록 한다.
 */
module.exports = class ConvexPointsGenerater {
    constructor(minLat, maxLat, minLng, maxLng) {
        // 위도 범위
        this.minLat = minLat;
        this.maxLat = maxLat;
        // 경도 범위
        this.minLng = minLng;
        this.maxLng = maxLng; 
    }

    /**
     * n각형을 이루는 
     * @param {number} numVertices : 만들려는 n각형의 "n"
     * @returns {[{lat, lng}]} : {lat, lng} 의 집합
     */
    generate(numVertices) {
        let angles = [];
        for (let i = 0; i < numVertices; i++) {
            angles.push(Math.random() * 2 * Math.PI);
        }
        angles.sort();
    
        // 위도와 경도의 범위에 따라 캔버스 상의 좌표를 계산
        let latRange = this.maxLat - this.minLat;
        let lngRange = this.maxLng - this.minLng;
        
        let points = angles.map(angle => {
            let lat = parseFloat((Math.cos(angle) * latRange / 2 + (this.minLat + this.maxLat) / 2).toFixed(10));
            let lng = parseFloat((Math.sin(angle) * lngRange / 2 + (this.minLng + this.maxLng) / 2).toFixed(10));    
            return { lat, lng };
        });
        return points;
    }
}
/* 
const minLat = 37.413294, maxLat = 37.715133; // 위도 범위
const minLng = 126.734086, maxLng = 127.269311; // 경도 범위
supplier = new ConvexPointsGenerater(minLat, maxLat, minLng, maxLng);
console.log(supplier.generate(3)); */

