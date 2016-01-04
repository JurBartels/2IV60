package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Implementation of the terrain.
 */
class Terrain {

    /**
     * Can be used to set up a display list.
     */
    public Terrain() {
        // code goes here ...
    }

    /**
     * Draws the terrain.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        double dx = 1;
        double dy = 1;
        for(double x = -20; x< 20; x+= dx){
            for(double y = -20; y< 20; y+= dy){
                gl.glEnable(gl.GL_COLOR_MATERIAL);
                gl.glColor3f(0.0f, 0.0f, 0.0f);
                gl.glBegin(gl.GL_TRIANGLES);
                   gl.glVertex3d(x, y+dy, heightAt(x, y+dy));
                   gl.glVertex3d(x, y, heightAt(x, y));
                   gl.glVertex3d(x+dx, y, heightAt(x+dx, y));
                   
                   gl.glVertex3d(x+dx, y+dy, heightAt(x+dx, y+dy));
                   gl.glVertex3d(x+dx, y, heightAt(x+dx, y));
                   gl.glVertex3d(x, y+dy, heightAt(x, y+dy));
                gl.glEnd();
                gl.glDisable(gl.GL_COLOR_MATERIAL);
            } 
        }
    }

    /**
     * Computes the elevation of the terrain at (x, y).
     */
    public double heightAt(double x, double y) {
        double d = 0.6 * Math.cos(0.3 * x + 0.2 * y) + 0.4 * Math.cos(x - 0.5 * y);
        return d; // <- code goes here
    }
}
