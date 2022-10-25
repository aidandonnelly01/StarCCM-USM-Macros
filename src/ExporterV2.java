//StarCCM exporter macro 
//V2.0

//import statements required for java stuff
import java.util.*;
import java.io.File;

//import statements required for star stuff
import star.common.*;
import star.base.neo.*;
import star.vis.*;
import star.screenplay.*;

//Class name is exporter inheriting methods from StarMacro 
public class ExporterV2 extends StarMacro{

    //Star searches for and runs the 'execute' method, all methods to be run should be in here
    public void execute() {
		//Creates simulation object named simulation_0 
        Simulation simulation_0 = getActiveSimulation();
		//sep contains the standard filepath separator for the users operating 
		//system, for windows this is '\'
        String sep = System.getProperty("file.separator");
		//dir stores the directory where the sim file is contained
        File dir = new File(simulation_0.getSessionDir() + sep + simulation_0.getPresentationName());

		//attempts to create another directory in the sim file's directory named after the sim file
		//mkdir will return false if the directory already exists to prevent data deletion 
        if(dir.mkdir()) {
			//creates directory and exports scenes
            simulation_0.println("Simulation Directory created: " + dir.getAbsolutePath());
            getScenes(simulation_0, dir, sep);
            getPlots(simulation_0, dir, sep);
        }
        else {
			//if the directory cannot be created then tell the user, ends macro
            simulation_0.println("Simulation directory failed to create, it may already exist");
        }
    }

    /*
    This should export all the scenes, it passes in the simulation, the CWD and the operating system's file separator,
    for windows this would be a \, other operating systems may vary
    */

    public void getScenes(Simulation simulation, File simulationDirectory, String fileSeparator) {
        Simulation simulation_0 = simulation;
        String sep = fileSeparator;
        File dir = new File(simulationDirectory + sep + "Scenes");

       if (dir.mkdir()) {
            //iterate through every scene and export the scenes into a .jpg file and a 3D representation 
            for (Scene scene : simulation_0.getSceneManager().getScenes()) {
                scene.printAndWait(resolvePath(dir + sep + scene.getPresentationName() + " .jpg"), 1, 1920, 1080);
                scene.export3DSceneFileAndWait(resolvePath(dir + sep + scene.getPresentationName() + " .sce"), scene.getPresentationName(), "", false, false);
            }
            simulation_0.println("Scenes saving complete");
        }
       else {
		   //if directory exists, do not export
           simulation_0.println("Scenes did not save successfully");
        }
    }

    public void getPlots(Simulation simulation, File simulationDirectory, String fileSeparator) {
        Simulation simulation_0 = simulation;
        String sep = fileSeparator;
        File dir = new File(simulationDirectory + sep + "Plots");

        if (dir.mkdir()) {
            //iterate through every plot and export the scenes into a .jpg file and exports data into .csv files
            for (StarPlot plot : simulation_0.getPlotManager().getObjects()) {
                plot.encode(resolvePath(dir + sep + plot.getPresentationName() + " .jpg"), "jpg", 1920, 1080);
                plot.export(dir + sep + plot.getPresentationName() + ".csv", ",");
            }
            simulation_0.println("Plot saving complete");
        } else {
			//if directory already exists, do not export
            simulation_0.println("Plots did not save successfully");
        }
    }
}