package kharybdys.util;

import java.awt.*;

public class Utils {
    private Utils() {
    }

    public static Polygon createTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        int[] xpoints = new int[3];
        xpoints[0] = x1;
        xpoints[1] = x2;
        xpoints[2] = x3;
        int[] ypoints = new int[3];
        ypoints[0] = y1;
        ypoints[1] = y2;
        ypoints[2] = y3;
        return new Polygon(xpoints, ypoints, 3);
    }

    public static int signum(int a)
    {
        if (a>0)
        {
            return 1;
        }
        if (a<0)
        {
            return -1;
        }
        return 0;
    }
}