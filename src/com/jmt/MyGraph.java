package com.jmt;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by jim on 4/2/14.
 */
public class MyGraph<V, E> implements Graph<V, E>
{

    //These are for DFS and BFS
    private ArrayList<EdgeProcessor<V, E>> edgeProcessors;
    private ArrayList<VertexProcessor<V, E>> vertexProcessors;


    private IdentifiableSet vertexList;
    private IdentifiableSet edgeList;


    public MyGraph()
    {

        vertexList = new IdentifiableSet();
        edgeList = new IdentifiableSet();

        edgeProcessors = new ArrayList<EdgeProcessor<V, E>>();
        vertexProcessors = new ArrayList<VertexProcessor<V, E>>();
    }






    //BIG O lg n
    private Vertex<V> getVertex(int vID) throws IllegalArgumentException
    {
        Vertex<V> v = (Vertex<V>) vertexList.findByUID(vID);

        if (v == null)
            throw new IllegalArgumentException("No such Vertex ID " + vID);

        return v;
    }


    //Big O lg n
    private Edge<E> getEdge(int eID) throws IllegalArgumentException
    {
        Edge<E> e = (Edge<E>) edgeList.findByUID(eID);

        if (e == null)
            throw new IllegalArgumentException("No such Edge ID " + eID);

        return e;
    }



    @Override
    public int addVertex(V data)
    {
        Vertex v = new Vertex(data);
        vertexList.add(v);
        return v.getUID();
    }


    //BIGO 2*lg(n) +BigO(IdentifiableSet.add)
    @Override
    public int addEdge(int srcID, int targetID, E attr) throws IllegalArgumentException
    {
        //getVertex() will throw IllegalArgumentException if srcID or targetID's are not found
        Vertex<V> src = getVertex(srcID);
        Vertex<V> target = getVertex(targetID);


        Edge<E> newEdge = new Edge(src.getUID(), target.getUID(), attr);
        int eID = newEdge.getUID();


        //TODO will SELF LOOPs be a problem???
        //if(srcID==targetID)
        //System.out.println(srcID);

        //TODO these return true/false but new edge should be unique
        if (!src.addEdgeOut(eID))
            throw new IllegalArgumentException("AlreadyExisits???");
        //TODO is it possible these vertices already have these edges registered
        if (!target.addEdgeIn(eID))
            throw new IllegalArgumentException("AlreadyExisits???");


        edgeList.add(newEdge);

        return newEdge.getUID();
    }


    //BIG O lg(n)
    public int getDegreeIn(int vID)
    {
        return getVertex(vID).getEdgesIn().size();
    }

    //BIG O lg(n)
    public int getDegreeOut(int vID)
    {
        return getVertex(vID).getEdgesOut().size();
    }


    @Override
    public Set<Integer> getVertices()
    {
        return vertexList.getIDList();
    }

    @Override
    public Set<Integer> getEdges()
    {
        return edgeList.getIDList();
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


    public void addVertexProcessor(VertexProcessor<V, E> proc)
    {
        if (!vertexProcessors.contains(proc))
            vertexProcessors.add(proc);
    }

    public void addEdgeProcessor(EdgeProcessor<V, E> proc)
    {
        if (!edgeProcessors.contains(proc))
            edgeProcessors.add(proc);

    }



    public void doDFS()
    {
        //TODO
    }

    public void initializeSearch()
    {
        for(Identifiable i: vertexList)
        {
            Vertex<V> v = (Vertex<V>)i;
            v.setStatus(Vertex.UNDISCOVERED);
            //TODO
        }
    }


}
