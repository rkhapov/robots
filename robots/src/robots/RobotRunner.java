package robots;

public class RobotRunner extends Thread {

  private final Robot robot;

  private final long dt;

  private int targetX;
  private int targetY;

  private boolean stopped = false;

  public RobotRunner(Robot robot, long dt) {
    this.dt = dt;
    this.robot = robot;
  }

  public void stopRunning() {
    stopped = true;
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

  @Override
  public void run() {
    while (!stopped) {
      try {
        Thread.sleep(dt);

        robot.move(dt, targetX, targetY);
      } catch (InterruptedException e) {
        //do literally nothing
      }
    }
  }
}
