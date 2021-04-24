package problem;

import java.util.ArrayList;
import java.util.Comparator;

import static java.lang.Math.atan2;
import static java.lang.Math.toDegrees;

public class PolarComparator implements Comparator<Vec2> {
    boolean clockwise;
    Vec2 origin;

    PolarComparator() {
        clockwise = true;
    }

    PolarComparator(boolean order) {
        clockwise = order;
    }

    public Vec2 fit(ArrayList<Vec2> arr) {
        origin = new Vec2(0,0);

        for (Vec2 point : arr) {
            origin.x += point.x;
            origin.y += point.y;
        }

        origin.x /= arr.size();
        origin.y /= arr.size();

        return origin;
    }

    @Override
    public int compare(Vec2 o1, Vec2 o2) {
        double d1 = (toDegrees(o1.sub(origin).direction()) + 360) % 360, d2 = (toDegrees(o2.sub(origin).direction()) + 360) % 360;

        return (int) (d1 - d2);
    }
}
