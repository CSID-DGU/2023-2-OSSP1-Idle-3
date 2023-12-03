export default class InsidePointsGenerator {
    constructor() {}
    
    getRandomLat(minLat, maxLat){
        // console.log(maxLat - minLat);
        return Math.random() * (maxLat - minLat) + minLat;
    }
    
    getRandomLng(minLng, maxLng){
        return Math.random() * (maxLng - minLng) + minLng;
    }

    /**
     * 다각형 내부의 점을 랜덤으로 생성합니다.
     * 1000번 시도시 Error를 발생시킨다. 
     * @param {*} polygon : 들어갈 다각형의 좌표들 (시계 방향 정렬)
     * @param {*} count : 출력할 내부 점의 개수
     * @returns : 랜덤으로 뽑힌 내부 점의 좌표들
     */
    generate(polygon, count, tryNumber = 1000){
        let minLat = 1000;
        let maxLat = 0;
        let minLng = 1000;
        let maxLng = 0;
        // lat, lng의 최소 최대값 찾기
        for(let i=0; i<polygon.length;i++){
            if(minLat > polygon[i].latitude){
                minLat = polygon[i].latitude;
            } else if(maxLat < polygon[i].latitude) {
                maxLat = polygon[i].latitude;
            }
            if(minLng > polygon[i].longitude){
                minLng = polygon[i].longitude;
            } else if(maxLng < polygon[i].longitude) {
                maxLng = polygon[i].longitude;
            }
        }
    
        let selected_dots = [];
        // 도형 내에 찍을 점 추가
        for (let i=0;i<count;i++){
            let randLat;
            let randLng;
            let isInside = false;
            let test = 0
            while(!isInside && test < tryNumber){
                test++;
                randLat = this.getRandomLat(minLat, maxLat);
                randLng = this.getRandomLng(minLng, maxLng);
                // console.log(randLat, randLng);
                isInside = this.isInsidePolygon(randLat, randLng, polygon);
            }
            if (test == tryNumber) throw Error("fail");
            selected_dots.push({ latitude: randLat, longitude: randLng});
        }
        
        return selected_dots;
    }


    ccw(a, b, p) {
        let lat = p[0], lng = p[1];
        let k = a.latitude*b.longitude + b.latitude*lng + lat*a.longitude
        - b.latitude*a.longitude - lat*b.longitude - a.latitude*lng;
        if(k>0) return 1;
        if(k<0) return -1;
        return 0;
    }

    // 점이 다각형 내부에 있는지 확인하는 함수
    isInsidePolygon(lat, lng, polygon) {
        let one = false, mone = false;
        let point = [lat, lng];
        for (let i=0;i<polygon.length;i++){

            let k = this.ccw(polygon[i], polygon[(i+1)%polygon.length], point);
            if(k==1){
                one = true;
            }
            if(k==-1){
                mone = true;
            }
        }
        return !(one && mone);
    }
}