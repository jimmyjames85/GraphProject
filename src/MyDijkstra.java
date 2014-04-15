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
            //-1 means infinity
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
        CostPath startCostPath = new CostPath(startID, -1, -1, true, 0.0);
        costMap.put(startID, startCostPath);

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
                if (nextVID == curVID)
                    nextVID = graph.getSource(eID);


                CostPath costPathToNextVertex = costMap.get(nextVID);

                if (costPathToNextVertex == null)//create it
                {
                    costPathToNextVertex = new CostPath(nextVID, curVID, eID, false, curEdgeWeight + curVIDCostPath.costToGetHere);
                    costMap.put(nextVID, costPathToNextVertex);
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
            if (nextCostPathQueue.isEmpty())
            {
                finished = true;
                break;
            }


            CostPath smallest = nextCostPathQueue.remove();
            smallest.complete = true;
            curVID = smallest.vID;
        }

        needToCallComputeShortestPath = false;

    }

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
        if (needToCallComputeShortestPath)
            throw new IllegalStateException("computeShortestPath() has not been called yet");

        //throws IllegalArgumentException if endId is not valid
        if (!graph.getVertices().contains(endId))
            throw new IllegalArgumentException("No such vertex ID " + endId);


        Stack<Integer> stackPath = new Stack<Integer>();

        CostPath cur = costMap.get(endId);

        while (cur != null)
        {
            if (cur.srcVID == -1)//then we one away from the start
                stackPath.push(startID);
            else
                stackPath.push(cur.vID);

            cur = costMap.get(cur.srcVID);
        }

        ArrayList<Integer> retList = new ArrayList<Integer>();
        while (!stackPath.isEmpty())
            retList.add(stackPath.pop());


        return retList;
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
        if (needToCallComputeShortestPath)
            throw new IllegalStateException("computeShortestPath() has not been called yet");

        //throws IllegalArgumentException if endId is not valid
        graph.getData(endId);

        CostPath cur = costMap.get(endId);

        if (cur == null)
            throw new IllegalArgumentException("No way to reach vertex " + endId + " from " + startID);

        return cur.costToGetHere;
    }

}
