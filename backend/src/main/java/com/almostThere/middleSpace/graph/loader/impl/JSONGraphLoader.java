package com.almostThere.middleSpace.graph.loader.impl;

import com.almostThere.middleSpace.graph.edge.MapEdge;
import com.almostThere.middleSpace.graph.node.MapNode;
import com.almostThere.middleSpace.constant.FILENAME;
import com.almostThere.middleSpace.graph.loader.GraphLoader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONGraphLoader implements GraphLoader {
    private final JSONParser jsonParser;

    public JSONGraphLoader() {
        jsonParser = new JSONParser();
    }

    @Override
    public List<MapNode> loadNodes(FILENAME graphFile) throws IOException, ParseException {
        FileReader fileReader = new FileReader("src/main/resources/data/" + graphFile.getName());
        JSONArray nodeArray = (JSONArray)jsonParser.parse(fileReader);
        List<MapNode> result = (List<MapNode>)nodeArray.stream().map((nodeObject) -> {
            JSONObject jsonObject = (JSONObject) nodeObject;
            JSONObject positionObject = (JSONObject) jsonObject.get("position");

            return new MapNode(
                    (long) jsonObject.get("id"),
                    (double) positionObject.get("longitude"),
                    (double) positionObject.get("latitude"),
                    (String) jsonObject.get("name")
            );
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<MapEdge> loadEdges(FILENAME graphFile) throws IOException, ParseException {
        FileReader fileReader = new FileReader("src/main/resources/data/" + graphFile.getName());
        JSONArray nodeArray = (JSONArray)jsonParser.parse(fileReader);
        List<MapEdge> result = (List<MapEdge>)nodeArray.stream().map((nodeObject) -> {
            JSONObject jsonObject = (JSONObject) nodeObject;
            double cost = ((Number) jsonObject.get("cost")).doubleValue();

            return new MapEdge(
                    (long) jsonObject.get("start"),
                    (long) jsonObject.get("end"),
                    cost,
                    (String) jsonObject.get("line")
            );
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<MapNode> loadNodes(List<FILENAME> graphFiles) throws IOException, ParseException {
        List<MapNode> nodeList = new ArrayList<>();
        for (FILENAME filename : graphFiles) {
            nodeList.addAll(loadNodes(filename));
        }
        return nodeList;
    }

    @Override
    public List<MapEdge> loadEdges(List<FILENAME> graphFiles) throws IOException, ParseException {
        List<MapEdge> edgeList = new ArrayList<>();
        for (FILENAME filename : graphFiles) {
            edgeList.addAll(loadEdges(filename));
        }
        return edgeList;
    }
}
