//StarCCM exporter macro 
//V2.0

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
                } else {
                    //This just creates the other scenes
                    displayer.getScalarDisplayQuantity().setFieldFunction(func);
                }
                //Calls the export scene method
                exportScene(scene, simulationDirectory, sep);
                //Calls the export sweep method
                exportSweep(simulation, simulationDirectory, sep, scene);
            }
        }

    public void getPlots(Simulation simulation, File simulationDirectory, String fileSeparator) {
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
            exportXSweep(dir, fileSeparator, currentScene, scalarDisplayer);
            exportYSweep(dir, fileSeparator, currentScene, scalarDisplayer);
            exportZSweep(dir, fileSeparator, currentScene, scalarDisplayer);
        }
    }

    public void exportYSweep(File simulationDirectory, String fileSeparator, Scene scene, ScalarDisplayer currentScalarDisplayer) {
        File dir = new File(simulationDirectory + fileSeparator + " Y Sweep");
        currentScalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        AnimationDirector animationDirector = scene.getAnimationDirector();

        if (dir.mkdir()) {
            SectionAnimationSettings animationSettings = ((SectionAnimationSettings) currentScalarDisplayer.getAnimationManager().getObject("Y Normal"));
            animationSettings.setCycleTime(20.0);
            animationSettings.setAutoRange(false);
            animationSettings.setStart(-1.0);

            CurrentView view = scene.getCurrentView();
            view.setInput(new DoubleVector(new double[]{0.8, 0.0, 0.8}), new DoubleVector(new double[]{0.8, -30.8059436014962, 0.8}), new DoubleVector(new double[]{0.0, 0.0, 1.0}), 1.3052619222005157, 1, 30.0);

            animationDirector.setIsRecording(true);
            animationDirector.record(800, 600, 15.0, -1.0, 20.0, dir.getAbsolutePath(), 1, true, false, VideoEncodingQualityEnum.Q20);
            animationDirector.setIsRecording(false);
        }

    }

    public void exportXSweep(File simulationDirectory, String fileSeparator, Scene scene, ScalarDisplayer scalarDisplayer) {
        File dir = new File(simulationDirectory + fileSeparator + "X Sweep");
        scalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        AnimationDirector animationDirector = scene.getAnimationDirector();

        if (dir.mkdir()) {
            SectionAnimationSettings animationSettings = ((SectionAnimationSettings) scalarDisplayer.getAnimationManager().getObject("X Normal"));
            animationSettings.setCycleTime(20.0);
            animationSettings.setAutoRange(false);
            animationSettings.setStart(-1.0);

            CurrentView view = scene.getCurrentView();
            view.setInput(new DoubleVector(new double[]{0.8, 0.0, 0.8}), new DoubleVector(new double[]{0.8, 30.8059436014962, 0.8}), new DoubleVector(new double[]{0.0, 0.0, 1.0}), 1.3052619222005157, 1, 30.0);

            animationDirector.setIsRecording(true);
            animationDirector.record(800, 600, 15.0, 0.0, 20.0, dir.getAbsolutePath(), 1, true, false, VideoEncodingQualityEnum.Q20);
            animationDirector.setIsRecording(false);
        }
    }

    public void exportZSweep(File simulationDirectory, String fileSeparator, Scene scene, ScalarDisplayer scalarDisplayer) {
        File dir = new File(simulationDirectory + fileSeparator + "Z Sweep");
        scalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        AnimationDirector animationDirector = scene.getAnimationDirector();

        if (dir.mkdir()) {
            SectionAnimationSettings animationSettings = ((SectionAnimationSettings) scalarDisplayer.getAnimationManager().getObject("Z Normal"));
            animationSettings.setCycleTime(20.0);
            animationSettings.setAutoRange(false);
            animationSettings.setStart(-1.0);

            CurrentView view = scene.getCurrentView();
            view.setInput(new DoubleVector(new double[]{0.8, 0.0, 0.8}), new DoubleVector(new double[]{0.8, -30.8059436014962, 0.8}), new DoubleVector(new double[]{0.0, 0.0, 1.0}), 1.3052619222005157, 1, 30.0);

            animationDirector.setIsRecording(true);
            animationDirector.record(800, 600, 15.0, 0.0, 20.0, dir.getAbsolutePath(), 1, true, false, VideoEncodingQualityEnum.Q20);
            animationDirector.setIsRecording(false);
        }
    }

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
    public void createYPlane(Units u, Scene sce, Simulation sim) {
        Simulation simulation_0 = sim;
        Scene scene = sce;
        Units units[] = new Units[3];
        for (int i = 0; i < 3; i++) {
            units[i] = u;
        }

        //scene.setTransparencyOverrideMode(SceneTransparencyOverride.MAKE_SCENE_TRANSPARENT);

        Region region_0 = simulation_0.getRegionManager().getRegion("Region");
        Region region_1 = simulation_0.getRegionManager().getRegion("Rad");

        scene.getCreatorGroup().setObjects(region_0, region_1);
        scene.getCreatorGroup().setQuery(null);

        PlaneSection planeSection = (PlaneSection) simulation_0.getPartManager().createImplicitPart(new NeoObjectVector(new Object[] {}), new DoubleVector(new double[] {0.0, 0.0, 1.0}), new DoubleVector(new double[] {0.0, 0.0, 0.0}), 0, 1, new DoubleVector(new double[] {0.0}));
        LabCoordinateSystem coordinateSystem = simulation_0.getCoordinateSystemManager().getLabCoordinateSystem();
        planeSection.setCoordinateSystem(coordinateSystem);

        planeSection.getInputParts().setQuery(null);

        planeSection.getInputParts().setObjects(region_0, region_1);

        planeSection.getOriginCoordinate().setValue(new DoubleVector(new double[] {0.0, 0.0, 0.0}));

        planeSection.getOriginCoordinate().setCoordinate(u, u, u, new DoubleVector(new double[] {0.0, 0.0, 0.0}));

        planeSection.getOriginCoordinate().setCoordinateSystem(coordinateSystem);

        planeSection.getOrientationCoordinate().setUnits(u);

        planeSection.getOrientationCoordinate().setDefinition("");

        planeSection.getOrientationCoordinate().setValue(new DoubleVector(new double[]{0.0, 1.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinate(u, u, u, new DoubleVector(new double[]{0.0, 1.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinateSystem(coordinateSystem);

        //Only our dear sweet lord Jesus Christ knows what the fuck is going on here
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

        planeSection.setPresentationName("Y Normal");
    }

    public void createXPlane (Units u, Scene sce, Simulation sim) {
        Simulation simulation_0 = sim;
        Scene scene = sce;

        Units units[] = new Units[3];
        for (int i = 0; i < 3; i++) {
            units[i] = u;
        }

        //scene.setTransparencyOverrideMode(SceneTransparencyOverride.MAKE_SCENE_TRANSPARENT);

        Region region_0 = simulation_0.getRegionManager().getRegion("Region");
        Region region_1 = simulation_0.getRegionManager().getRegion("Rad");

        scene.getCreatorGroup().setObjects(region_0, region_1);
        scene.getCreatorGroup().setQuery(null);

        PlaneSection planeSection = (PlaneSection) simulation_0.getPartManager().createImplicitPart(new NeoObjectVector(new Object[] {}), new DoubleVector(new double[] {0.0, 0.0, 1.0}), new DoubleVector(new double[] {0.0, 0.0, 0.0}), 0, 1, new DoubleVector(new double[] {0.0}));
        LabCoordinateSystem coordinateSystem = simulation_0.getCoordinateSystemManager().getLabCoordinateSystem();
        planeSection.setCoordinateSystem(coordinateSystem);

        planeSection.getInputParts().setQuery(null);

        planeSection.getInputParts().setObjects(region_0, region_1);

        planeSection.getOriginCoordinate().setValue(new DoubleVector(new double[] {0.0, 0.0, 0.0}));

        planeSection.getOriginCoordinate().setCoordinate(u, u, u, new DoubleVector(new double[] {0.0, 0.0, 0.0}));

        planeSection.getOriginCoordinate().setCoordinateSystem(coordinateSystem);

        planeSection.getOrientationCoordinate().setUnits(u);

        planeSection.getOrientationCoordinate().setDefinition("");

        planeSection.getOrientationCoordinate().setValue(new DoubleVector(new double[] {1.0, 0.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinate(u, u, u, new DoubleVector(new double[] {1.0, 0.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinateSystem(coordinateSystem);

        fuckKnowsFunction(scene, planeSection, u);

        planeSection.setPresentationName("X Normal");
    }
     */
    public void createZPlane (Units u, Scene sce, Simulation sim, CoordinateSystem coords) {
        PlaneSection planeSection = planeSetup(sim, sce, u);

        planeSection.getOrientationCoordinate().setValue(new DoubleVector(new double[] {0.0, 0.0, 1.0}));

        planeSection.getOrientationCoordinate().setCoordinate(u, u, u, new DoubleVector(new double[] {0.0, 0.0, 1.0}));

        planeSection.getOrientationCoordinate().setCoordinateSystem(coords);

        fuckKnowsFunction(sce, planeSection, u);

        planeSection.setPresentationName("Z Normal");
    }

    public void createYPlane (Units u, Scene sce, Simulation sim, CoordinateSystem coords) {
        PlaneSection planeSection = planeSetup(sim, sce, u);

        planeSection.getOrientationCoordinate().setValue(new DoubleVector(new double[] {0.0, 1.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinate(u, u, u, new DoubleVector(new double[] {0.0, 1.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinateSystem(coords);

        fuckKnowsFunction(sce, planeSection, u);

        planeSection.setPresentationName("Y Normal");
    }

    public void createXPlane (Units u, Scene sce, Simulation sim, CoordinateSystem coords) {
        PlaneSection planeSection = planeSetup(sim, sce, u);

        planeSection.getOrientationCoordinate().setValue(new DoubleVector(new double[] {1.0, 0.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinate(u, u, u, new DoubleVector(new double[] {1.0, 0.0, 0.0}));

        planeSection.getOrientationCoordinate().setCoordinateSystem(coords);

        fuckKnowsFunction(sce, planeSection, u);

        planeSection.setPresentationName("X Normal");
    }

    public PlaneSection planeSetup (Simulation sim, Scene scene, Units units) {
        //scene.setTransparencyOverrideMode(SceneTransparencyOverride.MAKE_SCENE_TRANSPARENT);

        Region region_0 = sim.getRegionManager().getRegion("Region");
        Region region_1 = sim.getRegionManager().getRegion("Rad");

        scene.getCreatorGroup().setObjects(region_0, region_1);
        scene.getCreatorGroup().setQuery(null);

        PlaneSection planeSection = (PlaneSection) sim.getPartManager().createImplicitPart(new NeoObjectVector(new Object[] {}), new DoubleVector(new double[] {0.0, 0.0, 1.0}), new DoubleVector(new double[] {0.0, 0.0, 0.0}), 0, 1, new DoubleVector(new double[] {0.0}));
        LabCoordinateSystem coordinateSystem = sim.getCoordinateSystemManager().getLabCoordinateSystem();
        planeSection.setCoordinateSystem(coordinateSystem);

        planeSection.getInputParts().setQuery(null);

        planeSection.getInputParts().setObjects(region_0, region_1);

        planeSection.getOriginCoordinate().setValue(new DoubleVector(new double[] {0.0, 0.0, 0.0}));

        planeSection.getOriginCoordinate().setCoordinate(units, units, units, new DoubleVector(new double[] {0.0, 0.0, 0.0}));

        planeSection.getOriginCoordinate().setCoordinateSystem(coordinateSystem);

        planeSection.getOrientationCoordinate().setUnits(units);

        planeSection.getOrientationCoordinate().setDefinition("");

        return planeSection;
    }
}