package com.jmt;

/**
 * Created by jim on 4/8/14.
 */
public interface GraphSearchProcessor<V,E>
{
    public void processVertexEarly(Graph<V, E> graph, int vID);

    public void processEdge(Graph<V, E> graph, int eID, int vIDfrom, int vIDto);

    public void processVertexLate(Graph<V, E> graph, int vID);

}
