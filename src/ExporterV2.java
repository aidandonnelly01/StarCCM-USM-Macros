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
public class ExporterV2 extends StarMacro {

    //Star searches for and runs the 'execute' method, all methods to be run should be in here
    public void execute() {
        //Creates simulation object named simulation_0
        Simulation simulation_0 = getActiveSimulation();
        //sep contains the standard filepath separator for the users operating
        //system, for windows this is '\'
        String sep = System.getProperty("file.separator");
        //dir stores the directory where the sim file is contained
        File dir = new File(simulation_0.getSessionDir() + sep + simulation_0.getPresentationName());

        //problem line below
        createScenes(simulation_0);
        //attempts to create another directory in the sim file's directory named after the sim file
        //mkdir will return false if the directory already exists to prevent data deletion
        /*
        if (dir.mkdir()) {
            creates directory and exports scenes
            simulation_0.println("Simulation Directory created: " + dir.getAbsolutePath());
            getScenes(simulation_0, dir, sep);
            getPlots(simulation_0, dir, sep);
        } else {
            if the directory cannot be created then tell the user, ends macro
            simulation_0.println("Simulation directory failed to create, it may already exist");
        }
         */
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
                //scene.printAndWait(resolvePath(dir + sep + scene.getPresentationName() + " .jpg"), 1, 1920, 1080);
                //Commented out for debugging purposes, this takes a lot of time to run
                //scene.export3DSceneFileAndWait(resolvePath(dir + sep + scene.getPresentationName() + " .sce"), scene.getPresentationName(), "", false, false);
                exportSweep(simulation_0, dir, sep, scene);
            }
            simulation_0.println("Scenes saving complete");
        } else {
            //if directory exists, do not export
            simulation_0.println("Scenes did not save successfully");
        }
    }

    private void createScenes(Simulation simulation) {
        Simulation simulation_0 = simulation;

        Collection<FieldFunction> scalarFuncs = new ArrayList<>();
        scalarFuncs.add((FieldFunction) "Velocity");
        //Collection<FieldFunction> vectorFuncs = simulation.getFieldFunctionManager().getVectorFieldFunctions();

        for (FieldFunction scalar : scalarFuncs) {
            ScalarDisplayer display = createNewScalarScene(scalar.getPresentationName());
            display.getScalarDisplayQuantity().setFieldFunction(scalar);
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

    public void exportSweep(Simulation simulation, File simulationDirectory, String fileSeparator, Scene currentScene) {
        Simulation simulation_0 = simulation;
        String sep = fileSeparator;
        Scene scene = currentScene;
        File dir = new File(simulationDirectory + sep + scene.getPresentationName());
        ScalarDisplayer scalarDisplayer = ((ScalarDisplayer) scene.getDisplayerManager().getObject("Scalar 1"));
        scalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        Units units = ((Units) simulation_0.getUnitsManager().getObject("m"));

        if (dir.mkdir()) {
            exportYSweep(simulation_0, dir, sep, scene, scalarDisplayer, units);
            //exportYSweep(simulation_0, dir, sep, scene, scalarDisplayer);
            //exportZSweep(simulation_0, dir, sep, scene);
        }
    }

    public void exportYSweep(Simulation simulation, File simulationDirectory, String fileSeparator, Scene currentScene, ScalarDisplayer currentScalarDisplayer, Units currentUnits) {
        Simulation simulation_0 = simulation;
        String sep = fileSeparator;
        Scene scene = currentScene;
        File dir = new File(simulationDirectory + sep + scene.getPresentationName() + "Y Sweep");
        ScalarDisplayer scalarDisplayer = currentScalarDisplayer;
        Units units = currentUnits;
        scalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        AnimationDirector animationDirector = scene.getAnimationDirector();

        if (dir.mkdir()) {
            SectionAnimationSettings animationSettings = ((SectionAnimationSettings) scalarDisplayer.getAnimationManager().getObject("Y Normal"));
            //animationSettings.setCycleTime(20.0);
            animationSettings.setAutoRange(false);
            //animationSettings.setStart(-1.0);

            CurrentView view = scene.getCurrentView();
            view.setInput(new DoubleVector(new double[]{0.8, 0.0, 0.8}), new DoubleVector(new double[]{0.8, -30.8059436014962, 0.8}), new DoubleVector(new double[]{0.0, 0.0, 1.0}), 1.3052619222005157, 1, 30.0);

            animationDirector.setIsRecording(true);
            animationDirector.record(800, 600, 15.0, 0.0, 20.0, dir.getAbsolutePath(), 1, true, false, VideoEncodingQualityEnum.Q20);
            animationDirector.setIsRecording(false);
        }

    }

    public void exportYSweep(Simulation simulation, File simulationDirectory, String fileSeparator, Scene scene, ScalarDisplayer scalarDisplayer) {

    }

    public void exportZSweep(Simulation simulation, File simulationDirectory, String fileSeparator, Scene scene) {

    }

    private ScalarDisplayer createNewScalarScene(String sceneName){
        Simulation simulation_0 = getActiveSimulation();

        Collection<NamedObject> sources = new ArrayList<>();
        Collection<Region> regions = new ArrayList<>(simulation_0.getRegionManager().getRegions());
        Collection<Boundary> boundaries = new ArrayList<>(simulation_0.getSceneManager().getBoundaryParts());
        //for (Region r : regions) {
            //boundaries.add(r.getBoundaryManager().getBoundary());
        //}

        sources.addAll(regions);
        sources.addAll(boundaries);

        simulation_0.getSceneManager().createScalarScene("New ScalarScene", "Outline", "Scalar");
        Scene scene = simulation_0.getSceneManager().getScene("New ScalarScene 1");
        scene.setPresentationName(sceneName);
        simulation_0.println(sceneName);
        scene.close();

        CurrentView view = scene.getCurrentView();
        view.setInput(new DoubleVector(new double[]{0.8, 0.0, 0.8}), new DoubleVector(new double[]{0.8, -30.8059436014962, 0.8}), new DoubleVector(new double[]{0.0, 0.0, 1.0}), 1.3052619222005157, 1, 30.0);

        ScalarDisplayer scalarDisplayer = ((ScalarDisplayer) scene.getDisplayerManager().getObject(sceneName));

        scalarDisplayer.getInputParts().setObjects(sources);

        return  scalarDisplayer;
    }
}