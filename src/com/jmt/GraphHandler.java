package com.jmt;

import java.util.*;

/**
 * Created by jim on 4/9/14.
 */
public class GraphHandler<V, E>
{

    private Graph<V, E> mainGraph;
    //    private boolean isDirected;

    private ArrayList<GraphSearchProcessor<V, E>> graphSearchProcessors;


    private HashMap<Integer, VertexInfo> vertInfo;

    public GraphHandler(Graph<V, E> graph, boolean isDirected)
    {
        setGraph(graph);//isDirected);
    }

    private void setGraph(Graph<V, E> graph)
    {
        if (graph == null)
            throw new NullPointerException();

        mainGraph = graph;

        graphSearchProcessors = new ArrayList<GraphSearchProcessor<V, E>>();
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

    private void processVertexEarly(int vID)
    {
        for (GraphSearchProcessor gsp : graphSearchProcessors)
            gsp.processVertexEarly(mainGraph, vID);
    }

    private void processEdge(int eID, int vIDfrom, int vIDto)
    {
        for (GraphSearchProcessor gsp : graphSearchProcessors)
            gsp.processEdge(mainGraph, eID, vIDfrom, vIDto);
    }

    private void processVertexLate(int vID)
    {
        for (GraphSearchProcessor gsp : graphSearchProcessors)
            gsp.processVertexLate(mainGraph, vID);
    }


    public void initializeForSearch()
    {
        vertInfo = new HashMap<Integer, VertexInfo>();
        searchTime = 1;
        keepSearching = true;//TODO implement this into the code

        Iterator<Integer> itr = mainGraph.getVertices().iterator();
        while (itr.hasNext())
        {
            int vID = itr.next();
            VertexInfo vi = new VertexInfo(vID);
            vertInfo.put(vID, vi);

        }
    }


    public void doExhaustiveBFS()
    {
        initializeForSearch();
        Iterator<VertexInfo> itr = vertInfo.values().iterator();
        while (itr.hasNext())
        {
            VertexInfo vi = itr.next();
            if (vi.searchState == DiscoverState.UNDISCOVERED)
                doBFS(vi.vID);
        }
    }


    public void doBFS(int startVID)
    {
        VertexInfo cur = getVertexInfo(startVID);

        //Visit starting node
        cur.searchDiscovererVID = -1; //null
        cur.searchDiscoverTime = searchTime++;
        cur.searchState = DiscoverState.DISCOVERED;

        ArrayDeque<Integer> queue = new ArrayDeque<Integer>();
        queue.add(cur.vID);

        while (!queue.isEmpty())
        {
            cur = getVertexInfo(queue.pop());
            processVertexEarly(cur.vID);

            while (cur.edgesOut.hasNext())
            {
                int curEdgeID = cur.edgesOut.next();
                VertexInfo curTarget = getVertexInfo(mainGraph.getTarget(curEdgeID));

                if (curTarget.searchState != DiscoverState.PROCESSED)//TODO|| isDirected)
                    processEdge(curEdgeID, cur.vID, curTarget.vID);

                if (curTarget.searchState == DiscoverState.UNDISCOVERED)
                {
                    curTarget.searchState = DiscoverState.DISCOVERED;
                    curTarget.searchDiscoverTime = searchTime++;
                    curTarget.searchDiscovererVID = cur.vID;
                    queue.add(curTarget.vID);
                }
            }

            //we've processed this node now
            cur.searchProcessTime = searchTime++;
            cur.searchState = DiscoverState.PROCESSED;
            processVertexLate(cur.vID);
        }
    }


    public VertexInfo getVertexInfo(int vID)
    {
        VertexInfo ret = vertInfo.get(vID);
        if (ret == null)
            throw new IllegalArgumentException(vID + " is not a registered Vertex ID.");

        return ret;
    }


    public ArrayList<Integer> findShortestPath(int vIDfrom, int vIDto)
    {
        //TODO TEST and change

        initializeForSearch();
        doBFS(vIDfrom);//TODO write doBFS/DFS(fromVID, stopSearchAtvID);

        ArrayList<Integer> ret = new ArrayList<Integer>();
        VertexInfo cur = getVertexInfo(vIDto);

        ret.add(cur.vID);
        while (cur.searchDiscovererVID != -1)
        {
            cur = getVertexInfo(cur.searchDiscovererVID);
            ret.add(cur.vID);
        }
        return ret;
    }

    public boolean isGraphCyclic()
    {
        ArrayList<GraphSearchProcessor<V, E>> tmp = graphSearchProcessors;
        graphSearchProcessors = new ArrayList<GraphSearchProcessor<V, E>>();


        GraphSearchProcessor<V, E> gsp = new GraphSearchProcessor<V, E>()
        {
            boolean cyclic = false;

            @Override
            public void processVertexEarly(Graph<V, E> graph, int vID)
            {

            }

            @Override
            public void processEdge(Graph<V, E> graph, int eID, int vIDfrom, int vIDto)
            {
                if (getVertexInfo(vIDto).searchState == DiscoverState.DISCOVERED)
                    cyclic = true;
            }

            @Override
            public void processVertexLate(Graph<V, E> graph, int vID)
            {

            }

            public Object getResults()
            {
                return new Boolean(cyclic);
            }

        };

        addGraphSearchProcessor(gsp);
        doExhaustiveDFS();
        graphSearchProcessors = tmp;
        return (Boolean) gsp.getResults();
    }


    public void doExhaustiveDFS()
    {
        initializeForSearch();
        Iterator<VertexInfo> itr = vertInfo.values().iterator();
        while (itr.hasNext())
        {
            VertexInfo vi = itr.next();
            if (vi.searchState == DiscoverState.UNDISCOVERED)
                doDFS(vi.vID);
        }
    }

    public DiscoverState getVertexState(int vID)
    {
        return getVertexInfo(vID).searchState;
    }


    private boolean keepSearching;

    //TO be called from a graphsearchprocessor to stop the search
    public void stopSearch()
    {
        keepSearching = false;
    }

    public void doDFS(int startVID)
    {
        if (getVertexState(startVID) != DiscoverState.UNDISCOVERED)
            throw new IllegalStateException("Vertex " + startVID + " has already been discovered.");

        int lastDiscovererVID = -1;//null
        int curVid = startVID;
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(curVid);
        while (!stack.isEmpty())
        {
            curVid = stack.peek();


            VertexInfo cur = getVertexInfo(curVid);


            if (cur.searchState == DiscoverState.UNDISCOVERED)
            {
                cur.searchDiscovererVID = lastDiscovererVID;
                cur.searchDiscoverTime = searchTime++;
                cur.searchState = DiscoverState.DISCOVERED;//visit vertex
                processVertexEarly(curVid);
            }


            // Every vertex has an iterator to keep track of which verticies it has searched already
            //We pick up where we left of and we seek to find the next undisovered neighbor

            boolean weHaveFoundTheNextUNDISCOVEREDNeighbor = false;
            VertexInfo curNeighbor = null;
            int curEdgeID = -1;
            while (cur.edgesOut.hasNext() && !weHaveFoundTheNextUNDISCOVEREDNeighbor)
            {

                curEdgeID = cur.edgesOut.next();

                curNeighbor = getVertexInfo(mainGraph.getTarget(curEdgeID));

                weHaveFoundTheNextUNDISCOVEREDNeighbor = curNeighbor.searchState == DiscoverState.UNDISCOVERED;

                if (!weHaveFoundTheNextUNDISCOVEREDNeighbor)
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
                    boolean curChildHasBeenProcessed = curNeighbor.searchState == DiscoverState.PROCESSED;


                    if (!theCurNeighborHasDiscoveredUs && !weDiscoveredTheCurChild)//TODO && (curChildHasBeenProcessed || isDirected))
                        processEdge(curEdgeID, cur.vID, curNeighbor.vID);
                }
            }


            // if we found the next undiscovered neighbor then lets explore it
            if (curNeighbor != null)
            {
                processEdge(curEdgeID, cur.vID, curNeighbor.vID);

                //TODO try it without this if statement
                if (curNeighbor.searchState == DiscoverState.UNDISCOVERED)
                {
                    lastDiscovererVID = cur.vID;
                    stack.push(curNeighbor.vID);
                }
            }
            else
            // all neighbors have been visited and we are done
            {
                // Complete Visit
                cur.searchProcessTime = searchTime++;
                cur.searchState = DiscoverState.PROCESSED;
                processVertexLate(cur.vID);
                stack.pop();
            }
        }

    }


    public enum DiscoverState
    {
        UNDISCOVERED, DISCOVERED, PROCESSED
    }

    private int searchTime = 1;


    public class VertexInfo
    {
        protected Iterator<Integer> edgesOut;    //used to keep track of the the current neighbor this vertex is exploring
        protected int searchDiscovererVID;//id of the vertex that discovered this vertex
        protected int searchDiscoverTime;
        protected int searchProcessTime;
        protected DiscoverState searchState;
        protected int vID;

        public VertexInfo(int vID)
        {
            this.vID = vID;
            searchState = DiscoverState.UNDISCOVERED;
            searchProcessTime = -1;
            searchDiscoverTime = -1;
            searchDiscovererVID = -1;
            edgesOut = mainGraph.getEdgesOf(vID).iterator();
        }

        public int getDiscoverorVID()
        {
            return searchDiscovererVID;
        }


    }

}
