package com.jmt;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by jim on 4/14/14.
 */
public class GuiGraphDriver extends JFrame implements ActionListener
{
    private GuiGraph guiGraph;
    public GuiGraphDriver(Graph<Coordinate, Street> graph)
    {
        guiGraph =new GuiGraph(graph);

        this.add(guiGraph, BorderLayout.CENTER);
        JButton btnNext = new  JButton("Next");
        btnNext.addActionListener(this);
        btnNext.setActionCommand("Next");
        this.add(btnNext,BorderLayout.SOUTH);
        init();
    }


    public void addPath(List<Integer> path)
    {


        guiGraph.addPath(path);
    }

    private void init()
    {
        setSize(750, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }



    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        if(actionEvent.getActionCommand().equals("Next"))
        {


        }
    }
}
