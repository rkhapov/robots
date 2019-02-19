package log;

public interface ILogChangeNotifier {
  void notifyListeners();
  void registerListener(LogChangeListener listener);
  void unregisterListener(LogChangeListener listener);
}
