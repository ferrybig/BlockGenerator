/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockgenerator;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class MainGrid {

    private final MultiColor color;
    private final ColorPattern[] patterns;
    private final int sizeX;
    private final int sizeY;

    public MainGrid(MultiColor color, ColorPattern... patterns) {
        this.color = color;
        this.patterns = patterns;
        if (patterns.length == 0) {
            throw new IllegalArgumentException("Invalid pattern");
        }

        int y = 0;
        int x = 0;
        MultiColor old = color;
        for (int i = 0; i < patterns.length; i++) {
            if (y == 0) {
                y = patterns[i].sizeY();
            } else if (y != patterns[i].sizeY()) {
                throw new IllegalArgumentException("Invalid Y sizes detected");
            }
            x += patterns[i].sizeX();
            patterns[i].initPattern(old);
            old = patterns[i].getColor();
        }

        this.sizeX = x;
        this.sizeY = y;
    }

    public BufferedImage render() {
        BufferedImage img = new BufferedImage(
            sizeX,
            sizeY,
            BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        try {
            renderPatterns(g2);
        } finally {
            g2.dispose();
        }
        return img;
    }

    private void renderPatterns(Graphics2D g2) {
        int x = 0;
        MultiColor old = color;
        for (int i = 0; i < patterns.length; i++) {
            patterns[i].renderPattern(old, g2, x);
            x += patterns[i].sizeX();
            old = patterns[i].getColor();
        }
    }
}
