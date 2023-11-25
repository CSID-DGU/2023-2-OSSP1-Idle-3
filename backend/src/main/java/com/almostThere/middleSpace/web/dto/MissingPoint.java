package com.almostThere.middleSpace.web.dto;

import com.almostThere.middleSpace.domain.gis.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MissingPoint {
    private final Position position;
    private final Double gapDifference;
    private final Double sumDifference;
}
