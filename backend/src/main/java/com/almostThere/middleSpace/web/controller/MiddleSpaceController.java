package com.almostThere.middleSpace.web.controller;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.service.recommendation.MapGraphService;
import com.almostThere.middleSpace.web.dto.IndexedPointsDTO;
import com.almostThere.middleSpace.web.dto.MiddleSpaceResponse;
import com.almostThere.middleSpace.web.dto.TestModuleResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class MiddleSpaceController {
    private final MapGraphService mapGraphService;

    @PostMapping("/")
    public MiddleSpaceResponse getMiddleSpacePaths(@RequestBody IndexedPointsDTO indexedPointsDTO) {
        return this.mapGraphService.findMostFairMiddleSpaceWithPathIndexed(
                indexedPointsDTO.getStartPoints(),
                indexedPointsDTO.getIndex()
        );
    }
    @PostMapping("/test")
    public TestModuleResponse getTestResult(@RequestBody List<Position> startPoints) {
        return this.mapGraphService.getTestResult(startPoints);
    }
}
