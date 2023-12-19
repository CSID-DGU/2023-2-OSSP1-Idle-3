package com.almostThere.middleSpace.web.controller;

import com.almostThere.middleSpace.domain.gis.Position;
import com.almostThere.middleSpace.service.recommendation.SelectionService;
import com.almostThere.middleSpace.service.recommendation.service.BaseMiddleSpaceFindService;
import com.almostThere.middleSpace.service.recommendation.service.FindWithStartPointIntervalTimeService;
import com.almostThere.middleSpace.service.recommendation.service.FindWithWeightCenterTimeDistanceService;
import com.almostThere.middleSpace.web.dto.AllResponse;
import java.util.List;
import java.util.NoSuchElementException;

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
    private final FindWithStartPointIntervalTimeService findWithStartPointIntervalTimeService;
    private final FindWithWeightCenterTimeDistanceService findWithWeightCenterTimeDistanceService;

    @PostMapping("/stdOnly")
    public ResponseEntity<Position> getTestInterval(@RequestBody List<Position> startPoints) {
        Position result = this.findWithStartPointIntervalTimeService.findMiddleSpace(startPoints);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/sumOnly")
    public ResponseEntity<Position> getTestResultSumOnly(@RequestBody List<Position> startPoints) {
        Position result = this.baseMiddleSpaceFindService.findMiddleSpace(startPoints);
        return ResponseEntity.ok(result);
    }
    @PostMapping("/sumAndStd")
    public ResponseEntity<Position> getTestCenterTimeDistance(@RequestBody List<Position> startPoints) {
        Position result = this.findWithWeightCenterTimeDistanceService.findMiddleSpace(startPoints);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/testAll")
    public ResponseEntity<AllResponse> getTestAll(@RequestBody List<Position> startPoints) {
        try {
            AllResponse allResponses = selectionService.getAllResponses(startPoints);
            return ResponseEntity.ok(allResponses);
        }catch (NoSuchElementException exception) {
            return ResponseEntity.ok(new AllResponse());
        }
    }
}
