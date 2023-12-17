package com.almostThere.middleSpace.web.dto;

import com.almostThere.middleSpace.domain.gis.Position;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinalTestResult {
    private Position end;
    private Double gap;
    private Double sum;
}
