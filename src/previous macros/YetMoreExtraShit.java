// Simcenter STAR-CCM+ macro: YetMoreExtraShit.java
// Written by Simcenter STAR-CCM+ 16.04.012
package macro;

import java.util.*;

import star.common.*;
import star.base.neo.*;
import star.vis.*;

public class YetMoreExtraShit extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

    Scene scene_4 = 
      simulation_0.getSceneManager().getScene("Turbulent Kinetic Energy");

    simulation_0.getSceneManager().deleteScenes(new NeoObjectVector(new Object[] {scene_4}));

    PlaneSection planeSection_0 = 
      ((PlaneSection) simulation_0.getPartManager().getObject("Z Normal"));

    simulation_0.getPartManager().removeObjects(planeSection_0);
  }
}
