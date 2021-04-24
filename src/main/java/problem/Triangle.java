package problem;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;
import static problem.TriangleType.NONE;
import static problem.TriangleType.UNKNOWN;

public class Triangle {
    ArrayList<Vec2> points;
    private double r, g, b;
    TriangleType type;

    Triangle()
    {
        points = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            points.add(new Vec2(0,0));

        setColor();

        type = UNKNOWN;
    }

    Triangle(Vec2 a, Vec2 b, Vec2 c)
    {
        points = new ArrayList<>();
        points.add(a);
        points.add(b);
        points.add(c);

        setColor();

        type = UNKNOWN;
    }

    void setType(TriangleType type)
    {
        this.type = type;
    }

    void setColor(double r, double g, double b)
    {
        this.r = r;
        this.b = b;
        this.g = g;
    }

    void setColor()
    {
        Random gen = new Random();
        setColor(gen.nextDouble(), gen.nextDouble(), gen.nextDouble());
    }

    void randomize()
    {
        Random gen = new Random();
        for (Vec2 point : points)
            point.randomize(gen);
    }

    double getArea()
    {
        return abs(Vec2.dot(points.get(1).sub(points.get(0)), points.get(2).sub(points.get(0))) / 2);
    }

    ArrayList<Segment> getEdges()
    {
        ArrayList<Segment> result = new ArrayList<>();
        for (int i = 0; i < 3; i++)
            result.add(new Segment(points.get(i), points.get((i + 1) % 3)));

        return result;
    }

    double sign (Vec2 p1, Vec2 p2, Vec2 p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    boolean isInside(Vec2 point)
    {
        Vec2 v1 = points.get(0), v2 = points.get(1), v3 = points.get(2);
        double d1, d2, d3;
        boolean has_neg, has_pos;

        d1 = sign(point, v1, v2);
        d2 = sign(point, v2, v3);
        d3 = sign(point, v3, v1);

        has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
        has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

        return !(has_neg && has_pos);
    }

    Polygon intersect(Beam other)
    {
        ArrayList<Segment> sf = this.getEdges();
        ArrayList<Segment> ss = other.getEdges();

        ArrayList<Vec2> vertices = new ArrayList<>();

        for (Segment first : sf) {
            for (Segment second : ss) {
                Intersection intersection = first.intersect(second);
                if (intersection.exist) {
                    vertices.add(intersection.position);
                }
            }
        }

        for (Vec2 point : points)
            if (other.isInside(point))
                vertices.add(point);

        return new Polygon(vertices);
    }

    void render(GL2 gl) {
        switch(type) {
            case UNKNOWN:
                gl.glLineWidth(2f);
                gl.glColor3d(r, g, b);
                break;

            case UNUSED:
                gl.glLineWidth(1f);
                gl.glColor3d(0.6, 0.4, 0.4);
                break;

            case CHOOSEN:
                gl.glLineWidth(3f);
                gl.glColor3d(0.9, 0.5, 0.3);
                break;

            default:
                return;
        }

        gl.glBegin(GL.GL_LINE_LOOP);

        for (Vec2 point : points)
            gl.glVertex2d(point.x, point.y);

        gl.glEnd();

        gl.glLineWidth(1f);
    }
}
