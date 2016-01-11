package robotrace;

import java.util.Random;

/**
 * Implementation of a camera with a position and orientation.
 */
class Camera {

    /**
     * The position of the camera.
     */
    public Vector eye = new Vector(3f, 6f, 5f);

    /**
     * The point to which the camera is looking.
     */
    public Vector center = Vector.O;

    /**
     * The up vector.
     */
    public Vector up = Vector.Z;

    //the time it takes for the automatic modus to switch from mode
    private final int switchTime = 5;
    //the time the last mode switch took place
    private double lastSwitch = 0;
    //the current mode the automode uses
    private int currentMode = 1;

    private Random rand = new Random();

    /**
     * Updates the camera viewpoint and direction based on the selected camera
     * mode. test test
     */
    public void update(GlobalState gs, Robot focus) {
        switch (gs.camMode) {

            // Helicopter mode
            case 1:
                setHelicopterMode(gs, focus);
                break;

            // Motor cycle mode    
            case 2:
                setMotorCycleMode(gs, focus);
                break;

            // First person mode    
            case 3:
                setFirstPersonMode(gs, focus);
                break;

            // Auto mode    
            case 4:
                setAutoMode(gs, focus);
                break;

            // Default mode    
            default:
                setDefaultMode(gs);
        }
    }

    /**
     * Computes eye, center, and up, based on the camera's default mode.
     */
    private void setDefaultMode(GlobalState gs) {
        //set camera coordinates based on spherical coordinates
        eye.x = gs.vDist * Math.cos(gs.theta) * Math.sin(90 - gs.phi);
        eye.y = gs.vDist * Math.sin(gs.theta) * Math.sin(90 - gs.phi);
        eye.z = gs.vDist * Math.cos(90 - gs.phi);

        center = gs.cnt;

        up = Vector.Z;
    }

    /**
     * Computes eye, center, and up, based on the helicopter mode. The camera
     * should focus on the robot.
     */
    private void setHelicopterMode(GlobalState gs, Robot focus) {
        center = focus.pos;                                                     //the center point is the position of the robot that is focussed
        up = focus.direction;                                                   //the up direction is the tangent of the robot that is focussed, such that the robot walks up on the screen

        eye = center;                                                           //the x and y of the eye point are also at the robots position
        eye = eye.add(new Vector(0, 0, 20));                                    //but the z is moved up by 20 such that we can look down to the center point
    }

    /**
     * Computes eye, center, and up, based on the motorcycle mode. The camera
     * should focus on the robot.
     */
    private void setMotorCycleMode(GlobalState gs, Robot focus) {
        center = focus.pos;                                                     //the center point is the position of the robot that is focussed
        up = Vector.Z;                                                          //the up direction is Vector.Z

        // calculate the eye position
        eye = focus.direction.cross(up).normalized().scale(10);                 //the eye position is located at the robots position plus 10 meter outside the track
        eye = center.add(eye);                                                  //to calculate in what direction this 10 meter is take the cross product from the up and the direction of the robot
        eye = eye.add(new Vector(0, 0, 1));                                     //finally add 1 to the z axis in order to be at a 'motor level' compared to the robots
    }

    /**
     * Computes eye, center, and up, based on the first person mode. The camera
     * should view from the perspective of the robot.
     */
    private void setFirstPersonMode(GlobalState gs, Robot focus) {
        eye = focus.pos;                                                        //the eye point is positioned at the robots head, so first take the robots position
        eye = eye.add(new Vector(0, 0, 2));                                     //then add 2 to the z axis to be at the robots head
        eye = eye.add(focus.direction.normalized().scale(0.5));                 //finally add .5 to be just in front of the head
        up = Vector.Z;                                                          //the up vector is Vector.Z

        // center is in the direction of the tangent
        center = focus.direction;                                               //the center point is located in the direction the robot is running
        center.normalized().scale(10);                                          //normalize this vector and then scale it to 10
        center = eye.add(center);                                               //add the robots position to this vector
    }

    /**
     * Computes eye, center, and up, based on the auto mode. The above modes are
     * alternated.
     */
    private void setAutoMode(GlobalState gs, Robot focus) {
        if (gs.tAnim - lastSwitch > switchTime) {                               //if the switchTime has passed
            lastSwitch = gs.tAnim;                                              //then set the lastSwitch to the current time
            currentMode += 1;                                                   //change the camera mode
            if (currentMode > 3) {                                              //if the modes exceeds 3, then set it back to 1
                currentMode = 1;
            }
        }
        setCurrentMode(gs, focus, currentMode);                                 //finally set the camera mode

    }

    //functino that setAutoMode to alternate between different modes 
    private void setCurrentMode(GlobalState gs, Robot focus, int set) {
        switch (set) {
            // Helicopter mode
            case 1:
                setHelicopterMode(gs, focus);
                break;

            // Motor cycle mode    
            case 2:
                setMotorCycleMode(gs, focus);
                break;

            // First person mode    
            case 3:
                setFirstPersonMode(gs, focus);
                break;
        }
    }

}
