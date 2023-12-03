/**
 * 볼록 n각형의 점들을 만들어내는 생성기
 * 위도, 경도의 최대 최소 범위를 조절하여
 * 해당 범위 내에 점이 생길 수 있도록 한다.
 */
export default class OvalConvexPointsGenerater { 

    /**
     * 높이 변하고, 너비도 변하는 랜덤한 단순 볼록 n각형 점 추출
     * @param {number} numVertices : 만들려는 n각형의 "n"
     * @returns {[{lat, lng}]} : 뽑힌 점{lat, lng} 의 배열
     */
    generate(numVertices, minLat, maxLat, minLng, maxLng) {
       let angles = this.sortedAngles(numVertices);
        // 위도와 경도의 범위에 따라 캔버스 상의 좌표를 계산
        let latRange = maxLat - minLat;
        let lngRange = maxLng - minLng;
        
        let points = angles.map(angle => {
            let latitude = parseFloat((Math.sin(angle) * latRange / 2 + (minLat + maxLat) / 2).toFixed(10));
            let longitude = parseFloat((Math.cos(angle) * lngRange / 2 + (minLng + maxLng) / 2).toFixed(10));    
            return { latitude, longitude };
        });
        return points;
    }

    /**
     * 높이 변하고, 너비는 고정시키서 랜덤 추출
     * @param {*} numVertices : 만들려는 n각형의 "n"
     * @param {*} minLat :최소 위도
     * @param {*} maxLat :최대 위도
     * @param {*} minLng :최소 경도, 반드시 최대 경도 범위보다 width만큼은 작아야 함.
     * @param {*} width  :고정 너비
     */
    generateFixedWidthSize(numVertices, minLat, maxLat, minLng, width) {
        let angles = this.sortedAngles(numVertices);
        let latRange = maxLat - minLat;

        let points = angles.map(angle => {
            let latitude = parseFloat((Math.sin(angle) * latRange / 2 + (minLat + maxLat) / 2).toFixed(10));
            let longitude = parseFloat((Math.cos(angle) * width / 2 + (minLng + minLng + width) / 2).toFixed(10));    
            return { latitude, longitude };
        });
        return points;
    }

     /**
     * 너비 변하고, 높이는 고정시키서 랜덤 추출
     * @param {*} numVertices : 만들려는 n각형의 "n"
     * @param {*} minLat :최소 위도 , 반드시 최대 위도 범위보다 height만큼은 작아야 함.
     * @param {*} height :높이
     * @param {*} minLng :최소 경도
     * @param {*} maxLng  :최대 경도
     */
     generateFixedHeightSize(numVertices, minLat, height, minLng, maxLng ) {
        let angles = this.sortedAngles(numVertices);
        let lngRange = maxLng - minLng;

        let points = angles.map(angle => {
            let latitude = parseFloat((Math.sin(angle) * height / 2 + (minLat + minLat + height) / 2).toFixed(10));
            let longitude = parseFloat((Math.cos(angle) * lngRange / 2 + (minLng + maxLng) / 2).toFixed(10));    
            return { latitude, longitude };
        });
        return points;
    }

    /**
     * 높이, 너비 고정해서 랜덤 추출, 내가 원하는 크기의 도형
     * @param {*} numVertices : 만들려는 n각형의 "n"
     * @param {*} minLat :최소 위도,  반드시 최대 위도 범위보다 height만큼은 작아야 함.
     * @param {*} height :높이
     * @param {*} minLng :최소 경도, 반드시 최대 경도 범위보다 width만큼은 작아야 함.
     * @param {*} width  :고정 너비
     */
    generateFixedSize(numVertices, minLat, height, minLng, width) {
        let angles = this.sortedAngles(numVertices);

        let points = angles.map(angle => {
            let latitude = parseFloat((Math.sin(angle) * height / 2 + (minLat + minLat + height) / 2).toFixed(10));
            let longitude = parseFloat((Math.cos(angle) * width / 2 + (minLng + minLng + width) / 2).toFixed(10));    
            return { latitude, longitude };
        });
        return points;
    }


    /**
     * 단순 볼록다각형을 만들기 위한 단위원 위의 랜덤한 점의 각도.
     * @param {*} numVertices : 점의 개수
     * @returns : 랜덤 각도이지만 오름차순으로 정렬
     */
    sortedAngles(numVertices) {
        let angles = [];
        for (let i = 0; i < numVertices; i++) {
            angles.push(Math.random() * 2 * Math.PI);
        }
        angles.sort();
        return angles;
    }
}
/* 
    const minLat = 37.413294, maxLat = 37.715133; // 위도 범위
    const minLng = 126.734086, maxLng = 127.269311; // 경도 범위
    supplier = new ConvexPointsGenerater(minLat, maxLat, minLng, maxLng);
    console.log(supplier.generate(3));
*/