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
        double dx = 0.5;
        double dy = 0.5;
        for(double x = -20; x< 20; x+= dx){
            for(double y = -20; y< 20; y+= dy){
                
                
            } 
        }
    }

    /**
     * Computes the elevation of the terrain at (x, y).
     */
    public float heightAt(float x, float y) {
        double d = 0.6 * Math.cos(0.3 * x + 0.2 * y) + 0.4 * Math.cos(x - 0.5 * y);
        float f = (float)d;
        return f; // <- code goes here
    }
}
