package com.jmt.old;

/**
 * Created by jim on 4/6/14.
 */
public interface NodeProcessor<T extends Comparable<? super T>>
{
    void processEarly(GraphAttempt.Node v);

    void processEdge(GraphAttempt.Node from, GraphAttempt.Node to);

    void processLate(GraphAttempt.Node v);
}
