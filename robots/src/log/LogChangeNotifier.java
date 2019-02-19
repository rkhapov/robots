package log;

import java.util.ArrayList;

/**
 * Что починить:
 * 1. Этот класс порождает утечку ресурсов (связанные слушатели оказываются
 * удерживаемыми в памяти)
 */

public class LogChangeNotifier implements ILogChangeNotifier {

  private final ArrayList<LogChangeListener> listeners;
  private volatile LogChangeListener[] activeListeners;

  public LogChangeNotifier() {
    listeners = new ArrayList<>();
  }

  @Override
  public void registerListener(LogChangeListener listener)
  {
    synchronized(listeners)
    {
      listeners.add(listener);
      activeListeners = null;
    }
  }

  @Override
  public void unregisterListener(LogChangeListener listener)
  {
    synchronized(listeners)
    {
      listeners.remove(listener);
      activeListeners = null;
    }
  }

  @Override
  public void notifyListeners() {
    var activeListeners = getCurrentActiveListeners();

    for (var listener : activeListeners)
      listener.onLogChanged();
  }

  private LogChangeListener[] getCurrentActiveListeners() {
    LogChangeListener[] activeListeners = this.activeListeners;

    synchronized (listeners)
    {
      if (this.activeListeners == null)
      {
        activeListeners = listeners.toArray(new LogChangeListener [0]);
        this.activeListeners = activeListeners;
      }
    }

    return activeListeners;
  }
}
