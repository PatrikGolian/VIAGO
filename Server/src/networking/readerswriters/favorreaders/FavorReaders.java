package networking.readerswriters.favorreaders;

import networking.readerswriters.ReadWrite;
import networking.readerswriters.Writer;
import networking.readerswriters.Reader;

public class FavorReaders implements ReadWrite
{
  public synchronized static void startReader(ReadWrite sharedResource)
  {
      Reader reader = new Reader(sharedResource);
      read[readers] = new Thread(reader,"Writer"+readers);
      read[readers].start();
  }
  //writer Threads
  public synchronized static void startWriter(ReadWrite sharedResource)
  {
      Writer writer = new Writer(sharedResource);
      write[writers] = new Thread(writer,"Writer"+writers);
      write[writers].start();
  }

  private static Thread[] read = new Thread[20];
  private static Thread[] write = new Thread[5];
  private static int readers;
  private static int writers;

  public FavorReaders()
  {
    readers = 0;
    writers = 0;
  }

  @Override public synchronized void acquireRead()
  {
    while (writers > 0)
    {
      try
      {
        System.out.println(Thread.currentThread().getName() + " WAIT (readers :: "+readers+"), (writers :: "+writers+")");
        wait();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }

    System.out.println(Thread.currentThread().getName() + " READING (readers :: "+readers+"), (writers :: "+writers+")");
    readers++;
  }

  @Override public synchronized void releaseRead()
  {
    readers--;
    if (readers == 0)
    {
      notify(); //notify one waiting writer

      System.out.println(Thread.currentThread().getName() + " FINISHED READING (readers :: "+readers+"), (writers :: "+writers+")");
    }
  }

  @Override public synchronized void acquireWrite()
  {
    while (readers > 0||writers > 0)
    {
      try
      {

        System.out.println(Thread.currentThread().getName() + " WAITING (readers :: "+readers+"), (writers :: "+writers+")");
        wait();
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
    }

    System.out.println(Thread.currentThread().getName() + " WRITING (readers :: "+readers+"), (writers :: "+writers+")");
    writers++;
  }

  @Override public synchronized void releaseWrite()
  {
    writers--;
    notifyAll();

    System.out.println(Thread.currentThread().getName() + " FINISHED WRITING (readers :: "+readers+"), (writers :: "+writers+")");
  }
}
