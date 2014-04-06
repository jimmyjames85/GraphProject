package com.jmt;

/**
 * Created by jim on 4/6/14.
 */
public interface EdgeProcessor<V,E>
{
    public void processEdge(MyGraph<V, E> graph, int eID);
}
