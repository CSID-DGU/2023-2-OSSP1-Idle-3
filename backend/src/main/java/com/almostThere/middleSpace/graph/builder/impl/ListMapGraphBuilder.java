package com.almostThere.middleSpace.graph.builder.impl;

import com.almostThere.middleSpace.graph.builder.MapGraphBuilder;
import com.almostThere.middleSpace.graph.edge.MapEdge;
import com.almostThere.middleSpace.graph.edge.OwnEdge;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.graph.impl.ListMapGraph;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListMapGraphBuilder implements MapGraphBuilder {
    private int ownId;
    private List<MapNode> actualNode;
    private Map<Long, Integer> map_to_id;
    private List<OwnEdge>[] adjacentGraph;

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
            Integer id = this.map_to_id.get(node.getMap_id());
            MapNode oldMapNode = actualNode.get(id);
            System.out.println("old : " + oldMapNode.getLatitude() +" , " + oldMapNode.getLongitude());
            System.out.println("new : " + node.getLatitude() +" , " + node.getLongitude());
            System.out.println(node.getMap_id());
            throw new IllegalArgumentException();
        }
        this.actualNode.add(node);
    }

    private void addEdge(MapEdge link) {
        Integer startIndex = map_to_id.get(link.getMap_start_id());
        Integer endIndex = map_to_id.get(link.getMap_end_id());
        if (startIndex != null && endIndex != null)
            this.adjacentGraph[startIndex].add(new OwnEdge(endIndex, link.getCost(), link.getLine()));
    }

    @Override
    public MapGraph build(List<MapNode> nodes, List<MapEdge> links) {
        for (MapNode node : nodes) {
            addNode(node);
        }
        this.adjacentGraph = new ArrayList[ownId];
        System.out.println("# of parsed node : " + ownId);
        for (int i = 0 ; i < ownId ; i++) {
            this.adjacentGraph[i] = new ArrayList<>();
        }
        for (MapEdge link : links) {
            addEdge(link);
        }
        return new ListMapGraph(actualNode, map_to_id, adjacentGraph);
    }

}
