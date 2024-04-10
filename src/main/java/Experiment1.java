import java.io.*;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;
import java.util.Scanner;

public class Experiment1 {

    static String[] targetSchedulers = new String[]{
            "FCFS",
            "FeedbackRR",
            "IdealSJF",
            "RR",
            "SJFExpo",
    };

    public static final int minQuantum = 5;
    public static final int maxQuantum = 50;


    /**
     *
     * @param args command line arguments
     */
    public static void main(String args[]){
        if (args.length == 0)
        {
            //actually run experiment
            //For each time quantum run each scheduler with all inputs
            //store the output in the output folder
            //This creates raw output files, that we then convert into transformed output files

            //First take all the inputs and put them in an array to be passed to the simulator
            //leave the first two elements of the array open for the other args
            String inputDir = "experiment1/input/processes/";
            String[] simArgs = new String[7];
            //5 is  the number of input files
            for (int j = 0; j < 5; j++)
            {
                simArgs[j+2] = inputDir + "inputs" + (j+1) + ".in";
            }

            int totalOutputCount = 0;
            for (int i = minQuantum; i <= maxQuantum; i++)
            {
                //For each scheduler
                for (String scheduler : targetSchedulers)
                {
                    simArgs[0] = "experiment1/input/gen-parameter-files/" + scheduler + Integer.toString(i) + ".prp";
                    simArgs[1] = "experiment1/output/raw/"
                            + Integer.toString(totalOutputCount+100) + "-" //We add 100 to fix a sorting issue with listFiles
                            + Integer.toString(i)
                            + scheduler
                            + ".out";
                    Simulator.main(simArgs);
                    totalOutputCount++;
                }
            }

            //Transform into useful output files
            File[] rawFiles = new File("experiment1/output/raw").listFiles();
            String outputString = "x identifier y\n";

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
            File newOutFile = new File("experiment1/output/transformed-output/transformed.out");

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
            //For each file in pre-gen base, generate n amount of simulator parameter files
            for (String scheduler : targetSchedulers)
            {
                //First load the base scheduler parameters
                //! This is copied code from the simulator class
                Properties parameters = new Properties();
                try {
                    File propertyFile = new File("experiment1/input/pre-gen-base/" + scheduler + ".prp");
                    parameters.load(new FileInputStream(propertyFile));
                } catch (FileNotFoundException e) {
                    System.err.println("Given property file not found");
                    System.err.print(e);
                    System.exit(1);
                } catch (IOException e) {
                    System.err.println("Problem loading property file");
                    System.exit(1);
                }

                //For each time quantum we want to test we must generate a parameter file for this scheduler
                for (int i = minQuantum; i <= maxQuantum; i++)
                {
                    //First set the timeQuantum in our parameters object
                    parameters.setProperty("timeQuantum", Integer.toString(i));

                    //Create the fileInputStream and save the file there
                    File newPropFile = new File("experiment1/input/gen-parameter-files/" + scheduler + Integer.toString(i) + ".prp");

                    if (!newPropFile.exists())
                    {
                        try {
                            newPropFile.createNewFile();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    try {
                        parameters.store(Files.newOutputStream(newPropFile.toPath()), "");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
