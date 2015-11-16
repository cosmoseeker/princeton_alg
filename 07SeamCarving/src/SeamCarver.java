import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private static final byte[][] DIRECTION = { { -1, 0 }, { 1, 0 }, { 0, -1 },
            { 0, 1 } };
    private int W;
    private int H;
    private Pixel[][] pic;
    private boolean trans = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        CheckUtil.checkNull(picture);
        this.W = picture.width();
        this.H = picture.height();
        int side;
        if (this.W > this.H) {
            side = this.W;
        } else {
            side = this.H;
        }
        this.pic = new Pixel[side][side];
        for (int row = 0; row < this.H; ++row) {
            for (int col = 0; col < this.W; ++col) {
                Color color = picture.get(col, row);
                this.pic[row][col] = new Pixel(1000 * 1000, color);
            }
        }
        for (int row = 1; row < this.H - 1; ++row) {
            for (int col = 1; col < this.W - 1; ++col) {
                int energy = this.gradientSq(col, row);
                this.pic[row][col].energy = energy;
            }
        }
    }

    // current picture
    public Picture picture() {
        transpose(false); // transpose stored picture back
        Picture picture = new Picture(this.width(), this.height());
        for (int row = 0; row < this.height(); ++row) {
            for (int col = 0; col < this.width(); ++col) {
                picture.set(col, row, this.pic[row][col].color);
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        if (!this.trans)
            return this.W; // transpose back
        else
            return this.H; // transposed
    }

    // height of current picture
    public int height() {
        if (!this.trans)
            return this.H; // transpose back
        else
            return this.W; // transposed
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) { // corresponding to transpose back
        if (CheckUtil.checkOutBound(x, 0, this.width())) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        if (CheckUtil.checkOutBound(y, 0, this.height())) {
            throw new java.lang.IndexOutOfBoundsException();
        }
        double energy;
        if (this.trans) {
            energy = this.pic[x][y].energy; // transposed
        } else {
            energy = this.pic[y][x].energy; // transpose back
        }
        return Math.sqrt(energy);
    }

    private int gradientSq(int x, int y) {
        if (isBorder(x, y)) {
            return 1000 * 1000;
        }
        int square = 0, diff;
        for (int i = 0; i < SeamCarver.DIRECTION.length; i += 2) {
            int X1 = x + SeamCarver.DIRECTION[i][0], Y1 = y
                    + SeamCarver.DIRECTION[i][1];
            int X2 = x + SeamCarver.DIRECTION[i + 1][0], Y2 = y
                    + SeamCarver.DIRECTION[i + 1][1];
            Color c1 = this.pic[Y1][X1].color, c2 = this.pic[Y2][X2].color;
            diff = c1.getRed() - c2.getRed();
            square += diff * diff;
            diff = c1.getGreen() - c2.getGreen();
            square += diff * diff;
            diff = c1.getBlue() - c2.getBlue();
            square += diff * diff;
        }
        return square;
    }

    private boolean isBorder(int col, int row) {
        return col == 0 || col == this.W - 1 || row == 0 || row == this.H - 1;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        this.transpose(true);
        return findSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        this.transpose(false);
        return findSeam();
    }

    private int[] findSeam() {
        SeamDFS dfs = new SeamDFS(this.pic, this.W, this.H);
        dfs.findSeam(); // just consider vertical
        return dfs.path();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        CheckUtil.checkNull(seam);
        checkSeam(seam, this.width(), this.height());
        this.transpose(true);
        this.removeSeam(seam);
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        CheckUtil.checkNull(seam);
        checkSeam(seam, this.height(), this.width());
        this.transpose(false);
        this.removeSeam(seam);
    }

    private void removeSeam(int[] seam) {
        for (int row = 0; row < this.H; ++row) {
            System.arraycopy(pic[row], seam[row] + 1, pic[row], seam[row],
                    this.W - seam[row] - 1);
            pic[row][this.W - 1] = null;
        }
        this.W--;
        for (int r = 0; r < this.H; ++r) {
            for (int i = 0; i < SeamCarver.DIRECTION.length; i++) {
                int col = seam[r] + SeamCarver.DIRECTION[i][0];
                int row = r + SeamCarver.DIRECTION[i][1];
                if ((!CheckUtil.checkOutBound(col, 0, this.W))
                        && (!CheckUtil.checkOutBound(row, 0, this.H))) {
                    this.pic[row][col].energy = this.gradientSq(col, row);
                }
            }
            if ((!CheckUtil.checkOutBound(seam[r], 0, this.W))
                    && (!CheckUtil.checkOutBound(r, 0, this.H))) {
                this.pic[r][seam[r]].energy = this.gradientSq(seam[r], r);
            }
        }
    }

    private void transpose(boolean isTrans) {
        if (this.trans != isTrans) {
            int side;
            if (this.W > this.H) {
                side = this.W;
            } else {
                side = this.H;
            }
            for (int i = 0; i < side; i++) {
                for (int j = i + 1; j < side; j++) {
                    swap(i, j);
                }
            }
            int tmp = this.H;
            this.H = this.W;
            this.W = tmp;
            this.trans = isTrans;
        }
    }

    private void swap(int i, int j) {
        Pixel tmp = this.pic[i][j];
        this.pic[i][j] = this.pic[j][i];
        this.pic[j][i] = tmp;
    }

    private void checkSeam(int[] seam, int len, int bound) {
        if (seam.length != len) {
            throw new java.lang.IllegalArgumentException();
        }
        int prev = seam[0];
        for (int i : seam) {
            int bias = i - prev;
            if (CheckUtil.checkOutBound(i, 0, bound) || bias < -1 || bias > 1) {
                throw new java.lang.IllegalArgumentException();
            }
            prev = i;
        }
    }

    public static void main(String[] args) {
        int[][] pic = { { 7, 8, 6 }, { 2, 9, 9 }, { 7, 4, 1 }, { 0, 9, 8 },
                { 2, 5, 7 }, { 2, 8, 0 }, { 3, 1, 5 }, { 6, 7, 0 },
                { 2, 6, 7 }, { 5, 6, 7 }, { 7, 2, 7 }, { 4, 5, 6 },
                { 2, 6, 0 }, { 9, 2, 1 }, { 1, 9, 5 }, { 1, 2, 4 },
                { 0, 1, 0 }, { 7, 6, 9 }, { 6, 6, 2 }, { 9, 3, 7 },
                { 6, 9, 5 }, { 3, 8, 8 }, { 8, 4, 0 }, { 8, 0, 9 },
                { 7, 0, 8 }, { 9, 9, 9 }, { 8, 7, 6 }, { 3, 4, 9 },
                { 2, 0, 1 }, { 7, 1, 0 }, { 1, 3, 0 }, { 0, 9, 4 },
                { 8, 3, 7 }, { 1, 9, 4 }, { 5, 1, 6 }, { 3, 9, 0 },
                { 5, 0, 3 }, { 4, 1, 0 }, { 1, 9, 5 }, { 8, 7, 3 },
                { 1, 7, 7 }, { 9, 1, 4 }, { 4, 9, 1 }, { 1, 6, 2 },
                { 0, 4, 1 }, { 2, 8, 6 }, { 2, 7, 7 }, { 6, 3, 3 },
                { 2, 9, 4 }, { 9, 7, 1 }, { 4, 2, 0 }, { 3, 7, 6 },
                { 4, 5, 1 }, { 3, 3, 5 }, { 9, 8, 3 }, { 5, 7, 4 },
                { 4, 4, 5 }, { 5, 7, 4 }, { 7, 1, 1 }, { 3, 2, 8 },
                { 7, 8, 8 }, { 4, 1, 4 }, { 6, 4, 8 }, { 8, 1, 5 } };
        Picture picture = new Picture(8, 8);
        for (int index = 0; index < pic.length; ++index) {
            int row = index / 8, col = index % 8;
            picture.set(col, row, new Color(pic[index][0], pic[index][1],
                    pic[index][2]));
        }
        SeamCarver seam = new SeamCarver(picture);
        int[] s = seam.findHorizontalSeam();
        PrintSeams.printSeam(seam, s, true);
        seam.removeHorizontalSeam(s);
        PrintSeams.printSeam(seam, s, true);
        double energy = seam.energy(3, 2);
        System.out.println(energy);
    }
}
