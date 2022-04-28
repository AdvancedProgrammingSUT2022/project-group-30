package models;

import java.util.HashSet;

public class TileGraph {
    private HashSet<GraphNode> nodes;

    public TileGraph(){
        this.nodes = new HashSet<>();
    }

    public void addNode(GraphNode node){
        this.nodes.add(node);
    }

    public GraphNode getNodeByTile(Tile tile){
        for (GraphNode node : nodes) {
            if(node.getTile().equals(tile)){
                return node;
            }
        }
        return null;
    }

    public boolean isTileAddedToGraph(Tile tile){
        for (GraphNode node : nodes) {
            if(node.getTile().equals(tile)){
                return true;
            }
        }
        return false;
    }

    public void setNodes(HashSet<GraphNode> nodes){
        this.nodes = nodes;
    }

    public HashSet<GraphNode> getNodes(){
        return this.nodes;
    }
}
