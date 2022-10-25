// Simcenter STAR-CCM+ macro: ScenesTest.java
// Written by Simcenter STAR-CCM+ 16.04.012


import java.util.*;

import star.common.*;
import star.base.neo.*;
import star.vis.*;

public class ScenesTest extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

    Scene scene_0 = 
      simulation_0.getSceneManager().getScene("Geometry Scene 1");

    scene_0.export3DSceneFileAndWait(resolvePath("N:\\1 - USM23 CAD\\Cooling\\CFD\\Radiator\\rad_template_Geometry Scene 2.sce"), "Geometry Scene 1", "", false, false);
  }
}
