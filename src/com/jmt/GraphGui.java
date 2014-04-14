package com.jmt;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by jim on 4/12/14.
 */
public class GraphGui extends JFrame
{

    private Graph<Coordinate, Street> graph;
    private double graphMinX, graphMinY, graphMaxX, graphMaxY;
    private double graphWidth, graphHeight;


    public GraphGui(Graph<Coordinate, Street> graph)
    {
        this.graph = graph;

        initGui();
        calculateMinMaxCoords();


    }


    private void calculateMinMaxCoords()
    {

        Set<Integer> verts = graph.getVertices();
        Iterator<Integer> vertItr = verts.iterator();


        Coordinate coord = graph.getData(vertItr.next());
        double minX, maxX, minY, maxY;
        minX = maxX = coord.latitude;
        minY = maxY = coord.longitude;


        while (vertItr.hasNext())
        {
            int vID = vertItr.next();
            coord = graph.getData(vID);


            if (coord.latitude < minX)
                minX = coord.latitude;
            if (coord.latitude > maxX)
                maxX = coord.latitude;
            if (coord.longitude < minY)
                minY = coord.longitude;
            else if (coord.longitude > maxY)
                maxY = coord.longitude;
        }

        this.graphMaxX = maxX;
        this.graphMaxY = maxY;
        this.graphMinX = minX;
        this.graphMinY = minY;
        this.graphWidth = graphMaxX - graphMinX;
        this.graphHeight = graphMaxY - graphMinY;

    }


    private void initGui()

    {
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }


    @Override
    public void paint(Graphics g)
    {
        g.getClipBounds();
        int width = (int) g.getClipBounds().getWidth();
        int height = (int) g.getClipBounds().getHeight();


        g.setColor(Color.white);
        g.fillRect(0, 0, width, height);


        Iterator<Integer> edgeItr = graph.getEdges().iterator();
        while (edgeItr.hasNext())
        {
            int edgeID = edgeItr.next();
            Coordinate coordA = graph.getData(graph.getSource(edgeID));
            Coordinate coordB = graph.getData(graph.getTarget(edgeID));

            g.setColor(Color.lightGray);
            String street =graph.getAttribute(edgeID).getStreetName();

            if(street.contains("Mortensen"))
            {
                g.setColor(Color.RED);
                System.out.println(coordA + " " + coordB);
            }
            else if  (street.length()>2)
                g.setColor(Color.BLUE);


            drawEdge(coordA, coordB, g, width, height);
        }

        g.setColor(Color.black);
        Iterator<Integer> vertItr = graph.getVertices().iterator();
        while (vertItr.hasNext())
        {
            Coordinate curCoord = graph.getData(vertItr.next());
            drawCoordinate(curCoord, g, width, height);
        }
    }

    private void drawCoordinate(Coordinate coord, Graphics g, int guiWidth, int guiHeight)
    {
        double graphX = coord.getLatitude();
        double graphY = coord.getLongitude();

        int guiX = (int) (guiWidth * ((graphX - graphMinX) / graphWidth));
        int guiY = (int) (guiHeight * ((graphY - graphMinY) / graphHeight));
        g.drawLine(guiX, guiY, guiX, guiY);
    }


    private void drawEdge(Coordinate coordA, Coordinate coordB, Graphics g, int guiWidth, int guiHeight)
    {

        double graphAX = coordA.getLatitude();
        double graphAY = coordA.getLongitude();
        double graphBX = coordB.getLatitude();
        double graphBY = coordB.getLongitude();


        int guiAX = (int) (guiWidth * ((graphAX - graphMinX) / graphWidth));
        int guiAY = (int) (guiHeight * ((graphAY - graphMinY) / graphHeight));
        int guiBX = (int) (guiWidth * ((graphBX - graphMinX) / graphWidth));
        int guiBY = (int) (guiHeight * ((graphBY - graphMinY) / graphHeight));

        g.drawLine(guiAX, guiAY, guiBX, guiBY);
    }
}
