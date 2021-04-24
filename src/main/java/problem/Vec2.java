package problem;

import java.util.Random;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

public class Vec2 {
    double x,y;

    public Vec2() {
        x = 0;
        y = 0;
    }

    public Vec2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vec2 add(Vec2 other) {
        return new Vec2(x + other.x, y + other.y);
    }

    public Vec2 add(double other) {
        return new Vec2(x + other, y + other);
    }

    public Vec2 sub(Vec2 other) {
        return new Vec2(x - other.x, y - other.y);
    }

    public Vec2 sub(double other) {
        return new Vec2(x - other, y - other);
    }

    public Vec2 scale(double factor) {
        return new Vec2(x * factor, y * factor);
    }

    public double sizeSq()
    {
        return x * x + y * y;
    }

    public double size()
    {
        return sqrt(sizeSq());
    }

    public double direction() {
        return atan2(y, x);
    }

    static public double dot(Vec2 u, Vec2 v)
    {
        return u.x * v.y - u.y * v.x;
    }

    public void randomize(Random gen) {
        x = gen.nextDouble() * 2 - 1;
        y = gen.nextDouble() * 2 - 1;
    }

    public void randomize()
    {
        Random gen = new Random();
        randomize(gen);
    }

    @Override
    public String toString() {
        return "Vec2(" + x + ", " + y + ")";
    }
}
