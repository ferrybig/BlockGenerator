/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockgenerator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

/**
 *
 * @author Fernando
 */
public class BlockGenerator {

    private final int pictureSizeX = 64;
    private final int pictureSizeY = 64;
    private final int squareSize = 16;
    private final int paddingSize = 3;
    //private final int roundedCornerSize = 0;
    private final Color background = Color.decode("#bfcfa0");
    private final Color foreground = Color.decode("#34403c");
    private final Color backgroundPad = background.darker().darker();
    private final Color foregroundPad = foreground.darker().darker();
    private final boolean skipCorners = false;

    public BlockGenerator() {
    }

    private boolean[][] createGrid() {
        Random random = new Random();
        boolean[][] grid = new boolean[pictureSizeX][pictureSizeY];

        for (int y = 0; y < pictureSizeY; y++) {
            double change = Math.min(1, Math.max(0, (Math.abs(y - 31.5) - 10)
                / 21.0));
            for (int x = 0; x < pictureSizeX; x++) {
                grid[x][y] = random.nextDouble() < change;
            }
        }
        return grid;
    }

    private boolean[][] removeUnneededFields(boolean[][] grid) {
        int changed;
        final int pictureSizeYMinusOne = pictureSizeY - 1;
        final int pictureSizeXMinusOne = pictureSizeX - 1;
        do {
            changed = 0;
            boolean[][] newGrid = new boolean[pictureSizeY][pictureSizeX];
            for (int x = 0; x < pictureSizeX - 0; x++) {
                for (int y = 0; y < pictureSizeY; y++) {
                    int yPlusOne = pictureSizeYMinusOne == y ? 0 : y + 1;
                    int yMinusOne = y < 1 ? pictureSizeYMinusOne : y - 1;
                    int xPlusOne = x == pictureSizeXMinusOne ? 0 : x + 1;
                    int xMinusOne = x == 0 ? pictureSizeXMinusOne : x - 1;

                    boolean top = grid[x][yPlusOne];
                    boolean bottom = grid[x][yMinusOne];
                    boolean left = grid[xMinusOne][y];
                    boolean right = grid[xPlusOne][y];
                    if ((top && bottom && left && right) && (!grid[x][y])) {
                        newGrid[x][y] = true;
                        changed++;
                    } else if (((!top) && (!bottom) && (!left) && (!right))
                        && (grid[x][y])) {
                        newGrid[x][y] = false;
                        changed++;
                    } else {
                        newGrid[x][y] = grid[x][y];
                    }
                }
            }
            grid = newGrid;
        } while (changed > 0);
        return grid;
    }

    private boolean[][][][] preRender(boolean[][] grid) {
        final int pictureSizeYMinusOne = pictureSizeY - 1;
        final int pictureSizeXMinusOne = pictureSizeX - 1;
        boolean[][][][] graphicGrid
            = new boolean[pictureSizeY][pictureSizeX][3][3];
        for (int x = 0; x < pictureSizeY; x++) {
            for (int y = 0; y < pictureSizeX; y++) {

                int yPlusOne = pictureSizeYMinusOne == y ? 0 : y + 1;
                int yMinusOne = y < 1 ? pictureSizeYMinusOne : y - 1;
                int xPlusOne = x == pictureSizeXMinusOne ? 0 : x + 1;
                int xMinusOne = x == 0 ? pictureSizeXMinusOne : x - 1;

                boolean currentType = grid[x][y];
                boolean toFind = !currentType;
                boolean[][] subGrid = graphicGrid[y][x];

                subGrid[0][0] = grid[xMinusOne][yMinusOne] == toFind;
                subGrid[1][0] = grid[xMinusOne][y] == toFind;
                subGrid[2][0] = grid[xMinusOne][yPlusOne] == toFind;

                subGrid[0][1] = grid[x][yMinusOne] == toFind;
                subGrid[1][1] = currentType;
                subGrid[2][1] = grid[x][yPlusOne] == toFind;

                subGrid[0][2] = grid[xPlusOne][yMinusOne] == toFind;
                subGrid[1][2] = grid[xPlusOne][y] == toFind;
                subGrid[2][2] = grid[xPlusOne][yPlusOne] == toFind;
            }
        }
        return graphicGrid;
    }

    private BufferedImage render(boolean[][][][] graphicGrid) {
        BufferedImage img = new BufferedImage(
            pictureSizeX * squareSize,
            pictureSizeY * squareSize,
            BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = img.createGraphics();
        try {

            for (int x = 0; x < pictureSizeY; x++) {
                for (int y = 0; y < pictureSizeX; y++) {
                    boolean[][] subGrid = graphicGrid[x][y];
                    boolean middle = subGrid[1][1];
                    Color mainColor = middle ? foreground : background;
                    Color paddingColor = middle ? foregroundPad : backgroundPad;
                    g2.setColor(mainColor);
                    g2.fillRect(
                        x * squareSize,
                        y * squareSize,
                        squareSize,
                        squareSize);
                    g2.setColor(paddingColor);

                    // top-left
                    if (subGrid[0][0] && !skipCorners) {
                        g2.fillRect(
                            x * squareSize,
                            y * squareSize,
                            paddingSize,
                            paddingSize);
                    }

                    // top-center
                    if (subGrid[1][0]) {
                        g2.fillRect(
                            x * squareSize,
                            y * squareSize,
                            squareSize,
                            paddingSize);
                    }

                    // top-right
                    if (subGrid[2][0] && !skipCorners) {
                        g2.fillRect(
                            x * squareSize + squareSize - paddingSize,
                            y * squareSize,
                            paddingSize,
                            paddingSize);
                    }

                    // center-left 
                    if (subGrid[0][1]) {
                        g2.fillRect(
                            x * squareSize,
                            y * squareSize,
                            paddingSize,
                            squareSize);
                    }

                    // center-right
                    if (subGrid[2][1]) {
                        g2.fillRect(
                            x * squareSize + squareSize - paddingSize,
                            y * squareSize,
                            paddingSize,
                            squareSize);
                    }

                    // bottom-left 
                    if (subGrid[0][2] && !skipCorners) {
                        g2.fillRect(
                            x * squareSize,
                            y * squareSize + squareSize - paddingSize,
                            paddingSize,
                            paddingSize);
                    }

                    // bottom-center 
                    if (subGrid[1][2]) {
                        g2.fillRect(
                            x * squareSize,
                            y * squareSize + squareSize - paddingSize,
                            squareSize,
                            paddingSize);
                    }

                    // bottom-right 
                    if (subGrid[2][2] && !skipCorners) {
                        g2.fillRect(
                            x * squareSize + squareSize - paddingSize,
                            y * squareSize + squareSize - paddingSize,
                            paddingSize,
                            paddingSize);
                    }
                }
            }
        } finally {
            g2.dispose();
        }
        return img;
    }

    public BufferedImage fullRender() {
        return render(preRender(removeUnneededFields(createGrid())));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        BlockGenerator generator = new BlockGenerator();
        BufferedImage img = generator.fullRender();
        File outputfile = new File("render.png");
        ImageIO.write(img, "png", outputfile);
    }
}
