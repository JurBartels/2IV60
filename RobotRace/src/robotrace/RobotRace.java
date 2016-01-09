package robotrace;

import javax.media.opengl.GL;
import static javax.media.opengl.GL2.*;
import java.nio.FloatBuffer;

/**
 * Handles all of the RobotRace graphics functionality,
 * which should be extended per the assignment.
 * 
 * OpenGL functionality:
 * - Basic commands are called via the gl object;
 * - Utility commands are called via the glu and
 *   glut objects;
 * 
 * GlobalState:
 * The gs object contains the GlobalState as described
 * in the assignment:
 * - The camera viewpoint angles, phi and theta, are
 *   changed interactively by holding the left mouse
 *   button and dragging;
 * - The camera view width, vWidth, is changed
 *   interactively by holding the right mouse button
 *   and dragging upwards or downwards;
 * - The center point can be moved up and down by
 *   pressing the 'q' and 'z' keys, forwards and
 *   backwards with the 'w' and 's' keys, and
 *   left and right with the 'a' and 'd' keys;
 * - Other settings are changed via the menus
 *   at the top of the screen.
 * 
 * Textures:
 * Place your "track.jpg", "brick.jpg", "head.jpg",
 * and "torso.jpg" files in the same folder as this
 * file. These will then be loaded as the texture
 * objects track, bricks, head, and torso respectively.
 * Be aware, these objects are already defined and
 * cannot be used for other purposes. The texture
 * objects can be used as follows:
 * 
 * gl.glColor3f(1f, 1f, 1f);
 * track.bind(gl);
 * gl.glBegin(GL_QUADS);
 * gl.glTexCoord2d(0, 0);
 * gl.glVertex3d(0, 0, 0);
 * gl.glTexCoord2d(1, 0);
 * gl.glVertex3d(1, 0, 0);
 * gl.glTexCoord2d(1, 1);
 * gl.glVertex3d(1, 1, 0);
 * gl.glTexCoord2d(0, 1);
 * gl.glVertex3d(0, 1, 0);
 * gl.glEnd(); 
 * 
 * Note that it is hard or impossible to texture
 * objects drawn with GLUT. Either define the
 * primitives of the object yourself (as seen
 * above) or add additional textured primitives
 * to the GLUT object.
 */
public class RobotRace extends Base {
    
    /** Array of the four robots. */
    private final Robot[] robots;
    
    /** Instance of the camera. */
    private final Camera camera;
    
    /** Instance of the race track. */
    private final RaceTrack[] raceTracks;
    
    /** Instance of the terrain. */
    private final Terrain terrain;
    
    public static int slices = 25;
    
    /**
     * Constructs this robot race by initializing robots,
     * camera, track, and terrain.
     */
    public RobotRace() {
        
        // Create a new array of four robots
        robots = new Robot[4];
        
        // Initialize robot 0
        robots[0] = new Robot(Material.GOLD,0.5
            /* add other parameters that characterize this robot */);
        
        // Initialize robot 1
        robots[1] = new Robot(Material.SILVER,0.5
            /* add other parameters that characterize this robot */);
        
        // Initialize robot 2
        robots[2] = new Robot(Material.WOOD,0.5
            /* add other parameters that characterize this robot */);

        // Initialize robot 3
        robots[3] = new Robot(Material.ORANGE,0.5
            /* add other parameters that characterize this robot */);
        
        // Initialize the camera
        camera = new Camera();
        
        // Initialize the race tracks
        raceTracks = new RaceTrack[5];
        
        // Test track
        raceTracks[0] = new RaceTrack();
        double c = 0.551915024494;
        // O-track
        raceTracks[1] = new RaceTrack(new Vector[] {
            /* add control points like:
            */
            /*
            new Vector(-12, -12, 2),
            new Vector(-12, -4, 2),
            new Vector(-12, 4, 2),
            new Vector(-12, 12, 2),
            new Vector(-12, 24, 2),
            new Vector(12, 24, 2),
            new Vector(12, 12, 2),
            new Vector(12, 4, 2),
            new Vector(12, -4, 2),
            new Vector(12, -12, 2),
            new Vector(12, -24, 2),
            new Vector(-12, -24, 2),
            new Vector(-12, -12, 2) 
            */
            new Vector(0, 12, 1), new Vector(12*c, 12, 1), new Vector(12, 12*c, 1), new Vector(12, 0, 1),
            new Vector(12, 0, 1), new Vector(12, -12*c, 1), new Vector(12*c, -12, 1), new Vector(0, -12, 1),
            new Vector(0, -12, 1), new Vector(-12*c, -12, 1), new Vector(-12, -12*c, 1), new Vector(-12, 0, 1),
            new Vector(-12, 0, 1), new Vector(-12, 12*c, 1), new Vector(-12*c, 12, 1), new Vector(0, 12, 1)
        });
        
        // L-track
        raceTracks[2] = new RaceTrack(new Vector[] { 
            /* add control points */
            new Vector(-7.5, 7.5, 2),
            new Vector(-7.5, 12.5, 2),
            new Vector(0, 12.5, 2),
            new Vector(0, 7.5, 2),
            new Vector(0, 2.5, 2),
            new Vector(2.5, 0, 2),
            new Vector(7.5, 0, 2),
            new Vector(12.5, 0, 2),
            new Vector(12.5, -7.5, 2),
            new Vector(7.5, -7.5, 2),
            new Vector(5, -7.5, 2),
            new Vector(2.5, -7.5, 2),
            new Vector(0, -7.5, 2),
            new Vector(-7.5, -7.5, 2),
            new Vector(-7.5, -7.5, 2),
            new Vector(-7.5, 0, 2),
            new Vector(-7.5, 2.5, 2),
            new Vector(-7.5, 5, 2),
            new Vector(-7.5, 7.5, 2),
        });
        
        // C-track
        raceTracks[3] = new RaceTrack(new Vector[] { 
            /* add control points */
        });
        
        // Custom track
        raceTracks[4] = new RaceTrack(new Vector[] { 
           /* add control points */
        });
        
        // Initialize the terrain
        terrain = new Terrain();
    }
    
    /**
     * Called upon the start of the application.
     * Primarily used to configure OpenGL.
     */
    @Override
    public void initialize() {
		
        // Enable blending.
        gl.glEnable(GL_BLEND);
        gl.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
                
        // Enable depth testing.
        gl.glEnable(GL_DEPTH_TEST);
        gl.glDepthFunc(GL_LESS);
		
	    // Normalize normals.
        gl.glEnable(GL_NORMALIZE);
        
        // Enable textures. 
        gl.glEnable(GL_TEXTURE_2D);
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
        gl.glBindTexture(GL_TEXTURE_2D, 0);
		
	    // Try to load four textures, add more if you like.
        track = loadTexture("track.jpg");
        brick = loadTexture("brick.jpg");
        head = loadTexture("head.jpg");
        torso = loadTexture("torso.jpg");
        
        
        //lighting
        //enable shading
        gl.glShadeModel(GL_SMOOTH);
        gl.glEnable(GL_LIGHTING);
        //gl.glEnable(GL_COLOR_MATERIAL);
        gl.glEnable(GL_LIGHT0);
        
   
        float[] lightAmbient = { 0.5f, 0.5f, 0.5f, 1f};
        gl.glLightfv(GL_LIGHT0, GL_AMBIENT,lightAmbient, 0);
         
        float lightpos[] = {(float)(-1*(Math.tan(Math.PI/18))), (float)(1*(Math.tan(Math.PI/18))),1f,0f};
        gl.glLightfv(GL_LIGHT0,GL_POSITION,lightpos,0);
        
        float lightdiff[] = {1f,1f,1f,1};
        gl.glLightfv(GL_LIGHT0,GL_DIFFUSE,lightdiff,0);
        
    }
    
    /**
     * Configures the viewing transform.
     */
    @Override
    public void setView() {
        // Select part of window.
        gl.glViewport(0, 0, gs.w, gs.h);
        
        // Set projection matrix.
        gl.glMatrixMode(GL_PROJECTION);
        gl.glLoadIdentity();

        // Set the perspective.
        // Modify this to meet the requirements in the assignment.
        //glu.gluPerspective(40, (float)gs.w / (float)gs.h, 0.1, 100);
        glu.gluPerspective(40, (float)gs.w / (float)gs.h, 0.1*gs.vDist, 10.0*gs.vDist);
        
        // Set camera.
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity();
               
        // Update the view according to the camera mode and robot of interest.
        // For camera modes 1 to 4, determine which robot to focus on.
        camera.update(gs, robots[0]);
        glu.gluLookAt(camera.eye.x(),    camera.eye.y(),    camera.eye.z(),
                      camera.center.x(), camera.center.y(), camera.center.z(),
                      camera.up.x(),     camera.up.y(),     camera.up.z());
    }
    
    /**
     * Draws the entire scene.
     */
    @Override
    public void drawScene() {
        // Background color.
        gl.glClearColor(1f, 1f, 1f, 0f);
        
        // Clear background.
        gl.glClear(GL_COLOR_BUFFER_BIT);
        
        // Clear depth buffer.
        gl.glClear(GL_DEPTH_BUFFER_BIT);
        
        // Set color to black.
        gl.glColor3f(0f, 0f, 0f);
        
        gl.glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
        
        // Draw the axis frame.
        if (gs.showAxes) {
            gl.glEnable(GL_COLOR_MATERIAL);
            drawAxisFrame();
            gl.glDisable(GL_COLOR_MATERIAL);
        }
        
        // Get the position and direction of the first robot.
        //if(gs.tAnim < 10){
            if(gs.tAnim<5){
                posUpdate(-2,0,1);
                posUpdate(-1,1,1);
                posUpdate(1,2,1);
                posUpdate(2,3,1);
            }
            if(gs.tAnim<6){
                posUpdate(-2,0,2);
                posUpdate(-1,1,1);
                posUpdate(1,2,1);
                posUpdate(2,3,1);
            }
            if(gs.tAnim<7){
                posUpdate(-2,0,3);
                posUpdate(-1,1,0.9);
                posUpdate(1,2,1);
                posUpdate(2,3,1);
            }
            if(gs.tAnim<8){
                posUpdate(-2,0,4);
                posUpdate(-1,1,1);
                posUpdate(1,2,1);
                posUpdate(2,3,1);
            }
            if(gs.tAnim<9){
                posUpdate(-2,0,5);
                posUpdate(-1,1,1);
                posUpdate(1,2,1);
                posUpdate(2,3,1);
            }
            if(gs.tAnim<10){
                posUpdate(-2,0,6);
                posUpdate(-1,1,1);
                posUpdate(1,2,1);
                posUpdate(2,3,1);
            }
            else{
                posUpdate(-2,0,6);
                posUpdate(-1,1,1);
                posUpdate(1,2,1);
                posUpdate(2,3,1);
            }
            
            
         
        //}else{
            
        //};
       
        // Draw the first robot.
            robots[0].draw(gl, glu, glut, gs.showStick, gs.tAnim);
            robots[1].draw(gl, glu, glut, gs.showStick, gs.tAnim);
            robots[2].draw(gl, glu, glut, gs.showStick, gs.tAnim);
            robots[3].draw(gl, glu, glut, gs.showStick, gs.tAnim);
        
        // Draw the race track.
        raceTracks[gs.trackNr].draw(gl, glu, glut,gs);
        
        // Draw the terrain.
        terrain.draw(gl, glu, glut);
        
        
    }
    
    /**
     * Draws the x-axis (red), y-axis (green), z-axis (blue),
     * and origin (yellow).
     */
    public void drawAxisFrame() {
        // code goes here ...
        gl.glColor3f(1.0f, 1.0f, 0.0f);     //set color to yellow
        glut.glutSolidSphere(0.1,slices,slices);    //create sphere in origin, radius 0.1 and slices slices and stacks
        
        //z direction cube
        gl.glColor3f(0.0f, 0.0f, 1.0f);     //set color to blue (for z axis)
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, 0.5f);        //move cube in z direction, since total length is 1 the middle of the cube should be at 0.5
        gl.glScalef(0.02f, 0.02f, 0.9f);    //scale x and y such that the cube is smaller
        glut.glutSolidCube(1.0f);           //scaled, translated cube
        gl.glPopMatrix();
        //z direction cone
        gl.glPushMatrix();
        gl.glTranslatef(0, 0, 0.8f);        //move the cone 0.8 in z, since the length of cone is 0.2 this results in lenght 1
        glut.glutSolidCone(0.05f, 0.2f, slices, slices);
        gl.glPopMatrix();
        
        //x direction cube
        gl.glColor3f(1.0f, 0.0f, 0.0f);     //set color to red (for x axis)
        gl.glPushMatrix();
        gl.glTranslatef(0.5f, 0, 0);        //move in x direction, since total length is 1 the middle of the cube should be at 0.5
        gl.glScalef(0.9f, 0.02f, 0.02f);    //scale y and z such that the cube is smaller
        glut.glutSolidCube(1.0f);           //scaled, translated, rotated cube
        gl.glPopMatrix();
        //x direction cone
        gl.glPushMatrix();
        gl.glTranslatef(0.8f, 0, 0);        //move the cone 0.8 in x, since the length of cone is 0.2 this results in lenght 1
        gl.glRotatef(90, 0, 1.0f, 0);       //rotate the cone 90 degrees in y direction
        glut.glutSolidCone(0.05f, 0.2f, slices, slices);
        gl.glPopMatrix();
        
        //y direction cube
        gl.glColor3f(0.0f, 1.0f, 0.0f);     //set color to green (for y axis)
        gl.glPushMatrix();
        gl.glTranslatef(0, 0.5f, 0);        //move in y direction, since total length is 1 the middle of the cube should be at 0.5
        gl.glScalef(0.02f, 0.9f, 0.02f);    //scale x and z such that the cube is smaller
        glut.glutSolidCube(1.0f);           //scaled, translated,rotated cube
        gl.glPopMatrix();
        //y direction cone
        gl.glPushMatrix();
        gl.glTranslatef(0, 0.8f, 0);        //move the cone 0.8 in y, since the length of cone is 0.2 this results in lenght 1
        gl.glRotatef(270, 1.0f, 0, 0);       //rotate the cone 90 degrees in x direction
        glut.glutSolidCone(0.05f, 0.2f, slices, slices);
        gl.glPopMatrix();
        
        gl.glColor3f(0.0f, 0.0f, 0.0f);     //set color to black as it was before
        
        
        
    }
    
    
    public void posUpdate(int t,int i, double speed){
        double baseSpeed = 100;
        robots[i].pos = raceTracks[gs.trackNr].getLanePoint(t, (gs.tAnim/(baseSpeed-speed))-(Math.floor(gs.tAnim/(baseSpeed-speed))));
        robots[i].direction = raceTracks[gs.trackNr].getLaneTangent(t, (gs.tAnim/(baseSpeed-speed))-(Math.floor(gs.tAnim/(baseSpeed-speed))));
        
    }   
 
    /**
     * Main program execution body, delegates to an instance of
     * the RobotRace implementation.
     */
    public static void main(String args[]) {
        RobotRace robotRace = new RobotRace();
        robotRace.run();
    } 
    //test
}
