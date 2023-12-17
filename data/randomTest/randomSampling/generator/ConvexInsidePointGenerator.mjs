import OvalConvexPointsGenerater from "./OvalConvexPointsGenerater.mjs";
import InsidePointsGenerator from "./InsidePointsGenerator.mjs";

export default class ConvexInsidePointGenerator {
    constructor(minLat, maxLat, minLng, maxLng) {
        // 위도 범위
        this.minLat = minLat;
        this.maxLat = maxLat;
        // 경도 범위
        this.minLng = minLng;
        this.maxLng = maxLng;
        // 볼록다각형과 그 안에 점을 생성하는 생성기
        this.polygonSupplier = new OvalConvexPointsGenerater();
        this.insideDotSupplier = new InsidePointsGenerator();
    }

    /**
     * @param {number} n : n각형의 n 
     * @param {number} insideDotNumber : 내부 점의 개수
     * @Param (number) tryNumber : 랜덤 시도 횟수
     */
    generate(n, insideDotNumber, tryNumber = 1000) {

        const minLocalLat = this.getRandomBetween(this.minLat, this.maxLat)
        const maxLocalLat = this.getRandomBetween(minLocalLat, this.maxLat)

        const minLocalLng = this.getRandomBetween(this.minLng, this.maxLng)
        const maxLocalLng = this.getRandomBetween(minLocalLng, this.maxLng)

        const polygon = this.polygonSupplier.generate(n, minLocalLat, maxLocalLat, minLocalLng, maxLocalLng);
        for (let dot of polygon) {
            if ((this.minLat <= dot.latitude && dot.latitude <= this.maxLat) && 
                (this.minLng <= dot.longitude && dot.longitude <= this.maxLng))
                continue;
            throw Error(`서울 밖의 점 ${dot.latitude} ${dot.longitude}`);
        }
        const inside = this.insideDotSupplier.generate(polygon, insideDotNumber, tryNumber);
        return [...polygon, ...inside];
    }
    /**
     * n각형의 정다각형 안에 원하는 개수의 내부의 점을 찍어주는 함수 
     * @param {number} n : n각형의 n 
     * @param {number} insideDotNumber : 내부 점의 개수
     * @param {number} minimumLatitude : 최소 위도 크기
     * @param {number} minimumLongitude : 최소 경도 크기 
     * @param {number} tryNumber : 랜덤 시도 횟수
     */
    generateRegularPolygon(n, insideDotNumber, minimumLatitude, minimumLongitude,tryNumber = 1000) {
        const minLocalLat = this.getRandomBetween(this.minLat, this.maxLat - minimumLatitude)
        const maxLocalLat = this.getRandomBetween(minLocalLat + minimumLatitude, this.maxLat)

        const minLocalLng = this.getRandomBetween(this.minLng, this.maxLng - minimumLongitude)
        const maxLocalLng = this.getRandomBetween(minLocalLng + minimumLongitude, this.maxLng)

        const polygon = this.polygonSupplier.generateRegularPolygon(n, minLocalLat, minLocalLng, maxLocalLat, maxLocalLng);
        const inside = this.insideDotSupplier.generate(polygon, insideDotNumber, tryNumber);
        return [...polygon, ...inside];
    }

    /**
     * 
     * @param {*} n 
     * @param {*} minimumLatitude  : 최소 이만큼은 멀리 떨어져 있어야한다.
     * @param {*} minimumLongitude : 최소 이만큼은 멀리 떨어져 있어야한다.
     * @param {*} largeOvalMinimumLatitude  : 먼 점이랑은 최소 이만큼은 멀리 떨어져 있어야 한다.
     * @param {*} largeOvalMinimumLongitude : 먼 점이랑은 최소 이만큼은 멀리 떨어져 있어야 한다.
     * @param {*} smallOvalMinimumLatitude  : 가까운 점끼리는 이 만큼까지 가까이 있어야 한다.
     * @param {*} smallOvalMinimumLongitude : 가까운 점끼리는 이 만큼까지 가까이 있어야 한다.
     * @param {*} tryNumber 
     */
    generateOneFromAnother(n, minimumLatitude, minimumLongitude, 
            largeOvalMinimumLatitude, largeOvalMinimumLongitude,
            smallOvalMaximumLatitude, smallOvalMaximumLongitude,
        tryNumber = 1000) {
        const minLargeOvalLocalLat = this.getRandomBetween(this.minLat + smallOvalMaximumLatitude, this.maxLat - largeOvalMinimumLatitude);
        const maxLargeOvalLocalLat = this.getRandomBetween(minLargeOvalLocalLat + largeOvalMinimumLatitude, this.maxLat - smallOvalMaximumLatitude);
        const minLargeOvalLocalLng = this.getRandomBetween(this.minLng + smallOvalMaximumLongitude, this.maxLng - largeOvalMinimumLongitude)
        const maxLargeOvalLocalLng = this.getRandomBetween(minLargeOvalLocalLng + largeOvalMinimumLongitude, this.maxLng - smallOvalMaximumLongitude)
        
        let opposites = this.polygonSupplier.generateOppositeDots(minLargeOvalLocalLat, minLargeOvalLocalLng, maxLargeOvalLocalLat, maxLargeOvalLocalLng);
        let far = opposites[0];
        let center = opposites[1];
        
        const minSmallOvalLocalLat = this.getRandomBetween(center.latitude - smallOvalMaximumLatitude, center.latitude - minimumLatitude);
        const maxSmallOvalLocalLat = this.getRandomBetween(center.latitude + minimumLatitude, center.latitude + smallOvalMaximumLatitude)
        
        const minSmallOvalLocalLng = this.getRandomBetween(center.longitude - smallOvalMaximumLongitude, center.longitude - minimumLongitude);
        const maxSmallOvalLocalLng = this.getRandomBetween(center.longitude + minimumLongitude, center.longitude + smallOvalMaximumLongitude);
        let closed = this.polygonSupplier.generateRegularPolygon(n - 1, minSmallOvalLocalLat, minSmallOvalLocalLng, maxSmallOvalLocalLat, maxSmallOvalLocalLng);
        return [far, ...closed];
    }

    generateIsoscelesTriangle(insideDotNumber) {
        const minLocalLat = this.getRandomBetween(this.minLat, this.maxLat)
        const maxLocalLat = this.getRandomBetween(minLocalLat, this.maxLat)

        const minLocalLng = this.getRandomBetween(this.minLng, this.maxLng)
        const maxLocalLng = this.getRandomBetween(minLocalLng, this.maxLng)

        let polygon = this.polygonSupplier.generateIsoscelesTriangle(minLocalLat, minLocalLng, maxLocalLat, maxLocalLng);
        let inside = this.insideDotSupplier.generate(polygon, insideDotNumber);
        return [...polygon, ...inside];
    }
    
    generateFlatPolygon(n, height) {
        const minLocalLat = this.getRandomBetween(this.minLat, this.maxLat - height)
        const maxLocalLat = this.getRandomBetween(minLocalLat, minLocalLat + height)
        
        const minLocalLng = this.getRandomBetween(this.minLng, this.maxLng)
        const maxLocalLng = this.getRandomBetween(minLocalLng, this.maxLng)

        let polygon = this.polygonSupplier.generate(n, minLocalLat, maxLocalLat, minLocalLng, maxLocalLng);
        return [...polygon];
    }

    getRandomBetween(min, max){
        return Math.random() * (max - min) + min;
    }
}