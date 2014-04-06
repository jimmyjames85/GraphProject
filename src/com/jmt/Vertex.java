package com.jmt;

/**
 * Created by jtappe on 4/3/2014.
 */
public class Vertex<E> implements Identifiable
{

    public final static int UNDISCOVERED = 1;
    public final static int DISCOVERED = 2;
    public final static int PROCESSED = 4;

    private static int nextVertexID = 1;




    private int status;
    private int vUID;
    private E data;

    private IntegerSet edgesIn, edgesOut, edgesAll;

    public Vertex(E data)
    {

        status = UNDISCOVERED;
        this.data = data;
        vUID = nextVertexID++;

        edgesIn = new IntegerSet();
        edgesOut = new IntegerSet();
        edgesAll = new IntegerSet();
    }

    @Override
    public int getUID()
    {
        return vUID;
    }


    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        if(status==UNDISCOVERED || status==DISCOVERED || status==PROCESSED)
            this.status = status;
    }

    public boolean addEdgeIn(int eID)
    {
        boolean ret = edgesIn.add(eID);

        if (ret)
            edgesAll.add(eID);

        return ret;
    }

    public boolean addEdgeOut(int eID)
    {
        boolean ret = edgesOut.add(eID);

        if (ret)
            edgesAll.add(eID);

        return ret;
    }

    public IntegerSet getEdges()
    {

        return edgesAll;
    }

    public IntegerSet getEdgesIn()
    {
        return edgesIn;
    }

    public IntegerSet getEdgesOut()
    {
        return edgesOut;
    }

    public E getData()
    {
        return data;
    }
}
