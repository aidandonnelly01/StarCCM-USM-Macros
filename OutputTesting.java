import java.util.*;
import java.io.File;


import star.common.*;
import star.base.neo.*;
import star.vis.*;
import star.screenplay.*;

public class OutputTesting extends StarMacro{

    //Star searches for and runs the 'execute' method, all methods to be run should be in here
    public void execute() {
        String sep = System.getProperty("file.separator");
        Simulation simulation_0 = getActiveSimulation();
        simulation_0.println(simulation_0.getSessionPath());
        simulation_0.println(simulation_0.getSessionDir());
        simulation_0.println(simulation_0.getSessionDirFile());
        simulation_0.println(simulation_0.getSessionDir() + sep + simulation_0.getPresentationName());
        File file = new File(simulation_0.getSessionDir() + sep + simulation_0.getPresentationName());
        if (file.mkdir()) {
            simulation_0.println("it worked");
        } else {
            simulation_0.println("it didn't work");
        }
    }
}