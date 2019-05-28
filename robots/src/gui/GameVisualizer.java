package gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JPanel;
import robots.RobotRunner;

public class GameVisualizer extends JPanel {

  private final Timer timer = initTimer();

  private static Timer initTimer() {
    return new Timer("events generator", true);
  }

  private volatile int targetPositionX = 150;
  private volatile int targetPositionY = 100;

  private final Map<RobotRunner, Color> runnerToColor;

  public GameVisualizer() {
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        onRedrawEvent();
      }
    }, 0, 50);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        setTargetPosition(e.getPoint());
        repaint();
      }
    });
    setDoubleBuffered(true);

    runnerToColor = new HashMap<>();
  }

  public void addRunner(RobotRunner robotRunner) {

    for (var runner : runnerToColor.keySet()) {
      runner.pauseRunning();
    }

    runnerToColor.put(robotRunner, new Color((int)(Math.random() * 0x1000000)));
    
    robotRunner.setTargetX(targetPositionX);
    robotRunner.setTargetY(targetPositionY);
  }

  public void startRunners() {
    for (var robotRunner : runnerToColor.keySet()) {
      if (robotRunner.isAlive() && !robotRunner.isPaused()) {
        return;
      }

      robotRunner.start();
    }
  }

  private void setTargetPosition(Point p) {
    targetPositionX = p.x;
    targetPositionY = p.y;

    for (var runner : runnerToColor.keySet()) {
      runner.setTargetX(targetPositionX);
      runner.setTargetY(targetPositionY);
    }
  }

  private void onRedrawEvent() {
    EventQueue.invokeLater(this::repaint);
  }

  private static int round(double value) {
    return (int) (value + 0.5);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    var g2d = (Graphics2D) g;

    for (var robotRunner : runnerToColor.keySet()) {
      drawRobot(g2d,
          round(robotRunner.getRobot().getPositionX()),
          round(robotRunner.getRobot().getY()),
          robotRunner.getRobot().getDirection(),
          runnerToColor.get(robotRunner));
    }

    drawTarget(g2d, targetPositionX, targetPositionY);
  }

  private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
    g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
  }

  private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
    g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
  }

  private void drawRobot(Graphics2D g, int centerX, int centerY, double direction, Color robotColor) {
    AffineTransform t = AffineTransform.getRotateInstance(direction, centerX, centerY);
    g.setTransform(t);
    g.setColor(robotColor);
    fillOval(g, centerX, centerY, 30, 10);
    g.setColor(Color.BLACK);
    drawOval(g, centerX, centerY, 30, 10);
    g.setColor(Color.WHITE);
    fillOval(g, centerX + 10, centerY, 5, 5);
    g.setColor(Color.BLACK);
    drawOval(g, centerX + 10, centerY, 5, 5);
  }

  private void drawTarget(Graphics2D g, int x, int y) {
    AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
    g.setTransform(t);
    g.setColor(Color.GREEN);
    fillOval(g, x, y, 5, 5);
    g.setColor(Color.BLACK);
    drawOval(g, x, y, 5, 5);
  }
}
