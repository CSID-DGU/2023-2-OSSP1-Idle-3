package com.almostThere.middleSpace.service.recommendation;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.domain.routetable.RouteTable;
import com.almostThere.middleSpace.service.recommendation.service.BaseMiddleSpaceFindService;
import com.almostThere.middleSpace.service.recommendation.service.FindWithStartPointIntervalTimeService;
import com.almostThere.middleSpace.service.recommendation.service.FindWithWeightCenterTimeDistanceService;
import com.almostThere.middleSpace.service.routing.Router;
import com.almostThere.middleSpace.web.dto.AllResponse;
import com.almostThere.middleSpace.web.dto.FinalTestResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelectionService {
    private final Router router;
    private final FindWithStartPointIntervalTimeService findWithStartPointIntervalTimeService;
    private final FindWithWeightCenterTimeDistanceService findWithWeightCenterTimeDistanceService;
    private final BaseMiddleSpaceFindService baseMiddleSpaceFindService;

    public AllResponse getAllResponses(List<Position> startPoints) {
        List<RouteTable> routeTables = router.getRouteTables(startPoints);
        FinalTestResult stdOnlyAlgorithm = findWithStartPointIntervalTimeService.findMiddleSpaceWithRouter(
                routeTables);
        FinalTestResult weightAlgorithm = findWithWeightCenterTimeDistanceService.findMiddleSpaceWithRouter(
                routeTables, startPoints);
        FinalTestResult distanceOnlyAlgorithm = baseMiddleSpaceFindService.findMiddleSpaceWithRouterAndSum(
                routeTables);
        FinalTestResult original = baseMiddleSpaceFindService.findMiddleSpaceOriginal(routeTables, startPoints);
        return AllResponse.builder()
                .stdOnlyAlgorithm(stdOnlyAlgorithm)
                .distanceOnlyAlgorithm(distanceOnlyAlgorithm)
                .weightAlgorithm(weightAlgorithm)
                .original(original)
                .build();
    }
}
