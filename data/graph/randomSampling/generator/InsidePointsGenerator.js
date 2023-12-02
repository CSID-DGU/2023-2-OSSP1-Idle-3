module.exports = class InsidePointsGenerator {
    constructor() {}
    
    getRandomLat(minLat, maxLat){
        return Math.random() * (maxLat - minLat + 1) + minLat;
    }
    
    getRandomLng(minLng, maxLng){
        return Math.random() * (maxLng - minLng + 1) + minLng;
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
        let maxLng = 0 ;
        // lat, lng의 최소 최대값 찾기
        for(let i=0; i<polygon.length;i++){
            if(minLat > polygon[i].lat){
                minLat = polygon[i].lat;
            } else if(maxLat < polygon[i].lat) {
                maxLat = polygon[i].lat;
            }
            if(minLng > polygon[i].lng){
                minLng = polygon[i].lng;
            } else if(maxLng < polygon[i].lng) {
                maxLng = polygon[i].lng;
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
                isInside = this.isInsidePolygon(randLat, randLng, polygon);
            }
            if (test == tryNumber) throw Error("fail");
            selected_dots.push({ lat: randLat, lng: randLng});
        }
        
        return selected_dots;
    }


    // 점이 다각형 내부에 있는지 확인하는 함수
    isInsidePolygon(lat, lng, polygon) {
        var isInside = false;
        var i, j = polygon.length - 1;

        for (i = 0; i < polygon.length; i++) {
            if ((polygon[i].lng > lng) !== (polygon[j].lng > lng) &&
                lat < (polygon[j].lat - polygon[i].lat) * (lng - polygon[i].lng) / (polygon[j].lng - polygon[i].lng) + polygon[i].lat) {
                isInside = !isInside;
            }
            j = i;
        }
        return isInside;
    }
}