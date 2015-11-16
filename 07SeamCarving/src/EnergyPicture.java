import java.awt.Color;

import edu.princeton.cs.algs4.Picture;


public class EnergyPicture {
    private static final byte[][] DIRECTION = { { -1, 0 }, { 1, 0 }, { 0, -1 },
        { 0, 1 } };
    Pixel[][] pic;
    int W;
    int H;
    public EnergyPicture(Picture picture) {
        this.W = picture.width();
        this.H = picture.height();
        this.pic = new Pixel[this.H][this.W];
        for (int row = 0; row < this.H; ++row) {
            for (int col = 0; col < this.W; ++col) {
                Color color = picture.get(col, row);
                this.pic[row][col] = new Pixel(1000 * 1000, color.getRGB());
            }
        }
        for (int row = 1; row < this.H - 1; ++row) {
            for (int col = 1; col < this.W - 1; ++col) {
                int energy = this.gradientSq(col, row);
                this.pic[row][col].energy = energy;
            }
        }
    }
    int gradientSq(int x, int y) {
        if (isBorder(x, y)) {
            return 1000 * 1000;
        }
        int square = 0, diff;
        for (int i = 0; i < EnergyPicture.DIRECTION.length; i += 2) {
            int X1 = x + EnergyPicture.DIRECTION[i][0], Y1 = y
                    + EnergyPicture.DIRECTION[i][1];
            int X2 = x + EnergyPicture.DIRECTION[i + 1][0], Y2 = y
                    + EnergyPicture.DIRECTION[i + 1][1];
            Pixel c1 = this.pic[Y1][X1], c2 = this.pic[Y2][X2];
            diff = c1.getRed() - c2.getRed();
            square += diff * diff;
            diff = c1.getGreen() - c2.getGreen();
            square += diff * diff;
            diff = c1.getBlue() - c2.getBlue();
            square += diff * diff;
        }
        return square;
    }
    void refreshEnergy(int[] seam) {
        for (int r = 0; r < this.H; ++r) {
            for (int i = 0; i < EnergyPicture.DIRECTION.length; i++) {
                int col = seam[r] + EnergyPicture.DIRECTION[i][0];
                int row = r + EnergyPicture.DIRECTION[i][1];
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
    private boolean isBorder(int col, int row) {
        return col == 0 || col == this.W - 1 || row == 0 || row == this.H - 1;
    }
}
