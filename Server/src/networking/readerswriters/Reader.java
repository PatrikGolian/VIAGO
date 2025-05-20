package networking.readerswriters;

import model.exceptions.ValidationException;

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
    }catch (ValidationException e)
    {
      throw new ValidationException(e.getMessage());
    }
    catch (Exception e) {
      throw  new RuntimeException(e);
    } finally {
      lock.releaseRead();
    }
  }

  public T getResult() {
    return result;
  }
}
