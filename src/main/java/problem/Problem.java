package problem;

import javax.media.opengl.GL2;
import java.io.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static problem.TriangleType.*;

public class Problem {
    public static final String PROBLEM_TEXT = "На плоскости задано множество \"широких лучей\" и \n" +
            "множество треугольников. Нужно найти такую пару \"широкий луч\"-треугольник, что фигура, \n" +
            "находящаяся внутри \"широкого луча\" и треугольника, имеет максимальную площадь.";

    public static final String PROBLEM_CAPTION = "Итоговый проект ученика Малкова Максима (10-2)";

    private static final String POLYGON_FILE_NAME = "polygon.txt";
    private static final String TRIANGLES_FILE_NAME = "triangles.txt";
    private static final String BEAMS_FILE_NAME = "beams.txt";

    private final ArrayList<Triangle> triangles;
    private ArrayList<Beam> beams;
    Polygon polygon;

    public Problem() {
        triangles = new ArrayList<>();
        polygon = new Polygon();
        beams = new ArrayList<>();
    }

    public void addTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        triangles.add(new Triangle(new Vec2(x1, y1), new Vec2(x2, y2), new Vec2(x3, y3)));
    }

    public void addBeam(double x1, double y1, double x2, double y2)
    {
        beams.add(new Beam(new Vec2(x1, y1), new Vec2(x2, y2)));
    }

    public void solve() {
        Triangle tri = new Triangle();
        Beam bea = new Beam();

        for (Triangle first : triangles) {
            for (Beam second : beams)
            {
                Polygon p = first.intersect(second);
                if (p.area > polygon.area) {
                    polygon = p;
                    tri = first;
                    bea = second;
                }
            }
        }

        for (Triangle triangle : triangles) {
            if (tri == triangle)
                triangle.setType(CHOOSEN);
            else
                triangle.setType(UNUSED);
        }

        for (Beam beam : beams)
        {
            if (beam == bea)
                beam.type = CHOOSEN;
            else
                beam.type = UNUSED;
        }
    }

    public void loadFromFile() {
        clear();

        // Загрузка треугольников
        try {
            File file = new File(TRIANGLES_FILE_NAME);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                Triangle tri = new Triangle(
                        new Vec2(sc.nextDouble(), sc.nextDouble()),
                        new Vec2(sc.nextDouble(), sc.nextDouble()),
                        new Vec2(sc.nextDouble(), sc.nextDouble())
                );

                sc.nextLine();

                tri.setType(TriangleType.valueOf(sc.nextLine()));
                triangles.add(tri);
            }
        } catch (Exception ex) {
            System.out.println("Ошибка чтения из файла: " + ex);
        }

        // Загрузка полигона
        try {
            File file = new File(POLYGON_FILE_NAME);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                polygon.points.add(new Vec2(sc.nextDouble(), sc.nextDouble()));
                sc.nextLine();
            }
        } catch (Exception ex) {
            System.out.println("Ошибка чтения из файла: " + ex);
        }

        try {
            File file = new File(BEAMS_FILE_NAME);
            Scanner sc = new Scanner(file);
            while (sc.hasNextLine()) {
                Beam beam = new Beam(
                        new Vec2(sc.nextDouble(), sc.nextDouble()),
                        new Vec2(sc.nextDouble(), sc.nextDouble()),
                        new Vec2(sc.nextDouble(), sc.nextDouble()),
                        new Vec2(sc.nextDouble(), sc.nextDouble())
                );

                sc.nextLine();

                beam.type = TriangleType.valueOf(sc.nextLine());
                beams.add(beam);
            }
        } catch (Exception ex) {
            System.out.println("Ошибка чтения из файла: " + ex);
        }
    }

    public void saveToFile() {
        // Сохранение треугольников
        try {
            PrintWriter out = new PrintWriter(new FileWriter(TRIANGLES_FILE_NAME));
            for (Triangle tri : triangles) {
                for (Vec2 point : tri.points)
                    out.printf("%.4f %.4f ", point.x, point.y);
                out.printf("\n");

                out.println(tri.type);
            }

            out.close();
        } catch (IOException ex) {
            System.out.println("Ошибка записи в файл: " + ex);
        }

        // Сохранение полигона
        try {
            PrintWriter out = new PrintWriter(new FileWriter(POLYGON_FILE_NAME));
            for (Vec2 point : polygon.points)
                out.printf("%.4f %.4f\n", point.x, point.y);

            out.close();
        } catch (IOException ex) {
            System.out.println("Ошибка записи в файл: " + ex);
        }

        try {
            PrintWriter out = new PrintWriter(new FileWriter(BEAMS_FILE_NAME));
            for (Beam beam : beams) {
                out.printf("%.4f %.4f ", beam.top.begin.x, beam.top.begin.y);
                out.printf("%.4f %.4f ", beam.top.end.x, beam.top.end.y);
                out.printf("%.4f %.4f ", beam.bottom.begin.x, beam.bottom.begin.y);
                out.printf("%.4f %.4f\n", beam.bottom.end.x, beam.bottom.end.y);

                out.println(beam.type);
            }

            out.close();
        } catch (IOException ex) {
            System.out.println("Ошибка записи в файл: " + ex);
        }
    }

    public void addRandom(int n) {
        polygon = new Polygon();
        for (Triangle tri : triangles)
            tri.setType(UNKNOWN);

        for (Beam beam : beams)
            beam.type = UNKNOWN;

        for (int i = 0; i < n; i++) {
            if (i < n / 2) {
                Triangle tri = new Triangle();
                tri.randomize();

                triangles.add(tri);
            }
            else
            {
                Beam beam = new Beam();
                beam.randomize();

                beams.add(beam);
            }
        }
    }

    public void clear() {
        triangles.clear();
        beams.clear();
        polygon = new Polygon();
    }

    public void render(GL2 gl) {
        for (Triangle tri : triangles)
            if (tri.type != CHOOSEN)
                tri.render(gl);

        for (Beam beam : beams)
            if (beam.type != CHOOSEN)
                beam.render(gl);

        for (Triangle tri : triangles)
            if (tri.type == CHOOSEN)
                tri.render(gl);

        for (Beam beam : beams)
            if (beam.type == CHOOSEN)
                beam.render(gl);

        polygon.render(gl);
    }
}
