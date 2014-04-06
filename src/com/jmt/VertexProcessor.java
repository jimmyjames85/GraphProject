package com.jmt;

import com.jmt.MyGraph;

/**
 * Created by jim on 4/6/14.
 */
public interface VertexProcessor<V, E>
{
    public void processVertexEarly(MyGraph<V, E> graph, int vID);

    public void processVertexLate(MyGraph<V, E> graph, int vID);
}
