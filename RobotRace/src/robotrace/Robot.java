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

    /**
     * Constructs the robot with initial parameters.
     */
    public Robot(Material material
        /* add other parameters that characterize this robot */) {
        this.material = material;

        // code goes here ...
    }

    /**
     * Draws this robot (as a {@code stickfigure} if specified).
     */
    public void draw(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim) {
        drawTorso(gl, glu, glut, stickFigure, tAnim);
    }
    
    public void drawTorso(GL2 gl, GLU glu, GLUT glut, boolean stickFigure, float tAnim){
        if(!stickFigure){
            gl.glPushMatrix();
            gl.glTranslatef(0,0,1f);                    //middle of bottom circle equals 1
            glut.glutSolidSphere(0.3f, 50, 50);
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glRotatef(180, 1f, 0, 0);
            gl.glTranslatef(0, 0, -1.45f);
            gl.glColor3f(0.5f, 0, 1f);
            glut.glutSolidCone(0.3f, 0.45f, 50, 50);    //height of cone equals 0.45
            gl.glPopMatrix();

            gl.glPushMatrix();
            gl.glColor3f(0.0f, 0.0f, 0.0f);
            gl.glTranslatef(0, 0, 1.7f);                //middle of top circle equals 1.7, so top equals 2.1
            glut.glutSolidSphere(0.4f, 50, 50);
            gl.glPopMatrix();
        }
        else{
            gl.glTranslatef(0, 0, 1.4f);
            gl.glScalef(0.05f, 0.05f, 1f);
            glut.glutSolidCube(1.4f);
        }
    }
}
