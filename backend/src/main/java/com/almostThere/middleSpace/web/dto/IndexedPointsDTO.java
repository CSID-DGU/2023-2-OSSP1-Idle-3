package com.almostThere.middleSpace.web.dto;

import com.almostThere.middleSpace.domain.gis.Position;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IndexedPointsDTO {
    private Integer index;
    private List<Position> startPoints;
}
