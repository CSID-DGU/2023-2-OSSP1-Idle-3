package com.almostThere.middleSpace.service.recommendation;

import com.almostThere.middleSpace.graph.node.MapNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
/**
 * 편차의 평균을 가지고 다니는 책임이 있는 객체
 * node : 도착 노드
 * cost : 도착 노드에 대한 소요시간의 평균의 편차
 */
public class AverageCost {
    private MapNode node;
    private Double cost;
}
