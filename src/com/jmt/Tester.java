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
        Graph<Character, String> g = GraphFactory.produceTopSortTest();



        MyCoffeeSolver<Character, String> solver = new MyCoffeeSolver<Character, String>();

        Iterator<Integer> itr = solver.sortVertices(g).iterator();
        while(itr.hasNext())
            System.out.print(g.getData(itr.next())+ " ");
        System.out.println();

        solver.generateValidSortS(g);

/*        Iterator<Integer> itr = solver.sortVertices(g).iterator();
        if (itr != null)
            while (itr.hasNext())
                System.out.print(g.getData(itr.next()) + " ");
                */


    }
}
