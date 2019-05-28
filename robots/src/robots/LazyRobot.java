package robots;

public class LazyRobot implements Robot {

  private double positionX;
  private double positionY;
  private double direction;

  public LazyRobot(double x, double y, double direction) {
    positionX = x;
    positionY = y;
    this.direction = direction;
  }

  @Override
  public double getPositionX() {
    return positionX;
  }

  @Override
  public double getY() {
    return positionY;
  }

  @Override
  public double getDirection() {
    return direction;
  }

  @Override
  public void move(double duration, int targetX, int targetY) {
    direction += duration * 0.1;
  }
}
