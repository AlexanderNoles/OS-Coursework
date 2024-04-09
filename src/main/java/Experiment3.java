import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

public class Experiment3 {

    static String[] targetSchedulers = new String[]{
            "FCFS",
            "FeedbackRR",
            "IdealSJF",
            "RR",
            "SJFExpo",
    };

    private static final int minProcessNumber = 5;
    private static final int maxProcessNumber = 25;

    /**
     *
     * @param args command line arguments
     */
    public static void main(String args[]){
        if (args.length == 0)
        {

        }
        else if (Objects.equals(args[0], "pre-gen"))
        {
            String dir = "experiment3/input/";
            for (int i = minProcessNumber; i <= maxProcessNumber; i++)
            {
                //For each process amount we want to generate 5 input files (one for each seed)
                //the file name should be [process-number]inputs[seed-index].in

                for (int j = 0; j < 5; j++) {
                    //Load the parameter file and change the number of processes
                    String parameterPath = dir + "base-input-parameters/input_parameters" + (j+1) + ".prp"; //Defined here because it is used multiple times

                    //! This is copied code from the InputGenerator class
                    Properties parameters = new Properties();
                    try {
                        File propertyFile = new File(parameterPath);
                        if(propertyFile != null) {
                            parameters.load(new FileInputStream(propertyFile));
                        }
                    } catch (FileNotFoundException e) {
                        System.err.println("Given property file not found");
                        System.err.print(e);
                        System.exit(1);
                    } catch (IOException e) {
                        System.err.println("Problem loading property file");
                        System.err.print(e);
                        System.exit(1);
                    }

                    parameters.setProperty("numberOfProcesses", Integer.toString(i));
                    File newPropFile = new File(parameterPath);
                    try {
                        parameters.store(Files.newOutputStream(newPropFile.toPath()), "");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    //Actually generate the inputs after the parameter file has been updated
                    InputGenerator.main(new String[]{
                            parameterPath,
                            dir + "pre-gen-inputs/" + i + "-inputs" + (j+1) + ".in"
                    });
                }
            }
        }
    }
}
