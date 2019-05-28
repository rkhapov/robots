package robots;

public class LineRobot implements Robot {

  private double x;
  private double y;
  private double direction;
  private double velocity = 0.1;

  public LineRobot(double x, double y, double direction) {
    this.x = x;
    this.y = y;
    this.direction = direction;
  }

  @Override
  public double getPositionX() {
    return x;
  }

  @Override
  public double getPositionY() {
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

    if (length < 1)
      return;

    var ortX = diffX / length;
    var ortY = diffY / length;
    var dx = ortX * velocity * duration;
    var dy = ortY * velocity * duration;

    x += dx;
    y += dy;
  }
}


