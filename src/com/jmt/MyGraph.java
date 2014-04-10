package com.jmt;

import java.util.*;

/**
 * Created by jim on 4/2/14.
 */
public class MyGraph<V, E> implements Graph<V, E>
{

    //These are for DFS and BFS
    private ArrayList<GraphSearchProcessor<V, E>> graphSearchProcessors;
    private int searchTime;

    //set to true if we have directed edges in our graph
    private boolean isDirected;


    private HashMap<Integer, Vertex<V, E>> vertexList;
    private HashMap<Integer, Edge<V, E>> edgeList;


    public MyGraph(boolean isDirected)
    {

        vertexList = new HashMap<Integer, Vertex<V, E>>();
        edgeList = new HashMap<Integer, Edge<V, E>>();

        graphSearchProcessors = new ArrayList<GraphSearchProcessor<V, E>>();
        this.isDirected = isDirected;
    }


    public boolean isDirected()
    {
        return isDirected;
    }

    public Vertex<V, E> getVertex(int vID) throws IllegalArgumentException
    {
        return vertexList.get(vID);
    }

    private Edge<V, E> getEdge(int eID) throws IllegalArgumentException
    {
        return edgeList.get(eID);
    }

    @Override
    public int addVertex(V data)
    {
        Vertex<V, E> v = new Vertex<V, E>(data, this);
        vertexList.put(v.vID, v);
        return v.vID;
    }

    @Override
    public int addEdge(int srcID, int targetID, E attr) throws IllegalArgumentException
    {

        //TODO what happens when vertexList doesn't have srcID we need throw illeagalargumentException
        Vertex<V, E> src = vertexList.get(srcID);
        Vertex<V, E> target = vertexList.get(targetID);


        Edge<V, E> newEdge = new Edge(src, target, attr, this);


        //TODO will SELF LOOPs be a problem???
        //if(srcID==targetID)
        //System.out.println(srcID);


        src.neighbors.put(targetID, target);
        src.edges.put(newEdge.eID, newEdge);

        if (!isDirected)
        {
            target.neighbors.put(srcID, src);
            target.edges.put(newEdge.eID, newEdge);
        }

        edgeList.put(newEdge.eID, newEdge);

        return newEdge.eID;
    }

    @Override
    public Set<Integer> getVertices()
    {
        return vertexList.keySet();
    }

    @Override
    public Set<Integer> getEdges()
    {
        return edgeList.keySet();
    }

    @Override
    public E getAttribute(int eID) throws IllegalArgumentException
    {
        return getEdge(eID).getAttr();
    }

    @Override
    public V getData(int vID) throws IllegalArgumentException
    {
        return getVertex(vID).getData();
    }

    @Override
    public int getSource(int eID) throws IllegalArgumentException
    {
        return getEdge(eID).getSourceID();
    }

    @Override
    public int getTarget(int eID) throws IllegalArgumentException
    {
        return getEdge(eID).getTargetID();
    }

    @Override
    public Set<Integer> getEdgesOf(int vID) throws IllegalArgumentException
    {
        return getVertex(vID).getEdges();
    }


}
