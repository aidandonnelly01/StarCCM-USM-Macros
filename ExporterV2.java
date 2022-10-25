import java.util.*;
import java.io.File;


import star.common.*;
import star.base.neo.*;
import star.vis.*;
import star.screenplay.*;

public class ExporterV2 extends StarMacro{

    //Star searches for and runs the 'execute' method, all methods to be run should be in here
    public void execute() {
        Simulation simulation_0 = getActiveSimulation();
        String sep = System.getProperty("file.separator");
        File dir = new File(simulation_0.getSessionDir() + sep + simulation_0.getPresentationName());

        if(dir.mkdir()) {
            simulation_0.println("Simulation Directory created: " + dir.getAbsolutePath());
            //getScenes(simulation_0, dir, sep);
            getPlots(simulation_0, dir, sep);
        }
        else {
            simulation_0.println("Simulation directory failed to create");
        }
    }

    /*
    This should export all the scenes, it passes in the simulation, the CWD and the operating system's file separator,
    for windows this would be a \, other operating systems may vary

     UNCOMMENT THIS LATER

    public void getScenes(Simulation simulation, File simulationDirectory, String fileSeparator) {
        Simulation simulation_0 = simulation;
        String sep = fileSeparator;
        File dir = new File(simulationDirectory + sep + "Scenes");


       if (dir.mkdir()) {
            //iterate through every scene and export the scenes into a .jpg file
            for (Scene scene : simulation_0.getSceneManager().getScenes()) {
                scene.printAndWait(resolvePath(dir + sep + scene.getPresentationName() + " .jpg"), 1, 1920, 1080);
                scene.export3DSceneFileAndWait(resolvePath(dir + sep + scene.getPresentationName() + " .sce"), scene.getPresentationName(), "", false, false);
            }
            simulation_0.println("Scenes saving complete");
        }
       else {
           simulation_0.println("Scenes did not save successfully");
        }
    }
     */

    public void getPlots(Simulation simulation, File simulationDirectory, String fileSeparator) {
        Simulation simulation_0 = simulation;
        String sep = fileSeparator;
        File dir = new File(simulationDirectory + sep + "Plots");

        if (dir.mkdir()) {
            //iterate through every plot and export the scenes into a .jpg file
            for (StarPlot plot : simulation_0.getPlotManager().getObjects()) {
                plot.encode(resolvePath(dir + sep + plot.getPresentationName() + " .jpg"), "jpg", 1920, 1080);
                plot.export(dir + sep + plot.getPresentationName() + ".csv", ",");
            }
            simulation_0.println("Plot saving complete");
        } else {
            simulation_0.println("Plots did not save successfully");
        }
    }
}