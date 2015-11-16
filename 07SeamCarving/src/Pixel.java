

public class Pixel {
    int energy;
    int color;
    Pixel(int energy, int color) {
        this.energy = energy;
        this.color = color;
    }
    public int getRed() {
        return (color >> 16) & 0xFF;
    }

    public int getGreen() {
        return (color >> 8) & 0xFF;
    }

    public int getBlue() {
        return (color >> 0) & 0xFF;
    }
}
