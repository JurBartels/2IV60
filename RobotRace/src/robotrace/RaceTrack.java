package robotrace;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL2;
import javax.media.opengl.glu.GLU;
import static javax.media.opengl.GL2.*;

/**
 * Implementation of a race track that is made from Bezier segments.
 */
class RaceTrack {

    /**
     * The width of one lane. The total width of the track is 4 * laneWidth.
     */
    private final static float laneWidth = 1.22f;

    /**
     * Array with 3N control points, where N is the number of segments.
     */
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
    public void draw(GL2 gl, GLU glu, GLUT glut, GlobalState gs) {
        if (null == controlPoints) {
            //NOT using control points
            gl.glEnable(GL_COLOR_MATERIAL);
            //Draw the lanes for the testtrack
            gl.glColor3f(1f, 1f, 1f);                                           //set the color to white
            RobotRace.track.enable(gl);                                         //enable the track texture
            RobotRace.track.bind(gl);                                           //bind the track texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);    //repeat the textures in both directions
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            for (double j = -2; j < 2; j++) {                                   //lanes defined from centerline, -2, -1, 1, 2. at first this was easier since we defined the testtrack from the centerline two lanes inwards and two lanes outwards
                gl.glBegin(GL_TRIANGLE_STRIP);
                double pOffset = 0.01;                                          //set the offset that defines how many triangles are used
                for (double i = 0; i <= 1 + 2*pOffset; i = i + 2 * pOffset) {                                                              
                    Vector a = getTangent(i).cross(new Vector(0, 0, 1));        //vector a is orthogonal to the centerline, such that it can be multiplied by laneWidth to find the next lane
                    a.normalized();                                             //normalize a, such that it can be multiplied by laneWidth
                    a.x = a.x * laneWidth;                                      //multiply a by laneWidth
                    a.y = a.y * laneWidth;
                    gl.glTexCoord2d(0, 0);                                                                              //set the first texture coordinate 
                    gl.glVertex3d(getPoint(i).x + j * a.x, getPoint(i).y + j * a.y, getPoint(i).z);                     //set the first point of the triangle strip in the current lane (j)
                    gl.glTexCoord2d(1, 0);                                                                              //set the second texture coordinate
                    gl.glVertex3d(getPoint(i).x + (j + 1) * a.x, getPoint(i).y + (j + 1) * a.y, getPoint(i).z);         //set the second point of the strip in the next lane (j+1) (we add (j*a.x, j*a.y, 0) to find the point in lane j

                    Vector b;                                                   //now we do the same again with vector b, this is done because the texture map needs four coordinates, thats why we increment i by 2*offset instead of offset
                    b = getTangent(i + pOffset).cross(new Vector(0, 0, 1));     //again b is orthogonal to the centerline such that we can find the correct lane
                    b.normalized();
                    b.x = b.x * laneWidth;                                      //multiply by laneWidth    
                    b.y = b.y * laneWidth;
                    gl.glTexCoord2d(0, 1);                                                                                                      //set the third texture coordinate
                    gl.glVertex3d(getPoint(i + pOffset).x + j * b.x, getPoint(i + pOffset).y + j * b.y, getPoint(i + pOffset).z);               //do the same as above, but increment i
                    gl.glTexCoord2d(1, 1);                                                                                                      //set the final coordinate for the texture
                    gl.glVertex3d(getPoint(i + pOffset).x + (j + 1) * b.x, getPoint(i + pOffset).y + (j + 1) * b.y, getPoint(i + pOffset).z);   //do the same as above, but increment i
                }
                gl.glEnd();                                                     //finish the triangle strip
            }
            RobotRace.track.disable(gl);                                        //disable the track texture

            gl.glColor3f(1f, 1f, 1f);
            RobotRace.brick.enable(gl);                                         //enable the brick texture
            RobotRace.brick.bind(gl);                                           //bind the brick texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);    //repeat the texture in both directions
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            //Inner circle that is located at -2*1.22 from the center line and outer circle that is located 2*1.22 from the centerline
            for (int j = -2; j <= 2; j += 4) {                                  //only use lane curves -2 and 2, these represent the outside lanes
                gl.glBegin(GL_QUADS);                                           //this time quads are used, since the 4 points lay in the same plane
                double pOffset = 0.01;                                          //set the offset, this defines how many quads are used
                for (double i = 0; i < 1; i = i + pOffset) {                    
                    Vector a = getTangent(i).cross(new Vector(0, 0, 1));        //as with the drawing of the tracks above, use vector a to later on find the correct lane
                    a.normalized();
                    a.x = a.x * laneWidth;
                    a.y = a.y * laneWidth;
                    gl.glTexCoord2d(0, 0);                                                                              //set the first texture coordinate
                    gl.glVertex3d(getPoint(i).x + j * a.x, getPoint(i).y + j * a.y, getPoint(i).z - 1);                 //set the first vertex, located at z-1
                    gl.glTexCoord2d(0, 1);                                                                              //set the second texture coordinate
                    gl.glVertex3d(getPoint(i).x + j * a.x, getPoint(i).y + j * a.y, getPoint(i).z);                     //set the second vertex, located at z

                    Vector b = getTangent(i + pOffset).cross(new Vector(0, 0, 1));  //as with the drawing of the tracks use vector b to find the correct lane, this time increment i again
                    b.normalized();
                    b.x = b.x * laneWidth;
                    b.y = b.y * laneWidth;
                    gl.glTexCoord2d(1, 1);                                                                                              //set the texture coordinate
                    gl.glVertex3d(getPoint(i + pOffset).x + j * b.x, getPoint(i + pOffset).y + j * b.y, getPoint(i + pOffset).z);       //set the vertex, at point i with offset and z
                    gl.glTexCoord2d(1, 0);                                                                                              //set texture coordinate
                    gl.glVertex3d(getPoint(i + pOffset).x + j * b.x, getPoint(i + pOffset).y + j * b.y, getPoint(i + pOffset).z - 1);   //set final vertex at point i with offset and z-1
                }
                gl.glEnd();                                                     //finish the quad
            }
            RobotRace.brick.disable(gl);                                        //disable the brick texture                                  
            gl.glDisable(GL_COLOR_MATERIAL);
        } else {                                                                //in this case there are controllpoints, so this is not the test track
            gl.glEnable(GL_COLOR_MATERIAL);
            gl.glColor3f(1f, 1f, 1f);
            RobotRace.track.enable(gl);                                         //enable the track texture
            RobotRace.track.bind(gl);                                           //bind the track texture
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);    //repeat the texture in both directions
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            // draw the spline track
            for (int lane = 0; lane < 4; lane++) {                           //at this point we decided to define the lanes 0 to 3 for easier coding

                gl.glBegin(GL_TRIANGLE_STRIP);                                  //again use a triangle strip for the tracks
                gl.glNormal3d(0, 0, 1);                                         //set the normal pointing upwards
                double pOffset = 0.01;                                          //define an offset that determines how many triangles are drawn
                for (double i = 0; i < 1; i = i + 2*pOffset) {
                    Vector inner = getPointOnCurve(i, lane,false);             //find the vertex at i in the first lane  
                    Vector outer = getPointOnCurve(i, lane + 1,false);

                    gl.glTexCoord2d(0,0);
                    gl.glVertex3d(inner.x(), inner.y(), inner.z());
                    gl.glTexCoord2d(1,0);
                    gl.glVertex3d(outer.x(), outer.y(), outer.z());

                    Vector inner2 = getPointOnCurve(i+pOffset, lane,false);
                    Vector outer2 = getPointOnCurve(i+pOffset, lane + 1,false);

                    gl.glTexCoord2d(0,1);
                    gl.glVertex3d(inner2.x(), inner2.y(), inner2.z());
                    gl.glTexCoord2d(1,1);
                    gl.glVertex3d(outer2.x(), outer2.y(), outer2.z());
                }
                //add last and first point to finish the triangle strip
                Vector inner = getPointOnCurve(.99, lane,false);
                Vector outer = getPointOnCurve(.99, lane + 1,false);
                gl.glTexCoord2d(0,0);
                gl.glVertex3d(inner.x(), inner.y(), inner.z());
                gl.glTexCoord2d(1,0);
                gl.glVertex3d(outer.x(), outer.y(), outer.z());
                
                Vector inner2 = getPointOnCurve(0, lane,false);
                Vector outer2 = getPointOnCurve(0, lane + 1,false);
                gl.glTexCoord2d(0,1);
                gl.glVertex3d(inner2.x(), inner2.y(), inner2.z());
                gl.glTexCoord2d(1,1);
                gl.glVertex3d(outer2.x(), outer2.y(), outer2.z());
                // Finish the triangle strip
                gl.glEnd();
            }
            RobotRace.track.disable(gl);
            
            RobotRace.brick.enable(gl);
            RobotRace.brick.bind(gl);
            //draw a strip downwards for curve 0 and 4, representing the brick wall
            for (int j = 0; j <= 4; j += 4) {
                gl.glBegin(GL_TRIANGLE_STRIP);
                double pOffset = 0.01;
                for (double i = 0; i < 1; i = i + 2*pOffset) {
                    Vector firstTop = getPointOnCurve(i,j,false);
                    Vector firstBottom = new Vector(firstTop.x, firstTop.y, firstTop.z -1);
                    Vector secondTop = getPointOnCurve(i + pOffset,j,false);
                    Vector secondBottom = new Vector(secondTop.x, secondTop.y, secondTop.z -1);
                    //Vector normal = second.subtract(first).cross()
                    
                    gl.glTexCoord2d(0, 1);
                    gl.glVertex3d(firstTop.x, firstTop.y, firstTop.z);
                    gl.glTexCoord2d(0, 0);
                    gl.glVertex3d(firstBottom.x, firstBottom.y, firstBottom.z);

                    gl.glTexCoord2d(1, 1);
                    gl.glVertex3d(secondTop.x, secondTop.y, secondTop.z);
                    gl.glTexCoord2d(1, 0);
                    gl.glVertex3d(secondBottom.x, secondBottom.y, secondBottom.z );
                }
                //add last and first points to finish the triangle strip
                Vector firstTop = getPointOnCurve(0.99,j,false);
                Vector firstBottom = new Vector(firstTop.x, firstTop.y, firstTop.z -1);
                Vector secondTop = getPointOnCurve(0,j,false);
                Vector secondBottom = new Vector(secondTop.x, secondTop.y, secondTop.z -1);
                
                gl.glTexCoord2d(0, 1);
                gl.glVertex3d(firstTop.x, firstTop.y, firstTop.z);
                gl.glTexCoord2d(0, 0);
                gl.glVertex3d(firstBottom.x, firstBottom.y, firstBottom.z);

                gl.glTexCoord2d(1, 1);
                gl.glVertex3d(secondTop.x, secondTop.y, secondTop.z);
                gl.glTexCoord2d(1, 0);
                gl.glVertex3d(secondBottom.x, secondBottom.y, secondBottom.z );
                
            }
            gl.glEnd();
            RobotRace.brick.disable(gl);
            gl.glDisable(GL_COLOR_MATERIAL);
        }
    }

    /**
     * Returns the center of a lane at 0 <= t < 1. Use this method to find the
     * position of a robot on the track.
     */
    public Vector getLanePoint(int lane, double t) {
        if (null == controlPoints) {
            //return Vector.O; // <- code goes here
            Vector point = getPoint(t);
            Vector tangent = getTangent(t);
            Vector normal = tangent.cross(Vector.Z).normalized();
            if (lane > 0) {
                return point.add(normal.scale((lane - 1) * laneWidth + laneWidth / 2));
            } else {
                return point.add(normal.scale((lane + 1) * laneWidth - laneWidth / 2));
            }
        } else {
            //return Vector.O; // <- code goes here

            Vector point;
            if (lane == -2) {
                point = getPointOnCurve(t, 0,true);
                return point;
            }

            if (lane == -1) {
                point = getPointOnCurve(t, 1,true);
                return point;
            }

            if (lane == 1) {
                point = getPointOnCurve(t, 2,true);
                return point;
            }

            if (lane == 2) {
                point = getPointOnCurve(t, 3,true);
                return point;
            }

            return null;

            /*
             Vector point = getCubicBezierPoint(t, );
             Vector tangent = getTangent(t);
             Vector normal = tangent.cross(Vector.Z).normalized();
             if(lane>0){
             return point.add(normal.scale((lane-1)*1.22+0.61)); 
             }
             else{
             return point.add(normal.scale((lane+1)*1.22-0.61));
             }
             */
        }
    }

    /**
     * Returns the tangent of a lane at 0 <= t < 1. Use this method to find the
     * orientation of a robot on the track.
     */
    public Vector getLaneTangent(int lane, double t) {
        if (null == controlPoints) {
            return getTangent(t); // <- code goes here
        } else {
            //each segment has 4 points
            int numberOfSegments = controlPoints.length / 4;
            //map the segments, starting at 0
            int segment = (int) Math.floor(t * numberOfSegments);

            Vector P0 = controlPoints[segment * 3 + segment];
            Vector P1 = controlPoints[segment * 3 + 1 + segment];
            Vector P2 = controlPoints[segment * 3 + 2 + segment];
            Vector P3 = controlPoints[segment * 3 + 3 + segment];
            double bezierT = (t - (((double) segment) / numberOfSegments)) * numberOfSegments;
            Vector tangent = getCubicBezierTangent(bezierT, P0, P1, P2, P3);
            return tangent;
        }
    }

    /**
     * Returns a point on the test track at 0 <= t < 1.
     */
    private Vector getPoint(double t) {
        //return Vector.O; // <- code goes here
        return new Vector((10 * Math.cos(2 * Math.PI * t)), (14 * Math.sin(2 * Math.PI * t)), 1);
    }

    /**
     * Returns a tangent on the test track at 0 <= t < 1.
     */
    private Vector getTangent(double t) {
        //return Vector.O; // <- code goes here
        //differentiate getPoint(t)
        double x = -20 * Math.PI * Math.sin(2 * Math.PI * t);
        double y = 28 * Math.PI * Math.cos(2 * Math.PI * t);
        double z = 0;

        //calculate length to normalize
        double length = Math.sqrt(x * x + y * y);
        x = x / length;
        y = y / length;

        return new Vector(x, y, z);

        //to get unit length do: getTangent(t) = (getPoint'(t)/|getPoint'(t)|)
    }

    /**
     * Returns a point on a bezier segment with control points P0, P1, P2, P3 at
     * 0 <= t < 1.
     */
    private Vector getCubicBezierPoint(double t, Vector P0, Vector P1,
            Vector P2, Vector P3) {
        //P(u) = ((1-u)^3)*P0 + 3u*((1-u)^2)*P1+3(u^2)*(1-u)*(u^3)*P3
        return P0.scale((1 - t) * (1 - t) * (1 - t)).add(P1.scale(3 * t * (1 - t) * (1 - t))).add(P2.scale(3 * t * t * (1 - t))).add(P3.scale(t * t * t));
    }

    /**
     * Returns a tangent on a bezier segment with control points P0, P1, P2, P3
     * at 0 <= t < 1.
     */
    private Vector getCubicBezierTangent(double t, Vector P0, Vector P1,
            Vector P2, Vector P3) {
        //derivative of the getCubicBezierPoint function
        return P1.subtract(P0).scale(3 * (1 - t) * (1 - t)).add(P2.subtract(P1).scale(6 * (1 - t) * t)).add(P3.subtract(P2).scale(3 * t * t));
    }

    public void drawTestTrack() {

    }

    public Vector getPointOnCurve(double t, double curve, boolean robot) {
        //each segment has 4 points
        int numberOfSegments = controlPoints.length / 4;
        //int numberOfSegments = (controlPoints.length - 1) / 3;
        //map the segments, starting at 0
        int segment = (int) Math.floor(t * numberOfSegments);

        Vector P0 = controlPoints[segment * 3 + segment];
        Vector P1 = controlPoints[segment * 3 + 1 + segment];
        Vector P2 = controlPoints[segment * 3 + 2 + segment];
        Vector P3 = controlPoints[segment * 3 + 3 + segment];
        double bezierT = (t - (((double) segment) / numberOfSegments)) * numberOfSegments;
        Vector point = getCubicBezierPoint(bezierT, P0, P1, P2, P3);
        Vector tangent = getCubicBezierTangent(bezierT, P0, P1, P2, P3).scale(-1);
        Vector normal = tangent.cross(Vector.Z).normalized();

        if(robot){
            return point.add(normal.scale((curve) * laneWidth + laneWidth/2));
        }
        else{
            return point.add(normal.scale((curve) * laneWidth));
        }
    }
}
