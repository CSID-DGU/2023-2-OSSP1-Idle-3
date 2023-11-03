package com.almostThere.domain.map.repository.graphLoader;

import com.almostThere.domain.map.entity.link.MapLink;
import com.almostThere.domain.map.entity.node.MapNode;
import java.io.IOException;
import java.util.List;
import org.json.simple.parser.ParseException;

public interface GraphLoader {
    List<MapNode> loadNodes(GRAPH_FILE graphFile) throws IOException, ParseException;
    List<MapLink> loadEdges(GRAPH_FILE graphFile) throws IOException, ParseException;
}
