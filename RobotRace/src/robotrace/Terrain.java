package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.awt.Color;

/**
 * Implementation of the terrain.
 */
class Terrain {

    private Color[] textureColors = new Color[]{
        Color.BLUE,
        Color.YELLOW,
        Color.GREEN
    };    
    private int texture;
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
        double dx = .5;                                                         //set dx to denote the width of the triangles
        double dy = .5;                                                         //set dy to denote the depth of the triangles
        texture = create1DTexture(gl, textureColors);     
        gl.glBindTexture(GL_TEXTURE_1D, texture);                               //bind the texture to the declared int texture                                  
        gl.glColor3f(1f, 1f, 1f);                                               //set the color to white, otherwise not all colors will be blended in
        for(double x = -20; x< 20; x+= dx){
            for(double y = -20; y< 20; y+= dy){
                //define the points for the first triangle
                Vector p1 = new Vector(x, y, heightAt(x, y));                   //the triangle first consists of the position (x,y)
                Vector p2 = new Vector(x, y+dy, heightAt(x, y+dy));             //and a point that lays dy above y, (x, y+dy)
                Vector p3 = new Vector(x+dx, y, heightAt(x+dx, y));             //and a point that lays dx further x, (x+dx, y)
                
                //define the points for the second triangle     
                Vector p4 = new Vector(x+dx, y+dy, heightAt(x+dx, y+dy));       //the second triangle has the same two corner points
                Vector p5 = new Vector(x, y+dy, heightAt(x, y+dy));             //namely (x, y+dy) and (x+dx, y)
                Vector p6 = new Vector(x+dx, y, heightAt(x+dx, y));             //but has point (x+dx, y+dy) to complete the rectangle
                gl.glBegin(GL_TRIANGLES);
                    //Calculate the normal of the first triangle
                    Vector normal = p3.subtract(p1).cross(p2.subtract(p1));
                    gl.glNormal3d(normal.x(), normal.y(), normal.z());          // Set normal vector of the first triangle
                    //Draw the first triangle
                    gl.glTexCoord1d(map(p1.z()));                               //set a texture coordinate based on the height, where the height (range -1 to 1) is mapped to the 1D texture map (range 0 to 1)
                    gl.glVertex3d(p1.x(), p1.y(), p1.z());                      //set the first vertex to point 1
                    gl.glTexCoord1d(map(p2.z()));                               //set a texture coordinate
                    gl.glVertex3d(p2.x(), p2.y(), p2.z());                      //set the second vertex to point 2
                    gl.glTexCoord1d(map(p3.z()));                               //set a texture coordinate
                    gl.glVertex3d(p3.x(), p3.y(), p3.z());                      //set the third vertex to point 3
                    
                    //Calculate the normal of the first triangle
                    Vector normal2 = p5.subtract(p4).cross(p6.subtract(p4));
                    gl.glNormal3d(normal2.x(), normal2.y(), normal2.z());       // Set normal vector of the first triangle
                    //Draw the second triangle
                    gl.glTexCoord1d(map(p4.z()));                               //set a texture coordinate   
                    gl.glVertex3d(p4.x(), p4.y(), p4.z());                      //set the first vertex to point 4
                    gl.glTexCoord1d(map(p5.z()));                               //set a texture coordinate    
                    gl.glVertex3d(p5.x(), p5.y(), p5.z());                      //set the second vertex to point 5
                    gl.glTexCoord1d(map(p6.z()));                               //set a texture coordinate
                    gl.glVertex3d(p6.x(), p6.y(), p6.z());                      //set the third vertex to point 6
               gl.glEnd();
            }  
        }
        //Draw the water surface at z = 0
        gl.glEnable(GL_COLOR_MATERIAL);
        gl.glColor4f(0.75f, 0.75f, 0.75f, 0.5f);                                //set rgba color to gray with 50% opacity
        gl.glEnable(GL_BLEND);                                                  //enable blend
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);                   //use Alpha from RGBA for opacity, in this case 50%
        gl.glBegin(GL_QUADS);
            gl.glVertex3d(-20, -20, 0);                                         //use the outer 4 points of the terrain, namely (-20, -20)
            gl.glVertex3d(20, -20, 0);                                          //second point (20,-20)
            gl.glVertex3d(20, 20, 0);                                           //third point (20,20)
            gl.glVertex3d(-20, 20, 0);                                          //fourth point (-20,20), all z values are 0
        gl.glEnd();
        gl.glDisable(GL_BLEND);
        gl.glDisable(GL_COLOR_MATERIAL);
        gl.glBindTexture(GL_TEXTURE_1D, 0);
    }
   
    public double map(double a){
        return 0.5 + a/2;                                                       //map the height of the terrain (-1 to 1) to the range of the 1D texture (0 to 1)
    }

    /**
     * Computes the elevation of the terrain at (x, y).
     */
    public double heightAt(double x, double y) {           
        return 0.6*Math.cos(0.3*x+0.2*y)+0.4*Math.cos(x-0.5*y);                 //using the height function given in the assignment description
    }
    
    /**
    * Creates a new 1D - texture.
    * @param gl
    * @param colors
    * @return the texture ID for the generated texture.
    */
    public int create1DTexture(GL2 gl, Color[] colors){                         //function given in the hints, tips and tricks section
        gl.glDisable(GL_TEXTURE_2D);
        gl.glEnable(GL_TEXTURE_1D);
        int[] texid = new int[]{-1};
        gl.glGenTextures(1, texid, 0);
        ByteBuffer bb = ByteBuffer.allocateDirect(colors.length * 4).order(ByteOrder.nativeOrder());
        
        for (Color color : colors) {
           int pixel = color.getRGB();
           bb.put((byte) ((pixel >> 16) & 0xFF)); // Red component
           bb.put((byte) ((pixel >> 8) & 0xFF));  // Green component
           bb.put((byte) (pixel & 0xFF));         // Blue component
           bb.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component
        }
        bb.flip();
        gl.glBindTexture(GL_TEXTURE_1D, texid[0]);
        gl.glTexImage1D(GL_TEXTURE_1D, 0, GL_RGBA8, colors.length, 0, GL_RGBA, GL_UNSIGNED_BYTE, bb);
        gl.glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        gl.glTexParameteri(GL_TEXTURE_1D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        gl.glBindTexture(GL_TEXTURE_1D, 0);
        return texid[0];
    }
}
