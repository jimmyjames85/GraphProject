package com.jmt;

import java.util.*;

/**
 * Created by jim on 4/2/14.
 */
public class MyGraph<V, E> implements Graph<V, E>
{

    //These are for DFS and BFS
    private ArrayList<GraphSearchProcessor<V, E>> graphSearchProcessors;


    private IdentifiableSet vertexList;
    private IdentifiableSet edgeList;


    private boolean isDirected;


    private int searchTime;


    public MyGraph(boolean isDirected)
    {

        vertexList = new IdentifiableSet();
        edgeList = new IdentifiableSet();

        graphSearchProcessors = new ArrayList<GraphSearchProcessor<V, E>>();
    }

    public boolean isDirected()
    {
        return isDirected;
    }


    //BIG O lg n
    //TODO make private???
    public Vertex<V, E> getVertex(int vID) throws IllegalArgumentException
    {
        return (Vertex<V, E>) vertexList.getIdentifiableByID(vID);
    }


    //Big O lg n
    private Edge<V, E> getEdge(int eID) throws IllegalArgumentException
    {
        return (Edge<V, E>) vertexList.getIdentifiableByID(eID);
    }


    @Override
    public int addVertex(V data)
    {
        Vertex<V, E> v = new Vertex<V, E>(data, this);
        vertexList.add(v);
        return v.getUID();
    }


    //BIGO 2*lg(n) +BigO(IdentifiableSet.add)
    @Override
    public int addEdge(int srcID, int targetID, E attr) throws IllegalArgumentException
    {
        //getVertex() will throw IllegalArgumentException if srcID or targetID's are not found
        Vertex<V, E> src = getVertex(srcID);
        Vertex<V, E> target = getVertex(targetID);


        Edge<V, E> newEdge = new Edge(src, target, attr, this);
        int eID = newEdge.getUID();


        //TODO will SELF LOOPs be a problem???
        //if(srcID==targetID)
        //System.out.println(srcID);

        //TODO these return true/false but new edge should be unique
        if (!src.addEdgeOut(newEdge))
            throw new IllegalArgumentException("AlreadyExisits???");
        //TODO is it possible these vertices already have these edges registered
        if (!target.addEdgeIn(newEdge))
            throw new IllegalArgumentException("AlreadyExisits???");




        edgeList.add(newEdge);

        return newEdge.getUID();
    }


    //BIG O lg(n)
    public int getDegreeIn(int vID)
    {
        return getVertex(vID).getEdgeIDsIn().size();
    }

    //BIG O lg(n)
    public int getDegreeOut(int vID)
    {
        return getVertex(vID).getEdgeIDsOut().size();
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
        return getVertex(vID).getEdgeIDsAll();
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

    private void processEdge(Edge<V, E> edge)
    {
        for (GraphSearchProcessor gsp : graphSearchProcessors)
            gsp.processEdge(this, edge);


    }

    private void processVertexLate(Vertex<V, E> v)
    {
        for (GraphSearchProcessor gsp : graphSearchProcessors)
            gsp.processVertexLate(this, v);
    }


    public void doExhaustiveBFS()
    {
        initializeSearch();

        Iterator<Identifiable> itr = vertexList.iterator();
        while (itr.hasNext())
        {
            Vertex<V, E> v = (Vertex<V, E>) itr.next();
            if (v.searchState == Vertex.DiscoverState.UNDISCOVERED)
                doBFS((v.getUID()));
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


            ArrayList<Edge<V, E>> neighborsOut = cur.getEdgesOut();

            //neighborsOut is sorted in order by uid

            for (int i = 0; i < neighborsOut.size(); i++)
            {
                Vertex<V, E> curTarget = neighborsOut.get(i).getTarget();
                if (curTarget.searchState != Vertex.DiscoverState.PROCESSED || isDirected) // || isDirected
                    processEdge(neighborsOut.get(i));

                if (curTarget.searchState == Vertex.DiscoverState.UNDISCOVERED)
                {
                    curTarget.searchState = Vertex.DiscoverState.DISCOVERED;
                    curTarget.searchDiscoverTime = searchTime++;
                    curTarget.searchDiscovererVID = cur.getUID();
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

        Iterator<Identifiable> itr = vertexList.iterator();
        while (itr.hasNext())
        {
            Vertex<V, E> v = (Vertex<V, E>) itr.next();
            if (v.searchState == Vertex.DiscoverState.UNDISCOVERED)
                doDFS((v.getUID()));
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

            // Every node has a marker to check which children it has searched
            // we continue our search from where ever we left off
            // we are searching for the next undiscovered child
            ArrayList<Edge<V, E>> neighborsOut = cur.getEdgesOut();


            while (cur.searchCurChild < neighborsOut.size() && neighborsOut.get(cur.searchCurChild).getTarget().searchState != Vertex.DiscoverState.UNDISCOVERED)
            {
                // we still want to process all edges. We must do that if
                // the cur child is not our parent
                // &&
                // we are not the child's parent
                // &&
                // (the child has not yet been proccessed OR we are directed graph)

                int curChildVID = neighborsOut.get(cur.searchCurChild).getTargetID();
                Vertex<V, E> curChild = neighborsOut.get(cur.searchCurChild).getTarget();


                boolean theCurChildDiscoveredUs = curChildVID == cur.searchDiscovererVID;
                boolean weDiscoveredTheCurChild = cur.getUID() == curChild.searchDiscovererVID;
                boolean curChildHasBeenProcessed = curChild.searchState == Vertex.DiscoverState.PROCESSED;


                if (!theCurChildDiscoveredUs && !weDiscoveredTheCurChild && (!curChildHasBeenProcessed || isDirected))
                    processEdge(neighborsOut.get(cur.searchCurChild));

                cur.searchCurChild++;
            }

            // if we found a non-parent, non-processed neighbor then lets
            // explore it
            if (cur.searchCurChild < neighborsOut.size())
            {
                processEdge(neighborsOut.get(cur.searchCurChild));
                if (neighborsOut.get(cur.searchCurChild).getTarget().searchState == Vertex.DiscoverState.UNDISCOVERED)
                {
                    lastDiscovererVID = cur.getUID();
                    stack.push(neighborsOut.get(cur.searchCurChild).getTarget());//EXPLORE
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

        for (Identifiable i : vertexList)
            ((Vertex<V, E>) i).initializeSearch();
    }


}
