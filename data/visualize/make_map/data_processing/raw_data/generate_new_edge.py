import json

def readSubwayNodeJson():
  with open("subway_node.json", "r", encoding="utf-8") as file:
    nodes = json.load(file)
  return nodes

def readSubwayEdgeJson():
  with open("subway_edge.json", "r", encoding="utf-8") as file:
    edges = json.load(file)
  return edges

def generate(edges, nodes):
  newEdge = []
  for edge in edges:
    start = edge["start"]
    end = edge["end"]
    line = edge["line"]
    for node in nodes:
      if start == node["id"]:
        startNode = node
      elif end == node["id"]:
        endNode = node
    item = {
      "line": int(line),
      "start": startNode,
      "end": endNode
    }
    newEdge.append(item)
  return newEdge

def writeNewEdge(newEdge):
  with open("subway_new_edge.json", "w", encoding="utf-8") as outfile:
    json.dump(newEdge, outfile, ensure_ascii=False, indent=4)

edges = readSubwayEdgeJson()
nodes = readSubwayNodeJson()

newEdge = generate(edges, nodes)
writeNewEdge(newEdge)