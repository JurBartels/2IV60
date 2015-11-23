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
        drawTorso(gl, glu, glut, stickFigure, tAnim,this.pos,bodyScale);
        drawArm(gl, glu, glut, stickFigure, tAnim,this.pos,bodyScale,true);
        drawArm(gl, glu, glut, stickFigure, tAnim,this.pos,bodyScale,false);
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
            gl.glColor3f(0.5f, 0, 1f);
            gl.glTranslated(pos.x, pos.y, (1.8*bodyScale)+pos.z);
            gl.glScalef(1f, 0.1f, 0.1f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
            
            //Draw hips
            gl.glPushMatrix();
            gl.glTranslated(pos.x, pos.y, (1*bodyScale)+pos.z);
            gl.glScalef(0.75f, 0.1f, 0.1f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
        }
        else{
            gl.glPushMatrix();
            gl.glTranslated(pos.x, pos.y, (1.4*bodyScale)+pos.z);
            gl.glScalef(0.05f, 0.05f, 1f);
            float s = (float)bodyScale;
            glut.glutSolidCube(1.4f*s);
            gl.glPopMatrix();
            
            gl.glPushMatrix();
            gl.glTranslated(pos.x, pos.y, (1.8*bodyScale)+pos.z);
            gl.glScalef(1f, 0.05f, 0.05f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
            
            gl.glPushMatrix();
            gl.glTranslated(pos.x, pos.y, (1*bodyScale)+pos.z);
            gl.glScalef(0.75f, 0.05f, 0.05f);
            glut.glutSolidCube(1f);
            gl.glPopMatrix();
        }
    }
    //Draws the right or left arm depending on leftArm
    public void drawArm(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim, Vector pos, double bodyScale, boolean leftArm){
        double arm;
        if(leftArm){
            arm = -0.5;
        }
        else{
            arm = 0.5;
        }
        if(!stickFigure){
            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslated(pos.x+arm, pos.y, (1.8*bodyScale)+pos.z);                //middle of top circle equals 1.7, so top equals 2.1
            glut.glutSolidSphere(0.1f*bodyScale, 50, 50);
            gl.glPopMatrix();
        }
    
    }
}
