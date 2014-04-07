package com.jmt;

import java.util.ArrayList;

/**
 * Created by jtappe on 4/3/2014.
 */
public class Vertex<V, E> implements Identifiable
{
    //Unique Identifier
    private static int nextVertexID = 1;

    //Node states for DFS and BFS
    public final static int UNDISCOVERED = 1;
    public final static int DISCOVERED = 2;
    public final static int PROCESSED = 4;

    private MyGraph<V, E> parentGraph;

    //Discovery state of the Node
    private int state;

    //DFS
    //used to keep track of the the current child this vertex is exploring
    private int dfsCurChild;

    private Vertex<V, E> dfsParent;

    private int discoverTime;

    private int vUID;
    private V data;

    private IntegerSet edgeIDsIn, edgeIDsOut, edgeIDsAll;


    private ArrayList<Edge<V, E>> edgesIn;
    private ArrayList<Edge<V, E>> edgesOut;

    public int getDfsCurChild()
    {
        return dfsCurChild;
    }

    public void setDfsCurChild(int dfsCurChild)
    {
        this.dfsCurChild = dfsCurChild;
    }

    public int getDiscoverTime()
    {
        return discoverTime;
    }

    public void setDiscoverTime(int discoverTime)
    {
        this.discoverTime = discoverTime;
    }


    public Vertex(V data, MyGraph<V, E> parentGraph)
    {
        this.parentGraph = parentGraph;

        initializeForSearch();
        this.data = data;
        vUID = nextVertexID++;


        edgeIDsIn = new IntegerSet();
        edgeIDsOut = new IntegerSet();
        edgeIDsAll = new IntegerSet();

        edgesIn = new ArrayList<Edge<V, E>>();
        edgesOut = new ArrayList<Edge<V, E>>();

    }


    public void initializeForSearch()
    {
        dfsParent = null;
        state = UNDISCOVERED;
        dfsCurChild = 0;

    }

    @Override
    public int getUID()
    {
        return vUID;
    }


    public void setDFSParent(Vertex<V, E> p)
    {
        this.dfsParent = p;
    }

    public Vertex<V, E> getDFSParent()
    {
        return dfsParent;
    }

    public int getState()
    {
        return state;
    }

    public void setState(int status)
    {
        if (status == UNDISCOVERED || status == DISCOVERED || status == PROCESSED)
            this.state = status;
    }


    private int findEdge(ArrayList<Edge<V, E>> arr, int eID)
    {
        return findRec(arr, eID, 0, arr.size() - 1);
    }

    private int findRec(ArrayList<Edge<V, E>> arr, int eID, int left, int right)
    {
        int mid = (left + right) / 2;

        //here we didn't find integer and the new index should be right + 1 which is out of bounds but we can expand to the right
        if (mid > right)
            return mid;

        //we didn't find integer and the new index should be 'left' which is still in bounds
        if (mid < left)
            return left;

        int cmp = arr.get(mid).getUID() - eID;
        //      int cmp = data.get(mid).compareTo(i);

        if (cmp == 0)
            return mid;

        if (cmp > 0)
            return findRec(arr, eID, left, mid - 1);
        else
            return findRec(arr, eID, mid + 1, right);
    }


    public boolean addEdgeIn(Edge<V, E> newEdge)
    {
        int eID = newEdge.getUID();
        boolean ret = edgeIDsIn.add(eID);

        //TODO test that edesIn is sorted
        if (ret)
        {
            edgeIDsAll.add(eID);

            //if ret is true then we know edgesIn does not have this edge yet
            //find where we should insert the edge
            int loc = findEdge(edgesIn, eID);
            //and insert it
            edgesIn.add(loc, newEdge);
        }

        return ret;
    }

    public boolean addEdgeOut(Edge<V, E> newEdge)
    {
        int eID = newEdge.getUID();
        boolean ret = edgeIDsOut.add(eID);


        //TODO test that edgesOut is sorted
        if (ret)
        {
            edgeIDsAll.add(eID);

            //if ret is true then we know edgesOut does not have this edge yet
            //find where we should insert the edge
            int loc = findEdge(edgesOut, eID);
            //and insert it
            edgesOut.add(loc, newEdge);
        }

        return ret;
    }


    //TODO protected or public
    protected ArrayList<Edge<V, E>> getEdgesIn()
    {
        return edgesIn;
    }

    //TODO protected or public
    protected ArrayList<Edge<V, E>> getEdgesOut()
    {
        return edgesOut;
    }

    public IntegerSet getEdgeIDsIn()
    {
        return edgeIDsIn;
    }

    public IntegerSet getEdgeIDsOut()
    {
        return edgeIDsOut;
    }

    public IntegerSet getEdgeIDsAll()
    {
        return edgeIDsAll;
    }

    public V getData()
    {
        return data;
    }
}
