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
            let latitude =  parseFloat((Math.sin(angle) * latRange / 2 + (minLat + maxLat) / 2).toFixed(10));
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
            let latitude = (Math.sin(angle) * latRange / 2 + (minLat + maxLat) / 2)
            let longitude = (Math.cos(angle) * width / 2 + (minLng + minLng + width) / 2)    
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
            let latitude = (Math.sin(angle) * height / 2 + (minLat + minLat + height) / 2)
            let longitude = (Math.cos(angle) * lngRange / 2 + (minLng + maxLng) / 2)    
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
            let latitude = (Math.sin(angle) * height / 2 + (minLat + minLat + height) / 2)
            let longitude = (Math.cos(angle) * width / 2 + (minLng + minLng + width) / 2)    
            return { latitude, longitude };
        });
        return points;
    }

    /**
     * @param {integer} numVertices 
     * @param {number} minLat : 최소 위도
     * @param {number} minLng : 최소 경도
     * @param {number} maxLat : 최대 위도
     * @param {number} maxLng : 최대 경도
     */
    generateRegularPolygon(numVertices, minLat, minLng, maxLat, maxLng) {
        let angles = this.regularAngles(numVertices);
        let latRange = maxLat - minLat;
        let lngRange = maxLng - minLng;

        let points = angles.map(angle=> {
            let latitude = (Math.sin(angle) * latRange / 2 + (minLat + maxLat) / 2)
            let longitude = (Math.cos(angle) * lngRange / 2 + (minLng + maxLng) / 2)    
            return { latitude, longitude };
        })
        return points;
    }

    /**
     * 서로 반대로 떨어진 두 점을 생성한다.
     * @param {*} minLat 
     * @param {*} minLng 
     * @param {*} maxLat 
     * @param {*} maxLng 
     */
    generateOppositeDots(minLat, minLng, maxLat, maxLng) {
        let opposites = this.oppositeAngles();
        let latRange = maxLat - minLat;
        let lngRange = maxLng - minLng;

        let points = opposites.map(angle=> {
            let latitude = (Math.sin(angle) * latRange / 2 + (minLat + maxLat) / 2)
            let longitude = (Math.cos(angle) * lngRange / 2 + (minLng + maxLng) / 2)    
            return { latitude, longitude };
        })
        return points;
    }

    generateIsoscelesTriangle(minLat, minLng, maxLat, maxLng) {
        let angles = this.isoscelesTriangleAngles();
        let latRange = maxLat - minLat;
        let lngRange = maxLng - minLng;

        let points = angles.map(angle=> {
            let latitude = (Math.sin(angle) * latRange / 2 + (minLat + maxLat) / 2)
            let longitude = (Math.cos(angle) * lngRange / 2 + (minLng + maxLng) / 2)    
            return { latitude, longitude };
        })
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
    /**
     * 모두가 동일한 각도로 떨어져 있는 각도들
     * @param {*} numVertices : 점의 개수
     * @returns : 정 다각형을 이루는 점들을 만들기 위한 각도들
     */
    regularAngles(numVertices) {
        let angles = [];
        let firstAngle = Math.random() * 2 * Math.PI;
        angles.push(firstAngle);
        for (let i = 1; i < numVertices; i++) {
            angles.push(firstAngle + i * (2 * Math.PI) / numVertices);
        }
        angles.sort();
        return angles;
    }

    /**
     * 정 반대의 두 점을 생성함
     * @returns : [] 
     */
    oppositeAngles() {
        let angles = [];
        let firstAngle = Math.random() * 2 * Math.PI;
        angles.push(firstAngle);
        angles.push(firstAngle + Math.PI);
        return angles;
    }

    /**
     * 이등변 삼각형을 만들기 위한 점 생성함
     */
    isoscelesTriangleAngles() {
        let angles = [];
        let firstAngle = Math.random() * 2 * Math.PI;
        angles.push(firstAngle);
        const seta = this.getRandomBetween(2*Math.PI * (1/3), Math.PI)
        angles.push(firstAngle + seta);
        angles.push(firstAngle - seta);
        angles.sort();
        return angles;
    }

    getRandomBetween(min, max){
        return Math.random() * (max - min) + min;
    }
}
/* 
    const minLat = 37.413294, maxLat = 37.715133; // 위도 범위
    const minLng = 126.734086, maxLng = 127.269311; // 경도 범위
    supplier = new ConvexPointsGenerater(minLat, maxLat, minLng, maxLng);
    console.log(supplier.generate(3));
*/
