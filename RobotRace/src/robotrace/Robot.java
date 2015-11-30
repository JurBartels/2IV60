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
    public Vector position = new Vector(0, 0, 0);
    
    /** The direction in which the robot is running. */
    public Vector direction = new Vector(1, 0, 0);

    /** The material from which this robot is built. */
    private final Material material;
    
    /** The factor that the robot's size will scale with while drawing  */
    private double bodyScale; 
    
    /** The current position on the x, y and z axis of the robot in a vector */
    private Vector pos; 
    
    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material,Vector pos, double bodyScale
        /* add other parameters that characterize this robot */) {
        this.material = material;
        this.pos = pos;
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
        gl.glPushMatrix();
        //gl.glRotatef(90+15*tAnim, 0, 0, 1f);
        setMaterial(gl);
        drawTorso(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale);
        drawArm(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, true);
        drawArm(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, false);
        drawLeg(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, true);
        drawLeg(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, false);
        drawHead(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale);
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
            
            drawUpperBody(gl,  glu, glut, tAnim,  pos, bodyScale);
            drawLowerBody(gl,  glu, glut, tAnim,  pos, bodyScale);
            
        }
        else{
           
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
            armOffset = -0.5*bodyScale;
        }
        else{
            armOffset = 0.5*bodyScale;
        }
        if(!stickFigure){
            drawUpperArm(gl,  glu, glut, tAnim,  pos,  bodyScale,  armOffset);
            drawLowerArm(gl,  glu, glut, tAnim,  pos,  bodyScale,  armOffset); 
            drawHand(gl,  glu, glut, tAnim,  pos,  bodyScale,  armOffset); 
        }
        else{
            drawUpperStickFigureArm( gl,  glu,  glut, tAnim,  pos,  bodyScale,  leftArm,  armOffset);
            drawLowerStickFigureArm( gl,  glu,  glut, tAnim,  pos,  bodyScale,  leftArm,  armOffset);
           
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
            legOffset = -0.4*bodyScale;
        }
        else{
            legOffset = 0.4*bodyScale;
        }
        if(!stickFigure){
            drawUpperleg(gl,  glu, glut, tAnim,  pos,  bodyScale,  legOffset);
            drawLowerleg(gl,  glu, glut, tAnim,  pos,  bodyScale,  legOffset); 
            drawFoot(gl,  glu, glut, tAnim,  pos,  bodyScale,  legOffset); 
           
             
            
        }
        else{
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
            gl.glTranslated(pos.x+armOffset, pos.y, (1.8*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
            gl.glPopMatrix();
            //upper arm
            gl.glPushMatrix();
            gl.glTranslated(pos.x+armOffset, pos.y, (1.8*bodyScale)+pos.z-(0.2*bodyScale));
            gl.glScalef(0.15f, 0.15f, 1f);
            float s = (float)bodyScale;
            glut.glutSolidCube(0.4f*s);
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
            gl.glTranslated(pos.x+armOffset, pos.y, (1.4*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
            gl.glPopMatrix();
            //lower arm
            gl.glPushMatrix();
            gl.glTranslated(pos.x+armOffset, pos.y+0.2*bodyScale, (1.4*bodyScale)+pos.z);
            gl.glScalef(0.15f, 1f, 0.15f);
            float s = (float)bodyScale;
            glut.glutSolidCube(0.4f*s);
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
            gl.glTranslated(pos.x+legOffset, pos.y, (1*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
            gl.glPopMatrix();
            //upper leg
            gl.glPushMatrix();
            gl.glTranslated(pos.x+legOffset, pos.y, (1*bodyScale)+pos.z-(0.25*bodyScale));
            gl.glScalef(0.15f, 0.15f, 1f);
            float s = (float)bodyScale;
            glut.glutSolidCube(0.5f*s);
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
            gl.glTranslated(pos.x+legOffset, pos.y, (0.5*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
            gl.glPopMatrix();
            //lower leg
            gl.glPushMatrix();
            gl.glTranslated(pos.x+legOffset, pos.y, (0.5*bodyScale)+pos.z-(0.25*bodyScale));
            gl.glScalef(0.15f, 0.15f, 1f);
            float s = (float)bodyScale;
            glut.glutSolidCube(0.5f*s);
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
            gl.glTranslated(pos.x+legOffset, pos.y+0.1*bodyScale, (0.01*bodyScale)+pos.z);
            gl.glScalef(0.15f, 1f, 0.15f);
            float s = (float)bodyScale;
            glut.glutSolidCube(0.5f*s);
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
            gl.glTranslated(pos.x, pos.y, (2.2*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.2f*bodyScale, RobotRace.slices, RobotRace.slices);
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
            gl.glTranslated(pos.x+armOffset, pos.y, (1.8*bodyScale)+pos.z);
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
            gl.glPopMatrix();
            //draw upper arm
            gl.glPushMatrix();
            gl.glColor3f(0.5f, 0.0f, 1.0f);
            gl.glTranslated(pos.x+armOffset, pos.y, (1.6*bodyScale)+pos.z); 
            gl.glScalef(1f, 1f, 2f);
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
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
            gl.glTranslated(pos.x+armOffset, pos.y, (1.4*bodyScale)+pos.z);
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
            gl.glPopMatrix();
            //draw lower arm
            gl.glPushMatrix();
            gl.glColor3f(0.5f, 0.0f, 1.0f);
            gl.glTranslated(pos.x+armOffset, pos.y+0.2*bodyScale, (1.4*bodyScale)+pos.z); 
            gl.glScalef(1f, 2f, 1f);
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
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
            gl.glTranslated(pos.x+armOffset, pos.y+0.4*bodyScale, (1.4*bodyScale)+pos.z);
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
            gl.glPopMatrix();
    };
    
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
            gl.glTranslated(pos.x+legOffset, pos.y, (1*bodyScale)+pos.z);
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
            gl.glPopMatrix();
            //draw upper leg
            gl.glPushMatrix();
            gl.glColor3f(0.5f, 0.0f, 1.0f);
            gl.glTranslated(pos.x+legOffset, pos.y, (0.7*bodyScale)+pos.z); 
            gl.glScalef(1f, 1f, 2.5f);
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
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
            gl.glTranslated(pos.x+legOffset, pos.y, (0.5*bodyScale)+pos.z);
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
            gl.glPopMatrix();
            
            //draw lower leg
            gl.glPushMatrix();
            gl.glColor3f(0.5f, 0.0f, 1.0f);
            gl.glTranslated(pos.x+legOffset, pos.y, (0.25*bodyScale)+pos.z); 
            gl.glScalef(1f, 1f, 2.5f);
            glut.glutSolidSphere(0.1f*bodyScale, RobotRace.slices, RobotRace.slices);
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
            gl.glTranslated(pos.x+legOffset, pos.y+0.1*bodyScale, (0.1*bodyScale)+pos.z);
            gl.glScalef(0.4f, 1f, 0.4f);
            float s = (float)bodyScale;
            glut.glutSolidCube(0.5f*s);
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
       gl.glTranslated(pos.x, pos.y, (2.2*bodyScale)+pos.z);
       glut.glutSolidSphere(0.2f*bodyScale, RobotRace.slices, RobotRace.slices);
       gl.glPopMatrix();
       
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
       
        //right eye
        gl.glPushMatrix();
        gl.glColor3f(1f, 0f, 0f);
        gl.glTranslated(pos.x+0.1*bodyScale, pos.y+0.2*bodyScale, (2.2*bodyScale)+pos.z);
        glut.glutSolidSphere(0.05f*bodyScale, RobotRace.slices, RobotRace.slices);
        gl.glPopMatrix();
       
        //left eye
        gl.glPushMatrix();
        gl.glColor3f(1f, 0f, 0f);
        gl.glTranslated(pos.x-0.1*bodyScale, pos.y+0.2*bodyScale, (2.2*bodyScale)+pos.z);
        glut.glutSolidSphere(0.05f*bodyScale, RobotRace.slices, RobotRace.slices);
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
       
        gl.glPushMatrix();
        gl.glColor3f(0.2f, 0f, 0.2f);
        gl.glTranslated(pos.x+0.25*bodyScale, pos.y, (2.4*bodyScale)+pos.z);
        glut.glutSolidSphere(0.2f*bodyScale, RobotRace.slices, RobotRace.slices);
        gl.glPopMatrix();
      
        gl.glPushMatrix();
        gl.glColor3f(0.2f, 0f, 0.2f);
        gl.glTranslated(pos.x-0.25*bodyScale, pos.y, (2.4*bodyScale)+pos.z);
        glut.glutSolidSphere(0.2f*bodyScale, RobotRace.slices, RobotRace.slices);
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
       gl.glTranslated(pos.x, pos.y, (1.7*bodyScale)+pos.z);                
       glut.glutSolidSphere(0.4f*bodyScale, RobotRace.slices, RobotRace.slices);
       gl.glPopMatrix();
       
       //Draw shoulders
       gl.glPushMatrix();
       float s = (float)bodyScale;
       gl.glColor3f(0.5f, 0, 1f);
       gl.glTranslated(pos.x, pos.y, (1.8*bodyScale)+pos.z);
       gl.glScalef(1f, 0.1f, 0.1f);
       glut.glutSolidCube(1f*s);
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
        gl.glTranslated(pos.x,pos.y,pos.z+bodyScale);                    //middle of bottom circle equals 1
        glut.glutSolidSphere(0.3f*bodyScale, RobotRace.slices, RobotRace.slices);
        gl.glPopMatrix();

        //middle cone
        gl.glPushMatrix();
        gl.glRotatef(180, 1f, 0, 0);
        gl.glTranslated(pos.x, -pos.y, (-1.45*bodyScale)-pos.z);
        gl.glColor3f(0.5f, 0, 1f);
        glut.glutSolidCone(0.3f*bodyScale, 0.45f*bodyScale, RobotRace.slices, RobotRace.slices);    //height of cone equals 0.45
        gl.glPopMatrix();
       
        //Draw hips
        gl.glPushMatrix();
        gl.glTranslated(pos.x, pos.y, (1*bodyScale)+pos.z);
        gl.glScalef(0.75f, 0.1f, 0.1f);
        float s = (float)bodyScale;
        glut.glutSolidCube(1f*s);
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
        gl.glTranslated(pos.x, pos.y, (1.4*bodyScale)+pos.z);
        gl.glScalef(0.05f, 0.05f, 1f);
        float s = (float)bodyScale;
        glut.glutSolidCube(1.4f*s);
        gl.glPopMatrix();
            
        //shoulders
        gl.glPushMatrix();
        gl.glTranslated(pos.x, pos.y, (1.8*bodyScale)+pos.z);
        gl.glScalef(1f, 0.05f, 0.05f);
        glut.glutSolidCube(1f*s);
        gl.glPopMatrix();
        
        //hips
        gl.glPushMatrix();
        gl.glTranslated(pos.x, pos.y, (1*bodyScale)+pos.z);
        gl.glScalef(0.75f, 0.05f, 0.05f);
        glut.glutSolidCube(1f*s);
        gl.glPopMatrix();
   };
   
   //Set the material properties from the material enum and apply them to the 
   //current robot.
   public void setMaterial(GL2 gl){
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, material.diffuse, 0);
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, material.specular, 0);
        gl.glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, material.shininess);
   }
  
   
}
