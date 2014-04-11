package com.jmt;

import java.util.*;

/**
 * Created by jim on 4/10/14.
 */
public class MyDijkstra<V, E> implements Dijkstra<V, E>
{

    private class CostPath implements Comparable<CostPath>
    {
        private final int vID;
        private int srcVID;
        private int connectingEID;
        private boolean complete;
        private double costToGetHere;

        private CostPath(int vID)
        {
            this.vID = vID;
            costToGetHere = -1;
            connectingEID = -1;
            srcVID = -1;
            complete = false;
        }

        private CostPath(int vID, int srcVID, int connectingEID, boolean complete, double costToGetHere)
        {
            this.vID = vID;
            this.srcVID = srcVID;
            this.connectingEID = connectingEID;
            this.complete = complete;
            this.costToGetHere = costToGetHere;
        }

        @Override
        public int compareTo(CostPath costPath)
        {
            //-1 means infiniti

            if (this.costToGetHere == -1 && costPath.costToGetHere != -1)
                return 1;
            else if (this.costToGetHere != -1 && costPath.costToGetHere == -1)
                return -1;

            return (new Double(this.costToGetHere)).compareTo(new Double(costPath.costToGetHere));
        }


    }

    private Graph<V, E> graph;
    private HashMap<Integer, CostPath> costMap;
    private HashMap<Integer, CostPath> costMapCompleted;

    private Weighing<E> weighing;
    private int startID;
    private boolean needToCallComputeShortestPath;


    public MyDijkstra()
    {
        graph = null;
        costMap = null;
        weighing = null;
        startID = -1;
        needToCallComputeShortestPath = true;
    }


    /**
     * Sets the graph to use in computation
     *
     * @param graph
     */
    @Override
    public void setGraph(Graph<V, E> graph)
    {

        this.graph = graph;
        costMap = null;

        this.startID = -1;
        needToCallComputeShortestPath = true;
    }


    /**
     * Sets the start vertex the algorithm will use in computation
     *
     * @param startId
     * @throws IllegalArgumentException if the start vertex does not exist in the graph
     * @throws IllegalStateException    if no graph has been set
     */
    @Override
    public void setStart(int startId) throws IllegalArgumentException, IllegalStateException
    {
        if (graph == null)
            throw new IllegalStateException("Please set graph first.");


        //This method call should throw IllegalArgumentException if startId is not in the graph
        graph.getData(startId);

        this.startID = startId;
        needToCallComputeShortestPath = true;
    }

    /**
     * Sets the weighing to be used in computing the cost of traversing an edge
     *
     * @param weighing
     */
    @Override
    public void setWeighing(Weighing<E> weighing)
    {
        this.weighing = weighing;
        needToCallComputeShortestPath = true;
    }

    /**
     * Computes the shortest path to all vertices from the start vertex in the graph
     * using the weighing function
     *
     * @throws IllegalStateException if the graph, start vertex, or weighing object have not been set
     */
    @Override
    public void computeShortestPath() throws IllegalStateException
    {


        PriorityQueue<CostPath> nextCostPathQueue = new PriorityQueue<CostPath>();


        if (startID == -1 || graph == null || weighing == null)
            throw new IllegalStateException();

        costMap = new HashMap<Integer, CostPath>();


        costMap.put(startID, new CostPath(startID));
        costMap.get(startID).costToGetHere = 0;
        costMap.get(startID).complete = true;

        nextCostPathQueue.add(costMap.get(startID));

        int curVID = startID;
        boolean finished = false;
        while (!finished)
        {
            Set<Integer> edgesOut = graph.getEdgesOf(curVID);
            Iterator<Integer> itr = edgesOut.iterator();

            CostPath curVIDCostPath = costMap.get(curVID);


            while (itr.hasNext())
            {//calculate and update costMap for all the targets of the edges

                int eID = itr.next();
                double curEdgeWeight = weighing.weight(graph.getAttribute(eID));

                int nextVID = graph.getTarget(eID);
                CostPath costPathToNextVertex = costMap.get(nextVID);

                if (costPathToNextVertex == null)//create it
                {
                    costPathToNextVertex = new CostPath(nextVID, curVID, eID, false, curEdgeWeight + curVIDCostPath.costToGetHere);
                    nextCostPathQueue.add(costPathToNextVertex);
                }
                else
                {

                    //update as neccessary
                    double costToGetHere = curVIDCostPath.costToGetHere + curEdgeWeight;
                    if (costPathToNextVertex.costToGetHere > costToGetHere)
                    {
                        nextCostPathQueue.remove(costPathToNextVertex);

                        costPathToNextVertex.srcVID = curVID;
                        costPathToNextVertex.connectingEID = eID;
                        costPathToNextVertex.costToGetHere = costToGetHere;

                        nextCostPathQueue.add(costPathToNextVertex);
                    }
                }
            }


            //find smallest cost non-completed costPath
            Iterator<CostPath> citr = nextCostPathQueue.iterator();
            boolean keepLooking = true;

            CostPath nextCostPath;
            while (citr.hasNext() && keepLooking)
            {
                nextCostPath = citr.next();
                keepLooking = nextCostPath.complete;
                curVID = nextCostPath.vID;
            }
            if(keepLooking)
                throw new Error("shouldn't happen");//TODO





        }


    }

/*
    private void initializeCostMap()
    {
        costMap = new HashMap<Integer, CostPath>();

        Iterator<Integer> itr = graph.getVertices().iterator();
        while (itr.hasNext())
        {
            int vID = itr.next();
            costMap.put(vID, new CostPath(vID));
            if (vID == startID)
                costMap.get(vID).costToGetHere = 0;
        }
    }*/


    /**
     * Returns the path from the start vertex to the end vertex provided
     *
     * @param endId
     * @return a list representing the shortest path. The first element is the start vertex, and the last
     * is the end vertex.
     * @throws IllegalArgumentException if endId is not a vertex in the graph
     * @throws IllegalStateException    if computeShortestPath has not been called since any of the set methods
     *                                  were last called
     */
    @Override
    public List<Integer> getPath(int endId) throws IllegalArgumentException, IllegalStateException
    {

        List<Integer> edgePath = getEdgePath(endId);
        List<Integer> verts = new ArrayList<Integer>();

        Iterator<Integer> itr = edgePath.iterator();


        int eid = -1;
        while (itr.hasNext())
        {
            eid = itr.next();
            verts.add(graph.getSource(eid));
        }
        if (eid != -1)
            verts.add(graph.getTarget(eid));

        return verts;
    }


    private List<Integer> getEdgePath(int endVID) throws IllegalArgumentException, IllegalStateException
    {
        if (needToCallComputeShortestPath)
            throw new IllegalStateException("computeShortestPath() has not been called yet");

        //throws IllegalArgumentException if endId is not valid
        graph.getData(endVID);

        //TODO

        return null;
    }

    /**
     * Returns the cost of the shortest path from the start vertex to the end vertex where
     * cost is defined by the sum of the weights of all the edges that connects the path as
     * defined by the weighing object.
     *
     * @param endId
     * @throws IllegalArgumentException if endId is not a vertex in the graph
     * @throws IllegalStateException    if computeShortestPath has not been called since any of the set methods
     *                                  were last called
     */
    @Override
    public double getCost(int endId) throws IllegalArgumentException, IllegalStateException
    {

        List<Integer> shortestPath = getEdgePath(endId);

        double totalCost = 0.0;

        Iterator<Integer> itr = shortestPath.iterator();


        while (itr.hasNext())
            totalCost += weighing.weight(graph.getAttribute(itr.next()));

        return totalCost;

    }

}
