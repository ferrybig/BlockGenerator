/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockgenerator;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Main {

    public static void main(String[] args) throws IOException {
        MultiColor border = new MultiColor(Color.yellow, Color.yellow.darker().darker());
        MultiColor sides = new MultiColor(Color.orange, Color.orange.darker().darker());
        MultiColor middle = new MultiColor(Color.red, Color.red.darker().darker());
        int height = 128;
        int squareSize = 8;
        int paddingSize = 2;
        boolean skipBorders = true;
        MainGrid grid = new MainGrid(
                border,
                new BlockPattern(256, height, sides, 256 / squareSize, height / squareSize, paddingSize, skipBorders, false, true),
                new BlockPattern(256, height, middle, 256 / squareSize, height / squareSize, paddingSize, skipBorders, false, true),
                //new SolidColorPattern(0, height, middle),
                new BlockPattern(256, height, sides, 256 / squareSize, height / squareSize, paddingSize, skipBorders, false, true),
                new BlockPattern(256, height, border, 256 / squareSize, height / squareSize, paddingSize, skipBorders, false, true)
        );

        BufferedImage img = grid.render();
        File outputfile = new File("render.png");
        System.out.println(img.toString());
        ImageIO.write(img, "png", outputfile);
    }
}
