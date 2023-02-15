import java.util.*;
import java.io.File;


import star.common.*;
import star.base.neo.*;
import star.vis.*;
import star.screenplay.*;

public class OutputTesting extends StarMacro {

    //Star searches for and runs the 'execute' method, all methods to be run should be in here
    public void execute() {
        Simulation simulation_0 = getActiveSimulation();

        Collection<NamedObject> sources = new ArrayList<>();
        Collection<Region> regions = new ArrayList<>(simulation_0.getRegionManager().getRegions());

        for (Region r : regions) {
            simulation_0.println(r.getPresentationName());
        }
    }
}