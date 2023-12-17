package com.almostThere.middleSpace.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllResponse {
    private FinalTestResult stdOnlyAlgorithm;
    private FinalTestResult weightAlgorithm;
    private FinalTestResult distanceOnlyAlgorithm;
}
