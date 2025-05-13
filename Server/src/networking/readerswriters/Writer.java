package networking.readerswriters;

public class Writer implements Runnable
{
  private ReadWrite lock;

  public Writer(ReadWrite lock)
  {
    this.lock = lock;
  }

  @Override public void run()
  {
    while (true)
    {
      //before writing
      //preparation
          //reuesting lock
      //acquire lock
      lock.acquireWrite();
      //write
          //writing database
      //release
      lock.releaseWrite();
    }
  }
  private void write()
  {

  }
}
