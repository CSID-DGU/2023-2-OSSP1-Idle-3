package com.almostThere.middleSpace.web.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class TestModuleResponse {
    private final AnswerPoint answer;
    private final List<MissingPoint> missingPoints;
}
