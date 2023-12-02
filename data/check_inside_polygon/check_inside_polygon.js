/*
  예시 데이터

  // 점의 좌표
  var testPointX = 4;
  var testPointY = 0;

  // 다각형의 꼭지점 좌표(시계방향)
  var polygonVertices = [
      { lat: 2, lng: 2 },
      { lat: 6, lng: 2 },
      { lat: 9, lng: 4 },
      { lat: 6, lng: 6 },
      { lat: 4, lng: 8 },
      { lat: 2, lng: 6 }
  ];
*/

// lat, lng: 확인하고자 하는 점
// polygon: 도형 배열(시계방향)
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
