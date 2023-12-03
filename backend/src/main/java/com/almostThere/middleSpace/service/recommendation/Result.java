package com.almostThere.middleSpace.service.recommendation;

import com.almostThere.middleSpace.domain.gis.Position;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
@Setter
@Builder
public class Result {
    private List<AverageCost> result;
    private List<AverageCost> normalizedResult;
    private Position middle;
    private Double alpha;
    private Double cost;
}
