package com.almostThere.middleSpace.web.dto;

import com.almostThere.middleSpace.domain.gis.Position;
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
    private final double alpha;
    private final double cost;
    private final Position middleSpace;
}
