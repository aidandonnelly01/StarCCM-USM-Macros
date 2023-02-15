// Simcenter STAR-CCM+ macro: ExtraShit.java
// Written by Simcenter STAR-CCM+ 16.04.012
package macro;

import java.util.*;

import star.common.*;
import star.base.neo.*;
import star.vis.*;

public class ExtraShit extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

    Scene scene_1 = 
      simulation_0.getSceneManager().getScene("Turbulent Kinetic Energy");

    SolidBackgroundColor solidBackgroundColor_0 = 
      scene_1.getSolidBackgroundColor();

    solidBackgroundColor_0.setColor(new DoubleVector(new double[] {0.4666999876499176, 0.53329998254776, 0.6000000238418579}));

    solidBackgroundColor_0.setColor(new DoubleVector(new double[] {0.1599999964237213, 0.14000000059604645, 0.12999999523162842}));

    PartDisplayer partDisplayer_2 = 
      ((PartDisplayer) scene_1.getDisplayerManager().getObject("Outline 1"));

    partDisplayer_2.setOutline(true);

    partDisplayer_2.setOutline(false);
  }
}
