package com.jmt.old;

import com.jmt.CoffeeSolver;
import com.jmt.Graph;
import com.jmt.Weighing;

import java.util.Collection;
import java.util.List;

/**
 * Created by jim on 4/9/14.
 */
public class MyCoffeeSolver<V, E> implements CoffeeSolver<V, E>
{
    /**
     * Sorts the vertices of the graph in a topological fashion.
     *
     * @param graph An arbitrary directed graph
     * @return An ordered topological list of vertex IDs of the graph or NULL if
     * a topological sort does not exist.
     */
    @Override
    public List<Integer> sortVertices(Graph<V, E> graph)
    {
        return null;
    }

    /**
     * Computes the shortest path that visits all the locations in
     * the graph in the order specified.
     *
     * @param graph     An arbitrary directed graph
     * @param locations An ordered list of vertex ID locations to be visited.
     * @param weigh     A object to determine the weight of each edge in the graph
     * @return An ordered list of vertex IDs representing the shortest path
     * that visits all the locations in the given order
     */
    @Override
    public List<Integer> shortestPath(Graph<V, E> graph, List<Integer> locations, Weighing<E> weigh)
    {
        return null;
    }

    /**
     * Computes all possible topological sorts of the graph.
     *
     * @param graph An arbitrary directed graph
     * @return A collection of all possible topological sorts of the graph.
     * Note that if a topological sort does not exist, it returns an
     * empty collection.
     */
    @Override
    public Collection<List<Integer>> generateValidSortS(Graph<V, E> graph)
    {
        return null;
    }
}
