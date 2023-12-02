// 점이 다각형 내부에 있는지 확인하는 함수
function isInsidePolygon(lat, lng, polygon) {
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

function getRandomLat(minLat, maxLat){
    return Math.random() * (maxLat - minLat + 1) + minLat;
}

function getRandomLng(minLng, maxLng){
    return Math.random() * (maxLng - minLng + 1) + minLng;
}

// 점 구하기
// polygon: 도형 좌표 배열
// count: 찍을 점 개수
function getRandomPosInPolygon(polygon, count){
    let minLat = 1000;
    let maxLat = 0;
    let minLng = 1000;
    let maxLng = 0 ;
    // lat, lng의 최소 최대값 찾기
    for(i=0; i<polygon.length;i++){
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
        while(!isInside){
            randLat = getRandomLat(minLat, maxLat);
            randLng = getRandomLng(minLng, maxLng);
            isInside = isInsidePolygon(randLat, randLng, polygon);
        }
        selected_dots.push({ lat: randLat, lng: randLng});
    }
    
    return selected_dots;
}

/* 예시
var polygon = [
    { lat: 2, lng: 2 },
    { lat: 6, lng: 2 },
    { lat: 9, lng: 4 },
    { lat: 6, lng: 6 },
    { lat: 4, lng: 8 },
    { lat: 2, lng: 6 }
];
const count = 3;
*/

// 도형 내의 점 배열
let selected_dots = getRandomPosInPolygon(polygon, count);