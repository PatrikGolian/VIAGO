package networking.readerswriters;

import model.exceptions.ValidationException;

import java.util.concurrent.Callable;

public class Writer implements Runnable {
  private final ReadWrite lock;
  private final Runnable task;

  public Writer(ReadWrite lock, Runnable task) {
    this.lock = lock;
    this.task = task;
  }

  @Override
  public void run() {
    lock.acquireWrite();
    try {
      task.run(); // execute write logic
    }catch (ValidationException e)
    {
      throw new ValidationException(e.getMessage());
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    finally {
      lock.releaseWrite();
    }
  }
}
