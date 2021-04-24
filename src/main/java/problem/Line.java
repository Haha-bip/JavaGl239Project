package problem;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Line {
    private double r, g, b;
    Vec2 begin, end;

    Line() {
        begin = new Vec2(0,0);
        end = new Vec2(0, 0);
    }

    public Line(Vec2 begin, Vec2 end) {
        this.begin = begin;
        this.end = end;

        setColor(0.8, 0.8, 0.9);
    }

    void setColor(double r, double g, double b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    private double denominator(Line other) {
        return (begin.x - end.x) * (other.begin.y - other.end.y) - (other.begin.x - other.end.x) * (begin.y - end.y);
    }

    boolean intersects(Line other) {
        return abs(denominator(other)) < 1e-10;
    }

    Vec2 findIntersection(Line other) {
        double x1 = begin.x, x2 = end.x, x3 = other.begin.x, x4 = other.end.x;
        double y1 = begin.y, y2 = end.y, y3 = other.begin.y, y4 = other.end.y;

        return (new Vec2(
                (x1 * y2 - y1 * x2) * (x3 - x4) - (x1 - x2) * (x3 * y4 - y3 * x4),
                (x1 * y2 - y1 * x2) * (y3 - y4) - (y1 - y2) * (x3 * y4 - y3 * x4)
        )).scale(1 / denominator(other));
    }

    double distanceFromOrigin() {
        double dY = end.y - begin.y, dX = end.x - begin.x;
        return abs(dX * begin.y - begin.x * dY) / sqrt(dX * dX + dY * dY);
    }

    void render(GL2 gl) {
        gl.glColor3d(r, g, b);

        gl.glBegin(GL.GL_LINES);
        gl.glVertex2d(begin.x, begin.y);
        gl.glVertex2d(end.x, end.y);
        gl.glEnd();
    }
}
