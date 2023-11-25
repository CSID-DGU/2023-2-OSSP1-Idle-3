package com.almostThere.middleSpace.web.dto;

import com.almostThere.middleSpace.domain.gis.Path;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class MiddleSpaceResponse {
    private List<Path> paths;
    private final double endLatitude;
    private final double endLongitude;
    private final double cost;
}
