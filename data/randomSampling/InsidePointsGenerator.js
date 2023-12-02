module.exports = class InsidePointsGenerator {
    constructor(minLat, maxLat, minLng, maxLng) {
        // 위도 범위
        this.minLat = minLat;
        this.maxLat = maxLat;
        // 경도 범위
        this.minLng = minLng;
        this.maxLng = maxLng; 
    }
}