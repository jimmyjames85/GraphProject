import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by jim on 4/12/14.
 */
public class GuiGraph extends JComponent
{

    private Graph<Coordinate, Street> graph;
    private double graphMinX, graphMinY, graphMaxX, graphMaxY;
    private double graphWidth, graphHeight;


    private ArrayList<Integer> drawEdgeList;

    private ArrayList<List<Integer>> drawPathList;


    public GuiGraph(Graph<Coordinate, Street> graph)
    {
        this.graph = graph;
        drawEdgeList = new ArrayList<Integer>();
        drawPathList = new ArrayList<List<Integer>>();

        calculateMinMaxCoords();
    }


    public void addPath(java.util.List<Integer> path)
    {
        drawPathList.add(path);
    }

    public void addDrawEdge(int EID)
    {
        drawEdgeList.add(EID);
    }

    private void calculateMinMaxCoords()
    {

        Set<Integer> verts = graph.getVertices();
        Iterator<Integer> vertItr = verts.iterator();


        Coordinate coord = graph.getData(vertItr.next());
        double minX, maxX, minY, maxY;
        minX = maxX = coord.longitude;
        minY = maxY = coord.latitude;


        while (vertItr.hasNext())
        {
            int vID = vertItr.next();
            coord = graph.getData(vID);


            if (coord.longitude < minX)
                minX = coord.longitude;
            if (coord.longitude > maxX)
                maxX = coord.longitude;
            if (coord.latitude < minY)
                minY = coord.latitude;
            else if (coord.latitude > maxY)
                maxY = coord.latitude;
        }

        this.graphMaxX = maxX;
        this.graphMaxY = maxY;
        this.graphMinX = minX;
        this.graphMinY = minY;
        this.graphWidth = graphMaxX - graphMinX;
        this.graphHeight = graphMaxY - graphMinY;

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

            if (drawEdgeList.contains(edgeID))
                g.setColor(Color.RED);

            drawEdge(coordA, coordB, g, width, height);
        }


        g.setColor(Color.BLUE);


        Iterator<List<Integer>> pathItr = drawPathList.iterator();
        while (pathItr.hasNext())
        {
            Iterator<Integer> vertItr = pathItr.next().iterator();

            Coordinate a = null;
            Coordinate b = null;
            if (vertItr.hasNext())
                a = graph.getData(vertItr.next());


            while (vertItr.hasNext())
            {
                if (b == null)
                    g.setColor(Color.GREEN);
                else
                    g.setColor(Color.BLUE);


                b = graph.getData(vertItr.next());

                if(!vertItr.hasNext())
                    g.setColor(Color.RED);

                drawEdge(a, b, g, width, height);
                a = b;
            }
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
        double graphX = coord.getLongitude();
        double graphY = coord.getLatitude();

        int guiX = (int) (guiWidth * ((graphX - graphMinX) / graphWidth));
        int guiY = guiHeight - (int) (guiHeight * ((graphY - graphMinY) / graphHeight));
        g.drawLine(guiX, guiY, guiX, guiY);
    }

    private void drawEdge(Coordinate coordA, Coordinate coordB, Graphics g, int guiWidth, int guiHeight)
    {
        double graphAX = coordA.getLongitude();
        double graphAY = coordA.getLatitude();
        double graphBX = coordB.getLongitude();
        double graphBY = coordB.getLatitude();


        int guiAX = (int) (guiWidth * ((graphAX - graphMinX) / graphWidth));
        int guiAY = guiHeight - (int) (guiHeight * ((graphAY - graphMinY) / graphHeight));
        int guiBX = (int) (guiWidth * ((graphBX - graphMinX) / graphWidth));
        int guiBY = guiHeight - (int) (guiHeight * ((graphBY - graphMinY) / graphHeight));

        g.drawLine(guiAX, guiAY, guiBX, guiBY);
    }
}
