// Simcenter STAR-CCM+ macro: VectorSceneAttempt2.java
// Written by Simcenter STAR-CCM+ 16.04.012
package macro;

import java.util.*;

import star.common.*;
import star.base.neo.*;
import star.vis.*;

public class VectorSceneAttempt2 extends StarMacro {

  public void execute() {
    execute0();
  }

  private void execute0() {

    Simulation simulation_0 = 
      getActiveSimulation();

    Scene scene_1 = 
      simulation_0.getSceneManager().getScene("Vector Scene 1");

    CurrentView currentView_1 = 
      scene_1.getCurrentView();

    currentView_1.setInput(new DoubleVector(new double[] {0.9571156770883633, -1.0765895743917444, 1.0143967174001338}), new DoubleVector(new double[] {-8.907139635156334, -50.41703181332147, 2.2709270061626983}), new DoubleVector(new double[] {-0.12044171508866786, 0.04932890794999273, 0.9914940504647565}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9564367530046674, -1.0798796510739885, 1.0144610953749478}), new DoubleVector(new double[] {-9.518960732757831, -50.30564443513023, 1.6865943546245075}), new DoubleVector(new double[] {-0.12244969087227481, 0.03959823266535699, 0.9916844524217937}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9558417664206736, -1.0826851977088143, 1.0144839171268085}), new DoubleVector(new double[] {-9.457848789511699, -50.3260924079642, 1.1905701148599068}), new DoubleVector(new double[] {-0.12451185391396645, 0.029877392526131103, 0.991768188464822}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9564367530046674, -1.0798796510739885, 1.0144610953749478}), new DoubleVector(new double[] {-9.518960732757831, -50.30564443513023, 1.6865943546245075}), new DoubleVector(new double[] {-0.12244969087227481, 0.03959823266535699, 0.9916844524217937}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9691713228431117, -0.9423308654786481, 1.0119256416357807}), new DoubleVector(new double[] {2.229912979373715, -51.245618262469655, 2.1744222233382597}), new DoubleVector(new double[] {-0.1242816284449432, 0.01981116577487162, 0.9920491895776962}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9691713228431117, -0.9423308654786481, 1.0119256416357807}), new DoubleVector(new double[] {2.229912979373715, -51.245618262469655, 2.1744222233382597}), new DoubleVector(new double[] {-0.1242816284449432, 0.01981116577487162, 0.9920491895776962}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9693071681850438, -0.9476211649160553, 1.0120217959597726}), new DoubleVector(new double[] {2.2921490232213566, -51.258333968635725, 1.6782637234466413}), new DoubleVector(new double[] {-0.12402657734554072, 0.00987856198913773, 0.9922297224559581}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9693071681850438, -0.9476211649160553, 1.0120217959597726}), new DoubleVector(new double[] {2.2921490232213566, -51.258333968635725, 1.6782637234466413}), new DoubleVector(new double[] {-0.12402657734554072, 0.00987856198913773, 0.9922297224559581}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9693071681850438, -0.9476211649160553, 1.0120217959597726}), new DoubleVector(new double[] {2.2921490232213566, -51.258333968635725, 1.6782637234466413}), new DoubleVector(new double[] {-0.12402657734554072, 0.00987856198913773, 0.9922297224559581}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9693071681850438, -0.9476211649160553, 1.0120217959597726}), new DoubleVector(new double[] {2.2921490232213566, -51.258333968635725, 1.6782637234466413}), new DoubleVector(new double[] {-0.12402657734554072, 0.00987856198913773, 0.9922297224559581}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9693071681850438, -0.9476211649160553, 1.0120217959597726}), new DoubleVector(new double[] {2.2921490232213566, -51.258333968635725, 1.6782637234466413}), new DoubleVector(new double[] {-0.12402657734554072, 0.00987856198913773, 0.9922297224559581}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9694479750102829, -0.9528539332538932, 1.0120652786005355}), new DoubleVector(new double[] {2.3542593902495987, -51.26602365400234, 1.1819867532306771}), new DoubleVector(new double[] {-0.12375927809312974, -5.501734668682258E-5, 0.9923122684210901}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9695548579570714, -0.9570868438798803, 1.0120783741484163}), new DoubleVector(new double[] {2.125757013788893, -51.27611734773227, 1.1534878165737703}), new DoubleVector(new double[] {-0.12375927809312977, -5.501734668713246E-5, 0.9923122684210901}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.9691205784906578, -0.995509469140584, 1.0127918219173246}), new DoubleVector(new double[] {-1.3238285025985441, -51.24612934866972, 2.7388202906291097}), new DoubleVector(new double[] {-0.12331008592298912, 0.03968566540542707, 0.9915743394577358}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.7762682784888622, -1.3965482995415275, 1.0769539718439491}), new DoubleVector(new double[] {-36.43868947168567, -33.30314855225424, 12.495245423382281}), new DoubleVector(new double[] {0.013093408989814776, 0.32333855596490185, 0.9461927609465292}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.6388906869280547, -1.664819334050995, 1.1162610300716087}), new DoubleVector(new double[] {-4.141265616669131, -51.766263220621425, 1.7137901426379583}), new DoubleVector(new double[] {-0.13961658807427585, 0.025126527177364685, 0.9898867945207486}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.6437438655203009, -1.5990749759530565, 1.1136208839324593}), new DoubleVector(new double[] {-1.976358004454464, -51.746297727096795, 4.541842893155736}), new DoubleVector(new double[] {-0.13596158191759722, 0.07463959623960185, 0.9878984658939969}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.6462816674762598, -1.5283743473110238, 1.1064826577904983}), new DoubleVector(new double[] {-0.3240537269378996, -51.407424943411065, 7.777344198512435}), new DoubleVector(new double[] {-0.13383352471957394, 0.13392530850768802, 0.9819127249416388}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.6462816674762598, -1.5283743473110238, 1.1064826577904983}), new DoubleVector(new double[] {-0.3240537269378996, -51.407424943411065, 7.777344198512435}), new DoubleVector(new double[] {-0.13383352471957394, 0.13392530850768802, 0.9819127249416388}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.6462816674762598, -1.5283743473110238, 1.1064826577904983}), new DoubleVector(new double[] {-0.3240537269378996, -51.407424943411065, 7.777344198512435}), new DoubleVector(new double[] {-0.13383352471957394, 0.13392530850768802, 0.9819127249416388}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.650542093194338, -1.3596703072127005, 1.0619743752698527}), new DoubleVector(new double[] {-0.8172690983531432, -48.02612199400414, 19.862226855038056}), new DoubleVector(new double[] {-0.12776888746827603, 0.37407225190872156, 0.9185559654953624}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.6507572341024495, -1.4247694697990663, 1.0829652457822374}), new DoubleVector(new double[] {2.4342038105933486, -50.273754080220314, 13.081204775047883}), new DoubleVector(new double[] {-0.1273000737426863, 0.23220301869652005, 0.9643010159350607}), 1.3052619222005157, 1, 30.0);

    currentView_1.setInput(new DoubleVector(new double[] {0.6507572341024495, -1.4247694697990663, 1.0829652457822374}), new DoubleVector(new double[] {2.4342038105933486, -50.273754080220314, 13.081204775047883}), new DoubleVector(new double[] {-0.1273000737426863, 0.23220301869652005, 0.9643010159350607}), 1.3052619222005157, 1, 30.0);
  }
}
