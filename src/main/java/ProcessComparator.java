import java.util.Comparator;

//This is used in the priority queue for processes
public class ProcessComparator implements Comparator<Process> {
    //lower number means higher priority
    @Override
    public int compare(Process p1, Process p2){
        if (p1.getPriority() < p2.getPriority())
            return 1; //p1 has more priority
        else if (p1.getPriority() > p2.getPriority())
            return -1; //p2 has more priority
        return 0; //If equal
    }
}
