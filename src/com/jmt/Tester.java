package com.jmt;

import com.jmt.old.IntegerSet;

import java.awt.*;
import java.util.*;

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


    public static void main(String args[])
    {

        Graph<Character, String> g = GraphFactory.produceIngredientOrder();

        Iterator<Integer> itr = g.getVertices().iterator();
        while (itr.hasNext())
            System.out.println(itr.next() + " ");

        System.out.println();


        GraphHandler<Character, String> handler = new GraphHandler<Character, String>(g, true);


        handler.addGraphSearchProcessor(new GraphSearchProcessor<Character, String>()
        {

            int nTab = 0;

            String tabs()
            {
                String ret = "";
                for (int i = 0; i < nTab; i++)
                    ret += "\t";
                return ret;
            }

            @Override
            public void processVertexEarly(Graph<Character, String> graph, int vID)
            {
                System.out.println(tabs() + "Early " + vID + graph.getData(vID));
                nTab++;
            }

            @Override
            public void processEdge(Graph<Character, String> graph, int eID, int vIDfrom, int vIDto)
            {
                System.out.println(tabs() + "EID(" + eID + ") [" + graph.getAttribute(eID) + "] " + graph.getData(vIDfrom) + " -> " + graph.getData(vIDto));
            }

            @Override
            public void processVertexLate(Graph<Character, String> graph, int vID)
            {
                nTab--;
                System.out.println(tabs() + "Late " + vID + graph.getData(vID));
            }
        });

        try
        {
            Graph<Coordinate, Street> gAmes = GraphFactory.loadGraph("/home/jim/IdealProjects/GraphProject/src/ames.txt", false);
            GraphHandler<Coordinate, Street> amesHandler = new GraphHandler<Coordinate, Street>(gAmes, false);

            System.out.println("ames is cyclic=" + amesHandler.isGraphCyclic());
        } catch (Exception e)
        {
        }

        System.out.println(handler.isGraphCyclic());


    }
}
