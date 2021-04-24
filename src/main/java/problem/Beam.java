package problem;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import java.util.ArrayList;
import java.util.Random;

public class Beam {
    TriangleType type;
    Segment top, bottom;
    double r, g, b;

    Beam(Vec2 f, Vec2 s) {
        init(f, s);
    }

    private void init(Vec2 f, Vec2 s) {
        double theta = Math.atan2(s.x - f.x, f.y - s.y);

        double dir = Math.atan2(f.y - s.y, f.x - s.x);
        f = f.sub(new Vec2(Math.cos(dir) * 3, Math.sin(dir) * 3));
        //s = s.add(new Vec2(Math.cos(dir) * 3, Math.sin(dir) * 3));

        Random rand = new Random();
        double size = rand.nextDouble() / 20 + 0.01;

        r = rand.nextDouble();
        g = rand.nextDouble();
        b = rand.nextDouble();

        Vec2 delta = new Vec2(Math.cos(theta) * size, Math.sin(theta) * size);

        Vec2 f1 = f.add(delta);
        Vec2 s1 = s.add(delta);
        Vec2 f2 = f.sub(delta);
        Vec2 s2 = s.sub(delta);

        top = new Segment(f1, s1);
        bottom = new Segment(f2, s2);
        type = TriangleType.UNKNOWN;
    }

    Beam() {}

    Beam(Vec2 a, Vec2 b, Vec2 c, Vec2 d) {
        top = new Segment(a, b);
        bottom = new Segment(c, d);
    }

    ArrayList<Segment> getEdges() {
        ArrayList<Segment> result = new ArrayList<>();

        result.add(top);
        result.add(bottom);
        result.add(new Segment(top.begin, bottom.begin));
        result.add(new Segment(top.end, bottom.end));


        return result;
    }

    boolean isInside(Vec2 p) {
        Vec2 v1 = new Vec2(top.end.x - top.begin.x, top.end.y - top.begin.y);
        Vec2 v2 = new Vec2(top.end.x - p.x, top.end.y - p.y);
        double xp1 = v1.x * v2.y - v1.y * v2.x;

        v1 = new Vec2(bottom.end.x - bottom.begin.x, bottom.end.y - bottom.begin.y);
        v2 = new Vec2(bottom.end.x - p.x, bottom.end.y - p.y);
        double xp2 = v1.x * v2.y - v1.y * v2.x;

        return (xp1 * xp2) <= 0;
    }

    void randomize() {
        Vec2 a = new Vec2(), b = new Vec2();
        a.randomize();
        b.randomize();

        init(a, b);
    }

    void render(GL2 gl) {
        switch (type) {
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

        gl.glVertex2d(top.begin.x, top.begin.y);

        gl.glVertex2d(bottom.begin.x, bottom.begin.y);
        gl.glVertex2d(bottom.end.x, bottom.end.y);

        gl.glVertex2d(top.end.x, top.end.y);

        gl.glEnd();

        gl.glLineWidth(1f);
    }
}
