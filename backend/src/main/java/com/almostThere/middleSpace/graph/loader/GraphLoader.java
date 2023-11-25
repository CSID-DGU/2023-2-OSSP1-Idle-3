package com.almostThere.middleSpace.graph.loader;

import com.almostThere.middleSpace.graph.edge.MapEdge;
import com.almostThere.middleSpace.graph.node.MapNode;
import java.io.IOException;
import java.util.List;
import org.json.simple.parser.ParseException;

public interface GraphLoader {
    List<MapNode> loadNodes(String graphFile) throws IOException, ParseException;
    List<MapEdge> loadEdges(String graphFile) throws IOException, ParseException;
    List<MapNode> loadNodes(List<String> graphFiles)throws IOException, ParseException;
    List<MapEdge> loadEdges(List<String> graphFiles) throws IOException, ParseException;

}
