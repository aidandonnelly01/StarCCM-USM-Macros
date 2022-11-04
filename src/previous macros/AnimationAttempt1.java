// Simcenter STAR-CCM+ macro: AnimationAttempt1.java
// Written by Simcenter STAR-CCM+ 16.04.012
package macro;

import java.util.*;

import star.common.*;
import star.base.neo.*;
import star.screenplay.*;
import star.vis.*;

public class AnimationAttempt1 extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

    Scene scene_44 = 
      simulation_0.getSceneManager().getScene("Velocity");

    Screenplay screenplay_0 = 
      simulation_0.get(ScreenplayManager.class).createScreenplay(scene_44);

    simulation_0.get(ScreenplayManager.class).setActiveScreenplay(screenplay_0);

    screenplay_0.openEditor();
  }
}
