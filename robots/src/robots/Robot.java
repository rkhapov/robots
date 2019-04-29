package robots;

public interface Robot {
  double getPositionX();
  double getPositionY();
  double getDirection();

  void move(double duration, int targetX, int targetY);
}
