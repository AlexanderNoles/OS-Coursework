import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Properties;

/**
 * Feedback Round Robin Scheduler
 * 
 * @version 2017
 */
public class FeedbackRRScheduler extends AbstractScheduler {

  protected PriorityQueue<Process> ready;
  protected int timeQuantum;

  @Override
  public void initialize(Properties parameters) {
    ready = new PriorityQueue<Process>(10);
    try{
      timeQuantum = Integer.parseInt(parameters.getProperty("timeQuantum"));
    }
    catch(NumberFormatException e) {
      System.err.println("timeQuantum not a number.");
      System.exit(1);
    }
  }

  @Override
  public int getTimeQuantum() {
    return timeQuantum;
  }

  @Override
  public boolean isPreemptive() {
    return true;
  }

  /**
   * Adds a process to the ready queue.
   * usedFullTimeQuantum is true if process is being moved to ready
   * after having fully used its time quantum.
   */
  public void ready(Process process, boolean usedFullTimeQuantum) {
    if (usedFullTimeQuantum){
      //Raise priority value, this means it has less priority
      process.setPriority(process.getPriority() + 1);
    }
    System.out.println(process.getId());
    ready.add(process);
  }

  /**
   * Removes the next process to be run from the ready queue 
   * and returns it. 
   * Returns null if there is no process to run.
   */
  public Process schedule() {
    //We are using getPriority to determine the priority of the process
    if (ready.size() > 0)
    {
      return ready.remove();
    }

    return null;
  }
}
