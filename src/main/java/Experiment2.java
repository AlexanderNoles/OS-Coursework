import java.util.Objects;

public class Experiment2 {

    /**
     *
     * @param args command line arguments
     */
    public static void main(String args[]){
        if (args.length == 0)
        {
            System.out.println("balls");
        }
        else if (Objects.equals(args[0], "pre-gen"))
        {
            //Autogenerate input files, input parameters have been set up ahead of time

            for (int i = 0; i < 5; i++) {
                String dir = "experiment2/input/CPU-BURST-" + (10 + (5 * i)) + "/";
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
