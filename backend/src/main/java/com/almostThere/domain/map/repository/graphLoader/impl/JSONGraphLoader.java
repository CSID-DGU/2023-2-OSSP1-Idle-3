package com.almostThere.domain.map.repository.graphLoader.impl;

import com.almostThere.domain.map.entity.link.MapLink;
import com.almostThere.domain.map.entity.node.MapNode;
import com.almostThere.domain.map.repository.graphLoader.GRAPH_FILE;
import com.almostThere.domain.map.repository.graphLoader.GraphLoader;
import java.io.FileReader;
import java.io.IOException;
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
    public List<MapNode> loadNodes(GRAPH_FILE graphFile) throws IOException, ParseException {
        FileReader fileReader = new FileReader("src/main/resources/data/" + graphFile.getName());
        JSONArray nodeArray = (JSONArray)jsonParser.parse(fileReader);
        List<MapNode> result = (List<MapNode>)nodeArray.stream().map((nodeObject) -> {
            JSONObject jsonObject = (JSONObject) nodeObject;
            JSONObject positionObject = (JSONObject) jsonObject.get("position");

            return new MapNode(
                    (long) jsonObject.get("id"),
                    (double) positionObject.get("latitude"),
                    (double) positionObject.get("longitude"),
                    (String) jsonObject.get("name")
            );
        }).collect(Collectors.toList());
        return result;
    }

    @Override
    public List<MapLink> loadEdges(GRAPH_FILE graphFile) throws IOException, ParseException {
        FileReader fileReader = new FileReader("src/main/resources/data/" + graphFile.getName());
        JSONArray nodeArray = (JSONArray)jsonParser.parse(fileReader);
        List<MapLink> result = (List<MapLink>)nodeArray.stream().map((nodeObject) -> {
            JSONObject jsonObject = (JSONObject) nodeObject;
            double cost = ((Number) jsonObject.get("cost")).doubleValue();

            return new MapLink(
                    (long) jsonObject.get("start"),
                    (long) jsonObject.get("end"),
                    cost,
                    (String) jsonObject.get("line")
            );
        }).collect(Collectors.toList());
        return result;
    }
}
