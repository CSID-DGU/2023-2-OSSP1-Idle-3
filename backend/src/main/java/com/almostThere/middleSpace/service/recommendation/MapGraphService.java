package com.almostThere.middleSpace.service.recommendation;

import com.almostThere.middleSpace.domain.routetable.RouteTable;
import java.util.List;
import org.springframework.data.geo.Point;

/**
 * 중간 지점 선정 알고리즘의 결과를 직접적으로 반환하는 Service 객체
 */
public interface MapGraphService {

    /**
     * @param startPoints : 출발점으로 입력된 좌표들
     * @return (도착노드, 그 노드까지의 각 출발점에서의 소요시간의 편차의 평균)
     */
    List<AverageCost> findMiddleSpace(List<Point> startPoints);

    /**
     * 아직까진 실험 용도로 사용.
     * 실제로 서비스에 배포할 때는 위의 메서드를 사용하여 구현할 예정
     * @param tables
     * @return
     */
    List<AverageCost> findMiddleSpaceWithTables(List<RouteTable> tables);
}
