package uz.limon.chatsecurity.robot;


import robocode.AdvancedRobot;
import robocode.HitRobotEvent;
import robocode.ScannedRobotEvent;
import robocode.WinEvent;

import java.awt.*;

public class Tursunboy extends AdvancedRobot {

    public void run() {
        setBodyColor(new Color(143, 113, 18));
        setGunColor(new Color(185, 242, 255));
        setRadarColor(new Color(112, 209, 244));
        setBulletColor(new Color(200, 229, 235));
        setScanColor(new Color(255, 242, 148));


        while(true) {
            turnRight(90);
            setMaxVelocity(55);
            ahead(250);
        }
    }


    public void onScannedRobot(ScannedRobotEvent e) {
        turnRight(e.getBearing());
        ahead(e.getDistance() + 3);
        scan();
        fire(3);
    }



    public void onHitRobot(HitRobotEvent e) {
        if (e.getBearing() > -5){
            fire(3);
        }

        if (e.getEnergy() > 40) {
            fire(3);
        } else if (e.getEnergy() > 30) {
            fire(2.5);
        } else if (e.getEnergy() > 10) {
            fire(1.5);
        } else if (e.getEnergy() > 1) {
            fire(0.3);
        }
        if (e.isMyFault()) {
            turnRight(2);
        }else{
            ahead(20);
        }
    }

    public void onWin(WinEvent e) {
        for (int i = 0; i < 50; i++) {
            turnRight(30);
            turnLeft(30);
        }
    }

}