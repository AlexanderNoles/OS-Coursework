import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;
import java.util.Properties;

public class Experiment1 {

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
                    File propertyFile = new File("experiment1/output/pre-gen-base/" + scheduler + ".prp");
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
                for (int i = 15; i < 25; i++)
                {
                    //First set the timeQuantum in our parameters object
                    parameters.setProperty("timeQuantum", Integer.toString(i));

                    //Create the fileInputStream and save the file there
                    File newPropFile = new File("experiment/output/gen-parameter-files/" + scheduler + Integer.toString(i) + ".prp");

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
