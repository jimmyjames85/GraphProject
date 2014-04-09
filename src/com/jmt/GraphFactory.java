package com.jmt;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.IllegalFormatException;
import java.util.Scanner;

/**
 * Created by jim on 4/6/14.
 */
public class GraphFactory
{


    private static int getAssignedVID(ArrayList<Point> vidMapping, int fileVID)
    {
        int loc = findRecFileVID(vidMapping, fileVID, 0, vidMapping.size() - 1);


        if (loc < vidMapping.size() && vidMapping.get(loc).x == fileVID)
            return vidMapping.get(loc).y;

        throw new IllegalArgumentException("could not find " + fileVID);

    }

    //assumes vidMapping is sorted by fileVID
    private static int findRecFileVID(ArrayList<Point> vidMapping, int fileVID, int left, int right)
    {
        int mid = (left + right) / 2;

        if (mid > right)
            return mid;

        if (mid < left)
            return left;

        if (fileVID == vidMapping.get(mid).x)
            return mid;

        if (fileVID < vidMapping.get(mid).x)
            return findRecFileVID(vidMapping, fileVID, left, mid - 1);
        else
            return findRecFileVID(vidMapping, fileVID, mid + 1, right);
    }


    //if byFileVID is true than sort is by FileVID
    private static void sortCoordinateMapping(ArrayList<Point> vidMapping, boolean byFileVID)
    {
        recMergeSort(vidMapping, 0, vidMapping.size() - 1, byFileVID);
    }


    private static void recMergeSort(ArrayList<Point> arr, int left, int right, boolean byFileVID)
    {

        if (left >= right)
            return;

        //changes for git test


        int mid = (left + right) / 2;

        recMergeSort(arr, left, mid, byFileVID);
        recMergeSort(arr, mid + 1, right, byFileVID);

        ArrayList<Point> tmp = new ArrayList<Point>();
        int li = left;
        int ri = mid + 1;

        while (li <= mid && ri <= right)
        {
            boolean addLeft = true;
            if (byFileVID)
                addLeft = (arr.get(li).x < arr.get(ri).x);
            else
                addLeft = (arr.get(li).y < arr.get(ri).y);

            if (addLeft)
                tmp.add(arr.get(li++));
            else
                tmp.add(arr.get(ri++));
        }
        while (li <= mid)
            tmp.add(arr.get(li++));
        while (ri <= right)
            tmp.add(arr.get(ri++));

        //transfer tmp to orig
        for (int i = 0; i < tmp.size(); i++)
            arr.set(left++, tmp.get(i));//replace


    }


    /**
     * extracts trimmed substrings from str seperated by commas
     * <p/>
     * Example
     * str = "1,string2,  3   " would return an ArrayList of three strings {"1","string2","3}
     *
     * @param str
     * @return
     */
    private static ArrayList<String> inputStringToParsableArray(String str)
    {
        ArrayList<String> ret = new ArrayList<String>();

        int left = 0;
        int right = str.indexOf(',');
        if (right == -1)
            right = str.length();

        while (left < right)
        {
            ret.add(str.substring(left, right).trim());
            left = right + 1;
            right = str.indexOf(',', right + 1);
            if (right == -1)
                right = str.length();
        }
        return ret;
    }


    /**
     * File Format should be
     * <p/>
     * VERTICES: <NUM VERTICES>
     * <VERTEX ID>,<LATITUDE>,<LONGITUDE>
     * ...
     * EDGES: <NUM EDGES>
     * <VID SOURCE>,<VID TARGET>,<WEIGHT>[,<STREET NAME>]
     *
     * @param fileLoc
     * @return
     */
    public static MyGraph<Coordinate, Street> loadGraph(String fileLoc, boolean isDirected) throws FileNotFoundException,  Exception
    {
        MyGraph<Coordinate, Street> graph = new MyGraph<Coordinate, Street>(isDirected);

        //This is a mapping FROM the input file's vertex ids TO the graph generated ids
        ArrayList<Point> vIDMap = new ArrayList<Point>();


        Scanner scanner = new Scanner(new File(fileLoc));


        //EXTRACT VERTICES and save generated vertex ids
        String curLine = scanner.nextLine();
        int totVerts = 0;
        if (curLine.startsWith("VERTICES:"))
            totVerts = Integer.parseInt(curLine.substring(9).trim());
        else
            throw new Exception("Cannot find keyword VERTICES in the input file.");

        for (int i = 0; i < totVerts; i++)
        {
            ArrayList<String> items = inputStringToParsableArray(scanner.nextLine());

            int fileVid = Integer.parseInt(items.get(0));
            double latitude = Double.parseDouble(items.get(1));
            double longitude = Double.parseDouble(items.get(2));

            //ADD VERTEX and store mapping
            int graphVid = graph.addVertex(new Coordinate(latitude, longitude));
            vIDMap.add(new Point(fileVid, graphVid));
        }


        //SORT vIDMap by fileVID
        sortCoordinateMapping(vIDMap, true);


        //Test for unique fileVIDs
        int lastID = vIDMap.get(0).x;
        for (int i = 1; i < vIDMap.size(); i++)
        {
            int thisID = vIDMap.get(i).x;
            if (lastID==thisID)
                throw new  Exception("Input file contains duplicate vertex ID ("+ thisID+").");

            lastID=thisID;
        }

        //Extract Edges
        int totEdges = 0;
        curLine = scanner.nextLine();
        if (curLine.startsWith("EDGES:"))
            totEdges = Integer.parseInt(curLine.substring(6).trim());
        else
            throw new Exception("Cannot find keyword EDGES in the input file.");

        for (int i = 0; i < totEdges; i++)
        {
            ArrayList<String> items = inputStringToParsableArray(scanner.nextLine());

            int vidSrc = Integer.parseInt(items.get(0));
            int vidTarget = Integer.parseInt(items.get(1));
            double weight = Double.parseDouble(items.get(2));
            String streetName = "";
            if (items.size() > 3)
                streetName = items.get(3);

            graph.addEdge(getAssignedVID(vIDMap, vidSrc), getAssignedVID(vIDMap, vidTarget), new Street(streetName, weight));


        }


        return graph;
    }
}
