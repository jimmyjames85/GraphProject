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



}
