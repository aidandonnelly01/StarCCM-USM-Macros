//StarCCM exporter macro
//Aidan Donnelly
//University of Strathclyde Motorsport

//import statements required for java stuff
import java.util.*;
import java.io.File;

//import statements required for star stuff
import star.common.*;
import star.base.neo.*;
import star.vis.*;

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

        //This attempts to create a new directory to save the exports into, if a directory already exists it will
        //keep trying to create one by putting 'copy' at the end
        while (!dir.mkdir()) {
                dir = new File(dir.getAbsolutePath() + " Copy");
        }
        //Announces to the world that the simulation directory has been created
        simulation_0.println("Simulation Directory created: " + dir.getAbsolutePath());
        //Calls the method to export the plots
        getPlots(simulation_0, dir, sep);
        //Calls thr method to create the scenes
        createScenes(simulation_0, dir, sep);
    }

    /*
        This should export all the scenes, it passes in a scene, the CWD and the operating system's file separator,
        for windows (other operating systems are available) this would be a \
    */
    public void exportScene(Scene sce, File simulationDirectory, String fileSeparator) {
        Scene scene = sce;
        File dir = new File (simulationDirectory.getAbsolutePath() + fileSeparator + "Scenes");
        while (!dir.mkdir()) {
            dir = new File(dir.getAbsolutePath() + " Copy");
        }
        scene.printAndWait(resolvePath(dir + fileSeparator + scene.getPresentationName() + " .jpg"), 1, 1920, 1080);
        //Commented out for debugging purposes, this takes a lot of time to run
        //scene.export3DSceneFileAndWait(resolvePath(dir + sep + scene.getPresentationName() + " .sce"), scene.getPresentationName(), "", false, false);
    }

    /*
        This will create the scenes, inside this method is the method to export the scenes and sweeps, this method
        takes in the current simulation, the directory where all the exports are saved, and the standard file separator
        for the user's operating system.
    */
    private void createScenes(Simulation simulation, File simulationDirectory, String sep) {
        String[] functionNames = {"Velocity", "PressureCoefficient", "TotalPressureCoefficient", "TurbulentKineticEnergy"};
        Units units = simulation.getUnitsManager().getObject("m");
        LabCoordinateSystem coordinateSystem = simulation.getCoordinateSystemManager().getLabCoordinateSystem();

        /*
            This takes in all the boundaries of the car, and adds them to an ArrayList so that the 3D model of the car can
            be created in the scene. Some of these methods are depreciated, so they may have to be replaced, but it works for
            now and there seems to be no other way to do this.
        */
        Collection<NamedObject> sources = new ArrayList<>();
        sources.addAll(simulation.getSceneManager().getBoundaryParts());
        sources.addAll(simulation.getSceneManager().getRegionParts());

        /*
            For every function in the function names array, create a scene with that function
        */
            for (String name : functionNames) {
                FieldFunction func = simulation.getFieldFunctionManager().getFunction(name);

                simulation.getSceneManager().createScalarScene("New Scene", "Outline", "Scalar");
                Scene scene = simulation.getSceneManager().getScene("New Scene 1");
                scene.setPresentationName(func.getPresentationName());
                CurrentView view = scene.getCurrentView();
                view.setInput(new DoubleVector(new double[]{0.8, 0.0, 0.8}), new DoubleVector(new double[]{0.8, 30.8059436014962, 0.8}), new DoubleVector(new double[]{0.0, 0.0, 1.0}), 1.3052619222005157, 1, 30.0);
                scene.close();

                ScalarDisplayer displayer = ((ScalarDisplayer) scene.getDisplayerManager().getDisplayer("Scalar 1"));
                displayer.getInputParts().setObjects(sources);
                /*
                    Velocity is a bit weird, the function required is 'Velocity Magnitude', so this part of the script needs to be
                    created specifically for it, however, since it only passes it once and the X, Y and Z planes need to only
                    be created once, it's a good way to call those methods. The plane creator methods require a scene to be created
                    first before the planes themselves can be created, a strange quirk of StarCCM perhaps

                    Scope to clean this up slightly, but it should work
                */
                if (name.equals("Velocity")) {
                    PrimitiveFieldFunction primFunc =
                            ((PrimitiveFieldFunction) simulation.getFieldFunctionManager().getFunction("Velocity"));

                    VectorMagnitudeFieldFunction vectorMagFunc =
                            ((VectorMagnitudeFieldFunction) primFunc.getMagnitudeFunction());

                    displayer.getScalarDisplayQuantity().setFieldFunction(vectorMagFunc);

                    //This creates 3 planes, a surprise tool that will help us later
                    createZPlane(units, scene, simulation, coordinateSystem);
                    createYPlane(units, scene, simulation, coordinateSystem);
                    createXPlane(units, scene, simulation, coordinateSystem);

                    //moved for debugging purposes
                    exportSweep(simulation, simulationDirectory, sep, scene);
                } else {
                    //This just creates the other scenes
                    displayer.getScalarDisplayQuantity().setFieldFunction(func);
                }
                //Calls the export scene method
                //exportScene(scene, simulationDirectory, sep);
                PartDisplayer partDisplayer = ((PartDisplayer) scene.getDisplayerManager().getObject("Outline 1"));
                partDisplayer.setOutline(false);

                SolidBackgroundColor solidBackgroundColor_0 = scene.getSolidBackgroundColor();

                solidBackgroundColor_0.setColor(new DoubleVector(new double[] {0.4666999876499176, 0.53329998254776, 0.6000000238418579}));
                //Calls the export sweep method
                //exportSweep(simulation, simulationDirectory, sep, scene);
            }
        }
    /*
        This method exports the all plots created in the sim as both .jpg files and as .csv files
        It takes in the current sim, the sim directory and the file separator as parameters
     */
    public void getPlots(Simulation simulation, File simulationDirectory, String fileSeparator) {
        //Creates a new directory within the output directory called 'plots' to store all the plots, I guess
        File dir = new File(simulationDirectory + fileSeparator + "Plots");

        if (dir.mkdir()) {
            //iterate through every plot and export the scenes into a .jpg file and exports data into .csv files
            for (StarPlot plot : simulation.getPlotManager().getObjects()) {
                plot.encode(resolvePath(dir + fileSeparator + plot.getPresentationName() + " .jpg"), "jpg", 1920, 1080);
                plot.export(dir + fileSeparator + plot.getPresentationName() + ".csv", ",");
            }
            simulation.println("Plot saving complete");
        } else {
            //if directory already exists, do not export
            simulation.println("Plots did not save successfully");
        }
    }

    /*
        This method sets up the sweep animations, it adds the X, Y and Z plane to the scene that gets passed in,
        so the animation can run. It takes in the current sim, the directory, file separator and the current scene that
        is passed from the createScenes method.

        At the moment, all 3 planes are added at once, meaning it will animate in all 3 planes at once, which is obviously
        a bit of a burden on the processor and generally inefficient, definitely scope to fix this
     */
    public void exportSweep(Simulation simulation, File simulationDirectory, String fileSeparator, Scene currentScene) {
        File dir = new File(simulationDirectory + fileSeparator + currentScene.getPresentationName() + " Sweeps");
        ScalarDisplayer scalarDisplayer = ((ScalarDisplayer) currentScene.getDisplayerManager().getObject("Scalar 1"));
        scalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        scalarDisplayer.getInputParts().setQuery(null);

        PlaneSection xPlane = ((PlaneSection) simulation.getPartManager().getObject("X Normal"));

        PlaneSection yPlane = ((PlaneSection) simulation.getPartManager().getObject("Y Normal"));

        PlaneSection zPlane = ((PlaneSection) simulation.getPartManager().getObject("Z Normal"));

        scalarDisplayer.getInputParts().setObjects(xPlane, yPlane, zPlane);

        if (dir.mkdir()) {
            //exportXSweep(dir, fileSeparator, currentScene, scalarDisplayer);
            exportYSweep(dir, fileSeparator, currentScene, scalarDisplayer);
            //exportZSweep(dir, fileSeparator, currentScene, scalarDisplayer);
        }
    }

    /*
        The next 3 methods are pretty much the same, exporting the X, Y and Z sweeps, not much to say here tbh

        Probably scope to reduce duplication. Probably.
     */
    public void exportYSweep(File simulationDirectory, String fileSeparator, Scene scene, ScalarDisplayer currentScalarDisplayer) {
        File dir = new File(simulationDirectory + fileSeparator + " Y Sweep");
        currentScalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        AnimationDirector animationDirector = scene.getAnimationDirector();

        if (dir.mkdir()) {
            SectionAnimationSettings animationSettings = ((SectionAnimationSettings) currentScalarDisplayer.getAnimationManager().getObject("Y Normal"));
            //animationSettings.setCycleTime(20.0);
            animationSettings.setAutoRange(false);
            animationSettings.setStart(1.3);
            animationSettings.setEnd(-1.3);

            CurrentView view = scene.getCurrentView();
            view.setInput(new DoubleVector(new double[]{1.1222830462226314, -7.001308425034657, 1.0809199577009214}), new DoubleVector(new double[]{1.1222830462226314, -0.00010000203239712846, 1.0809199577009214}), new DoubleVector(new double[]{0.0, 0.0, 1.0}), 1.7867152313349846, 1, 30.0);

            animationDirector.setIsRecording(true);
            animationDirector.record(1920, 1080, 10.0, 0.0, 10.0, dir.getAbsolutePath(), 1, true, false, VideoEncodingQualityEnum.Q20);
            animationDirector.setIsRecording(false);
        }

    }

    public void exportXSweep(File simulationDirectory, String fileSeparator, Scene scene, ScalarDisplayer scalarDisplayer) {
        File dir = new File(simulationDirectory + fileSeparator + "X Sweep");
        scalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        AnimationDirector animationDirector = scene.getAnimationDirector();

        if (dir.mkdir()) {
            SectionAnimationSettings animationSettings = ((SectionAnimationSettings) scalarDisplayer.getAnimationManager().getObject("X Normal"));
            animationSettings.setCycleTime(13.0);
            animationSettings.setAutoRange(false);
            animationSettings.setStart(-1.15);
            animationSettings.setEnd(2.5);

            CurrentView view = scene.getCurrentView();
            view.setInput(new DoubleVector(new double[]{-11.399494936611665, -1.4235649330303537, 0.6076538377070125}), new DoubleVector(new double[]{-1.500000000997599 , -1.4235649330303537, 0.6076538377070125}), new DoubleVector(new double[]{0.0, 0.0, 1.0}), 0.8960019770398103, 1, 30.0);

            animationDirector.setIsRecording(true);
            animationDirector.record(1980, 1080, 5.0, 0.0, 13.0, dir.getAbsolutePath(), 1, true, false, VideoEncodingQualityEnum.Q20);
            animationDirector.setIsRecording(false);
        }
    }

    public void exportZSweep(File simulationDirectory, String fileSeparator, Scene scene, ScalarDisplayer scalarDisplayer) {
        File dir = new File(simulationDirectory + fileSeparator + "Z Sweep");
        scalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        AnimationDirector animationDirector = scene.getAnimationDirector();

        if (dir.mkdir()) {
            SectionAnimationSettings animationSettings = ((SectionAnimationSettings) scalarDisplayer.getAnimationManager().getObject("Z Normal"));
            animationSettings.setCycleTime(13.0);
            animationSettings.setAutoRange(false);
            animationSettings.setStart(-1.4);
            animationSettings.setEnd(-2.5);

            CurrentView view = scene.getCurrentView();
            view.setInput(new DoubleVector(new double[]{0.9371019096585375, -1.4819896673401434, 37.24694708761353}), new DoubleVector(new double[]{0.9371019096585375, -1.4819896673401434, 0.2499999944870197}), new DoubleVector(new double[]{0.0, 1.0, 0.0}), 1.7867152313349846, 1, 30.0);

            animationDirector.setIsRecording(true);
            animationDirector.record(1920, 1080, 10.0, 0.0, 13.0, dir.getAbsolutePath(), 1, true, false, VideoEncodingQualityEnum.Q20);
            animationDirector.setIsRecording(false);
        }
    }

    /*
        This seems to be some sort of tail function that gets executed after each plane is created, thought I'd make a
        method to reduce duplication, no idea what this does though.
        @param scene - current scene
        @param planeSection - current plane section, X,Y or Z
        @param u - current units, usually metres (m)
     */
    public void fuckKnowsFunction(Scene scene, PlaneSection planeSection, Units u) {
        SingleValue singleValue_0 = planeSection.getSingleValue();
        singleValue_0.getValueQuantity().setValue(0.0);
        singleValue_0.getValueQuantity().setUnits(u);
        RangeMultiValue rangeMultiValue_0 = planeSection.getRangeMultiValue();
        rangeMultiValue_0.setNValues(2);
        rangeMultiValue_0.getStartQuantity().setValue(0.0);
        rangeMultiValue_0.getStartQuantity().setUnits(u);
        rangeMultiValue_0.getEndQuantity().setValue(1.0);
        rangeMultiValue_0.getEndQuantity().setUnits(u);
        DeltaMultiValue deltaMultiValue_0 = planeSection.getDeltaMultiValue();
        deltaMultiValue_0.setNValues(2);
        deltaMultiValue_0.getStartQuantity().setValue(0.0);
        deltaMultiValue_0.getStartQuantity().setUnits(u);
        deltaMultiValue_0.getDeltaQuantity().setValue(1.0);
        deltaMultiValue_0.getDeltaQuantity().setUnits(u);
        MultiValue multiValue_0 = planeSection.getArbitraryMultiValue();
        multiValue_0.getValueQuantities().setUnits(u);
        multiValue_0.getValueQuantities().setArray(new DoubleVector(new double[] {0.0}));
        planeSection.setValueMode(ValueMode.SINGLE);
        scene.setTransparencyOverrideMode(SceneTransparencyOverride.USE_DISPLAYER_PROPERTY);
    }

    /*
        Next 3 methods are pretty much the same, they call a setup method and then create the X, Y and Z planes
        Only 1 method is commented on because they're literally the same except for the coordinates it uses.
     */
    public void createZPlane (Units u, Scene sce, Simulation sim, CoordinateSystem coords) {
        //Calls the setup function, which creates a general plane before the method gives it properties
        PlaneSection planeSection = planeSetup(sim, sce, u);

        //Seems to set the value of the orientation then the coordinate, what I think happens here is that it sets where the plane is
        //and then sets the way the plane faces, is the best way to explain it.
        planeSection.getOrientationCoordinate().setValue(new DoubleVector(new double[] {0.0, 0.0, 1.0}));

        planeSection.getOrientationCoordinate().setCoordinate(u, u, u, new DoubleVector(new double[] {0.0, 0.0, 1.0}));

        //This method just sets the coordinate system to the standard one used by the sim
        planeSection.getOrientationCoordinate().setCoordinateSystem(coords);

        //Calls the tail function on the plane section to finish setup
        fuckKnowsFunction(sce, planeSection, u);

        //Names the plane
        planeSection.setPresentationName("Z Normal");
    }

    //See createZPlane for comments
    public void createYPlane (Units u, Scene sce, Simulation sim, CoordinateSystem coords) {
        PlaneSection planeSection = planeSetup(sim, sce, u);

        planeSection.getOrientationCoordinate().setValue(new DoubleVector(new double[] {0.0, 1.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinate(u, u, u, new DoubleVector(new double[] {0.0, 1.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinateSystem(coords);

        fuckKnowsFunction(sce, planeSection, u);

        planeSection.setPresentationName("Y Normal");
    }

    //See createZPlane for comments
    public void createXPlane (Units u, Scene sce, Simulation sim, CoordinateSystem coords) {
        PlaneSection planeSection = planeSetup(sim, sce, u);

        planeSection.getOrientationCoordinate().setValue(new DoubleVector(new double[] {1.0, 0.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinate(u, u, u, new DoubleVector(new double[] {1.0, 0.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinateSystem(coords);

        fuckKnowsFunction(sce, planeSection, u);

        planeSection.setPresentationName("X Normal");
    }

    /*
        This method creates a plane, before the X, Y and Z methods make them specific to their axes.
        Takes in the sim, a scene (any will do) and the units being used in the sim (usually metres).

        Scope to improve efficiency of this method, see comments within.
     */
    public PlaneSection planeSetup (Simulation sim, Scene scene, Units units) {
        scene.setTransparencyOverrideMode(SceneTransparencyOverride.MAKE_SCENE_TRANSPARENT);

        //Creates the two regions of the car, 'Region' seems to be the main car, 'Rad' is the radiator specifically,
        //for sims that aren't just for cooling, the region Rad may not exist, so that is something to look at
        Region region_0 = sim.getRegionManager().getRegion("Region");
        Region region_1 = sim.getRegionManager().getRegion("Rad");

        //Adds the objects to the plane, I think
        scene.getCreatorGroup().setObjects(region_0, region_1);
        scene.getCreatorGroup().setQuery(null);

        //Creates the plane itself and sets the coordinate system
        PlaneSection planeSection = (PlaneSection) sim.getPartManager().createImplicitPart(new NeoObjectVector(new Object[] {}), new DoubleVector(new double[] {0.0, 0.0, 1.0}), new DoubleVector(new double[] {0.0, 0.0, 0.0}), 0, 1, new DoubleVector(new double[] {0.0}));
        LabCoordinateSystem coordinateSystem = sim.getCoordinateSystemManager().getLabCoordinateSystem();
        planeSection.setCoordinateSystem(coordinateSystem);

        planeSection.getInputParts().setQuery(null);

        planeSection.getInputParts().setObjects(region_0, region_1);

        //Coordinates are set to (0,0,0) to start, these get changed later
        planeSection.getOriginCoordinate().setValue(new DoubleVector(new double[] {0.0, 0.0, 0.0}));

        planeSection.getOriginCoordinate().setCoordinate(units, units, units, new DoubleVector(new double[] {0.0, 0.0, 0.0}));

        //Sets some extra things that the plane needs
        planeSection.getOriginCoordinate().setCoordinateSystem(coordinateSystem);

        planeSection.getOrientationCoordinate().setUnits(units);

        planeSection.getOrientationCoordinate().setDefinition("");

        //returns the generic plane section
        return planeSection;
    }
}