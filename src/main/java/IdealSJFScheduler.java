import java.util.LinkedList;
import java.util.Properties;

/**
 * Ideal Shortest Job First Scheduler
 * 
 * @version 2017
 */
public class IdealSJFScheduler extends AbstractScheduler {

  protected LinkedList<Process> ready;
  protected int timeQuantum;

  @Override
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

  @Override
  public int getTimeQuantum() {
    return timeQuantum;
  }

  /**
   * Adds a process to the ready queue.
   * usedFullTimeQuantum is true if process is being moved to ready
   * after having fully used its time quantum.
   */
  public void ready(Process process, boolean usedFullTimeQuantum) {
    ready.add(process);
  }

  /**
   * Removes the next process to be run from the ready queue 
   * and returns it. 
   * Returns null if there is no process to run.
   */
  public Process schedule() {
    //Cycle through all current processes, finding the one with the lowest next burst
    //Return that

    //Initialize to null so if ready queue is empty, we just return null
    Process toReturn = null;
    int currentLowestBurst = 0;

    for(Process process : ready){
      int nextBurst = process.getNextBurst();
      if (toReturn == null || nextBurst < currentLowestBurst)
      {
        toReturn = process;
        currentLowestBurst = nextBurst;
      }
    }

    //If process isn't null remove it from the ready queue
    if (toReturn != null)
    {
      ready.remove(toReturn);
    }
    return toReturn;
  }
}
