/**
 * Created by jim on 4/6/14.
 */
public class Street
{
    private String name;
    private double weight;


    public Street(double weight)
    {
        this.weight = weight;
        name = "";
    }

    public Street(String name, double weight)
    {
        this.name = name;
        if (weight < 0)
            weight = -weight;

        this.weight = weight;
    }

    public String toString()
    {
        return "(" + weight + ")" + name;
    }


    public String getStreetName()
    {
        return name;
    }

    public double getWeight()
    {
        return weight;
    }

}
