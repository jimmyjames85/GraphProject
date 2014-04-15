import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
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


    public static MyGraph<Character, String> produceGraph_HW_5_2()
    {
        MyGraph<Character, String> graph = new MyGraph<Character, String>();

        int[] vid = new int[10];
        for (int i = 0; i < vid.length; i++)
            vid[i] = graph.addVertex((char) ('A' + i));


        // A B C D E F G H I J
        // 0 1 2 3 4 5 6 7 8 9
        graph.addEdge(vid[0], vid[1], "A to B");
        graph.addEdge(vid[0], vid[3], "A to D");

        graph.addEdge(vid[1], vid[2], "B to C");
        graph.addEdge(vid[1], vid[3], "B to D");
        graph.addEdge(vid[1], vid[4], "B to E");

        graph.addEdge(vid[2], vid[5], "C to F");

        graph.addEdge(vid[3], vid[6], "D to G");
        graph.addEdge(vid[3], vid[4], "D to E");

        graph.addEdge(vid[4], vid[2], "E to C");
        graph.addEdge(vid[4], vid[5], "E to F");
        graph.addEdge(vid[4], vid[6], "E to G");

        graph.addEdge(vid[6], vid[5], "G to F");
        graph.addEdge(vid[6], vid[8], "G to I");

        graph.addEdge(vid[7], vid[5], "H to F");
        graph.addEdge(vid[7], vid[9], "H to J");

        graph.addEdge(vid[8], vid[9], "I to J");

        return graph;
    }

    public static MyGraph<Integer, String> produceIngredientOrder()
    {
        MyGraph<Integer, String> graph = new MyGraph<Integer, String>();



        //                     A    B     C     D     E     F     G    S
        int[] ingredients = {1055, 371, 2874, 2351, 2956, 1171, 1208, 2893};
        int[] vid = new int[ingredients.length];

        for (int i = 0; i < vid.length; i++)
            vid[i] = graph.addVertex(ingredients[i]);


        //Ingredient A must be picked up before ingredients C and F
        graph.addEdge(vid[0], vid[2], "A before C");//
        graph.addEdge(vid[0], vid[5], "A before F");//

        //Ingredient B must be picked up before ingredients C and D
        graph.addEdge(vid[1], vid[2], "B before C");//
        graph.addEdge(vid[1], vid[3], "B before D");//

        //Ingredient C must be picked up before ingredients D and E
        graph.addEdge(vid[2], vid[3], "C before D");
        graph.addEdge(vid[2], vid[4], "C before E");

        //Ingredient F must be picked up before ingredients C and E
        graph.addEdge(vid[5], vid[2], "F before C");
        graph.addEdge(vid[5], vid[4], "F before E");

        //Ingredient G must be the last ingredient picked up
        graph.addEdge(vid[0], vid[6], "A before G");//
        graph.addEdge(vid[1], vid[6], "B before G");//
        graph.addEdge(vid[2], vid[6], "C before G");//
        graph.addEdge(vid[3], vid[6], "D before G");//
        graph.addEdge(vid[4], vid[6], "E before G");//
        graph.addEdge(vid[5], vid[6], "F before G");//


        //S is the starting point
        graph.addEdge(vid[7], vid[0], "A before G");//
        graph.addEdge(vid[7], vid[1], "B before G");//
        graph.addEdge(vid[7], vid[2], "C before G");//
        graph.addEdge(vid[7], vid[3], "D before G");//
        graph.addEdge(vid[7], vid[4], "E before G");//
        graph.addEdge(vid[7], vid[5], "F before G");//
        graph.addEdge(vid[7], vid[6], "F before G");//


        return graph;
    }


    public static MyGraph<Character, String> produceTopSortTest()
    {
        MyGraph<Character, String> graph = new MyGraph<Character, String>();


        int[] vid = new int[8];


        for (int i = 0; i < vid.length; i++)
            vid[i] = graph.addVertex((char) ('A' + i));


        graph.addEdge(vid[0], vid[5], "A before F");//


        graph.addEdge(vid[1], vid[3], "B before D");//


        graph.addEdge(vid[2], vid[3], "C before D");
        graph.addEdge(vid[2], vid[4], "C before E");


        graph.addEdge(vid[5], vid[4], "F before E");


        graph.addEdge(vid[6], vid[0], "G before A");//
        graph.addEdge(vid[6], vid[1], "G before B");//
        graph.addEdge(vid[6], vid[2], "G before B");//
        graph.addEdge(vid[6], vid[3], "G before D");//
        graph.addEdge(vid[6], vid[4], "G before E");//
        graph.addEdge(vid[6], vid[5], "G before F");//
        graph.addEdge(vid[6], vid[7], "G before H");//


        return graph;
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
     * @param fileToGraphVIDMap uses the file's VID's as a key and stores the created vID's as the object
     * @return
     */
    public static MyGraph<Coordinate, Street> loadGraph(String fileLoc, boolean isDirected, HashMap<Integer,Integer> fileToGraphVIDMap) throws FileNotFoundException, Exception
    {


        fileToGraphVIDMap.clear();

        MyGraph<Coordinate, Street> graph = new MyGraph<Coordinate, Street>();
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



            if(fileToGraphVIDMap.get(fileVid)!=null)
                throw new Exception("Input file contains duplicate vertex ID (" + fileVid + ").");

            //ADD VERTEX and store mapping
            int graphVid = graph.addVertex(new Coordinate(latitude, longitude));
            fileToGraphVIDMap.put(fileVid, graphVid);
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


            graph.addEdge(fileToGraphVIDMap.get(vidSrc), fileToGraphVIDMap.get(vidTarget), new Street(streetName, weight));
            if (!isDirected)
                graph.addEdge(fileToGraphVIDMap.get(vidTarget) , fileToGraphVIDMap.get(vidSrc),  new Street(streetName, weight));
        }


        return graph;
    }
}
