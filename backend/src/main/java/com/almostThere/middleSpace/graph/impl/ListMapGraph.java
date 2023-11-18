package com.almostThere.middleSpace.graph.impl;

import com.almostThere.middleSpace.graph.edge.OwnEdge;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.graph.MapGraph;
import com.almostThere.middleSpace.util.GIS;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class ListMapGraph implements MapGraph {
    private final List<MapNode> actualNode;
    private final Map<Long, Integer> map_to_id;
    private final List<OwnEdge>[] adjacentGraph;

    public ListMapGraph(
            List<MapNode> actualNode,
            Map<Long, Integer> map_to_id,
            List<OwnEdge>[] adjacentGraph
        ) {
        this.actualNode = actualNode;
        this.map_to_id = map_to_id;
        this.adjacentGraph = adjacentGraph;
    }

    @Override
    public List<OwnEdge> getAdjacentNodes(int index) {
        return this.adjacentGraph[index];
    }

    @Override
    public int getNodeNum() {
        return this.adjacentGraph.length;
    }

    @Override
    public Integer findSearchId(Long mapId) {
        return this.map_to_id.get(mapId);
    }
    @Override
    public Long findMapId(Integer searchId) {
        return this.actualNode.get(searchId).getMap_id();
    }

    @Override
    public MapNode findMapNode(Integer searchId) {
        return this.actualNode.get(searchId);
    }
    @Override
    public Integer findNearestId(Double latitude, Double longitude) {
        double length = Double.MAX_VALUE;
        int minIndex = 0;

        for (int i = 0 ; i < adjacentGraph.length ; i++) {
            MapNode mapNode = this.actualNode.get(i);
            if (mapNode.getMap_id() < 1000000 || mapNode.getMap_id() > 1000000000)
                continue;
            double distance = GIS.getDistance(latitude, longitude, mapNode);
            if (distance < length) {
                length = distance;
                minIndex = i;
            }
        }
        return minIndex;
    }
    @Override
    public MapNode findNearestWalkNode(Double latitude, Double longitude) {
        return this.actualNode.get(findNearestId(latitude, longitude));
    }

    @Override
    public MapNode getNode(Integer searchId) {
        return this.actualNode.get(searchId);
    }

    private void dfs(int v, boolean[] visited, List<MapNode> component) {
        Stack<Integer> stk = new Stack<>();
        stk.push(v);
        component.add(this.actualNode.get(v));

        visited[v] = true;
        while (!stk.empty()) {
            Integer topNode = stk.pop();

            visited[topNode] = true;
            List<OwnEdge> ownEdges = adjacentGraph[topNode];
            for (OwnEdge edge : ownEdges) {
                if (!visited[edge.getIndex()]) {
                    visited[edge.getIndex()] = true;
                    stk.push(edge.getIndex());
                    component.add(this.actualNode.get(edge.getIndex()));
                }
            }
        }
    }

    @Override
    public List<List<MapNode>> getIsolatedNetworks() {
        List<List<MapNode>> subNetworks = new ArrayList<>();
        final int size = actualNode.size();
        boolean[] visited = new boolean[size];

        for (int i = 0; i < size ; i++) {
            if (!visited[i]) {
                List<MapNode> subNetwork = new ArrayList<>();
                dfs(i, visited, subNetwork);
                subNetworks.add(subNetwork);
            }
        }
        return subNetworks;
    }
}
