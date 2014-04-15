/**
 * Created by jtappe on 4/3/2014.
 */
public class Edge<V, E>
{
    private static int nextVertexID = 1;


    protected final int eID;
    private E attr;

    private MyGraph<V, E> parentGraph;

    private int srcID;
    private int targetID;

    protected  Vertex<V, E> source;
    protected Vertex<V, E> target;



    protected Edge(Vertex<V, E> src, Vertex<V, E> target, E attr, MyGraph<V, E> parentGraph)
    {
        this.parentGraph = parentGraph;
        eID = nextVertexID++;

        this.source = src;
        this.target = target;

        this.srcID = src.vID;
        this.targetID = target.vID;

        this.attr = attr;
    }


    public int getEID()
    {
        return eID;
    }

    public int getSourceID()
    {
        return srcID;
    }

    public int getTargetID()
    {
        return targetID;
    }

    public E getAttr()
    {
        return attr;
    }
}
