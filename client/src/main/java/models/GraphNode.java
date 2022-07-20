package models;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GraphNode {
    private Tile tile;
    private List<GraphNode> shortestPath;
    private Integer distance;
    private HashMap<GraphNode, Integer> adjacentNodes;

    public GraphNode(Tile tile) {
        this.tile = tile;
        this.shortestPath = new LinkedList<>();
        this.distance = Integer.MAX_VALUE;
        this.adjacentNodes = new HashMap<>();
    }

    public void addDestination(GraphNode node, int distance) {
        adjacentNodes.put(node, distance);
    }

    public void setTile(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return this.tile;
    }

    public void setShortestPath(List<GraphNode> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public List<GraphNode> getShortestPath() {
        return this.shortestPath;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Integer getDistance() {
        return this.distance;
    }

    public void setAdjacentNodes(HashMap<GraphNode, Integer> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }

    public HashMap<GraphNode, Integer> getAdjacentNodes() {
        return this.adjacentNodes;
    }
}