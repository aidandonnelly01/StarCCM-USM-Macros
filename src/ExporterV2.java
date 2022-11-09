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

        //attempts to create another directory in the sim file's directory named after the sim file
        //mkdir will return false if the directory already exists to prevent data deletion
        if (dir.mkdir()) {
            //creates directory and exports scenes
            simulation_0.println("Simulation Directory created: " + dir.getAbsolutePath());
            createScenes(simulation_0, dir, sep);
            getPlots(simulation_0, dir, sep);
        } else {
            //if the directory cannot be created then tell the user, ends macro
            simulation_0.println("Simulation directory failed to create, it may already exist");
        }
    }

    /*
    This should export all the scenes, it passes in the simulation, the CWD and the operating system's file separator,
    for windows this would be a \, other operating systems may vary
    */

    public void exportScene(Scene sce, File simulationDirectory, String fileSeparator) {
        Scene scene = sce;
        File dir = new File(simulationDirectory + "Scenes");
                scene.printAndWait(resolvePath(dir + fileSeparator + scene.getPresentationName() + " .jpg"), 1, 1920, 1080);
                //Commented out for debugging purposes, this takes a lot of time to run
                //scene.export3DSceneFileAndWait(resolvePath(dir + sep + scene.getPresentationName() + " .sce"), scene.getPresentationName(), "", false, false);
            }

    private void createScenes(Simulation simulation, File simulationDirectory, String sep) {
        Simulation simulation_0 = simulation;
        String[] functionNames = {"Velocity", "PressureCoefficient", "TotalPressureCoefficient", "TurbulentKineticEnergy"};
        Units units = ((Units) simulation_0.getUnitsManager().getObject("m"));
        LabCoordinateSystem coordinateSystem = simulation_0.getCoordinateSystemManager().getLabCoordinateSystem();

        Collection<NamedObject> sources = new ArrayList<>();
        sources.addAll(simulation_0.getSceneManager().getBoundaryParts());
        sources.addAll(simulation_0.getSceneManager().getRegionParts());

        File dir = new File(simulationDirectory + "Scenes");
        if (dir.mkdir()) {

            for (String name : functionNames) {
                FieldFunction func = simulation.getFieldFunctionManager().getFunction(name);

                simulation_0.getSceneManager().createScalarScene("New Scene", "Outline", "Scalar");
                Scene scene = simulation_0.getSceneManager().getScene("New Scene 1");
                scene.setPresentationName(func.getPresentationName());
                CurrentView view = scene.getCurrentView();
                view.setInput(new DoubleVector(new double[]{0.8, 0.0, 0.8}), new DoubleVector(new double[]{0.8, 30.8059436014962, 0.8}), new DoubleVector(new double[]{0.0, 0.0, 1.0}), 1.3052619222005157, 1, 30.0);
                scene.close();

                ScalarDisplayer displayer = ((ScalarDisplayer) scene.getDisplayerManager().getDisplayer("Scalar 1"));
                displayer.getInputParts().setObjects(sources);
                if (name.equals("Velocity")) {
                    PrimitiveFieldFunction primFunc =
                            ((PrimitiveFieldFunction) simulation_0.getFieldFunctionManager().getFunction("Velocity"));

                    VectorMagnitudeFieldFunction vectorMagFunc =
                            ((VectorMagnitudeFieldFunction) primFunc.getMagnitudeFunction());

                    displayer.getScalarDisplayQuantity().setFieldFunction(vectorMagFunc);
                } else {
                    displayer.getScalarDisplayQuantity().setFieldFunction(func);
                }
                createZPlane(units, scene, simulation_0, coordinateSystem);
                createYPlane(units, scene, simulation_0);
                createXPlane(units, scene, simulation_0);
                exportScene(scene, dir, sep);
                //exportSweep(simulation_0, dir, sep, scene);
            }
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
        File dir = new File(simulationDirectory + sep + scene.getPresentationName() + " Sweeps");
        ScalarDisplayer scalarDisplayer = ((ScalarDisplayer) scene.getDisplayerManager().getObject("Scalar 1"));
        scalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        Units units = ((Units) simulation_0.getUnitsManager().getObject("m"));

        if (dir.mkdir()) {
            //exportXSweep(simulation_0, dir, sep, scene, scalarDisplayer, units);
            exportYSweep(simulation_0, dir, sep, scene, scalarDisplayer, units);
            //exportZSweep(simulation_0, dir, sep, scene, scalarDisplayer, units);
        }
    }

    public void exportYSweep(Simulation simulation, File simulationDirectory, String fileSeparator, Scene currentScene, ScalarDisplayer currentScalarDisplayer, Units currentUnits) {
        Simulation simulation_0 = simulation;
        String sep = fileSeparator;
        Scene scene = currentScene;
        File dir = new File(simulationDirectory + " Y Sweep");
        ScalarDisplayer scalarDisplayer = currentScalarDisplayer;
        Units units = currentUnits;
        scalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        AnimationDirector animationDirector = scene.getAnimationDirector();

        if (dir.mkdir()) {
            //SectionAnimationSettings animationSettings = ((SectionAnimationSettings) scalarDisplayer.getAnimationManager().getObject("Y Normal"));
            //SectionAnimationSettings animationSettings = new SectionAnimationSettings(((ClientServerObjectKey) "UTwxT7Fy2MXLm+BdcDVfXA"));
                    //animationSettings.setCycleTime(20.0);
            //animationSettings.setAutoRange(false);
            //animationSettings.setStart(-1.0);

            CurrentView view = scene.getCurrentView();
            view.setInput(new DoubleVector(new double[]{0.8, 0.0, 0.8}), new DoubleVector(new double[]{0.8, 30.8059436014962, 0.8}), new DoubleVector(new double[]{0.0, 0.0, 1.0}), 1.3052619222005157, 1, 30.0);

            animationDirector.setIsRecording(true);
            animationDirector.record(800, 600, 15.0, -1.0, 20.0, dir.getAbsolutePath(), 1, true, false, VideoEncodingQualityEnum.Q20);
            animationDirector.setIsRecording(false);
        }

    }

    public void exportXSweep(Simulation simulation, File simulationDirectory, String fileSeparator, Scene sce, ScalarDisplayer currentScalarDisplayer, Units currentUnits) {
        Simulation simulation_0 = simulation;
        String sep = fileSeparator;
        Scene scene = sce;
        File dir = new File(simulationDirectory + sep + scene.getPresentationName() + "X Sweep");
        ScalarDisplayer scalarDisplayer = currentScalarDisplayer;
        Units units = currentUnits;
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

    public void exportZSweep(Simulation simulation, File simulationDirectory, String fileSeparator, Scene sce, ScalarDisplayer currentScalarDisplayer, Units currentUnits) {
        Simulation simulation_0 = simulation;
        String sep = fileSeparator;
        Scene scene = sce;
        File dir = new File(simulationDirectory + sep + scene.getPresentationName() + "Z Sweep");
        ScalarDisplayer scalarDisplayer = currentScalarDisplayer;
        Units units = currentUnits;
        scalarDisplayer.getAnimationManager().setMode(DisplayerAnimationMode.SWEEP);
        AnimationDirector animationDirector = scene.getAnimationDirector();

        if (dir.mkdir()) {
            SectionAnimationSettings animationSettings = ((SectionAnimationSettings) scalarDisplayer.getAnimationManager().getObject("Z Normal"));
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

    public void createYPlane(Units u, Scene sce, Simulation sim) {
        Simulation simulation_0 = sim;
        Scene scene = sce;
        Units units[] = new Units[3];
        for (int i = 0; i < 3; i++) {
            units[i] = u;
        }

        scene.setTransparencyOverrideMode(SceneTransparencyOverride.MAKE_SCENE_TRANSPARENT);

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

        scene.setTransparencyOverrideMode(SceneTransparencyOverride.MAKE_SCENE_TRANSPARENT);

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
    public void createZPlane (Units u, Scene sce, Simulation sim, CoordinateSystem coords) {
        PlaneSection planeSection = planeSetup(sim, sce, u);

        planeSection.getOrientationCoordinate().setValue(new DoubleVector(new double[] {0.0, 0.0, 1.0}));

        planeSection.getOrientationCoordinate().setCoordinate(u, u, u, new DoubleVector(new double[] {0.0, 0.0, 1.0}));

        planeSection.getOrientationCoordinate().setCoordinateSystem(coords);

        fuckKnowsFunction(sce, planeSection, u);

        planeSection.setPresentationName("Z Normal");
    }

    public PlaneSection planeSetup (Simulation sim, Scene sce, Units u) {
        Simulation simulation_0 = sim;
        Scene scene = sce;
        Units units[] = new Units[3];
        for (int i = 0; i < 3; i++) {
            units[i] = u;
        }

        scene.setTransparencyOverrideMode(SceneTransparencyOverride.MAKE_SCENE_TRANSPARENT);

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

        return planeSection;
    }
}