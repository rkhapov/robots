package log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

public class LogQueue {

  private final int queueLength;
  private final Queue<LogEntry> messages;

  public LogQueue(int queueLength) {
    this.queueLength = queueLength;
    messages = new ArrayDeque<>();
  }


  public void append(LogLevel logLevel, String strMessage) {
    append(new LogEntry(logLevel, strMessage));
  }

  public void append(LogEntry entry) {
    pushMessage(entry);
  }

  private void pushMessage(LogEntry entry) {
    if (messages.size() == queueLength) {
      messages.poll();
    }

    messages.add(entry);
  }

  public int size() {
    return messages.size();
  }

  public Iterable<LogEntry> range(int startFrom, int count) {
    Queue<LogEntry> messages;

    synchronized (this.messages) {
      messages = this.messages;
    }

    if (startFrom < 0 || startFrom >= messages.size()) {
      return Collections.emptyList();
    }

    int indexTo = Math.min(startFrom + count, messages.size());

    return new ArrayList<>(messages).subList(startFrom, indexTo);
  }

  public Iterable<LogEntry> all() {
    return messages;
  }
}

