package gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import robots.RobotRunner;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer visualizer;

    public GameWindow() 
    {
        super("Игровое поле", true, true, true, true);
        visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public void addRobot(RobotRunner robotRunner) {
        visualizer.addRunner(robotRunner);
    }

    public void startRobot() {
        visualizer.startRunners();
    }
}
