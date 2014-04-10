package com.jmt;

import java.awt.*;
import java.util.*;

/**
 * Created by jtappe on 4/3/2014.
 */
public class Tester
{

    public static void testMyGraph()
    {

        System.out.println("Hello, Graph World!!");

        MyGraph<Point, String> g = new MyGraph<Point, String>(true);


        g.addVertex(new Point(0, 2));
        g.addVertex(new Point(4, 8));
        g.addVertex(new Point(2, 234));
        g.addVertex(new Point(23, 23));
        g.addVertex(new Point(221, 25));


        g.addEdge(1, 5, "1 TO 5");
        g.addEdge(5, 1, "5 TO 1");
        g.addEdge(2, 5, "2 TO 5");
        g.addEdge(3, 4, "3 TO 4");
        int edgeFive = g.addEdge(4, 5, "4 TO 5");


        Iterator<Integer> itr = g.getVertices().iterator();

        while (itr.hasNext())
        {
            int nextID = itr.next();
            int x = g.getData(nextID).x;
            int y = g.getData(nextID).y;
            System.out.println("VID: " + nextID + "\t(" + x + "," + y + ")");
        }

        itr = g.getEdges().iterator();
        while (itr.hasNext())
        {
            int nextID = itr.next();
            System.out.println("EID: " + nextID + "\t'" + g.getAttribute(nextID) + ";");
        }

        System.out.println("*************");


        itr = g.getEdgesOf(edgeFive).iterator();
        while (itr.hasNext())
        {
            int nextId = itr.next();

            System.out.println("EID " + nextId + "\t'" + g.getAttribute(nextId) + "'");
        }
    }


    public static <V, E> int countPartitions(MyGraph<V, E> graph)
    {
        graph.initializeSearch();
        Set<Integer> vIDs = graph.getVertices();
        Iterator<Integer> itr = vIDs.iterator();

        int count = 0;
        while (itr.hasNext())
        {
            int curID = itr.next();
            Vertex<V, E> curVertex = graph.getVertex(curID);
            if (curVertex.getState() == Vertex.DiscoverState.UNDISCOVERED)
            {
                System.out.println(curID);
                graph.doDFS(curID);
                count++;
            }

        }
        return count;
    }

    private static String tabs(int t)
    {
        if (t < 0)
            return "";

        String tab = "";
        for (int i = 0; i < t; i++)
            tab += "\t";
        return tab;
    }


    public static <V, E> void printSearch(MyGraph<V, E> graph, boolean DFS)
    {
        GraphSearchProcessor gsp = new GraphSearchProcessor<V, E>()
        {
            public int sTabs = 0;

            @Override
            public void processVertexEarly(MyGraph<V, E> graph, Vertex<V, E> vertex)
            {
                System.out.println(tabs(sTabs++) + "early: " + vertex.getVID() + "(" + vertex.getData().toString() + ")");
            }

            @Override
            public void processEdge(MyGraph<V, E> graph, Vertex<V, E> from, Vertex<V, E> to)
            {
                System.out.println(tabs(sTabs) + " edge: " + from.getVID() + " -> " + to.getVID());
            }

            @Override
            public void processVertexLate(MyGraph<V, E> graph, Vertex<V, E> vertex)
            {
                System.out.println(tabs(--sTabs) + " late: " + vertex.getVID() + "(" + vertex.getData().toString() + ")");

            }
        };
        graph.addGraphSearchProcessor(gsp);

        if (DFS)
            graph.doExhaustiveDFS();
        else
            graph.doExhaustiveBFS();


        graph.removeGraphSearchProcessor(gsp);
    }


    public static void main(String args[])
    {
        MyGraph<Coordinate, Street> simpleDirectedGraph;
        MyGraph<Coordinate, Street> amesGraph;

        try
        {
            simpleDirectedGraph = GraphFactory.loadGraph("/home/jim/IdealProjects/GraphProject/src/simpleDirectedGraph.txt", false);
            amesGraph = GraphFactory.loadGraph("/home/jim/IdealProjects/GraphProject/src/ames.txt", false);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("Unable to open graph file");
            System.out.println(e.getMessage());
            return;
        }
        printSearch(simpleDirectedGraph, true);

        System.out.println("Ames has " + countPartitions(amesGraph) + " partition(s)");
        System.out.println("Simple Directed Map has " + countPartitions(simpleDirectedGraph) + " partition(s)");

        System.out.println("----------------------");
    }
}
