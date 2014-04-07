package com.jmt;

import java.util.ArrayList;
import java.util.Set;
import java.util.Stack;

/**
 * Created by jim on 4/2/14.
 */
public class MyGraph<V, E> implements Graph<V, E>
{

    //These are for DFS and BFS
    private ArrayList<EdgeProcessor<V, E>> edgeProcessors;
    private ArrayList<VertexProcessor<V, E>> vertexProcessors;


    private IdentifiableSet vertexList;
    private IdentifiableSet edgeList;


    public MyGraph()
    {

        vertexList = new IdentifiableSet();
        edgeList = new IdentifiableSet();

        edgeProcessors = new ArrayList<EdgeProcessor<V, E>>();
        vertexProcessors = new ArrayList<VertexProcessor<V, E>>();
    }


    //BIG O lg n
    private Vertex<V, E> getVertex(int vID) throws IllegalArgumentException
    {
        Vertex<V, E> v = (Vertex<V, E>) vertexList.findByUID(vID);


        if (v == null)
            throw new IllegalArgumentException("No such Vertex ID " + vID);

        return v;
    }


    //Big O lg n
    private Edge<V, E> getEdge(int eID) throws IllegalArgumentException
    {
        Edge<V, E> e = (Edge<V, E>) edgeList.findByUID(eID);

        if (e == null)
            throw new IllegalArgumentException("No such Edge ID " + eID);

        return e;
    }


    @Override
    public int addVertex(V data)
    {
        Vertex v = new Vertex(data, this);
        vertexList.add(v);
        return v.getUID();
    }


    //BIGO 2*lg(n) +BigO(IdentifiableSet.add)
    @Override
    public int addEdge(int srcID, int targetID, E attr) throws IllegalArgumentException
    {
        //getVertex() will throw IllegalArgumentException if srcID or targetID's are not found
        Vertex<V, E> src = getVertex(srcID);
        Vertex<V, E> target = getVertex(targetID);


        Edge<V, E> newEdge = new Edge(src, target, attr, this);
        int eID = newEdge.getUID();


        //TODO will SELF LOOPs be a problem???
        //if(srcID==targetID)
        //System.out.println(srcID);

        //TODO these return true/false but new edge should be unique
        if (!src.addEdgeOut(newEdge))
            throw new IllegalArgumentException("AlreadyExisits???");
        //TODO is it possible these vertices already have these edges registered
        if (!target.addEdgeIn(newEdge))
            throw new IllegalArgumentException("AlreadyExisits???");


        edgeList.add(newEdge);

        return newEdge.getUID();
    }


    //BIG O lg(n)
    public int getDegreeIn(int vID)
    {
        return getVertex(vID).getEdgeIDsIn().size();
    }

    //BIG O lg(n)
    public int getDegreeOut(int vID)
    {
        return getVertex(vID).getEdgeIDsOut().size();
    }


    @Override
    public Set<Integer> getVertices()
    {
        return vertexList.getIDList();
    }

    @Override
    public Set<Integer> getEdges()
    {
        return edgeList.getIDList();
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
        return getVertex(vID).getEdgeIDsAll();
    }


    public void addVertexProcessor(VertexProcessor<V, E> proc)
    {
        if (!vertexProcessors.contains(proc))
            vertexProcessors.add(proc);
    }

    public void addEdgeProcessor(EdgeProcessor<V, E> proc)
    {
        if (!edgeProcessors.contains(proc))
            edgeProcessors.add(proc);

    }

    private void processEarly(int vID)
    {
    }

    private void processEdge(int eID)
    {
    }

    private void processLate(int vID)
    {
    }


    public void doDFS(int startVID) throws IllegalArgumentException
    {
        Vertex<V, E> parent = null;
        Vertex<V, E> cur = getVertex(startVID);

        Stack<Vertex> stack = new Stack<Vertex>();

        stack.push(cur);
        while (!stack.isEmpty())
        {

            cur = stack.peek();
            if (cur.getState() == Vertex.UNDISCOVERED)
            {

                cur.setDFSParent(parent);
                cur.setDiscoverTime(discoverTime++);
                cur.setState(Vertex.DISCOVERED);//visit vertex
                processEarly(cur.getUID());
            }

            // Every node has a marker to check which children it has searched
            // we continue our search from where ever we left off
            //
            // we are searching for the next undiscovered child
            while (cur.getDfsCurChild() < cur.getEdgesOut().size() && cur.getEdgesOut().get(cur.getDfsCurChild()).getTarget().getState() != Vertex.DISCOVERED)
            {

                // we still want to process all edges. We must do that if
                // the child is not our parent
                // &&
                // we are not the child's parent
                // &&
                // (the child has not yet been proccessed OR we are directed graph)


                //                               TODO left of here
                //if( (cur.getDFSParent().getUID() != cur.getEdgesOut().get(cur.getDfsCurChild()).getUID()) && cur.getEdgesOut().get(cur.
                //if ((cur.parent != neighbors.get(cur.curChild) && neighbors.get(cur.curChild).parent != cur) && (neighbors.get(cur.curChild).state != Node.PROCESSED || isDirected))
                //  processEdge(cur, neighbors.get(cur.curChild));

                //cur.curChild++;

            }

            /*
            ArrayList<Node> neighbors = cur.neighbors;

            while (cur.curChild < cur.neighbors.size() && cur.neighbors.get(cur.curChild).state != Node.UNDISCOVERED)
            {

            }

            // if we found a non-parent, non-processed neighbor then lets
            // explore it
            if (cur.curChild < cur.neighbors.size())
            {

                processEdge(cur, neighbors.get(cur.curChild));
                if (neighbors.get(cur.curChild).state == Node.UNDISCOVERED)
                {
                    parent = cur;
                    stack.push(neighbors.get(cur.curChild));
                }
            }
            else
            // all neighbors have been visited and we are done
            {
                // Complete Visit
                cur.processTime = ++searchTime;
                cur.state = Node.PROCESSED;
                processLate(cur);

                stack.pop();
            }

             */

        }


        initializeSearch();
        //TODO
    }

    private int discoverTime;

    public void initializeSearch()
    {
        discoverTime = 1;

        for (Identifiable i : vertexList)
            ((Vertex<V, E>) i).initializeForSearch();
    }


}
