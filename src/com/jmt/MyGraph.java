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

    public void addGraphSearchProcessor(GraphSearchProcessor<V, E> proc)
    {
        if (!graphSearchProcessors.contains(proc))
            graphSearchProcessors.add(proc);
    }

    public void removeGraphSearchProcessor(GraphSearchProcessor<V, E> proc)
    {
        if (graphSearchProcessors.contains(proc))
            graphSearchProcessors.remove(proc);

    }

    private void processVertexEarly(Vertex<V, E> vertex)
    {
        for (GraphSearchProcessor gsp : graphSearchProcessors)
            gsp.processVertexEarly(this, vertex);


    }

    private void processEdge(Vertex<V, E> from, Vertex<V, E> to)
    {
        for (GraphSearchProcessor gsp : graphSearchProcessors)
            gsp.processEdge(this, from, to);
    }

    private void processVertexLate(Vertex<V, E> v)
    {
        for (GraphSearchProcessor gsp : graphSearchProcessors)
            gsp.processVertexLate(this, v);
    }


    public void doExhaustiveBFS()
    {
        initializeSearch();

        Iterator<Vertex<V, E>> itr = vertexList.values().iterator();

        while (itr.hasNext())
        {
            Vertex<V, E> v = (Vertex<V, E>) itr.next();
            if (v.searchState == Vertex.DiscoverState.UNDISCOVERED)
                doBFS((v.vID));
        }

    }


    public void doBFS(int startVID) throws IllegalArgumentException
    {
        Vertex<V, E> cur = getVertex(startVID);

        //Visit starting node
        cur.searchDiscovererVID = -1; //null
        cur.searchDiscoverTime = searchTime++;
        cur.searchState = Vertex.DiscoverState.DISCOVERED;

        ArrayDeque<Vertex<V, E>> queue = new ArrayDeque<Vertex<V, E>>();
        queue.add(cur);

        while (!queue.isEmpty())
        {


            cur = queue.pop();
            processVertexEarly(cur);


            while (cur.searchVertexItr.hasNext())
            {

                Vertex<V, E> curTarget = cur.searchVertexItr.next();
                if (curTarget.searchState != Vertex.DiscoverState.PROCESSED || isDirected) // || isDirected
                    processEdge(cur, curTarget);

                if (curTarget.searchState == Vertex.DiscoverState.UNDISCOVERED)
                {
                    curTarget.searchState = Vertex.DiscoverState.DISCOVERED;
                    curTarget.searchDiscoverTime = searchTime++;
                    curTarget.searchDiscovererVID = cur.vID;
                    queue.add(curTarget);
                }
            }

            //we've processed this node now
            cur.searchProcessTime = searchTime++;
            cur.searchState = Vertex.DiscoverState.PROCESSED;
            processVertexLate(cur);
        }
    }


    public ArrayList<Vertex<V, E>> findShortestPath(int vIDfrom, int vIDto) throws IllegalArgumentException
    {
        initializeSearch();
        doBFS(vIDfrom);//TODO write doBFS/DFS(fromVID, stopSearchAtvID);

        ArrayList<Vertex<V, E>> ret = new ArrayList<Vertex<V, E>>();
        Vertex<V, E> cur = getVertex(vIDto);


        ret.add(cur);
        while (cur.searchDiscovererVID != -1)
        {
            cur = getVertex(cur.searchDiscovererVID);
            ret.add(cur);
        }

        return ret;
    }


    public void doExhaustiveDFS()
    {
        initializeSearch();

        Iterator<Vertex<V, E>> itr = vertexList.values().iterator();
        while (itr.hasNext())
        {
            Vertex<V, E> v = (Vertex<V, E>) itr.next();
            if (v.searchState == Vertex.DiscoverState.UNDISCOVERED)
                doDFS((v.vID));
        }

    }

    public void doDFS(int startVID) throws IllegalArgumentException
    {
        int lastDiscovererVID = -1;//null
        Vertex<V, E> cur = getVertex(startVID);

        Stack<Vertex> stack = new Stack<Vertex>();

        stack.push(cur);
        while (!stack.isEmpty())
        {
            cur = stack.peek();
            if (cur.searchState == Vertex.DiscoverState.UNDISCOVERED)
            {
                cur.searchDiscovererVID = lastDiscovererVID;
                cur.searchDiscoverTime = searchTime++;
                cur.searchState = Vertex.DiscoverState.DISCOVERED;//visit vertex
                processVertexEarly(cur);
            }


            // Every vertex has an iterator to keep track of which verticies it has searched already
            //We pick up where we left of and we seek to find the next undisovered neighbor

            boolean weHaveFoundTheNextUNDISCOVEREDNeighbor = false;
            Vertex<V, E> curNeighbor = null;
            while (cur.searchVertexItr.hasNext() && !weHaveFoundTheNextUNDISCOVEREDNeighbor)
            {

                curNeighbor = cur.searchVertexItr.next();
                weHaveFoundTheNextUNDISCOVEREDNeighbor = curNeighbor.searchState == Vertex.DiscoverState.UNDISCOVERED;

                if (weHaveFoundTheNextUNDISCOVEREDNeighbor)
                {

                }
                else
                {
                    // we still want to process the edges to show that we have traversed them
                    //
                    // We must do that IF
                    //
                    // the current neighbor has NOT already discovered the cur vertex
                    //        &&
                    // the cur vertex has not already discovered the neighbor (//TODO is this even possible with an itr
                    //        &&
                    // (the current neighbor has NOT yet been processed  OR  we are a directed graph)


                    boolean theCurNeighborHasDiscoveredUs = cur.searchDiscovererVID == curNeighbor.vID;
                    boolean weDiscoveredTheCurChild = curNeighbor.searchDiscovererVID == cur.vID;
                    boolean curChildHasBeenProcessed = curNeighbor.searchState == Vertex.DiscoverState.PROCESSED;


                    if (!theCurNeighborHasDiscoveredUs && !weDiscoveredTheCurChild && (curChildHasBeenProcessed || isDirected))
                        processEdge(cur, curNeighbor);
                }
            }


            // if we found the next undiscovered neighbor then lets explore it
            if (curNeighbor != null)
            {
                processEdge(cur, curNeighbor);

                //TODO try it without this if statement
                if (curNeighbor.searchState == Vertex.DiscoverState.UNDISCOVERED)
                {
                    lastDiscovererVID = cur.vID;
                    stack.push(curNeighbor);
                }
            }
            else
            // all neighbors have been visited and we are done
            {
                // Complete Visit
                cur.searchProcessTime = searchTime++;
                cur.searchState = Vertex.DiscoverState.PROCESSED;
                processVertexLate(cur);
                stack.pop();
            }
        }


    }

    public void initializeSearch()
    {
        searchTime = 1;
        for (Vertex<V, E> v : vertexList.values())
            v.initializeSearch();
    }


}
