package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
* Materials that can be used for the robots.
*/
public enum Material {

    /** 
     * Gold material properties.
     * Modify the default values to make it look like gold.
     */
    GOLD (
        new float[] {0.75164f, 0.60648f, 0.22648f, 1.0f},
        new float[] {0.797357f, 0.723991f, 0.208006f, 1.0f},
        80f),

    /**
     * Silver material properties.
     * Modify the default values to make it look like silver.
     */
    SILVER (
       // new float[] {0.8f, 0.8f, 0.8f, 1.0f},
       // new float[] {0.0f, 0.0f, 0.0f, 1.0f},
       // 0f),
        new float[] {0.50754f, 0.50754f, 0.50754f, 1.0f},
        new float[] {0.773911f, 0.773911f, 0.773911f, 1.0f},
        90f),

    /** 
     * Wood material properties.
     * Modify the default values to make it look like wood.
     */
    WOOD (
        new float[] {0.6484375f, 0.40625f, 0.12890625f, 1.0f},
        new float[] {0.01f, 0.01f, 0.01f, 1.0f},
        40f),

    /**
     * Orange material properties.
     * Modify the default values to make it look like orange.
     */
    ORANGE (
        new float[] {1.0f, 0.4f, 0.0f, 1.0f},
        new float[] {0.02f, 0.008f, 0.0f, 1.0f},
        32f);

    /** The diffuse RGBA reflectance of the material. */
    float[] diffuse;

    /** The specular RGBA reflectance of the material. */
    float[] specular;
    
    /** The specular exponent of the material. */
    float shininess;

    /**
     * Constructs a new material with diffuse and specular properties.
     */
    private Material(float[] diffuse, float[] specular, float shininess) {
        this.diffuse = diffuse;
        this.specular = specular;
        this.shininess = shininess;
    }
  
}
