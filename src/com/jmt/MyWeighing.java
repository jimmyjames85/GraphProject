package com.jmt;

/**
 * Created by jim on 4/8/14.
 */
public class MyWeighing extends MyGraph<Coordinate, Street> implements Weighing<Street>
{
    //TODO understand this!!!

    public MyWeighing(boolean isDirected)
    {
        super(isDirected);
    }

    @Override
    public double weight(Street edge)
    {
        return edge.getWeight();
    }
}
