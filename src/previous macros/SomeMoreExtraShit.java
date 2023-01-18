// Simcenter STAR-CCM+ macro: SomeMoreExtraShit.java
// Written by Simcenter STAR-CCM+ 16.04.012
package macro;

import java.util.*;

import star.common.*;
import star.base.neo.*;
import star.vis.*;

public class SomeMoreExtraShit extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

    Scene scene_5 = 
      simulation_0.getSceneManager().getScene("Pressure Coefficient");

    ScalarDisplayer scalarDisplayer_3 = 
      ((ScalarDisplayer) scene_5.getDisplayerManager().getObject("Scalar 1"));

    scalarDisplayer_3.getInputParts().setQuery(null);

    scalarDisplayer_3.getInputParts().setObjects();
  }
}
