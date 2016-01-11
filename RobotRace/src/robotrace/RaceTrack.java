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
            for (double j = -2; j < 3; j++) {
                gl.glBegin(GL_LINE_LOOP);
                for (double i = 0; i < 1; i = i + 0.02) {
                    Vector a;
                    a = getTangent(i).cross(new Vector(0, 0, 1));
                    a.normalized();
                    a.x = a.x * laneWidth;
                    a.y = a.y * laneWidth;
                    gl.glColor3f(1f, 0.0f, 0.0f);
                    gl.glVertex3d(getPoint(i).x + j * a.x, getPoint(i).y + j * a.y, getPoint(i).z);
                }
                gl.glEnd();
            }
            //Draw the lanes for the testtrack
            gl.glColor3f(1f, 1f, 1f);
            //gl.glEnable(GL_TEXTURE_2D);
            RobotRace.track.enable(gl);
            RobotRace.track.bind(gl);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            for (double j = -2; j < 2; j++) {
                gl.glBegin(GL_TRIANGLE_STRIP);
                double pOffset = 0.01;
                for (double i = 0; i <= 1 + 2*pOffset; i = i + 2 * pOffset) {

                    Vector a;
                    a = getTangent(i).cross(new Vector(0, 0, 1));
                    a.normalized();
                    a.x = a.x * laneWidth;
                    a.y = a.y * laneWidth;
                    gl.glTexCoord2d(0, 0);
                    gl.glVertex3d(getPoint(i).x + j * a.x, getPoint(i).y + j * a.y, getPoint(i).z);
                    gl.glTexCoord2d(1, 0);
                    gl.glVertex3d(getPoint(i).x + (j + 1) * a.x, getPoint(i).y + (j + 1) * a.y, getPoint(i).z);

                    Vector b;
                    b = getTangent(i + pOffset).cross(new Vector(0, 0, 1));
                    b.normalized();
                    b.x = b.x * laneWidth;
                    b.y = b.y * laneWidth;
                    gl.glTexCoord2d(0, 1);
                    gl.glVertex3d(getPoint(i + pOffset).x + j * b.x, getPoint(i + pOffset).y + j * b.y, getPoint(i + pOffset).z);
                    gl.glTexCoord2d(1, 1);
                    gl.glVertex3d(getPoint(i + pOffset).x + (j + 1) * b.x, getPoint(i + pOffset).y + (j + 1) * b.y, getPoint(i + pOffset).z);
                }
                gl.glEnd();
            }
            RobotRace.track.disable(gl);

            gl.glColor3f(1f, 1f, 1f);
            RobotRace.brick.enable(gl);
            RobotRace.brick.bind(gl);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            //Inner circle that is located at -2*1.22 from the center line and outer circle that is located 2*1.22 from the centerline
            for (int j = -2; j <= 2; j += 4) {
                gl.glBegin(GL_QUADS);
                double pOffset = 0.01;
                for (double i = 0; i < 1; i = i + pOffset) {

                    Vector a;
                    a = getTangent(i).cross(new Vector(0, 0, 1));
                    a.normalized();
                    a.x = a.x * laneWidth;
                    a.y = a.y * laneWidth;
                    gl.glTexCoord2d(0, 0);
                    gl.glVertex3d(getPoint(i).x + j * a.x, getPoint(i).y + j * a.y, getPoint(i).z - 1);
                    gl.glTexCoord2d(0, 1);
                    gl.glVertex3d(getPoint(i).x + j * a.x, getPoint(i).y + j * a.y, getPoint(i).z);

                    Vector b;
                    b = getTangent(i + pOffset).cross(new Vector(0, 0, 1));
                    b.normalized();
                    b.x = b.x * laneWidth;
                    b.y = b.y * laneWidth;
                    gl.glTexCoord2d(1, 1);
                    gl.glVertex3d(getPoint(i + pOffset).x + j * b.x, getPoint(i + pOffset).y + j * b.y, getPoint(i + pOffset).z);
                    gl.glTexCoord2d(1, 0);
                    gl.glVertex3d(getPoint(i + pOffset).x + j * b.x, getPoint(i + pOffset).y + j * b.y, getPoint(i + pOffset).z - 1);
                }
            }
            gl.glEnd();
            RobotRace.brick.disable(gl);
            gl.glDisable(GL_COLOR_MATERIAL);
        } else {
            gl.glEnable(GL_COLOR_MATERIAL);
            gl.glColor3f(1f, 1f, 1f);
            RobotRace.track.enable(gl);
            RobotRace.track.bind(gl);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            // draw the spline track
            for (int curve = 0; curve < 4; curve++) {

                // Use a triangle strip and create a closed ring out of triangles
                gl.glBegin(GL_TRIANGLE_STRIP);
                // Normal is pointing up for track
                gl.glNormal3d(0, 0, 1);
                double pOffset = 0.01;
                for (double i = 0; i < 1; i = i + 2*pOffset) {
                    // SEGMENTS times: add a vertex describing an inner and outer point of this curve
                    //double t = (i-1) / ((double) 300);
                    Vector inner = getPointOnCurve(i, curve,false);
                    Vector outer = getPointOnCurve(i, curve + 1,false);
                    // Add these two vectors, that are on the same distance on the track, as vertices to the triangle strip
                    gl.glTexCoord2d(0,0);
                    gl.glVertex3d(inner.x(), inner.y(), inner.z());
                    gl.glTexCoord2d(1,0);
                    gl.glVertex3d(outer.x(), outer.y(), outer.z());

                    Vector inner2 = getPointOnCurve(i+pOffset, curve,false);
                    Vector outer2 = getPointOnCurve(i+pOffset, curve + 1,false);

                    gl.glTexCoord2d(0,1);
                    gl.glVertex3d(inner2.x(), inner2.y(), inner2.z());
                    gl.glTexCoord2d(1,1);
                    gl.glVertex3d(outer2.x(), outer2.y(), outer2.z());
                }
                //add last and first point to finish the triangle strip
                Vector inner = getPointOnCurve(.99, curve,false);
                Vector outer = getPointOnCurve(.99, curve + 1,false);
                gl.glTexCoord2d(0,0);
                gl.glVertex3d(inner.x(), inner.y(), inner.z());
                gl.glTexCoord2d(1,0);
                gl.glVertex3d(outer.x(), outer.y(), outer.z());
                
                Vector inner2 = getPointOnCurve(0, curve,false);
                Vector outer2 = getPointOnCurve(0, curve + 1,false);
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
        //Vector tangent = getCubicBezierTangent(bezierT, P0, P1, P2, P3);
        Vector normal = tangent.cross(Vector.Z).normalized();

        if(robot){
            return point.add(normal.scale((curve) * laneWidth + laneWidth/2));
        }
        else{
            return point.add(normal.scale((curve) * laneWidth));
        }
    }
}
