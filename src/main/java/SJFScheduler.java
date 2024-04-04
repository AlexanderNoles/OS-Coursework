import java.util.*;

/**
 * Shortest Job First Scheduler
 * 
 * @version 2017
 */
public class SJFScheduler extends AbstractScheduler {

  protected LinkedList<Process> ready;
  private HashMap<Integer, List<Integer>> previousBursts;

  protected int initialBurstEstimate;
  protected int timeQuantum;
  protected float alphaBurstEstimate;

  @Override
  public void initialize(Properties parameters) {
    //Init ready queue
    ready = new LinkedList<Process>();
    //Init previous bursts
    previousBursts = new HashMap<Integer, List<Integer>>();

    //Store parameters in class
    try{
      initialBurstEstimate = Integer.parseInt(parameters.getProperty("initialBurstEstimate"));
    }
    catch(NumberFormatException e) {
      System.err.println("initialBurstEstimate not a number.");
      System.exit(1);
    }

    try{
      alphaBurstEstimate = Float.parseFloat(parameters.getProperty("alphaBurstEstimate"));
    }
    catch(NumberFormatException e) {
      System.err.println("alphaBurstEstimate not a number.");
      System.exit(1);
    }

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
    int lastBurst = process.getRecentBurst();

    if (lastBurst == -1) //First burst for this process. i.e, it is getting added to the queue for the first time
    {
      //Set to initialBurstEstimate
      lastBurst = initialBurstEstimate;
    }

    if (!previousBursts.containsKey(process.getId()))
    {
      //Initialize new array to store the previous bursts for this process
      previousBursts.put(process.getId(), new ArrayList<Integer>());
    }

    //Add last burst to previous bursts list
    //Used later in the schedule function to estimate the next burst
    previousBursts.get(process.getId()).add(lastBurst);

    //Finally add the process to the ready queue
    ready.add(process);
  }

  /**
   * Removes the next process to be run from the ready queue 
   * and returns it. 
   * Returns null if there is no process to run.
   */
  public Process schedule() {
    //Iterate through each process and estimate their next burst time
    //if that burst time is lower than the current found the lowest set that
    //process to be the new lowest

    //At the end return the lowest estimate burst time

    //By default, the value is null
    //This means if the ready list is empty null is returned
    Process toReturn = null;
    int currentLowestEstimate = 0;

    for (Process process : ready)
    {
      //Calculate estimate
      int burstEstimate = estimateExponentialAverageBurst(process.getId());

      //Comparison
      if (toReturn == null || burstEstimate < currentLowestEstimate)
      {
        toReturn = process;
        currentLowestEstimate = burstEstimate;
      }
    }

    //If process isn't null remove it from the ready queue
    if (toReturn != null)
    {
      ready.remove(toReturn);
    }

    return toReturn;
  }

  private int estimateExponentialAverageBurst(int processID)
  {
    if (previousBursts.containsKey(processID))
    {
      List<Integer> previousBurst = previousBursts.get(processID);

      if (previousBurst.size() > 0)
      {
        float toReturn = previousBurst.get(0);
        for (int i = 1; i < previousBurst.size(); i++)
        {
          //Perform exponential average
          toReturn = (alphaBurstEstimate * previousBurst.get(i)) + ((1-alphaBurstEstimate) * toReturn);
        }

        return (int)toReturn;
      }
      else
      {
        //This throws an error as there should always be at least 1 previous burst time
        //(the initial burst estimate)
        System.err.println("No previous burst times!");
        System.exit(1);
      }
    }
    else
    {
      System.err.println("No key for process!");
      System.exit(1);
    }

    return -1;
  }

  @Override
  public int getTimeQuantum() {
    return timeQuantum;
  }
}
