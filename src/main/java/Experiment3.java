import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Experiment3 {

    static String[] targetSchedulers = new String[]{
            "FCFS",
            "FeedbackRR",
            "IdealSJF",
            "RR",
            "SJFExpo",
    };

    private static final int minProcessNumber = 5;
    private static final int maxProcessNumber = 50;

    /**
     *
     * @param args command line arguments
     */
    public static void main(String args[]){
        if (args.length == 0)
        {
            int totalOutputCount = 0;
            //We want to run each scheduler with all 5 input sets
            //for each process amount
            for (int i = minProcessNumber; i <= maxProcessNumber; i++)
            {
                //Construct input string
                String inputDir = "experiment3/input/pre-gen-inputs/";
                String[] simArgs = new String[7];

                //5 input sets per processes amount
                for (int j = 0; j < 5; j++) {
                    simArgs[j+2] = inputDir + i + "-inputs" + (j+1) + ".in";
                }

                //Run the simulator
                for(String scheduler : targetSchedulers){
                    simArgs[0] = "experiment3/input/" + scheduler + ".prp";
                    simArgs[1] = "experiment3/output/raw/" + Integer.toString(totalOutputCount + 100) + "-" + Integer.toString(i) + scheduler + ".out"; //Add 100 to fix a sorting issue with listFiles
                    Simulator.main(simArgs);
                    totalOutputCount++;
                }
            }

            //Transform into useful output files
            File[] rawFiles = new File("experiment3/output/raw").listFiles();
            String outputString = "";

            for (int i = 0; i < totalOutputCount; i++)
            {
                assert rawFiles != null;
                try {
                    String[] rawFile = new Scanner(rawFiles[i]).useDelimiter("\\Z").next().split("\n");

                    int totalTurnaroundTime = 0;
                    for(int j = 1; j < rawFile.length-1; j++)
                    {
                        //7 is the index of the turnaround time in the file
                        totalTurnaroundTime += Integer.parseInt(rawFile[j].split("\\s+")[7]);
                    }

                    String identifier = rawFiles[i].getName();
                    identifier = identifier.split("-")[1];
                    identifier = identifier.replace(".out", "");
                    if (i >= 25)
                    {
                        identifier = identifier.substring(0, 2) + "," + identifier.substring(2);
                    }
                    else
                    {
                        identifier = identifier.substring(0, 1) + "," + identifier.substring(1);
                    }


                    outputString += identifier + "," + (totalTurnaroundTime/(rawFile.length-2)) + "\n";

                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            //Need to output this to a file
            System.out.println(outputString);
            File newOutFile = new File("experiment3/output/transformed-output/transformed.out");

            if (!newOutFile.exists())
            {
                try {
                    newOutFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            PrintWriter actualOutputWriter = null;
            try {
                actualOutputWriter = new PrintWriter(newOutFile.getPath(), "UTF-8");
            } catch (FileNotFoundException | UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
            actualOutputWriter.write(outputString);
            actualOutputWriter.close();
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
