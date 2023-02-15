// Simcenter STAR-CCM+ macro: WHATSTHENAMEOFTHEFUCKINGOBJECT.java
// Written by Simcenter STAR-CCM+ 16.04.012
package macro;

import java.util.*;

import star.common.*;
import star.base.neo.*;
import star.vis.*;
import star.surfacewrapper.*;

public class WHATSTHENAMEOFTHEFUCKINGOBJECT extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

    PlaneSection planeSection_6 = 
      ((PlaneSection) simulation_0.getPartManager().getObject("X Normal"));

    FeatureRetentionTag featureRetentionTag_0 = 
      ((FeatureRetentionTag) simulation_0.get(TagManager.class).getObject("Wrapper Features"));

    simulation_0.get(TagManager.class).setTags(planeSection_6, new NeoObjectVector(new Object[] {featureRetentionTag_0}));

    Units units_1 = 
      ((Units) simulation_0.getUnitsManager().getObject("mm"));

    planeSection_6.getOriginCoordinate().setCoordinate(units_1, units_1, units_1, new DoubleVector(new double[] {0.0, 0.0, 0.0}));
  }
}
