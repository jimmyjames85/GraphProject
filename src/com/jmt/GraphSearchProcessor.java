package com.jmt;

/**
 * Created by jim on 4/8/14.
 */
public interface GraphSearchProcessor<V,E>
{
    void processVertexEarly(MyGraph<V, E> graph, Vertex<V, E> vertex);

    void processEdge(MyGraph<V, E> graph, Vertex<V,E> from, Vertex<V, E> to);

    void processVertexLate(MyGraph<V, E> graph, Vertex<V, E> vertex);
}
