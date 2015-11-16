import java.awt.Color;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private EnergyPicture ePic;
    private boolean trans = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        CheckUtil.checkNull(picture);
        this.ePic = new EnergyPicture(picture);
    }

    // current picture
    public Picture picture() {
        transpose(false); // transpose stored picture back
        Picture picture = new Picture(this.width(), this.height());
        for (int row = 0; row < this.height(); ++row) {
            for (int col = 0; col < this.width(); ++col) {
                Pixel p = ePic.pic[row][col];
                picture.set(col, row, new Color(p.getRed(), p.getGreen(), p.getBlue()));
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        if (!this.trans)
            return ePic.W; // transpose back
        else
            return ePic.H; // transposed
    }

    // height of current picture
    public int height() {
        if (!this.trans)
            return ePic.H; // transpose back
        else
            return ePic.W; // transposed
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
            energy = ePic.pic[x][y].energy; // transposed
        } else {
            energy = ePic.pic[y][x].energy; // transpose back
        }
        return Math.sqrt(energy);
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
        SeamDFS dfs = new SeamDFS(ePic.pic, ePic.W, ePic.H);
        dfs.findSeam(); // just consider vertical
        int[] seam = dfs.path();
        dfs = null;
        return seam;
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
        for (int row = 0; row < ePic.H; ++row) {
            System.arraycopy(ePic.pic[row], seam[row] + 1, ePic.pic[row], seam[row],
                    ePic.W - seam[row] - 1);
            ePic.pic[row][ePic.W - 1] = null;
        }
        ePic.W--;
        ePic.refreshEnergy(seam);
    }

    private void transpose(boolean isTrans) {
        if (this.trans != isTrans) {
            int h = ePic.H, w = ePic.W;
            Pixel[][] tranPic = new Pixel[w][h];
            for (int row = 0; row < h; row++) {
                for (int col = 0; col < w; col++) {
                    tranPic[col][row] = ePic.pic[row][col];
                    ePic.pic[row][col] = null;
                }
            }
            ePic.pic = tranPic;
            int tmp = ePic.H;
            ePic.H = ePic.W;
            ePic.W = tmp;
            this.trans = isTrans;
        }
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
