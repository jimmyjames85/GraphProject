package com.jmt;

/**
 * Created by jtappe on 4/3/2014.
 */
public class Edge<V, E> implements Identifiable
{

    private static int nextVertexID = 1;
    private int eUID;
    private E attr;



    private MyGraph<V, E> parentGraph;
    private int srcID;
    private int targetID;

    protected Vertex<V, E> getSource()
    {
        return source;
    }

    protected Vertex<V, E> getTarget()
    {
        return target;
    }

    private Vertex<V,E> source;
    private Vertex<V,E> target;


    public Edge(Vertex<V,E> src, Vertex<V,E> target, E attr, MyGraph<V, E> parentGraph)
    {
        this.parentGraph = parentGraph;
        eUID = nextVertexID++;

        this.source = src;
        this.target = target;

        this.srcID = src.getUID();
        this.targetID = target.getUID();

        this.attr = attr;
    }


    @Override
    public int getUID()
    {
        return eUID;
    }

    public int getSourceID()
    {
        return srcID;
    }

    public int getTargetID()
    {
        return targetID;
    }

    public E getAttr()
    {
        return attr;
    }
}
