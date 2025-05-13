package networking.readerswriters;

import java.util.concurrent.Callable;

public class Reader<T> implements Runnable {
  private final ReadWrite lock;
  private final Callable<T> task;
  private T result;

  public Reader(ReadWrite lock, Callable<T> task) {
    this.lock = lock;
    this.task = task;
  }

  @Override
  public void run() {
    lock.acquireRead();
    try {
      result = task.call(); // execute read logic
    } catch (Exception e) {
      e.printStackTrace(); // handle/log exception
    } finally {
      lock.releaseRead();
    }
  }

  public T getResult() {
    return result;
  }
}
