package com.almostThere.middleSpace.web.controller;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.service.recommendation.SelectionService;
import com.almostThere.middleSpace.service.recommendation.service.BaseMiddleSpaceFindService;
import com.almostThere.middleSpace.service.recommendation.Result;
import com.almostThere.middleSpace.service.recommendation.service.FindWithBoundaryService;
import com.almostThere.middleSpace.service.recommendation.service.FindWithStartPointIntervalTimeService;
import com.almostThere.middleSpace.service.recommendation.service.FindWithTimeWeightCenterService;
import com.almostThere.middleSpace.service.recommendation.service.FindWithWeightCenterService;
import com.almostThere.middleSpace.service.recommendation.service.FindWithWeightCenterTimeDistanceService;
import com.almostThere.middleSpace.web.dto.AllResponse;
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
    private final SelectionService selectionService;
    private final BaseMiddleSpaceFindService baseMiddleSpaceFindService;
    private final FindWithBoundaryService findWithBoundaryService;
    private final FindWithStartPointIntervalTimeService findWithStartPointIntervalTimeService;
    private final FindWithWeightCenterService findWithWeightCenterService;
    private final FindWithWeightCenterTimeDistanceService findWithWeightCenterTimeDistanceService;
    private final FindWithTimeWeightCenterService findWithTimeWeightCenterService;

    @PostMapping("/")
    public ResponseEntity<MiddleSpaceResponse> getMiddleSpacePaths(@RequestBody IndexedPointsDTO indexedPointsDTO) {
        return ResponseEntity.ok(this.baseMiddleSpaceFindService.findMostFairMiddleSpaceWithPathIndexed(
                indexedPointsDTO.getStartPoints(),
                indexedPointsDTO.getIndex()
        ));
    }
    @PostMapping("/testStdOnly")
    public ResponseEntity<TestModuleResponse> getTestResultStdOnly(@RequestBody List<Position> startPoints) {
        Result result = this.baseMiddleSpaceFindService.findMiddleSpaceTest(startPoints);
        return ResponseEntity.ok(this.baseMiddleSpaceFindService.getTestResult(result));
    }
    @PostMapping("/test")
    public ResponseEntity<TestModuleResponse> getTestResult(@RequestBody List<Position> startPoints) {
        Result candidate = this.findWithWeightCenterService.findMiddleSpaceTest(startPoints);
        return ResponseEntity.ok(this.findWithWeightCenterService.getTestResult(candidate));
    }
    @PostMapping("/testTimeCenter")
    public ResponseEntity<TestModuleResponse> getTestResultTimeCenter(@RequestBody List<Position> startPoints) {
        Result candidate = this.findWithTimeWeightCenterService.findMiddleSpaceTest(startPoints);
        return ResponseEntity.ok(this.findWithTimeWeightCenterService.getTestResult(candidate));
    }
    @PostMapping("/testBoundary")
    public ResponseEntity<TestModuleResponse> getTestBoundary(@RequestBody List<Position> startPoints) {
        Result result = this.findWithBoundaryService.findMiddleSpaceTest(startPoints);
        return ResponseEntity.ok(this.findWithBoundaryService.getTestResult(result));
    }
    @PostMapping("/testCenterTimeDistance")
    public ResponseEntity<TestModuleResponse> getTestCenterTimeDistance(@RequestBody List<Position> startPoints) {
        Result candidate = this.findWithWeightCenterTimeDistanceService.findMiddleSpaceTest(startPoints);
        return ResponseEntity.ok(this.findWithWeightCenterTimeDistanceService.getTestResult(candidate));
    }
    @PostMapping("/testInterval")
    public ResponseEntity<TestModuleResponse> getTestInterval(@RequestBody List<Position> startPoints) {
        Result result = this.findWithStartPointIntervalTimeService.findMiddleSpaceTest(startPoints);
        return ResponseEntity.ok(this.findWithStartPointIntervalTimeService.getTestResult(result));
    }

    @PostMapping("/testAll")
    public ResponseEntity<AllResponse> getTestAll(@RequestBody List<Position> startPoints) {
        AllResponse allResponses = selectionService.getAllResponses(startPoints);
        return ResponseEntity.ok(allResponses);
    }
}
