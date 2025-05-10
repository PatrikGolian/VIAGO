package utilities.logging;

public class ConsoleLogger implements Logger
{
  private LogLevel logAboveThisLevel;

  public ConsoleLogger(LogLevel logAboveThisLevel)
  {
    this.logAboveThisLevel = logAboveThisLevel;
  }

  @Override public void log(String text, LogLevel level)
  {
    if (level.ordinal() >= logAboveThisLevel.ordinal())
    {
      System.out.println(level.name() + ": " + text);
    }
  }
}
