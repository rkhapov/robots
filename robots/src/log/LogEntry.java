package log;

public class LogEntry
{
    private final LogLevel logLevel;
    private final String message;
    
    public LogEntry(LogLevel logLevel, String strMessage)
    {
        message = strMessage;
        this.logLevel = logLevel;
    }
    
    public String getMessage()
    {
        return message;
    }
    
    public LogLevel getLevel()
    {
        return logLevel;
    }
}

