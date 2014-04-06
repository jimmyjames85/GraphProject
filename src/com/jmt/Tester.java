package com.jmt;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by jtappe on 4/3/2014.
 */
public class Tester
{

    public static void testMyGraph()
    {

        System.out.println("Hello, Graph World!!");

        MyGraph<Point, String> g = new MyGraph<Point, String>();


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


    public static void main(String args[])
    {
        MyGraph<Coordinate, Street> simpleGraph;
        try
        {
            simpleGraph = GraphFactory.loadGraph("/home/jim/IdealProjects/GraphProject/src/ames.txt");

        } catch (Exception e)
        {
            System.out.println("Unable to open graph file");
            return;
        }


        String save = "";

        //"/home/jim/IdealProjects/GraphProject/src/simpleGraph.txt");


        Iterator<Integer> itr = simpleGraph.getVertices().iterator();


        int sumDeg = 0;
        while (itr.hasNext())
        {

            int vID = itr.next();
            int deg = simpleGraph.getDegreeIn(vID) + simpleGraph.getDegreeOut(vID);


            if (deg != simpleGraph.getEdgesOf(vID).size())
                System.out.println("VID " + vID);

            // int deg = simpleGraph.getEdgesOf(vID).size();
            sumDeg += deg;
        }

        System.out.println("sumDeg=" + sumDeg);


//        System.out.println(simpleGraph.getSource(3059) + "," + simpleGraph.getTarget(3059));

        while(itr.hasNext())
            System.out.print(" " + itr.next());

        System.out.println();





    }
}
