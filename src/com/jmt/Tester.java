package com.jmt;


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


    //assumes src is 1 to 1 and onto
    public static HashMap<Integer, Integer> inverseHash(HashMap<Integer, Integer> src)
    {
        HashMap<Integer, Integer> invMap = new HashMap<Integer, Integer>();
        Iterator<Integer> itr = src.keySet().iterator();
        while (itr.hasNext())
        {
            int key = itr.next();
            int value = src.get(key);
            invMap.put(value, key);
        }

        return invMap;
    }

    public static void main(String args[]) throws Exception
    {


        //Load ames map
        HashMap<Integer, Integer> fileToGraphVIDMap = new HashMap<Integer, Integer>();
        Graph<Coordinate, Street> amesGraph = GraphFactory.loadGraph("/home/jim/IdealProjects/GraphProject/src/newAmes.txt", false, fileToGraphVIDMap);
        HashMap<Integer, Integer> graphToFileVIDMap = inverseHash(fileToGraphVIDMap);

        //Ingrediants DAG
        Graph<Integer, String> ingredientsGraph = GraphFactory.produceIngredientOrder();
        CoffeeSolver<Integer, String> ingredientsSolver = new JimAlgorithm<Integer, String>();

        //Create Ordered List of graphID's
        ArrayList<Integer> ingredientOrder = new ArrayList<Integer>();
        Iterator<Integer> itr = ingredientsSolver.sortVertices(ingredientsGraph).iterator();
        while (itr.hasNext())
            ingredientOrder.add(fileToGraphVIDMap.get(ingredientsGraph.getData(itr.next())));

        //Insert our location first
        ingredientOrder.add(0, fileToGraphVIDMap.get(2893) );

        System.out.println("\nOrder of Ingredient Locations to visit (starting at our location): ");
        itr = ingredientOrder.iterator();
        while (itr.hasNext())
            System.out.print(graphToFileVIDMap.get(itr.next()) + " ");
        System.out.println();


        Weighing<Street> weigh = new MyWeighing(amesGraph);
        CoffeeSolver<Coordinate, Street> pathSolver = new JimAlgorithm<Coordinate, Street>();
        List<Integer> path = pathSolver.shortestPath(amesGraph, ingredientOrder, weigh);

        System.out.println("\nPath we will take to collect all ingredients: ");
        itr = path.iterator();
        while(itr.hasNext())
            System.out.print(graphToFileVIDMap.get(itr.next())+" ");
        System.out.println();

        MyDijkstra<Coordinate,Street> dijkstra = new MyDijkstra<Coordinate, Street>();
        dijkstra.setGraph(amesGraph);
        dijkstra.setWeighing(weigh);

        double totalWeight=0;

        itr=ingredientOrder.iterator();
        int curLoc = itr.next();
        while(itr.hasNext())
        {
            int nextLoc = itr.next();
            dijkstra.setStart(curLoc);
            dijkstra.computeShortestPath();
            totalWeight += dijkstra.getCost(nextLoc);
            curLoc = nextLoc;
        }

        System.out.println("\nTotal Cost: " + totalWeight);







        /*
        ArrayList<Integer> myArr = new ArrayList<Integer>();
        for (int i = 0; i < path.size(); i++)
            myArr.add(path.get(i));

        recSort(myArr, 0, myArr.size() - 1);
        printList(myArr);*/


    }
}
