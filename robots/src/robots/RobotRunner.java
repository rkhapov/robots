package robots;

import java.util.Timer;
import java.util.TimerTask;

public class RobotRunner {

  private final int period = 10;

  private final Timer timer;
  private final Robot robot;

  private int targetX;
  private int targetY;

  public RobotRunner(Robot robot) {
    timer = new Timer(java.util.UUID.randomUUID().toString(), true);
    this.robot = robot;

    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        onUpdate();
      }
    }, 0, period);
  }

  private void onUpdate() {
    robot.move(period, targetX, targetY);
  }

  public void setTargetX(int x) {
    targetX = x;
  }

  public void setTargetY(int y) {
    targetY = y;
  }

  public Robot getRobot() {
    return robot;
  }
}
