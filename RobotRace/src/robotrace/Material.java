package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;

/**
* Materials that can be used for the robots.
*/
public enum Material {

    /** 
     * Gold material properties.
     */
    GOLD (
        new float[] {0.247f, 0.199f, 0.074f, 1.0f},
        new float[] {0.75164f, 0.60648f, 0.22648f, 1.0f},
        new float[] {0.797357f, 0.723991f, 0.208006f, 1.0f},
        0.4f),

    /**
     * Silver material properties.
     */
    SILVER (
        new float[] {0.192f,0.192f,0.192f,1.0f},
        new float[] {0.50754f, 0.50754f, 0.50754f, 1.0f},
        new float[] {0.773911f, 0.773911f, 0.773911f, 1.0f},
        0.4f),

    /** 
     * Wood material properties.
     */
    WOOD (
        new float[] {0.0f,0.05f,0.0f,1.0f},    
        new float[] {0.6484375f, 0.40625f, 0.12890625f, 1.0f},
        new float[] {0.01f, 0.01f, 0.01f, 1.0f},
        0.078f),

    /**
     * Orange material properties.
     */
    ORANGE (
        new float[] {0.2f,0.1f,0.0f,1.0f},    
        new float[] {1.0f, 0.5f, 0.0f, 1.0f},
        new float[] {0.02f, 0.008f, 0.0f, 1.0f},
        0.25f);
      
    float[] ambience;

    /** The diffuse RGBA reflectance of the material. */
    float[] diffuse;

    /** The specular RGBA reflectance of the material. */
    float[] specular;
    
    /** The specular exponent of the material. */
    float shininess;

    /**
     * Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] ambience,float[] diffuse, float[] specular, float shininess) {
        this.ambience = ambience;
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
    //Set the material properties from the material enum and apply them to the 
   //current robot.
   public void setMaterial(GL2 gl){
       //set material properties
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_AMBIENT, ambience, 0);
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_DIFFUSE, diffuse, 0);
        gl.glMaterialfv(GL_FRONT_AND_BACK, GL_SPECULAR, specular, 0);
        gl.glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, shininess);
   }
  
}
