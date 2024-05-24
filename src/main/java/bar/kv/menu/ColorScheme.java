package bar.kv.menu;

import java.awt.*;

public class ColorScheme {
    private Color darkColor;
    private Color brightColor;
    private Color mediumColor;

    public ColorScheme() {
        darkColor = new Color(20, 21, 27);
        brightColor = new Color(210, 143, 32);
        mediumColor = new Color(112, 122, 142);
    }

    public ColorScheme(Color darkColor, Color brightColor, Color mediumColor) {
        setDarkColor(darkColor);
        setBrightColor(brightColor);
        setMediumColor(mediumColor);
    }

    public Color getDarkColor() {
        return darkColor;
    }

    public void setDarkColor(Color darkColor) {
        this.darkColor = darkColor;
    }

    public Color getBrightColor() {
        return brightColor;
    }

    public void setBrightColor(Color brightColor) {
        this.brightColor = brightColor;
    }

    public Color getMediumColor() {
        return mediumColor;
    }

    public void setMediumColor(Color mediumColor) {
        this.mediumColor = mediumColor;
    }
}