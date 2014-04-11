package com.jmt;

/**
 * Created by jim on 4/8/14.
 */
public class MyWeighing implements Weighing<Street>
{
    MyGraph<Coordinate, Street> graph;
    boolean isDirected;

    public MyWeighing(MyGraph<Coordinate, Street> graph, boolean isDirected)
    {
        this.isDirected = isDirected;
        this.graph = graph;
    }

    /**
     * Computes the weight of the edge
     *
     * @param edge
     */
    @Override
    public double weight(Street edge)
    {
        return edge.getWeight();
    }
}
