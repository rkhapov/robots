package robots;

public class RobotRunner extends Thread {

  private final String id;

  private final Robot robot;

  private final long dt;

  private int targetX;
  private int targetY;

  private boolean stopped = false;
  private boolean paused = false;

  public RobotRunner(Robot robot, long dt) {
    this.dt = dt;
    this.robot = robot;
    id = java.util.UUID.randomUUID().toString();
  }

  public void stopRunning() {
    stopped = true;
  }

  public void pauseRunning() {
    paused = true;
  }

  public boolean isPaused() {
    return paused;
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

        if (!paused) {
          robot.move(dt, targetX, targetY);
        }
      } catch (InterruptedException e) {
        //do literally nothing
      }
    }
    System.out.println(String.format("Shutting down %s", id));
  }

  @Override
  public void start() {
    System.out.println(String.format("%s %s", isAlive() ? "Resumed" : "Start", id));

    if (paused)
      paused = false;

    if (!isAlive())
      super.start();
  }
}
