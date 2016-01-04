package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
class RaceTrack {
    
    /** The width of one lane. The total width of the track is 4 * laneWidth. */
    private final static float laneWidth = 1.22f;

    /** Array with 3N control points, where N is the number of segments. */
    private Vector[] controlPoints = null;
    
    /**
     * Constructor for the default track.
     */
    public RaceTrack() {
    }
    
    /**
     * Constructor for a spline track.
     */
    public RaceTrack(Vector[] controlPoints) {
        this.controlPoints = controlPoints;
    }

    /**
     * Draws this track, based on the control points.
     */
    public void draw(GL2 gl, GLU glu, GLUT glut) {
        if (null == controlPoints) {
            
            //NOT using control points
            gl.glEnable(gl.GL_COLOR_MATERIAL);
            for(double j= -2; j<3; j++){
                gl.glBegin(gl.GL_LINE_LOOP);
                for(double i=0; i<1; i = i+0.02){
                    Vector a = new Vector(0,0,0);
                    a = getTangent(i).cross(new Vector(0,0,1));
                    a.normalized();
                    a.x = a.x*0.61;
                    a.y = a.y*0.61;
                    gl.glColor3f(1f, 0.0f, 0.0f);
                    gl.glVertex3d(getPoint(i).x+2*j*a.x,getPoint(i).y+2*j*a.y,getPoint(i).z);
                }
                gl.glEnd();
            }
               for(double j= -2; j<2; j++){
                gl.glBegin(gl.GL_TRIANGLE_STRIP);
                double pOffset = 0.0005;
                for(double i=0; i<1; i = i+pOffset){
                    
                    Vector a = new Vector(0,0,0);
                    a = getTangent(i).cross(new Vector(0,0,1));
                    a.normalized();
                    a.x = a.x*0.61;
                    a.y = a.y*0.61;
                    gl.glColor3f(0.5f, 0.0f, 1.0f);
                    gl.glVertex3d(getPoint(i).x+2*j*a.x,getPoint(i).y+2*j*a.y,getPoint(i).z);
                    gl.glVertex3d(getPoint(i+pOffset).x+2*(j+1)*a.x,getPoint(i+pOffset).y+2*(j+1)*a.y,getPoint(i+pOffset).z);
                }
                gl.glEnd();
            }
               
               
               //outer circle strip to z -1
               gl.glBegin(gl.GL_TRIANGLE_STRIP);
                double pOffset = 0.0005;
                for(double i=0; i<1; i = i+pOffset){
                    
                    Vector a = new Vector(0,0,0);
                    a = getTangent(i).cross(new Vector(0,0,1));
                    a.normalized();
                    a.x = a.x*0.61;
                    a.y = a.y*0.61;
                    gl.glColor3f(1f, 0.0f, 0f);
                    gl.glVertex3d(getPoint(i).x+2*2*a.x,getPoint(i).y+2*2*a.y,getPoint(i).z);
                    gl.glVertex3d(getPoint(i+pOffset).x+2*(2)*a.x,getPoint(i+pOffset).y+2*(2)*a.y,getPoint(i+pOffset).z-1);
                }
                gl.glEnd();
               
                //innner circle strip to z -1 
               gl.glBegin(gl.GL_TRIANGLE_STRIP);
                for(double i=0; i<1; i = i+pOffset){
                    
                    Vector a = new Vector(0,0,0);
                    a = getTangent(i).cross(new Vector(0,0,1));
                    a.normalized();
                    a.x = a.x*0.61;
                    a.y = a.y*0.61;
                    gl.glColor3f(0f, 1f, 0f);
                    gl.glVertex3d(getPoint(i).x+2*(-2)*a.x,getPoint(i).y+2*(-2)*a.y,getPoint(i).z);
                    gl.glVertex3d(getPoint(i+pOffset).x+2*(-2)*a.x,getPoint(i+pOffset).y+2*(-2)*a.y,getPoint(i+pOffset).z-1);
                }
                gl.glEnd();
               gl.glDisable(gl.GL_COLOR_MATERIAL);
        } else {
            // draw the spline track
        }
    }
    
    /**
     * Returns the center of a lane at 0 <= t < 1.
     * Use this method to find the position of a robot on the track.
     */
    public  Vector getLanePoint(int lane, double t) {
        if (null == controlPoints) {
            //return Vector.O; // <- code goes here
            Vector point = getPoint(t);
            Vector tangent = getTangent(t);
            Vector normal = tangent.cross(Vector.Z).normalized();
            if(lane>0){
                return point.add(normal.scale((lane-1)*1.22+0.61)); 
            }
            else{
                return point.add(normal.scale((lane+1)*1.22-0.61));
            }
        } else {
            //return Vector.O; // <- code goes here
            return new Vector((10*Math.cos(2*Math.PI*t)),(14*Math.sin(2*Math.PI*t)),1);
        }
    }
    
    
    /**
     * Returns the tangent of a lane at 0 <= t < 1.
     * Use this method to find the orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t) {
        if (null == controlPoints) {
            return getTangent(t); // <- code goes here
        } else {
            return Vector.O; // <- code goes here
        }
    }

    /**
     * Returns a point on the test track at 0 <= t < 1.
     */
    private Vector getPoint(double t) {
        //return Vector.O; // <- code goes here
        return new Vector((10*Math.cos(2*Math.PI*t)),(14*Math.sin(2*Math.PI*t)),1);
    }

    /**
     * Returns a tangent on the test track at 0 <= t < 1.
     */
    private Vector getTangent(double t) {
        //return Vector.O; // <- code goes here
        //differentiate getPoint(t)
        double x = -20*Math.PI*Math.sin(2*Math.PI*t);
        double y = 28*Math.PI*Math.cos(2*Math.PI*t);
        double z = 0;
        
        //calculate length to normalize
        double length = Math.sqrt(x*x+y*y);
        x = x / length;
        y = y / length;
        
        return new Vector(x,y,z);
        
        //to get unit length do: getTangent(t) = (getPoint'(t)/|getPoint'(t)|)
        
    }
    
    /**
     * Returns a point on a bezier segment with control points
     * P0, P1, P2, P3 at 0 <= t < 1.
     */
    private Vector getCubicBezierPoint(double t, Vector P0, Vector P1,
                                                 Vector P2, Vector P3) {
        return Vector.O; // <- code goes here
    }
    
    /**
     * Returns a tangent on a bezier segment with control points
     * P0, P1, P2, P3 at 0 <= t < 1.
     */
    private Vector getCubicBezierTangent(double t, Vector P0, Vector P1,
                                                   Vector P2, Vector P3) {
        return Vector.O; // <- code goes here
    }
    
    public void drawTestTrack(){
    
    };
}
