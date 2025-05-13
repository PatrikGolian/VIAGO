package networking.readerswriters;

public class Reader implements Runnable
{
  private ReadWrite lock;

  public Reader(ReadWrite lock)
  {
    this.lock = lock;
  }

  @Override public void run()
  {
    while (true)
    {
      //before reading
      //preparation
          //  requesting lock
      //acquire lock
      lock.acquireRead();
      //read
          //reading from database
      //release
      lock.releaseRead();
    }
  }

  private void read()
  {

  }
}
