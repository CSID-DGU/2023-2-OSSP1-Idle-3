package com.almostThere.middleSpace.service.recommendation;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import java.util.List;

/**
 * 중간 지점 선정 알고리즘의 결과를 직접적으로 반환하는 Service 객체
 */
public interface MapGraphService {

    /**
     * @param startPoints : 출발점으로 입력된 좌표들
     * @return (도착노드, 그 노드까지의 각 출발점에서의 소요시간의 편차의 평균)
     */
    List<AverageCost> findMiddleSpace(List<Position> startPoints);

    /**
     * 아직까진 실험 용도로 사용.
     * 실제로 서비스에 배포할 때는 위의 메서드를 사용하여 구현할 예정
     * @param tables : (출발노드 인덱스, 그 노드에서 다른 노드까지 걸리는 시간이 기록된 테이블)들
     * @return (도착노드, 소요시간 편차, 총 소요시간)
     */
    List<AverageCost> findMiddleSpaceWithTables(List<RouteTable> tables);

    List<AverageCost> findMiddleSpaceWithBoundary(List<AverageCost> averageCosts, List<Position> startPoints);
}
