package problem;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.util.ArrayList;

public class Polygon {
    ArrayList<Vec2> points;
    Vec2 origin;
    double area;

    Polygon() {
        points = new ArrayList<>();
        area = 0;
        origin = new Vec2(0, 0);
    }

    Polygon(ArrayList<Vec2> vertices) {
        points = new ArrayList<>();
        points.addAll(vertices);

        PolarComparator comp = new PolarComparator();
        origin = comp.fit(points);
        points.sort(comp);

        area = getArea();
    }

    private double getArea() {
        double area = 0;

        if (points.size() < 3)
            return 0;

        for (int i = 0; i < points.size(); i++) {
            Triangle part = new Triangle(points.get(i), points.get((i + 1) % points.size()), origin);
            area += part.getArea();
        }

        return area;
    }

    void render(GL2 gl) {
        gl.glColor3d(0.6, 0.95, 0.8);
        gl.glBegin(GL.GL_TRIANGLE_FAN);

        for (Vec2 point : points)
            gl.glVertex2d(point.x, point.y);

        gl.glEnd();

        gl.glColor3d(0.2, 0.2, 0.9);
        gl.glLineWidth(3f);

        gl.glBegin(GL.GL_LINE_LOOP);

        for (Vec2 point : points)
            gl.glVertex2d(point.x, point.y);
        gl.glLineWidth(1f);

        gl.glEnd();
    }
}
