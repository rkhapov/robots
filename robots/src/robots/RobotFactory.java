package robots;

import java.lang.reflect.InvocationTargetException;

public class RobotFactory {
  public Robot createRobot(Class<?> robotClass, int x, int y, double direction)
      throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
    if (Robot.class.isAssignableFrom(robotClass)) {
      return createDirectly(robotClass, x, y, direction);
    }

    return createWrapped(robotClass, x, y, direction);
  }

  private Robot createDirectly(Class<?> robotClass, int x, int y, double direction)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    var ctor = robotClass.getConstructor(double.class, double.class, double.class);

    return (Robot) ctor.newInstance((double) x, (double) y, direction);
  }

  private Robot createWrapped(Class<?> robotClass, int x, int y, double direction) throws NoSuchMethodException {
    if (!containsMethod(robotClass, "getPositionX")){
      throw new NoSuchMethodException("getPositionX not found");
    }

    if (!containsMethod(robotClass, "getY")){
      throw new NoSuchMethodException("getY not found");
    }

    if (!containsMethod(robotClass, "getDirection")){
      throw new NoSuchMethodException("getDirection not found");
    }

    if (!containsMethod(robotClass, "move", double.class, int.class, int.class)) {
      throw new NoSuchMethodException("move not found");
    }

    if (!containsCtor(robotClass, double.class, double.class, double.class)) {
      throw new NoSuchMethodException("ctor not found");
    }

    try {
      var ctor = robotClass.getConstructor(double.class, double.class, double.class);

      return new RobotWrapper(ctor.newInstance(x, y, direction));
    } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
      throw new NoSuchMethodException("Unsupportable robot class");
    }
  }

  private boolean containsMethod(Class<?> class_, String name, Class<?>... args) {
    try {
      class_.getMethod(name, args);

      return true;
    } catch (NoSuchMethodException e) {
      return false;
    }
  }

  private boolean containsCtor(Class<?> class_, Class<?>... args) {
    try {
      class_.getConstructor(args);

      return true;
    } catch (NoSuchMethodException e) {
      return false;
    }
  }


  class RobotWrapper implements Robot {

    private final Object realRobotInstance;

    private RobotWrapper(Object realRobotInstance) {
      this.realRobotInstance = realRobotInstance;
    }


    @Override
    public double getPositionX() {
      try {
        var method = realRobotInstance.getClass().getMethod("getPositionX");

        return (double) method.invoke(realRobotInstance);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        //there is no way this can happened, because given object is Robot instance
        return 0;
      }
    }

    @Override
    public double getY() {
      try {
        var method = realRobotInstance.getClass().getMethod("getY");

        return (double) method.invoke(realRobotInstance);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        //there is no way this can happened, because given object is Robot instance
        return 0;
      }
    }

    @Override
    public double getDirection() {
      try {
        var method = realRobotInstance.getClass().getMethod("getDirection");

        return (double) method.invoke(realRobotInstance);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        //there is no way this can happened, because given object is Robot instance
        return 0;
      }
    }

    @Override
    public void move(double duration, int targetX, int targetY) {
      try {
        var method = realRobotInstance.getClass().getMethod("move", double.class, int.class, int.class);

        method.invoke(realRobotInstance, duration, targetX, targetY);
      } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
        //there is no way this can happened, because given object is Robot instance
      }
    }
  }
}
