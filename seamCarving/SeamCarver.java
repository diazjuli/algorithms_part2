import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.EdgeWeightedDigraph;
import edu.princeton.cs.algs4.AcyclicSP;
import edu.princeton.cs.algs4.DirectedEdge;
import java.awt.Color;

public class SeamCarver {

    private int height;
    private int width;
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture == null)
            throw new java.lang.IllegalArgumentException("argument can not be null");
        height = picture.height();
        width = picture.width();
        this.picture = new Picture(picture);
    }    // create a seam carver object based on the given picture

    public Picture picture() {
        return (picture);
    }                          // current picture

    public     int width() {
        return (width);
    }                           // width of current picture

    public     int height() {
        return (height);
    }                       // height of current picture

    public  double energy(int x, int y) {
        if (x < 0 || y < 0 || x > width - 1 || y > height - 1)
            throw new java.lang.IllegalArgumentException("index is out of range");
        double red;
        double blue;
        double green;
        double x_grad;
        double y_grad;
        Color prev_color;
        Color next_color;
        if (x == 0 || y == 0 || x == width - 1 || y == height - 1)
            return (1000.0);
        prev_color = picture.get(x -1, y);
        next_color = picture.get(x + 1, y);
        red = Math.pow((double)(prev_color.getRed() - next_color.getRed()), 2);
        blue = Math.pow((double)(prev_color.getBlue() - next_color.getBlue()), 2);
        green = Math.pow((double)(prev_color.getGreen() - next_color.getGreen()), 2);
        x_grad = red + blue + green;
        prev_color = picture.get(x, y - 1);
        next_color = picture.get(x, y + 1);
        red = Math.pow((double)(prev_color.getRed() - next_color.getRed()), 2);
        blue = Math.pow((double)(prev_color.getBlue() - next_color.getBlue()), 2);
        green = Math.pow((double)(prev_color.getGreen() - next_color.getGreen()), 2);
        y_grad = red + blue + green;
        return (java.lang.Math.sqrt(x_grad + y_grad));
    }            // energy of pixel at column x and row y

    public   int[] findHorizontalSeam() {
        int[][]parent;
        double[][] distTo;
        int[] result;

        result = new int[width];
        parent = new int[width][height];
        distTo = new double[width][height];
        for (int y = 0; y < height; y++) {
            distTo[0][y] = energy(0, y);
            parent[0][y] = y;
        }
        for (int x = 1; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double min = distTo[x - 1][y];
                parent[x][y] = y;
                if (y != 0 && min > distTo[x - 1][y - 1]) {
                    min = distTo[x - 1][y - 1];
                    parent[x][y] = y - 1;
                }
                if (y != height - 1 && min > distTo[x - 1][y + 1]) {
                    min = distTo[x - 1][y + 1];
                    parent[x][y] = y + 1;
                }
                distTo[x][y] = min + energy(x, y);
            }
        }
        int min = 0;
        for (int y = 1; y < height; y++) {
            if (distTo[width - 1][min] > distTo[width - 1][y])
                min = y;
        }
        for (int x = width - 1; x >= 0; x--) {
            result[x] = min;
            min = parent[x][min];
        }
        return (result);
    }              // sequence of indices for horizontal seam

    public   int[] findVerticalSeam() {
        int[][]parent;
        double[][] distTo;
        int[] result;

        result = new int[height];
        parent = new int[width][height];
        distTo = new double[width][height];
        for (int x = 0; x < width; x++) {
            distTo[x][0] = energy(x, 0);
            parent[x][0] = x;
        }
        for (int y = 1; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double min = distTo[x][y - 1];
                parent[x][y] = x;
                if (x != 0 && min > distTo[x - 1][y - 1]) {
                    min = distTo[x - 1][y - 1];
                    parent[x][y] = x - 1;
                }
                if (x != width - 1 && min > distTo[x + 1][y - 1]) {
                    min = distTo[x + 1][y - 1];
                    parent[x][y] = x + 1;
                }
                distTo[x][y] = min + energy(x, y);
            }
        }
        int min = 0;
        for (int x = 1; x < width; x++) {
            if (distTo[x][height - 1] < distTo[min][height - 1])
                min = x;
        }
        for (int y = height - 1; y >= 0; y--) {
            result[y] = min;
            min = parent[min][y];
        }
        return (result);
    }                // sequence of indices for vertical seam

    private void    valid_seam(int[] seam, boolean vertical) {
        int diff;
        if (seam == null)
            throw new IllegalArgumentException("argument can not be null");
        else if (vertical == true) {
            if (seam.length != this.height)
                throw new IllegalArgumentException("seam array is of the wrong size");
            for (int i = 0; i < seam.length - 1; i++) {
                if (seam[i] < 0 || seam[i] > this.height - 1)
                    throw  new IllegalArgumentException("index is outside of the prescribed range");
                diff = seam[i] - seam[i + 1];
                if (diff > 1 || diff < -1)
                    throw new java.lang.IllegalArgumentException("two indices in seam sequence are not connected");
            }
        }
        else  {
            if (seam.length != this.width)
                throw new IllegalArgumentException("seam array is of the wrong size");
            for (int i = 0; i < seam.length - 1; i++) {
                if (seam[i] < 0 || seam[i] > this.width - 1)
                    throw  new IllegalArgumentException("index is outside of the prescribed range");
                diff = seam[i] - seam[i + 1];
                if (diff > 1 || diff < -1)
                    throw new java.lang.IllegalArgumentException("two indices in seam sequence are not connected");
            }
        }
    }

    public    void removeHorizontalSeam(int[] seam) {
        Picture result = new Picture(width, height - 1);
        //valid_seam(seam, false);
        if (this.height <= 1)
            throw new java.lang.IllegalArgumentException("width of picture is too small");
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < seam[x]; y++)
                result.setRGB(x, y, picture.getRGB(x, y));
            for (int y = seam[x] + 1; y < this.height; y++)
                result.setRGB(x, y - 1, picture.getRGB(x, y));
        }
        this.picture = result;
        this.height--;
    }  // remove horizontal seam from current picture

    public    void removeVerticalSeam(int[] seam) {
        Picture result = new Picture(width - 1, height);
        //valid_seam(seam, true);
        if (this.width <= 1)
            throw new java.lang.IllegalArgumentException("height of picture is too small");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < seam[y]; x++)
                result.setRGB(x, y, picture.getRGB(x, y));
            for (int x = seam[y] + 1; x < this.width; x++)
                result.setRGB(x - 1, y, picture.getRGB(x, y));
        }
        this.picture = result;
        this.width--;
    }    // remove vertical seam from current picture
}