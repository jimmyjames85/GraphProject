package com.jmt;

import java.awt.*;
import java.util.*;
import java.util.List;


/**
 * Created by jtappe on 4/3/2014.
 */
public class Tester
{
    private static String tabs(int t)
    {
        if (t < 0)
            return "";

        String tab = "";
        for (int i = 0; i < t; i++)
            tab += "\t";
        return tab;
    }

    public static void printList(List<Integer> list)
    {
        Iterator<Integer> itr = list.iterator();
        while (itr.hasNext())
            System.out.print(itr.next() + " ");
        System.out.println();
    }

    public static void recSort(ArrayList<Integer> arr, int left, int right)
    {

        if (left >= right)
            return;


        int mid = (left + right) / 2;

        recSort(arr, left, mid);
        recSort(arr, mid + 1, right);

        //combine

        ArrayList<Integer> copy = new ArrayList<Integer>();

        // MERGE
        int a = left;
        int b = mid + 1;

        while (a <= mid && b <= right)
        {
            int cmp;
            cmp = arr.get(a) - arr.get(b);

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


    private static List<Integer> doTopologicalSort(Graph<Coordinate, Street> g)
    {
        final GraphHandler<Coordinate, Street> handler = new GraphHandler<Coordinate, Street>(g, true);


        GraphSearchProcessor gsp = new GraphSearchProcessor<Coordinate, Street>()
        {
            Stack<Integer> stack = new Stack();
            boolean foundCycle = false;

            @Override
            public void processVertexEarly(Graph<Coordinate, Street> graph, int vID)
            {
            }

            @Override
            public void processEdge(Graph<Coordinate, Street> graph, int eID, int vIDfrom, int vIDto)
            {
                if (handler.getVertexState(vIDto) == GraphHandler.DiscoverState.DISCOVERED)
                {
                    //We have found a cycle and need to abort!
                    handler.stopSearch();
                    foundCycle = true;
                }

            }

            @Override
            public void processVertexLate(Graph<Coordinate, Street> graph, int vID)
            {
                stack.push(vID);
            }

            @Override
            public Object getResults()
            {
                ArrayList<Integer> topSortOrder = new ArrayList<Integer>();
                if (foundCycle)
                    return topSortOrder;

                while (!stack.isEmpty())
                    topSortOrder.add(stack.pop());

                return topSortOrder;
            }
        };

        handler.addGraphSearchProcessor(gsp);
        handler.doExhaustiveDFS();
        ArrayList<Integer> ret = (ArrayList<Integer>) gsp.getResults();


        return ret;

    }

    public static void main(String args[]) throws Exception
    {
        HashMap<Integer, Integer> fileToGraphVIDMap = new HashMap<Integer, Integer>();
        MyGraph<Coordinate, Street> g = GraphFactory.loadGraph("./graphs/distanceedjohnDijkstra.txt", true, fileToGraphVIDMap);


        g.removeEdge(7);
        g.removeEdge(8);
        g.removeEdge(14);
        printList(doTopologicalSort(g));


        MyDijkstra<Coordinate, Street> dijkstra = new MyDijkstra<Coordinate, Street>();
        dijkstra.setGraph(g);
        dijkstra.setStart(5);
        dijkstra.setWeighing(new MyWeighing(g));


        dijkstra.computeShortestPath();


        GuiGraphDriver gui = new GuiGraphDriver(g);

        List<Integer> path = dijkstra.getPath(8);
        gui.addPath(path, Color.yellow);
        printList(path);

        g.removeEdge(dijkstra.getConnectingEID(2));
        dijkstra.computeShortestPath();


        path = dijkstra.getPath(8);
        gui.addPath(path, Color.cyan);
        printList(path);




/*        g.removeEdge(dijkstra.getConnectingEID(7));
        dijkstra.computeShortestPath();
        printList(dijkstra.getPath(7));
        */


    }


}
