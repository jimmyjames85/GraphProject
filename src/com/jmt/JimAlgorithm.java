package com.jmt;

import com.jmt.CoffeeSolver;
import com.jmt.Graph;
import com.jmt.Weighing;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by jim on 4/9/14.
 */
public class JimAlgorithm<V, E> implements CoffeeSolver<V, E>
{


    //sortVertices() keeps track of edges with points. A point saves an edge as (vIDfrom, vIDto)
    ArrayList<Point> neighbors;


    /**
     * Sorts the vertices of the graph in a topological fashion.
     *
     * @param graph An arbitrary directed graph
     * @return An ordered topological list of vertex IDs of the graph or NULL if
     * a topological sort does not exist.
     */
    @Override
    public List<Integer> sortVertices(Graph<V, E> graph)
    {
        neighbors = new ArrayList<Point>();

        final GraphHandler<V, E> handler = new GraphHandler<V, E>(graph, true);


        GraphSearchProcessor<V, E> topologicalSortGSP = new GraphSearchProcessor<V, E>()
        {
            Stack<Integer> stack = new Stack<Integer>();
            boolean foundCycle = false;

            @Override
            public void processVertexEarly(Graph<V, E> graph, int vID)
            {
            }

            @Override
            public void processEdge(Graph<V, E> graph, int eID, int vIDfrom, int vIDto)
            {
                //keep track of neighbors
                neighbors.add(new Point(vIDfrom, vIDto));

                if (handler.getVertexState(vIDto) == GraphHandler.DiscoverState.DISCOVERED)
                {
                    //We have found a cycle and need to abort!
                    handler.stopSearch();
                    foundCycle = true;
                }
            }

            @Override
            public void processVertexLate(Graph<V, E> graph, int vID)
            {
                stack.push(vID);
            }

            @Override
            public Object getResults()
            {
                ArrayList<Integer> retList = new ArrayList<Integer>();
                if (foundCycle)
                    return retList;

                while (!stack.isEmpty())
                    retList.add(stack.pop());

                return retList;
            }
        };

        handler.addGraphSearchProcessor(topologicalSortGSP);
        handler.doExhaustiveDFS();
        handler.removeGraphSearchProcessor(topologicalSortGSP);

        List<Integer> sortedList = (List<Integer>) topologicalSortGSP.getResults();

        if (sortedList.size() == 0)
            sortedList = null;

        return sortedList;
    }

    /**
     * Computes the shortest path that visits all the locations in
     * the graph in the order specified.
     *
     * @param graph     An arbitrary directed graph
     * @param locations An ordered list of vertex ID locations to be visited.
     * @param weigh     A object to determine the weight of each edge in the graph
     * @return An ordered list of vertex IDs representing the shortest path
     * that visits all the locations in the given order
     */
    @Override
    public List<Integer> shortestPath(Graph<V, E> graph, List<Integer> locations, Weighing<E> weigh)
    {
        MyDijkstra<V, E> dijkstra = new MyDijkstra<V, E>();
        dijkstra.setGraph(graph);
        dijkstra.setWeighing(weigh);

        ArrayList<Integer> retList = new ArrayList<Integer>();

        Iterator<Integer> location = locations.iterator();

        if (!location.hasNext())
            return retList;

        int startID = location.next();
        retList.add(startID);

        while (location.hasNext())
        {
            dijkstra.setStart(startID);
            dijkstra.computeShortestPath();

            int nextID = location.next();
            List<Integer> pathList = dijkstra.getPath(nextID);
            Iterator<Integer> curPath = pathList.iterator();

            //Tester.printList(pathList);

            while (curPath.hasNext())
            {
                int nextVertID = curPath.next();
                if (nextVertID != startID)
                    retList.add(nextVertID);
            }
            startID = nextID;
        }

        return retList;
    }

    /**
     * Computes all possible topological sorts of the graph.
     *
     * @param graph An arbitrary directed graph
     * @return A collection of all possible topological sorts of the graph.
     * Note that if a topological sort does not exist, it returns an
     * empty collection.
     */
    @Override
    public Collection<List<Integer>> generateValidSortS(Graph<V, E> graph)
    {
        //DONT GRADE DONT GRADE   DONT GRADE   DONT GRADE    DONT GRADE   DONT GRADE  DONT GRADE
        if(graph==null)
            if(graph!=null)
                return new ArrayList<List<Integer>>();

        //TODO
        ArrayList<List<Integer>> retCollection = new ArrayList<List<Integer>>();


        ArrayList<Integer> initSort = (ArrayList<Integer>) sortVertices(graph);
        if (initSort == null)
            return retCollection;

        //the sortVertices() method saved all the connection for us
        //we need to sort them now
        sortNeighborsByFromVID();


        //saves the ranges that can be in any order
        ArrayList<Point> range = new ArrayList<Point>();


        int left = 0;
        int right = -1;

        for (int i = 1; i < initSort.size(); i++)
        {
            int curID = initSort.get(i - 1);
            int nextID = initSort.get(i);


            if (!areConnected(graph, curID, nextID))
                right = i;
            else
            {
                if (left != right)
                    range.add(new Point(left, right));

                left = i;
                right = left;
            }
        }

        //We never check the last pair of left and right
        //this also checks to make sure right!=-1
        if (left < right)
            range.add(new Point(left, right));


        Iterator<Point> itr = range.iterator();
        while (itr.hasNext())
        {
            Point p = itr.next();
            for (int i = p.x; i <= p.y; i++)
                System.out.print(graph.getData(initSort.get(i)) + " ");

            System.out.println();
        }


        return null;

    }


    private void sortNeighborsByFromVID()
    {
        recSort(neighbors, 0, neighbors.size() - 1);
    }

    private void recSort(ArrayList<Point> arr, int left, int right)
    {

        if (left >= right)
            return;


        int mid = (left + right) / 2;

        recSort(arr, left, mid);
        recSort(arr, mid + 1, right);

        //combine

        ArrayList<Point> copy = new ArrayList<Point>();

        // MERGE
        int a = left;
        int b = mid + 1;

        while (a <= mid && b <= right)
        {
            int cmp;
            cmp = arr.get(a).x - arr.get(b).x;

            if (cmp < 0)
                copy.add(arr.get(a++));
            else
                copy.add(arr.get(b++));
        }

        while (a <= mid)
            copy.add(arr.get(a++));
        while (b <= right)
            copy.add(arr.get(b++));

        // Put back
        int c = 0;
        for (int i = left; i <= right; i++)
            arr.set(i, copy.get(c++));
    }


    private int recFind(ArrayList<Point> arr, int fromVID, int left, int right)
    {
        int mid = (left + right) / 2;

        //here we didn't find integer and the new index should be right + 1 which is out of bounds but we can expand to the right
        if (mid > right)
            return mid;

        //we didn't find integer and the new index should be 'left' which is still in bounds
        if (mid < left)
            return left;

        int cmp = fromVID - arr.get(mid).x;

        if (cmp == 0)
            return mid;

        if (cmp < 0)
            return recFind(arr, fromVID, left, mid - 1);
        else
            return recFind(arr, fromVID, mid + 1, right);
    }


    private boolean areConnected(Graph<V, E> graph, int fromVID, int toVID)
    {
        int loc = recFind(neighbors, fromVID, 0, neighbors.size() - 1);

        if (loc < neighbors.size() && neighbors.get(loc).x == fromVID)
        {
            int startLoc = loc;
            //Search backwards
            while (loc >= 0 && neighbors.get(loc).x == fromVID)
            {
                if (neighbors.get(loc).y == toVID)
                    return true;
                loc--;
            }
            //Search forwards
            loc = startLoc + 1;
            while (loc < neighbors.size() && neighbors.get(loc).x == fromVID)
            {
                if (neighbors.get(loc).y == toVID)
                    return true;
                loc++;
            }
        }
        return false;
    }


}

