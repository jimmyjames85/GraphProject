package com.jmt;

/**
 * Created by jim on 4/8/14.
 */
public class MyWeighing implements Weighing<Street>
{
    Graph<Coordinate, Street> graph;


    public MyWeighing(Graph<Coordinate, Street> graph)
    {
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
