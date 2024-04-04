import java.util.*;

/**
 * Round Robin Scheduler
 * 
 * @version 2017
 */
public class RRScheduler extends AbstractScheduler {

  //Our ready queue
  protected Queue<Process> ready;
  protected int timeQuantum;

  public void initialize(Properties parameters) {
    ready = new LinkedList<Process>();
    try{
      timeQuantum = Integer.parseInt(parameters.getProperty("timeQuantum"));
    }
    catch(NumberFormatException e) {
      System.err.println("timeQuantum not a number.");
      System.exit(1);
    }
  }


  /**
   * Adds a process to the ready queue.
   * usedFullTimeQuantum is true if process is being moved to ready
   * after having fully used its time quantum.
   */
  public void ready(Process process, boolean usedFullTimeQuantum) {
    //For basic round-robin we don't need to care about the full time quantum
    ready.add(process);
  }

  /**
   * Removes the next process to be run from the ready queue 
   * and returns it. 
   * Returns null if there is no process to run.
   */
  public Process schedule() {

    if (ready.size() > 0)
    {
      return ready.remove();
    }

    return null;
  }

  @Override
  public int getTimeQuantum() {
    return timeQuantum;
  }
}
