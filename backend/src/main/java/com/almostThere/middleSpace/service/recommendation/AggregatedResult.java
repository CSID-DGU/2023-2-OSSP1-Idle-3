package com.almostThere.middleSpace.service.recommendation;

import com.almostThere.middleSpace.graph.node.MapNode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AggregatedResult implements Cloneable{
    private MapNode node;
    private Double cost;
    private Double sum;

    public AggregatedResult(MapNode node) {
        this.node = node;
        this.cost = Double.MAX_VALUE;
        this.sum = Double.MAX_VALUE;
    }

    @Override
    public AggregatedResult clone() {
        try {
            AggregatedResult cloned = (AggregatedResult) super.clone();

            // MapNode는 얕은 복사가 적절한지, 아니면 깊은 복사가 필요한지에 따라 다를 수 있음
            // 얕은 복사의 경우 아래와 같이 할당
             cloned.node = this.node;

            // Double 객체는 불변이므로, 단순한 값 복사
            cloned.cost = this.cost;
            cloned.sum = this.sum;

            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // 이 경우는 일어나지 않을 것이므로 AssertionError로 처리
        }
    }
}
