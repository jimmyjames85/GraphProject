package com.jmt;



import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jtappe on 4/3/2014.
 */
public class Vertex<V, E>
{
    //Unique Identifier
    private static int nextVertexID = 1;

    //Node states for DFS and BFS
    public enum DiscoverState
    {
        UNDISCOVERED, DISCOVERED, PROCESSED
    }

    private MyGraph<V, E> parentGraph;


    //graph search protected Variables

    //used to keep track of the the current child this vertex is exploring
    protected Iterator<Vertex<V,E>> searchVertexItr;
//    protected int searchCurEdge;



    protected int searchDiscovererVID;//id of the vertex that discovered this vertex
    protected int searchDiscoverTime;
    protected int searchProcessTime;
    protected DiscoverState searchState;



    //Member Data
    protected final int vID;
    private V data;

    //Vertices that we connect to
    protected HashMap<Integer, Vertex<V, E>> neighbors;



    //Edges that we connect through
    protected HashMap<Integer, Edge<V, E>> edges;


    public Vertex(V data, MyGraph<V, E> parentGraph)
    {
        this.parentGraph = parentGraph;


        this.data = data;
        vID = nextVertexID++;

        neighbors = new HashMap<Integer, Vertex<V, E>>();
        edges = new HashMap<Integer, Edge<V, E>>();


        initializeSearch();
    }


    public DiscoverState getState()
    {
        return searchState;
    }

    public void initializeSearch()
    {
        searchDiscovererVID = -1;//null
        searchDiscoverTime = 1;
        searchProcessTime = 1;

        searchVertexItr = neighbors.values().iterator();
//        searchCurEdge = 0;
        searchState = DiscoverState.UNDISCOVERED;
    }


    public int getVID()
    {
        return vID;
    }

    //returns the vIDs that this vertex connects to
    public Set<Integer> getNeighbors()
    {
        return neighbors.keySet();
    }

    public Set<Integer> getEdges()
    {
        return edges.keySet();
    }

    public V getData()
    {
        return data;
    }
}
