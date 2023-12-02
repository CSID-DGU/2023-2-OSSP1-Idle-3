package com.almostThere.middleSpace.web.controller;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.service.recommendation.AverageCost;
import com.almostThere.middleSpace.service.recommendation.MapGraphService;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.web.dto.IndexedPointsDTO;
import com.almostThere.middleSpace.web.dto.MiddleSpaceResponse;
import com.almostThere.middleSpace.web.dto.TestModuleResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/middleSpace")
@CrossOrigin(origins = "http://localhost:5500")
@RequiredArgsConstructor
public class MiddleSpaceController {
    private final MapGraphService mapGraphService;

    @PostMapping("/")
    public ResponseEntity<MiddleSpaceResponse> getMiddleSpacePaths(@RequestBody IndexedPointsDTO indexedPointsDTO) {
        return ResponseEntity.ok(this.mapGraphService.findMostFairMiddleSpaceWithPathIndexed(
                indexedPointsDTO.getStartPoints(),
                indexedPointsDTO.getIndex()
        ));
    }
    @PostMapping("/testStdOnly")
    public ResponseEntity<TestModuleResponse> getTestResultStdOnly(@RequestBody List<Position> startPoints) {
        Result candidate = this.mapGraphService.findMiddleSpaceStdOnly(startPoints);
        return ResponseEntity.ok(this.mapGraphService.getTestResult(candidate));
    }

    @PostMapping("/test")
    public ResponseEntity<TestModuleResponse> getTestResult(@RequestBody List<Position> startPoints) {
        Result candidate = this.mapGraphService.findMiddleSpaceWithCenter(startPoints);
        return ResponseEntity.ok(this.mapGraphService.getTestResult(candidate));
    }
    @PostMapping("/testTimeCenter")
    public ResponseEntity<TestModuleResponse> getTestResultTimeCenter(@RequestBody List<Position> startPoints) {
        Result candidate = this.mapGraphService.findMiddleSpaceWithWeightedPosition(startPoints);
        return ResponseEntity.ok(this.mapGraphService.getTestResult(candidate));
    }
    @PostMapping("/testBoundary")
    public ResponseEntity<TestModuleResponse> getTestBoundary(@RequestBody List<Position> startPoints) {
        List<AverageCost> middleSpaceWithBoundary = this.mapGraphService.findMiddleSpaceWithBoundary(startPoints);
        Result result = Result.builder()
                .result(middleSpaceWithBoundary)
                .alpha(0.0)
                .middle(null)
                .build();
        return ResponseEntity.ok(this.mapGraphService.getTestResult(result));
    }
    @PostMapping("/testInterval")
    public ResponseEntity<TestModuleResponse> getTestInterval(@RequestBody List<Position> startPoints) {
        Result result = this.mapGraphService.findMiddleSpaceWithLongestStartPointIntervalTime(
                startPoints);
        return ResponseEntity.ok(this.mapGraphService.getTestResult(result));
    }
}
