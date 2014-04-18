package com.jmt;

import java.util.*;

/**
 * Created by jim on 4/2/14.
 */
public class MyGraph<V, E> implements Graph<V, E>
{

    private HashMap<Integer, Vertex<V, E>> vertexList;
    private HashMap<Integer, Edge<V, E>> edgeList;


    public MyGraph()
    {
        vertexList = new HashMap<Integer, Vertex<V, E>>();
        edgeList = new HashMap<Integer, Edge<V, E>>();
    }

    public Vertex<V, E> getVertex(int vID) throws IllegalArgumentException
    {
        Vertex<V, E> v = vertexList.get(vID);
        if (v == null)
            throw new IllegalArgumentException("No such vertex " + vID);
        return v;
    }

    private Edge<V, E> getEdge(int eID) throws IllegalArgumentException
    {
        Edge<V, E> e = edgeList.get(eID);
        if (e == null)
            throw new IllegalArgumentException("No such edge " + eID);

        return e;
    }




    public void removeEdge(int eID)
    {

        Edge e  = edgeList.remove(eID);
        if(e==null)
            throw new IllegalArgumentException("No such Edge " + eID);

        e.source.edges.remove(e.eID);
        e.source.neighbors.remove(e.target.vID);

        e.target.edges.remove(e.eID);
        e.target.neighbors.remove(e.source.vID);

    }

    public void removeVertex(int vID)
    {

        Vertex v = vertexList.remove(vID);
        if(v==null)
            throw new IllegalArgumentException("No such Vertex " + vID);

        Iterator<Integer> itr = edgeList.keySet().iterator();

        ArrayList<Integer> removeList = new ArrayList<Integer>();

        while(itr.hasNext())
        {
            Edge curEdge = getEdge(itr.next());

            if(curEdge.source.vID==v.vID)
            {
                Vertex target = getVertex(curEdge.target.vID);
                target.edges.remove(curEdge.eID);
                target.neighbors.remove(v.vID);
                removeList.add(curEdge.eID);

            }
            else if( curEdge.target.vID==v.vID)
            {
                Vertex source = getVertex(curEdge.source.vID);
                source.edges.remove(curEdge.eID);
                source.neighbors.remove(v.vID);
                removeList.add(curEdge.eID);
            }
        }

        while(!removeList.isEmpty())
            edgeList.remove(removeList.remove(0));


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
        Vertex<V, E> src = vertexList.get(srcID);
        Vertex<V, E> target = vertexList.get(targetID);
        Edge<V, E> newEdge = new Edge(src, target, attr, this);

        src.neighbors.put(targetID, target);
        src.edges.put(newEdge.eID, newEdge);

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
