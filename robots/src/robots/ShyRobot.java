package robots;

public class ShyRobot implements Robot {

  private double x;
  private double y;
  private double direction;
  private State current;
  private double velocity = 0.1;

  public ShyRobot(double positionX, double positionY, double direction) {
    this.x = positionX;
    this.y = positionY;
    this.direction = direction;
    this.current = State.FromTarget;
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

    if (current.equals(State.RotationToTarget)) {
      var angleToTarget = Math.atan2(targetY - y, targetX - x);

      if (Math.abs(RobotsHelper.asNormalizedRadians(angleToTarget - direction)) > 1e-1) {
        direction += duration * 0.001;
      }
      else{
        current = State.ToTarget;
      }
    } else if (current.equals(State.InstantRotationFromTarget)) {
      direction = direction + Math.random() * Math.PI + Math.PI / 2;
      current = State.FromTarget;
    } else {
      var ortX = Math.cos(direction);
      var ortY = Math.sin(direction);
      var dx = ortX * velocity * duration;
      var dy = ortY * velocity * duration;

      x += dx;
      y += dy;

      if (length <= 50 && current != State.FromTarget) {
        current = State.InstantRotationFromTarget;
      } else if (length >= 500 && current != State.ToTarget) {
        current = State.RotationToTarget;
      }
    }
  }
}

