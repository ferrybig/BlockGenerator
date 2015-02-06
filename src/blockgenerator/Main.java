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
        MultiColor sides = new MultiColor(Color.yellow, Color.yellow.darker().darker());
        MultiColor middle = new MultiColor(Color.orange, Color.orange.darker().darker());
        MainGrid grid = new MainGrid(
            sides,
            new BlockPattern(256, 1024, middle, 256 / 16, 1024 / 16, 3, true, false, true),
            new SolidColorPattern(512, 1024, middle),
            new BlockPattern(256, 1024, sides, 256 / 16, 1024 / 16, 3, true, false, true)
        );

        BufferedImage img = grid.render();
        File outputfile = new File("render.png");
        System.out.println(img.toString());
        ImageIO.write(img, "png", outputfile);
    }
}
