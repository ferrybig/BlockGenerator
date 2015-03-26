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
import java.util.Random;
import javax.imageio.ImageIO;

public class Main {

    public static void main(String[] args) throws IOException {
        MultiColor background = new MultiColor(Color.green.darker(), Color.green.darker().darker().darker());
        MultiColor border = new MultiColor(Color.orange, Color.orange.darker().darker());
        MultiColor sides = new MultiColor(Color.blue, Color.blue.darker().darker());
        MultiColor middle = new MultiColor(Color.red, Color.red.darker().darker());
        MultiColor heart = new MultiColor(new Color(164,16,16), new Color(164,16,16).darker().darker());
        int height = 256;
        int squareSize = 8;
        int paddingSize = 2;
        boolean skipBorders = true;
        Random random = new Random();
        MainGrid grid = new MainGrid(
                background,
                new BlockPattern(random, 128, height, border, 128 / squareSize, height / squareSize, paddingSize, skipBorders, false, true),
                new BlockPattern(random, 128, height, sides, 128 / squareSize, height / squareSize, paddingSize, skipBorders, false, true),
                new BlockPattern(random, 128, height, middle, 128 / squareSize, height / squareSize, paddingSize, skipBorders, false, true),
                new BlockPattern(random, 128, height, heart, 128 / squareSize, height / squareSize, paddingSize, skipBorders, false, true),
                //new SolidColorPattern(0, height, middle),
                new BlockPattern(random, 128, height, middle, 128 / squareSize, height / squareSize, paddingSize, skipBorders, false, true),
                new BlockPattern(random, 128, height, sides, 128 / squareSize, height / squareSize, paddingSize, skipBorders, false, true),
                new BlockPattern(random, 128, height, border, 128 / squareSize, height / squareSize, paddingSize, skipBorders, false, true),
                new BlockPattern(random, 128, height, background, 128 / squareSize, height / squareSize, paddingSize, skipBorders, false, true)
        );

        BufferedImage img = grid.render();
        File outputfile = new File("render.png");
        System.out.println(img.toString());
        ImageIO.write(img, "png", outputfile);
    }
}
