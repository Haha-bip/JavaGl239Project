package Gui;

import com.jogamp.opengl.util.FPSAnimator;
import problem.Problem;

import javax.media.opengl.*;
import javax.media.opengl.awt.GLCanvas;


/**
 * Класс рисования OpenGL
 */
class RendererGL implements GLEventListener {
    /**
     * то, где рисуется OpenGL-сцена
     */
    private final GLCanvas canvas;
    /**
     * аниматор для перерисовки сцены
     */
    private FPSAnimator animator;
    /**
     * решение задачи
     */
    final Problem problem;

    /**
     * Конструктор класса рисования OpenGL
     */
    RendererGL() {
        // создаём OpenGL-профиль
        GLProfile profile = GLProfile.get(GLProfile.GL2);
        // получаем набор возможностей OpenGL по созданному профилю
        GLCapabilities capabilities = new GLCapabilities(profile);
        // создаём область рисования
        this.canvas = new GLCanvas(capabilities);
        // вешаем на неё обработчик событий OpenGL
        canvas.addGLEventListener(this);
        // переменная класса решения задачи
        problem = new Problem();
    }

    /**
     * Вызывается при первой отрисовке кадра
     *
     * @param drawable объект рисования OpenGL
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        // получаем класс для работы с openGL
        GL2 gl = drawable.getGL().getGL2();
        // задаём тип матриц преобразований
        gl.glMatrixMode(GL2.GL_3D);
        // задаём функцию проверки глубины
        gl.glDepthFunc(GL2.GL_LEQUAL);
        // создаём аниматор с частотой 25 кадров в секунду
        animator = new FPSAnimator(canvas, 25, true);
        // запускаем аниматор
        animator.start();
    }

    /**
     * Отрисовка кадра OpenGL
     *
     * @param drawable объект рисования OpenGL
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        // получаем класс для работы с openGL
        GL2 gl = drawable.getGL().getGL2();
        // задаём цвет фона
        gl.glClearColor(0.1f, 0.1f, 0.1f, 1.0f); // Set background color to black and opaque
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT);         // Clear the color buffer (background)
        problem.render(gl);
        // объязываем openGL отрисовать всю сцену и только потом рисовать следующий кадр
        gl.glFlush();
    }

    GLCanvas getCanvas() {
        return canvas;
    }

    void close() {
        animator.stop();
        System.out.println("terminated");
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        animator.stop();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int posX, int posY, int width, int height) {
        // if window resized -> do not do anything
    }


}