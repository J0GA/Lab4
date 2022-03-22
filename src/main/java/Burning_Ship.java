import java.awt.geom.Rectangle2D;

public class Burning_Ship extends FractalGenerator{
    public static final int MAX_ITERATIONS = 2000;

    public void getInitialRange(Rectangle2D.Double rect)
    {
        rect.x = -2;
        rect.y = -2.5;
        rect.width = 4;
        rect.height = 3;
    }

    public int numIterations(double x, double y) {
        int iter = 0;
        double zr = 0;
        double zi = 0;
        while (iter < MAX_ITERATIONS && zr * zr + zi * zi < 4)
        {
            double zrUpdated = zr * zr - zi * zi + x;
            double ziUpdated = -2 * zr * zi + y;
            zr = zrUpdated;
            zi = ziUpdated;
            iter += 1;
        }
        if (iter == MAX_ITERATIONS)
        {
            return -1;
        }

        return iter;
    }

    public String toString() {
        return "Burning Ship";
    }
}
