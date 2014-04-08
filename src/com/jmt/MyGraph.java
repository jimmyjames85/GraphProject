package com.jmt;

import java.util.ArrayList;
import java.util.Iterator;
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


    private int searchTime;


    public MyGraph()
    {

        vertexList = new IdentifiableSet();
        edgeList = new IdentifiableSet();

        edgeProcessors = new ArrayList<EdgeProcessor<V, E>>();
        vertexProcessors = new ArrayList<VertexProcessor<V, E>>();
    }


    //BIG O lg n
    //TODO make private???
    public Vertex<V, E> getVertex(int vID) throws IllegalArgumentException
    {
        return (Vertex<V,E>)vertexList.getIdentifiableByID(vID);
    }




    //Big O lg n
    private Edge<V, E> getEdge(int eID) throws IllegalArgumentException
    {
        return (Edge<V,E>)vertexList.getIdentifiableByID(eID);
    }


    @Override
    public int addVertex(V data)
    {
        Vertex<V,E> v = new Vertex<V, E>(data,this);
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

    private int sTabs = 0;
    private String tabs(int t)
    {
        if (t < 0)
            return "";

        String tab = "";
        for (int i = 0; i < t; i++)
            tab += "\t";
        return tab;
    }

    private void processVertexEarly(Vertex<V, E> v)
    {
        System.out.println(tabs(sTabs++) + "early: " + v.getUID());

    }

    private void processEdge(Edge<V, E> edge)
    {
        System.out.println(tabs(sTabs) + " edge: " + edge.getSource().getUID() + " -> " + edge.getTarget().getUID());
    }

    private void processVertexLate(Vertex<V, E> v)
    {
        System.out.println(tabs(--sTabs) + " late: " + v.getUID());
    }


    public void doExhaustiveDFS()
    {
        Iterator<Identifiable> itr = vertexList.iterator();
        while(itr.hasNext())
        {
            Vertex<V,E> v = (Vertex<V,E>)itr.next();
            if(v.dfsState==Vertex.UNDISCOVERED)
                doDFS((v.getUID()));
        }
    }

    public void doDFS(int startVID) throws IllegalArgumentException
    {
        int lastDiscovererVID = -1;//null
        Vertex<V, E> cur = getVertex(startVID);

        Stack<Vertex> stack = new Stack<Vertex>();

        stack.push(cur);
        while (!stack.isEmpty())
        {
            cur = stack.peek();
            if (cur.dfsState == Vertex.UNDISCOVERED)
            {
                cur.dfsDiscovererVID = lastDiscovererVID;
                cur.dfsDiscoverTime = searchTime++;
                cur.dfsState = Vertex.DISCOVERED;//visit vertex
                processVertexEarly(cur);
            }

            // Every node has a marker to check which children it has searched
            // we continue our search from where ever we left off
            // we are searching for the next undiscovered child
            ArrayList<Edge<V, E>> neighborsOut = cur.getEdgesOut();


            while (cur.dfsCurChild < neighborsOut.size() && neighborsOut.get(cur.dfsCurChild).getTarget().dfsState != Vertex.UNDISCOVERED)
            {
                // we still want to process all edges. We must do that if
                // the cur child is not our parent
                // &&
                // we are not the child's parent
                // &&
                // (the child has not yet been proccessed OR we are directed graph)

                int curChildVID = neighborsOut.get(cur.dfsCurChild).getTargetID();


                boolean theCurChildDiscoveredUs = curChildVID == cur.dfsDiscovererVID;
                boolean weDiscoveredTheCurChild = cur.getUID() == neighborsOut.get(cur.dfsCurChild).getTarget().dfsDiscovererVID;
                //boolean childHasBeenProcessed = neighborsOut.get(cur.dfsCurChild).getTarget().getDfsState() == Vertex.PROCESSED;

                if (!theCurChildDiscoveredUs && !weDiscoveredTheCurChild)// && !childHasBeenProcessed)<----DIRECTED GRAPH
                    processEdge(neighborsOut.get(cur.dfsCurChild));

                cur.dfsCurChild++;
            }

            // if we found a non-parent, non-processed neighbor then lets
            // explore it
            if (cur.dfsCurChild < neighborsOut.size())
            {
                processEdge(neighborsOut.get(cur.dfsCurChild));
                if (neighborsOut.get(cur.dfsCurChild).getTarget().dfsState == Vertex.UNDISCOVERED)
                {
                    lastDiscovererVID = cur.getUID();
                    stack.push(neighborsOut.get(cur.dfsCurChild).getTarget());//EXPLORE
                }
            }
            else
            // all neighbors have been visited and we are done
            {
                // Complete Visit
                cur.dfsProcessTime = searchTime++;
                cur.dfsState = Vertex.PROCESSED;
                processVertexLate(cur);
                stack.pop();
            }
        }


    }


    public void initializeSearch()
    {
        searchTime = 1;

        for (Identifiable i : vertexList)
            ((Vertex<V, E>) i).initializeDFSSearch();
    }


}
