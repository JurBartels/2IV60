package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;

/**
* Represents a Robot, to be implemented according to the Assignments.
*/
class Robot {
    
    /** The position of the robot. */
    public Vector pos = new Vector(0, 0, 0);
    
    /** The direction in which the robot is running. */
    public Vector direction = new Vector(1,0, 0);

    /** The material from which this robot is built. */
    private final Material material;
    
    /** The factor that the robot's size will scale with while drawing  */
    private double bodyScale; 
    
    private double rotatez;
    
    /** The current position on the x, y and z axis of the robot in a vector */
     
    
    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material, double bodyScale
        /* add other parameters that characterize this robot */) {
        this.material = material;
        
        this.bodyScale = bodyScale;
            // code goes here ...
    }
    

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     * The draw functions are divided into sub functions for different body parts,
     * i.e an arm has subroutines for drawing the upper and lower arm plus a hand.
     * There are also different draw functions for drawing the stick figure parts
     * and the full robot parts. 
     * 
     * The general hierarchy is defined according to this pattern:
     * drawBodyPart() -> (drawUpperBodyPart() && drawLowerBodyPart()) || 
     * (drawUpperStickFigureBodyPart() && drawLowerStickFigureBodyPart())
     * 
     * The stick figure draw functions are called whenever gs.stickFigure is 
     * True. If false, the full robot will be drawn.
     * 
     * To draw the arm and legs, we define an offset (armOffset and legOffset) 
     * which is used to place the leg elements along the x-axis, and to define 
     * the right and left leg by having the middle of the body be 0 on the x-axis
     * and using + or - offset to place the element right or left of the body.
     * This scales with bodyScale to preserve the form of the robot when scaling.
     * 
     * 
     * 
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim) {
        
        //gl.glRotated(Math.acos(((direction.dot(y))/direction.length())), 0, 0, 1f);
        rotatez = Math.toDegrees(Math.atan2(-direction.x(), direction.y()));
        gl.glPushMatrix();
        material.setMaterial(gl);
        gl.glTranslated(pos.x, pos.y, pos.z);    
        gl.glRotated(rotatez, 0, 0, 1f);
        drawTorso(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale);
        drawArm(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, true);
        drawArm(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, false);
        drawLeg(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, true);
        drawLeg(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, false);
        drawHead(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale);
        //pos = new Vector(RaceTrack.getLanePoint(1,tAnim).x,RaceTrack.getLanePoint(1,tAnim).y,0);
        gl.glPopMatrix();
        
    } 
    
    /**
     * Call the functions to draw the full robot head or the
     * stick figure head depending on the boolean input stickFigure. 
     * @param gl       
     * @param glu
     * @param glut
     * @param stickFigure    
     * @param tAnim
     * @param pos
     * @param bodyScale 
     */
    public void drawHead(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale){
        if(!stickFigure){
            
            drawRealHead(gl,  glu,  glut, tAnim,  pos,  bodyScale);
        }
        else{
      
            drawStickFigureHead(gl,  glu,  glut, tAnim,  pos,  bodyScale);
        };
    };
    
    
    /**
     * Call the functions to draw the full robot torso or the
     * stick figure torso depending on the boolean input stickFigure.  
     * @param gl
     * @param glu
     * @param glut
     * @param stickFigure
     * @param tAnim
     * @param pos
     * @param bodyScale 
     */
    public void drawTorso(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale){
        if(!stickFigure){
            //call upper and lower body
            drawUpperBody(gl,  glu, glut, tAnim,  pos, bodyScale);
            drawLowerBody(gl,  glu, glut, tAnim,  pos, bodyScale);
            
        }
        else{
           //call torso stick figure
           drawStickFigureTorso(gl,  glu, glut, tAnim,  pos, bodyScale); 
        }
    }
    
    
    /**
     * If stickFigure is false, decide which arm (left or right) 
     * depending on the boolean leftArm and call the functions to draw
     * the different parts. 
     * @param gl
     * @param glu
     * @param glut
     * @param stickFigure
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param leftArm 
     */
    public void drawArm(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale, boolean leftArm){
        double armOffset;
        if(leftArm){
            //set offset for the arms
            armOffset = -0.5*bodyScale;
        }
        else{
            //set offset for the arms
            armOffset = 0.5*bodyScale;
        }
        if(!stickFigure){
            //call upper, lower and hand draws
            gl.glPushMatrix();
            //First move to the turning point and rotate the arm based on right
            //or left. The draw the arm based on the relative coordinates.
            gl.glTranslated(armOffset, 0, (1.4*bodyScale));
            if(leftArm){
                rotateLimb(tAnim/10, Vector.X, 0.25,gl);
            }
            else{
                rotateLimb2(tAnim/10, Vector.X, 0.25,gl);
            }
            drawUpperArm(gl,  glu, glut, tAnim,  pos,  bodyScale,  armOffset);
            drawLowerArm(gl,  glu, glut, tAnim,  pos,  bodyScale,  armOffset); 
            drawHand(gl,  glu, glut, tAnim,  pos,  bodyScale,  armOffset);
            gl.glPopMatrix();
        }
        else{
            gl.glPushMatrix();
            gl.glTranslated(armOffset, 0, (1.4*bodyScale));
            //call upper and lower arms for stick figure
            if(leftArm){
                rotateLimb(tAnim/10, Vector.X, 0.25,gl);
            }
            else{
                rotateLimb2(tAnim/10, Vector.X, 0.25,gl);
            }
            drawUpperStickFigureArm( gl,  glu,  glut, tAnim,  pos,  bodyScale,  leftArm,  armOffset);
            drawLowerStickFigureArm( gl,  glu,  glut, tAnim,  pos,  bodyScale,  leftArm,  armOffset);
            gl.glPopMatrix();
        }
    
    }
    
    /**
     * If stickFigure is false, decide which leg (left or right) 
     * depending on the boolean leftLeg and call the functions to draw
     * the different parts. 
     * @param gl
     * @param glu
     * @param glut
     * @param stickFigure
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param leftLeg 
     */
    public void drawLeg(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale, boolean leftLeg){
        
        double legOffset;
        if(leftLeg){
            //set leg offset
            legOffset = -0.4*bodyScale;
        }
        else{
            //set leg offset
            legOffset = 0.4*bodyScale;
        }
        if(!stickFigure){
            //call upper and lower leg and foot draw functions
            gl.glPushMatrix();
            gl.glTranslated(legOffset, 0, (1*bodyScale));
            if(leftLeg){
                rotateLimb(tAnim/10, Vector.X, 0.0,gl);
            }
            else{
                rotateLimb2(tAnim/10, Vector.X, 0.0,gl);
            }
            drawUpperleg(gl,  glu, glut, tAnim,  pos,  bodyScale,  legOffset);
            gl.glPopMatrix();
            
            gl.glPushMatrix();
            gl.glTranslated(legOffset, 0, (1*bodyScale));
            if(leftLeg){
                gl.glRotated(30, 1, 0, 0);
            }
            else{
                gl.glRotated(30, 1, 0, 0);
            }
            drawLowerleg(gl,  glu, glut, tAnim,  pos,  bodyScale,  legOffset); 
            drawFoot(gl,  glu, glut, tAnim,  pos,  bodyScale,  legOffset); 
            gl.glPopMatrix();
           
             
            
        }
        else{
            //call upper and lower leg and foot draw functions for stick figure
            drawUpperStickFigureLeg( gl,  glu,  glut,   tAnim,  pos,  bodyScale,  leftLeg,  legOffset);
            drawLowerStickFigureLeg( gl,  glu,  glut,   tAnim,  pos,  bodyScale,  leftLeg,  legOffset);
            drawLowerStickFigureFoot( gl,  glu,  glut,   tAnim,  pos,  bodyScale,  leftLeg,  legOffset);
        }
    
    }
    
    /**
     * Draw the shoulder joint and upper arm for the stick figure
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param leftArm
     * @param armOffset 
     */
    public void drawUpperStickFigureArm(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale, boolean leftArm, double armOffset){
            //shoulder joint
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(0, 0, (0.4*bodyScale));                 //move to position, based on arm offset and bodyheight       
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);       //place shoulder joint
            gl.glPopMatrix();
            //upper arm
            gl.glPushMatrix();
            gl.glTranslated(0, 0, (0.4*bodyScale)-(0.2*bodyScale)); //move to position, based on arm offset and bodyheight
            gl.glScalef(0.15f, 0.15f, 1f);                                                  //scale in the x and y direction, such that the cube becomes a bar
            float s = (float)bodyScale;
            glut.glutSolidCube(0.4f*s);                                                     //place the stick upper arm
            gl.glPopMatrix();
    }
    
    /**
     * Draw the elbow joint and lower arm for the stick figure
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param leftArm
     * @param armOffset 
     */
    public void drawLowerStickFigureArm(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale, boolean leftArm, double armOffset){
            //elbow joint
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(0, 0, (0));                 //move to position, based on arm offset and bodyheight
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);       //place elbow joint
            gl.glPopMatrix();
            //lower arm
            gl.glPushMatrix();
            gl.glTranslated(0, 0.2*bodyScale, (0));   //move to position, based on arm offset and bodyheight
            gl.glScalef(0.15f, 1f, 0.15f);                                                  //scale such that cube becomes a bar
            float s = (float)bodyScale;
            glut.glutSolidCube(0.4f*s);                                                     //place stick lower arm
            gl.glPopMatrix();
    }
    
    /**
     * Draw the shoulder joint and upper arm for the stick figure
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param leftArm
     * @param legOffset 
     */
    public void drawUpperStickFigureLeg(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale, boolean leftArm, double legOffset){
            //hip joint
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(legOffset, 0, (1*bodyScale));                       //move to position, based on leg offset    
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);           //place hip joint
            gl.glPopMatrix();
            //upper leg
            gl.glPushMatrix();
            gl.glTranslated(legOffset, 0, (1*bodyScale)-(0.25*bodyScale));      //move to position, based on leg offset
            gl.glScalef(0.15f, 0.15f, 1f);                                                      //scale such that cube becomes a bar
            float s = (float)bodyScale;
            glut.glutSolidCube(0.5f*s);                                                         //place stick upper leg
            gl.glPopMatrix();
    } 
    
    /**
     * Draw the knee joint and lower leg for the stick figure
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param leftArm
     * @param legOffset 
     */
    public void drawLowerStickFigureLeg(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale, boolean leftArm, double legOffset){
            //knee joint
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(legOffset, 0, (0.5*bodyScale));                     //move to position, based on leg offset
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);           //place knee joint
            gl.glPopMatrix();
            //lower leg
            gl.glPushMatrix();
            gl.glTranslated(legOffset, 0, (0.5*bodyScale)-(0.25*bodyScale));    //move to position, based on leg offset
            gl.glScalef(0.15f, 0.15f, 1f);                                                      //scale such that cube will become bar
            float s = (float)bodyScale;
            glut.glutSolidCube(0.5f*s);                                                         //place stick lower leg
            gl.glPopMatrix();
    } 
    
    /**
     * draw the foot for the stick figure
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param leftArm
     * @param legOffset 
     */
   public void drawLowerStickFigureFoot(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale, boolean leftArm, double legOffset){
            //foot
            gl.glPushMatrix();
            gl.glTranslated(legOffset, 0.1*bodyScale, (0.01*bodyScale));      //move to position, based on leg offset
            gl.glScalef(0.15f, 1f, 0.15f);                                                      //scale such that cube becomes bar
            float s = (float)bodyScale;
            glut.glutSolidCube(0.5f*s);                                                         //place stick foot
            gl.glPopMatrix();
    } 
   
   
   /**
    * Draw the head for the stick figure
    * @param gl
    * @param glu
    * @param glut
    * @param tAnim
    * @param pos
    * @param bodyScale 
    */
    public void drawStickFigureHead(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale){
            //head
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(0, 0, (2.2*bodyScale));                               //move to position based on bodyheight and radius of sphere
            glut.glutSolidSphere(0.2f*bodyScale, RobotRace.slices, RobotRace.slices);           //place stick head
            gl.glPopMatrix();
    }
    
    /**
     * Draw the shoulder joints and upper arm for the full robot
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param armOffset 
     */
    public void drawUpperArm(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale, double armOffset){
            //shoulder joints
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(0, 0, (0.4*bodyScale));                     //move to position based on arm offset and bodyheight
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);           //place shoulder joint
            gl.glPopMatrix();
            //draw upper arm
            gl.glPushMatrix();
            gl.glColor3f(0.5f, 0.0f, 1.0f);
            gl.glTranslated(0, 0, (0.2*bodyScale));                     //move to position based on arm offset and bodyheight
            gl.glScalef(1f, 1f, 2f);                                                            //scale arm such that it becomes an ellipsoid
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);           //place upper arm
            gl.glPopMatrix();
    };
    
    /**
     * draw the elbow joint and lower arm for the full robot
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param armOffset 
     */
    public void drawLowerArm(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale, double armOffset){
            //elbow joints
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);           //place elbow joint
            gl.glPopMatrix();
            //draw lower arm
           gl.glPushMatrix();
            gl.glColor3f(0.5f, 0.0f, 1.0f);
            gl.glTranslated(0, 0.2*bodyScale, (0));       //move to position based on arm offset and bodyheight
            gl.glScalef(1f, 2f, 1f);                                                            //scale such that it becomes an ellipsoid
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);           //place lower arm
            gl.glPopMatrix();
    };
    
    /**
     * Draw a hand of the full robot 
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param armOffset 
     */
    public void drawHand(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale, double armOffset){
            //hands
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(0, 0.4*bodyScale, (0));       //move to position based on arm offset and bodyheight
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);           //place hands
            gl.glPopMatrix();
    };
    //gl.glTranslated(armOffset, 0, (1.4*bodyScale));
    /**
     * Draw the hip joint and the upper leg for the full robot
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param legOffset 
     */
    public void drawUpperleg(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale, double legOffset){
           
            //hip joints
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            //gl.glTranslated(legOffset, 0, (1*bodyScale));                       //move to position based on leg offset
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);           //place hip joints
            gl.glPopMatrix();
            //draw upper leg
            gl.glPushMatrix();
            gl.glColor3f(0.5f, 0.0f, 1.0f);
            gl.glTranslated(0, 0, (-0.3*bodyScale));                     //move to position based on leg offset
            gl.glScalef(1f, 1f, 2.5f);                                                          //scale such that it becomes an ellipsoid
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);           //place upper leg
            gl.glPopMatrix();       
    };
    
    /**
     * Draw the knee joint and lower leg for the full robot
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param legOffset 
     */  
    public void drawLowerleg(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale, double legOffset){
            
            //knee joints
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(0, 0, (-0.5*bodyScale));                     //move to position based on leg offset
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);           //place knee joint
            gl.glPopMatrix();
            
            //draw lower leg
            gl.glPushMatrix();
            gl.glColor3f(0.5f, 0.0f, 1.0f);
            gl.glTranslated(0, 0, (-0.75*bodyScale));                    //move to position based on leg offset
            gl.glScalef(1f, 1f, 2.5f);                                                          //scale such that it becomes an ellipsoid
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);           //place lower leg
            gl.glPopMatrix();
    };
    
   
    /**
     * Draw a foot as cube in the full robot, either left or right depending on legOffset.
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale
     * @param legOffset 
     */
    public void drawFoot(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale, double legOffset){
            //foot
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(0, 0.1*bodyScale, (-0.9*bodyScale));       //move to position based on leg offset
            gl.glScalef(0.4f, 1f, 0.4f);                                                        //scale such that it becomes a bar
            float s = (float)bodyScale;
            glut.glutSolidCube(0.5f*s);                                                         //place the foot
            gl.glPopMatrix(); 
    };
    
    //Draw the head as a sphere, call the functions to draw the eyes and ears
    /**
     * Draw the head as a sphere, call the functions to draw the eyes and ears
     * @param gl
     * @param glu
     * @param glut
     * @param tAnim
     * @param pos
     * @param bodyScale 
     */
   public void drawRealHead(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale){
      
       //head
       gl.glPushMatrix();
       gl.glColor3f(0.2f, 0f, 0.2f);
       gl.glTranslated(0, 0, (2.2*bodyScale));                                    //move to position based on bodyheight and radius sphere
       glut.glutSolidSphere(0.2f*bodyScale, RobotRace.slices, RobotRace.slices);                //place the head
       gl.glPopMatrix();
       
       //call the draw funcions for the eyes and ears
       drawEyes(gl,glu,glut,tAnim,pos,bodyScale);
       drawEars(gl,glu,glut,tAnim,pos,bodyScale);
       
       
   };
   

   /**
    * Draw the two coloured eyes on the head.
    * @param gl
    * @param glu
    * @param glut
    * @param tAnim
    * @param pos
    * @param bodyScale 
    */
   public void drawEyes(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale){
       
        //left eye
        gl.glPushMatrix();
        gl.glColor3f(1f, 0f, 0f);
        gl.glTranslated(0.1*bodyScale, 0.2*bodyScale, (2.2*bodyScale));       //translate to left eye position
        glut.glutSolidSphere(0.05f*bodyScale, RobotRace.slices, RobotRace.slices);              //place left eye
        gl.glPopMatrix();
       
        //right eye
        gl.glPushMatrix();
        gl.glColor3f(1f, 0f, 0f);
        gl.glTranslated(-0.1*bodyScale, 0.2*bodyScale, (2.2*bodyScale));       //translate to right eye position
        glut.glutSolidSphere(0.05f*bodyScale, RobotRace.slices, RobotRace.slices);              //place right eye
        gl.glPopMatrix();
   };
   
   /**
    * Draw the two ears, which are spheres above the head.
    * @param gl
    * @param glu
    * @param glut
    * @param tAnim
    * @param pos
    * @param bodyScale 
    */
   public void drawEars(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale){
       
        //left ear
        gl.glPushMatrix();
        gl.glColor3f(0.2f, 0f, 0.2f);
        gl.glTranslated(0.25*bodyScale, 0, (2.4*bodyScale));                    //translate to left ear position
        glut.glutSolidSphere(0.2f*bodyScale, RobotRace.slices, RobotRace.slices);               //place left ear
        gl.glPopMatrix();
      
        //right ear
        gl.glPushMatrix();
        gl.glColor3f(0.2f, 0f, 0.2f);
        gl.glTranslated(-0.25*bodyScale, 0, (2.4*bodyScale));                    //translate to right ear position
        glut.glutSolidSphere(0.2f*bodyScale, RobotRace.slices, RobotRace.slices);               //place right ear
        gl.glPopMatrix();
   };
   
  
   /**
    * Draw the upper part of the body, consisting of a sphere and cube.
    * @param gl
    * @param glu
    * @param glut
    * @param tAnim
    * @param pos
    * @param bodyScale 
    */
   public void drawUpperBody(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale){
      
       //upper body
       gl.glPushMatrix();
       gl.glColor3f(0.0f, 0.0f, 0.0f);
       gl.glTranslated(0,0, (1.7*bodyScale));                                    //move to position based on bodyheight
       glut.glutSolidSphere(0.4f*bodyScale, RobotRace.slices, RobotRace.slices);                //place upper body
       gl.glPopMatrix();
       
       //Draw shoulders
       gl.glPushMatrix();
       float s = (float)bodyScale;
       gl.glColor3f(0.5f, 0, 1f);
       gl.glTranslated(0, 0, (1.8*bodyScale));                                    //move to position based on bodyheight
       gl.glScalef(1f, 0.1f, 0.1f);                                                             //scale such that it becomes a bar
       glut.glutSolidCube(1f*s);                                                                //place shoulder bar
       gl.glPopMatrix();
   
   
   };
   
   
   /**
    * Draw the lower part of the body, consisting of a sphere, cone and cube.
    * @param gl
    * @param glu
    * @param glut
    * @param tAnim
    * @param pos
    * @param bodyScale 
    */
   public void drawLowerBody(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale){
       
       //Lower body
        gl.glPushMatrix();
        gl.glColor3f(0.0f, 0.0f, 0.0f);
        gl.glTranslated(0,0,bodyScale);                                               //middle of bottom circle equals 1
        glut.glutSolidSphere(0.3f*bodyScale, RobotRace.slices, RobotRace.slices);                   //place lower body
        gl.glPopMatrix();

        //middle cone
        gl.glPushMatrix();
        gl.glRotatef(180, 1f, 0, 0);                                                                //rotate such that cone points downwards
        //gl.glTranslated(0, -2*pos.y, (-1.45*bodyScale)-2*pos.z);                                    //translate based on reverse bodyheight, since z is flipped
        gl.glTranslated(0, 0, (-1.45*bodyScale));                                    //translate 
        gl.glColor3f(0.5f, 0, 1f);  
        glut.glutSolidCone(0.3f*bodyScale, 0.45f*bodyScale, RobotRace.slices, RobotRace.slices);    //height of cone equals 0.45
        gl.glPopMatrix();
       
        //Draw hips
        gl.glPushMatrix();
        gl.glTranslated(0, 0, (1*bodyScale));                                         //move to middle of robot
        gl.glScalef(0.75f, 0.1f, 0.1f);                                                             //scale such that it becomes a bar
        float s = (float)bodyScale;
        glut.glutSolidCube(1f*s);                                                                   //place hip bar
        gl.glPopMatrix();
   
   };
   
   
   /**
    * Draw the torso, consisting of the backbone, shoulders and hips in the stickfigure mode.
    * @param gl
    * @param glu
    * @param glut
    * @param tAnim
    * @param pos
    * @param bodyScale 
    */
   public void drawStickFigureTorso(GL2 gl, GLU glu, GLUT glut, float tAnim, Vector pos, double bodyScale){
       
       //backbone
        gl.glPushMatrix();
        gl.glTranslated(0, 0, (1.4*bodyScale));                                       //move to middle of torso based on bodyheight
        gl.glScalef(0.05f, 0.05f, 1f);                                                              //scale such that it becomes a bar
        float s = (float)bodyScale;
        glut.glutSolidCube(1.4f*s);                                                                 //place backbone
        gl.glPopMatrix();
            
        //shoulders
        gl.glPushMatrix();
        gl.glTranslated(0, 0, (1.8*bodyScale));                                       //move to shoulderheight
        gl.glScalef(1f, 0.05f, 0.05f);                                                              //scale such that it becomes a bar
        glut.glutSolidCube(1f*s);                                                                   //place shoulder bar
        gl.glPopMatrix();
        
        //hips
        gl.glPushMatrix();
        gl.glTranslated(0, 0, (1*bodyScale));                                         //move to middle of robot for hips
        gl.glScalef(0.75f, 0.05f, 0.05f);                                                           //scale such that it becomes a bar
        glut.glutSolidCube(1f*s);                                                                   //place hip bar
        gl.glPopMatrix();
   };
    
   /* 
   Rotate the affected part around a Vector, with offset trans.
   Used this to animate limbs
   */
    public void rotateLimb(float t, Vector axis, double trans, GL2 gl)
    {
        gl.glTranslated(0, 0, trans);
        gl.glRotated(Math.sin(t * 10) * 45, axis.x(), axis.y(), axis.z());
        gl.glTranslated(0, 0, trans * -1);
    }
    
    //Rotate at the same angle as above, *-1 to get a forwards/backwards motion 
    //in the limbs
       public void rotateLimb2(float t, Vector axis, double trans, GL2 gl)
    {
        gl.glTranslated(0, 0, trans);
        gl.glRotated(Math.sin(t * 10) * -45, axis.x(), axis.y(), axis.z());
        gl.glTranslated(0, 0, trans * -1);
    }
 
}