import robots.Robot;

public class StandardRobot implements Robot {

  private static final double maxVelocity = 0.1;
  private static final double maxAngularVelocity = 0.001;

  private double x;
  private double y;
  private double direction;

  private double velocity;

  public StandardRobot(double x, double y, double direction) {
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
    var distance = distance(targetX, targetY, x, y);

    if (distance < 0.5)
      return; //no update needed because we have reached the target

    update(duration, targetX, targetY, distance);
  }

  private void update(double duration, int targetX, int targetY, double distance) {
    velocity = maxVelocity;

    var angularVelocity = getAngularVelocity(targetX, targetY, distance);
    var newX = y + maxVelocity / angularVelocity *
        (Math.sin(direction  + angularVelocity * duration) -
            Math.sin(direction));

    if (!Double.isFinite(newX))
    {
      newX = y + velocity * duration * Math.cos(direction);
    }
    var newY = y - velocity / angularVelocity *
        (Math.cos(direction  + angularVelocity * duration) -
            Math.cos(direction));

    if (!Double.isFinite(newY))
    {
      newY = y + velocity * duration * Math.sin(direction);
    }

    x = newX;
    y = newY;

    direction = asNormalizedRadians(direction + angularVelocity * duration);
  }

  private double getAngularVelocity(int targetX, int targetY, double distance){

    var angleToTarget = angleTo(x, y, targetX, targetY);
    var angularVelocity = 0.0;
    var angleFromTargetToRobot = asNormalizedRadians(angleToTarget - direction);

    if (angleFromTargetToRobot > Math.PI)
    {
      angularVelocity = -maxAngularVelocity;
    }
    else if (angleFromTargetToRobot < Math.PI)
    {
      angularVelocity = maxAngularVelocity;
    }
    if (Math.abs(angleFromTargetToRobot) <= 0.1)
      velocity = maxVelocity;
    else
      velocity = angularVelocity * distance/2;
    return applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
  }

  private static double applyLimits(double value, double min, double max)
  {
    if (value < min)
      return min;
    if (value > max)
      return max;
    return value;
  }

  private static double angleTo(double fromX, double fromY, double toX, double toY)
  {
    double diffX = toX - fromX;
    double diffY = toY - fromY;

    return asNormalizedRadians(Math.atan2(diffY, diffX));
  }

  public static double asNormalizedRadians(double angle)
  {
    while (angle < 0)
    {
      angle += 2*Math.PI;
    }
    while (angle >= 2*Math.PI)
    {
      angle -= 2*Math.PI;
    }
    return angle;
  }

  private static double distance(double x1, double y1, double x2, double y2)
  {
    double diffX = x1 - x2;
    double diffY = y1 - y2;
    return Math.sqrt(diffX * diffX + diffY * diffY);
  }
}
