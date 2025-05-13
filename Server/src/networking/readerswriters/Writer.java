package networking.readerswriters;

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
    } finally {
      lock.releaseWrite();
    }
  }
}
