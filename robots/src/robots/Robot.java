package robots;

public interface Robot {
  double getPositionX();
  double getY();
  double getDirection();

  void move(double duration, int targetX, int targetY);
}
