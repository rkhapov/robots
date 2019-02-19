package log;

public final class Logger
{
    private static final LogQueue defaultLogSource;

    static {
        defaultLogSource = new LogQueue(100);
    }
    
    private Logger()
    {
    }

    public static void debug(String strMessage)
    {
        defaultLogSource.append(LogLevel.Debug, strMessage);
    }
    
    public static void error(String strMessage)
    {
        defaultLogSource.append(LogLevel.Error, strMessage);
    }

    public static LogQueue getDefaultLogSource()
    {
        return defaultLogSource;
    }
}
