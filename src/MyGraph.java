import java.util.*;

/**
 * Created by jim on 4/2/14.
 */
public class MyGraph<V, E> implements Graph<V, E>
{

    private HashMap<Integer, Vertex<V, E>> vertexList;
    private HashMap<Integer, Edge<V, E>> edgeList;


    public MyGraph()
    {
        vertexList = new HashMap<Integer, Vertex<V, E>>();
        edgeList = new HashMap<Integer, Edge<V, E>>();
    }

    public Vertex<V, E> getVertex(int vID) throws IllegalArgumentException
    {
        Vertex<V,E> v = vertexList.get(vID);
        if(v==null)
            throw new IllegalArgumentException("No such vertex " + vID);
        return v;
    }

    private Edge<V, E> getEdge(int eID) throws IllegalArgumentException
    {
        Edge<V,E> e = edgeList.get(eID);
        if(e==null)
            throw new IllegalArgumentException("No such edge " + eID);

        return e;
    }

    @Override
    public int addVertex(V data)
    {
        Vertex<V, E> v = new Vertex<V, E>(data, this);
        vertexList.put(v.vID, v);
        return v.vID;
    }

    @Override
    public int addEdge(int srcID, int targetID, E attr) throws IllegalArgumentException
    {
        Vertex<V, E> src = vertexList.get(srcID);
        Vertex<V, E> target = vertexList.get(targetID);
        Edge<V, E> newEdge = new Edge(src, target, attr, this);

        src.neighbors.put(targetID, target);
        src.edges.put(newEdge.eID, newEdge);

        edgeList.put(newEdge.eID, newEdge);

        return newEdge.eID;
    }

    @Override
    public Set<Integer> getVertices()
    {
        return vertexList.keySet();
    }

    @Override
    public Set<Integer> getEdges()
    {
        return edgeList.keySet();
    }

    @Override
    public E getAttribute(int eID) throws IllegalArgumentException
    {
        return getEdge(eID).getAttr();
    }

    @Override
    public V getData(int vID) throws IllegalArgumentException
    {
        return getVertex(vID).getData();
    }

    @Override
    public int getSource(int eID) throws IllegalArgumentException
    {
        return getEdge(eID).getSourceID();
    }

    @Override
    public int getTarget(int eID) throws IllegalArgumentException
    {
        return getEdge(eID).getTargetID();
    }

    @Override
    public Set<Integer> getEdgesOf(int vID) throws IllegalArgumentException
    {
        return getVertex(vID).getEdges();
    }


}
