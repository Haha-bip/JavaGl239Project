package problem;

import problem.Line;

public class Segment extends Line {
    public Segment(Vec2 begin, Vec2 end) {
        super(begin, end);
    }

    private boolean ccw(Vec2 a, Vec2 b, Vec2 c) {
        return Vec2.dot(b.sub(a), c.sub(a)) > 0;
    }

    public Intersection intersect(Segment other)
    {
        Intersection result = new Intersection();
        result.exist = false;

        if (ccw(begin, other.begin, other.end) != ccw(end, other.begin, other.end) &&
                ccw(begin, end, other.begin) != ccw(begin, end, other.end)) {

            result.position = findIntersection(other);
            result.exist = true;
        }

        return result;
    }
}
