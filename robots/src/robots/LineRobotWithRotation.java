package robots;

public class LineRobotWithRotation implements Robot {

  private double x;
  private double y;
  private double direction;
  private double velocity = 0.1;

  public LineRobotWithRotation(double x, double y, double direction) {
    this.x = x;
    this.y = y;
    this.direction = direction;
  }

  @Override
  public double getPositionX() {
    return x;
  }

  @Override
  public double getY() {
    return y;
  }

  @Override
  public double getDirection() {
    return direction;
  }

  @Override
  public void move(double duration, int targetX, int targetY) {
    var diffX = targetX - x;
    var diffY = targetY - y;
    var length = Math.sqrt(diffX * diffX + diffY * diffY);
    var ortX = diffX / length;
    var ortY = diffY / length;
    var dx = ortX * velocity * duration;
    var dy = ortY * velocity * duration;

    if (length < 1) {
      return;
    }

    var angleToTarget = Math.atan2(targetY - y, targetX - x);

    if (Math.abs(RobotsHelper.asNormalizedRadians(angleToTarget - direction)) > 1e-1) {
      direction += duration * 0.001;
      return;
    }

    x += dx;
    y += dy;
  }
}
