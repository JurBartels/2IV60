package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

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
    private final double bodyScale = 1;
    private Vector pos; 
    private double facing;
    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material,Vector pos
        /* add other parameters that characterize this robot */) {
        this.material = material;
        this.pos = pos;
        this.facing = 0;
            // code goes here ...
    }
    

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim) {
        drawTorso(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale);
        drawArm(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, true);
        drawArm(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, false);
        drawLeg(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, true);
        drawLeg(gl, glu, glut, stickFigure, tAnim, this.pos, bodyScale, false);
        
    }
    
    public void drawTorso(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale){
        if(!stickFigure){
            gl.glPushMatrix();
            gl.glTranslated(pos.x,pos.y,pos.z+bodyScale);                    //middle of bottom circle equals 1
            glut.glutSolidSphere(0.3f*bodyScale, 50, 50);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glRotatef(180, 1f, 0, 0);
            gl.glTranslated(pos.x, -pos.y, (-1.45*bodyScale)-pos.z);
            gl.glColor3f(0.5f, 0, 1f);
            glut.glutSolidCone(0.3f*bodyScale, 0.45f*bodyScale, 50, 50);    //height of cone equals 0.45
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(pos.x, pos.y, (1.7*bodyScale)+pos.z);                //middle of top circle equals 1.7, so top equals 2.1
            glut.glutSolidSphere(0.4f*bodyScale, 50, 50);
            gl.glPopMatrix();
            
            //Draw shoulders
            gl.glPushMatrix();
            float s = (float)bodyScale;
            gl.glColor3f(0.5f, 0, 1f);
            gl.glTranslated(pos.x, pos.y, (1.8*bodyScale)+pos.z);
            gl.glScalef(1f, 0.1f, 0.1f);
            glut.glutSolidCube(1f*s);
            gl.glPopMatrix();
            
            //Draw hips
            gl.glPushMatrix();
            gl.glTranslated(pos.x, pos.y, (1*bodyScale)+pos.z);
            gl.glScalef(0.75f, 0.1f, 0.1f);
            glut.glutSolidCube(1f*s);
            gl.glPopMatrix();
        }
        else{
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
            drawStickFigureHead(gl,  glu,  glut, stickFigure,  tAnim,  pos,  bodyScale);
        }
    }
    //Draws the right or left arm depending on leftArm
    public void drawArm(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale, boolean leftArm){
        double armOffset;
        if(leftArm){
            armOffset = -0.5*bodyScale;
        }
        else{
            armOffset = 0.5*bodyScale;
        }
        if(!stickFigure){
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(pos.x+armOffset, pos.y, (1.8*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.1f*bodyScale, 50, 50);
            gl.glPopMatrix();
             
            
        }
        else{
            drawUpperStickFigureArm( gl,  glu,  glut, stickFigure,  tAnim,  pos,  bodyScale,  leftArm,  armOffset);
            drawLowerStickFigureArm( gl,  glu,  glut, stickFigure,  tAnim,  pos,  bodyScale,  leftArm,  armOffset);
           
        }
    
    }
    
       public void drawLeg(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale, boolean leftLeg){
        double legOffset;
        if(leftLeg){
            legOffset = -0.4*bodyScale;
        }
        else{
            legOffset = 0.4*bodyScale;
        }
        if(!stickFigure){
            //hip joint
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(pos.x+legOffset, pos.y, (1*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.1f*bodyScale, 50, 50);
            gl.glPopMatrix();
           
             
            
        }
        else{
            drawUpperStickFigureLeg( gl,  glu,  glut, stickFigure,  tAnim,  pos,  bodyScale,  leftLeg,  legOffset);
            drawLowerStickFigureLeg( gl,  glu,  glut, stickFigure,  tAnim,  pos,  bodyScale,  leftLeg,  legOffset);
            drawLowerStickFigureFoot( gl,  glu,  glut, stickFigure,  tAnim,  pos,  bodyScale,  leftLeg,  legOffset);
        }
    
    }
    public void drawUpperStickFigureArm(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale, boolean leftArm, double armOffset){
            //shoulder joint
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(pos.x+armOffset, pos.y, (1.8*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.1f*bodyScale, 50, 50);
            gl.glPopMatrix();
            //upper arm
            gl.glPushMatrix();
            gl.glTranslated(pos.x+armOffset, pos.y, (1.8*bodyScale)+pos.z-(0.2*bodyScale));
            gl.glScalef(0.15f, 0.15f, 1f);
            float s = (float)bodyScale;
            glut.glutSolidCube(0.4f*s);
            gl.glPopMatrix();
    }
    
     public void drawLowerStickFigureArm(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale, boolean leftArm, double armOffset){
            //elbow joint
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(pos.x+armOffset, pos.y, (1.4*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.1f*bodyScale, 50, 50);
            gl.glPopMatrix();
            //lower arm
            gl.glPushMatrix();
            gl.glTranslated(pos.x+armOffset, pos.y+0.2*bodyScale, (1.4*bodyScale)+pos.z);
            gl.glScalef(0.15f, 1f, 0.15f);
            float s = (float)bodyScale;
            glut.glutSolidCube(0.4f*s);
            gl.glPopMatrix();
    }
    public void drawUpperStickFigureLeg(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale, boolean leftArm, double legOffset){
            //hip joint
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(pos.x+legOffset, pos.y, (1*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.1f*bodyScale, 50, 50);
            gl.glPopMatrix();
            //upper leg
            gl.glPushMatrix();
            gl.glTranslated(pos.x+legOffset, pos.y, (1*bodyScale)+pos.z-(0.25*bodyScale));
            gl.glScalef(0.15f, 0.15f, 1f);
            float s = (float)bodyScale;
            glut.glutSolidCube(0.5f*s);
            gl.glPopMatrix();
    } 
    public void drawLowerStickFigureLeg(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale, boolean leftArm, double legOffset){
            //knee joint
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(pos.x+legOffset, pos.y, (0.5*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.1f*bodyScale, 50, 50);
            gl.glPopMatrix();
            //lower leg
            gl.glPushMatrix();
            gl.glTranslated(pos.x+legOffset, pos.y, (0.5*bodyScale)+pos.z-(0.25*bodyScale));
            gl.glScalef(0.15f, 0.15f, 1f);
            float s = (float)bodyScale;
            glut.glutSolidCube(0.5f*s);
            gl.glPopMatrix();
    } 
   public void drawLowerStickFigureFoot(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale, boolean leftArm, double legOffset){
            //foot
            gl.glPushMatrix();
            gl.glTranslated(pos.x+legOffset, pos.y+0.1*bodyScale, (0.01*bodyScale)+pos.z);
            gl.glScalef(0.15f, 1f, 0.15f);
            float s = (float)bodyScale;
            glut.glutSolidCube(0.5f*s);
            gl.glPopMatrix();
    } 
    public void drawStickFigureHead(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale){
            //head
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(pos.x, pos.y, (2.2*bodyScale)+pos.z);               
            glut.glutSolidSphere(0.2f*bodyScale, 50, 50);
            gl.glPopMatrix();
    }  
}
