package com.almostThere.domain.map.repository.graphBuilder.impl;

import com.almostThere.domain.map.entity.link.MapLink;
import com.almostThere.domain.map.entity.link.OwnLink;
import com.almostThere.domain.map.entity.node.MapNode;
import com.almostThere.domain.map.repository.mapGraph.MapGraph;
import com.almostThere.domain.map.repository.graphBuilder.MapGraphBuilder;
import com.almostThere.domain.map.repository.mapGraph.impl.ListMapGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;


@Component
public class ListMapGraphBuilder implements MapGraphBuilder {
    private int ownId;
    private List<MapNode> actualNode;
    private Map<Integer, Integer> map_to_id;
    private List<OwnLink>[] adjacentGraph;

    public ListMapGraphBuilder() {
        this.actualNode = new ArrayList<>();
        this.map_to_id = new HashMap<>();

        this.ownId = 0;
    }

    private void addNode(MapNode node) {
        if (this.map_to_id.get(node.getMap_id()) == null) {
            this.map_to_id.put(node.getMap_id(), ownId);
            ownId += 1;
        }else {
            throw new IllegalArgumentException();
        }
        this.actualNode.add(node);
    }

    private void addEdge(MapLink link) {
        Integer startIndex = map_to_id.get(link.getMap_start_id());
        Integer endIndex = map_to_id.get(link.getMap_end_id());
        this.adjacentGraph[startIndex].add(new OwnLink(endIndex, link.getCost()));
    }

    @Override
    public MapGraph build(List<MapNode> nodes, List<MapLink> links) {
        for (MapNode node : nodes) {
            addNode(node);
        }
        this.adjacentGraph = new ArrayList[ownId];
        for (MapLink link : links) {
            addEdge(link);
        }
        return new ListMapGraph(actualNode, map_to_id, adjacentGraph);
    }

}
