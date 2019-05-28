package robots;

public class RobotsHelper {
  public static double angleTo(double fromX, double fromY, double toX, double toY) {
    double diffX = toX - fromX;
    double diffY = toY - fromY;

    return asNormalizedRadians(Math.atan2(diffY, diffX));
  }

  public static double asNormalizedRadians(double angle) {
    while (angle < 0) {
      angle += 2 * Math.PI;
    }
    while (angle >= 2 * Math.PI) {
      angle -= 2 * Math.PI;
    }
    return angle;
  }
}
