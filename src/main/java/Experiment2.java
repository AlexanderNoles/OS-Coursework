import java.util.Objects;

public class Experiment2 {

    static String[] targetSchedulers = new String[]{
            "FCFS",
            "FeedbackRR",
            "IdealSJF",
            "RR",
            "SJFExpo",
    };

    /**
     *
     * @param args command line arguments
     */
    public static void main(String args[]){
        if (args.length == 0)
        {
            for (int i = 0; i < 5; i++) {
                //For each cpu burst frame we want to run each simulator with all the inputs
                //First we need to create the input dir string

                String inputDir = "experiment2/input/CPU-BURST-" + (i+1) + "/";
                String[] simArgs = new String[7];
                //5 is  the number of input files per cpu-burst frame
                for (int j = 0; j < 5; j++)
                {
                    simArgs[j+2] = inputDir + "inputs" + (j+1) + ".in ";
                }

                //Then we run the simulator with those inputs on all the target simulators
                for (String scheduler : targetSchedulers)
                {
                    simArgs[0] = "experiment2/input/" + scheduler + ".prp";
                    simArgs[1] = "experiment2/output/CPU-BURST-" + (i+1) + "/" + scheduler + ".out";
                    Simulator.main(simArgs);
                }
            }
        }
        else if (Objects.equals(args[0], "pre-gen"))
        {
            //Autogenerate input files, input parameters have been set up ahead of time

            for (int i = 0; i < 5; i++) {
                String dir = "experiment2/input/CPU-BURST-" + (i+1) + "/";
                for (int j = 0; j < 5; j++)
                {
                    InputGenerator.main(new String[]
                            {
                                    dir + "input_parameters" + (j+1) + ".prp",
                                    dir + "inputs" + (j+1) + ".in"
                            });
                }
            }
        }
    }
}
