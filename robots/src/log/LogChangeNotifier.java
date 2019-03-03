package log;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;


public class LogChangeNotifier implements ILogChangeNotifier {

  private final Set<LogChangeListener> listeners;
  private volatile LogChangeListener[] activeListeners;

  public LogChangeNotifier() {
    listeners = Collections.newSetFromMap(new WeakHashMap<>());
  }

  @Override
  public void registerListener(LogChangeListener listener) {
    synchronized (listeners) {
      listeners.add(listener);
      activeListeners = null;
    }
  }

  @Override
  public void unregisterListener(LogChangeListener listener) {
    synchronized (listeners) {
      listeners.remove(listener);
      activeListeners = null;
    }
  }

  @Override
  public void notifyListeners() {
    var activeListeners = getCurrentActiveListeners();

    for (var listener : activeListeners) {
      listener.onLogChanged();
    }
  }

  private LogChangeListener[] getCurrentActiveListeners() {
    var activeListeners = this.activeListeners;

    if (activeListeners != null) {
      return activeListeners;
    }

    synchronized (listeners) {
      return this.activeListeners = listeners.toArray(new LogChangeListener[0]);
    }
  }
}
