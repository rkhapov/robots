package log;

public final class Logger implements ILogger {

  private final ILogChangeNotifier notifier;
  private final LogQueue queue;

  public Logger(int queueLength) {
    this.notifier = new LogChangeNotifier();
    queue = new LogQueue(queueLength);
  }

  public Logger(ILogChangeNotifier notifier, int queueLength) {
    this.notifier = notifier;
    queue = new LogQueue(queueLength);
  }

  public Logger(ILogChangeNotifier notifier, LogQueue queue) {
    this.notifier = notifier;
    this.queue = queue;
  }

  @Override
  public void debug(String message) {
    queue.append(LogLevel.Debug, message);
    notifier.notifyListeners();
  }

  @Override
  public void error(String message) {
    queue.append(LogLevel.Debug, message);
    notifier.notifyListeners();
  }

  public ILogChangeNotifier notifier() {
    return notifier;
  }

  public LogQueue queue() {
    return queue;
  }
}
