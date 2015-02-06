/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockgenerator;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class BlockPattern implements ColorPattern {

    private final int xSize;
    private final int ySize;

    private final MultiColor color;
    private final int tilesX;
    private final int tilesY;

    private final double stepX, stepY;

    private final int paddingSize;
    private final boolean skipCorners;

    private boolean[][] grid;

    private final boolean horizontalWrap;
    private final boolean verticalWrap;

    public BlockPattern(int xSize, int ySize, MultiColor color, int tilesX, int tilesY, int paddingSize, boolean skipCorners, boolean horizontalWrap, boolean verticalWrap) {
        this.xSize = xSize;
        this.ySize = ySize;
        this.color = color;
        this.tilesX = tilesX;
        this.tilesY = tilesY;
        this.stepX = this.xSize * 1.0 / tilesX;
        this.stepY = this.ySize * 1.0 / tilesY;
        this.paddingSize = paddingSize;
        this.skipCorners = skipCorners;
        this.horizontalWrap = horizontalWrap;
        this.verticalWrap = verticalWrap;
    }

    private boolean[][] createGrid() {
        Random random = new Random();
        boolean[][] grid = new boolean[tilesX][tilesY];
        for (int x = 0; x < tilesX; x++) {
            double change = x * 1.0 / (tilesX - 1);
            for (int y = 0; y < tilesY; y++) {
                grid[x][y] = random.nextDouble() < change;
            }
        }
        return grid;
    }

    private int getRawCordinateX(int x) {
        if (horizontalWrap) {
            while (x > tilesX - 1) {
                x -= tilesX;
            }
            while (x < 0) {
                x += tilesX;
            }
            return x;
        } else {
            if (x > tilesX - 1) {
                return tilesX - 1;
            } else if (x < 0) {
                return 0;
            }
            return x;
        }
    }

    private int getRawCordinateY(int y) {
        if (verticalWrap) {
            while (y > tilesY - 1) {
                y -= tilesY;
            }
            while (y < 0) {
                y += tilesY;
            }
            return y;
        } else {
            if (y > tilesY - 1) {
                return tilesY - 1;
            } else if (y < 0) {
                return 0;
            }
            return y;
        }
    }

    private boolean[][] removeUnneededFields(boolean[][] grid) {
        int changed;
        do {
            changed = 0;
            boolean[][] newGrid = new boolean[tilesX][tilesY];
            for (int x = 0; x < tilesX; x++) {
                for (int y = 0; y < tilesY; y++) {
                    int on = 0;
                    int off = 0;
                    for(int i = -1; i < 2; i++) {
                        int rawX = getRawCordinateX(x + i);
                        for(int j = -1; j < 2; j++) {
                            if(i != 0 && j != 0) continue;
                            int rawY = getRawCordinateY(y + j);
                            if(grid[rawX][rawY]) {
                                on++;
                            } else {
                                off++;
                            }
                        }
                    }
                    boolean selfOn = grid[x][y];
                    if(!selfOn && off < 2) {
                        newGrid[x][y] = true;
                        changed++;
                    } else if (selfOn && on < 2) {
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
        boolean[][][][] graphicGrid
            = new boolean[tilesX][tilesY][3][3];
        for (int x = 0; x < tilesX; x++) {
            for (int y = 0; y < tilesY; y++) {

                int yPlusOne = getRawCordinateY(y + 1);
                int yMinusOne = getRawCordinateY(y - 1);
                int xPlusOne = getRawCordinateX(x + 1);
                int xMinusOne = getRawCordinateX(x - 1);

                boolean currentType = grid[x][y];
                boolean toFind = !currentType;
                boolean[][] subGrid = graphicGrid[x][y];

                subGrid[0][0] = grid[xMinusOne][yMinusOne] == toFind;
                subGrid[0][1] = grid[xMinusOne][y] == toFind;
                subGrid[0][2] = grid[xMinusOne][yPlusOne] == toFind;

                subGrid[1][0] = grid[x][yMinusOne] == toFind;
                subGrid[1][1] = currentType;
                subGrid[1][2] = grid[x][yPlusOne] == toFind;

                subGrid[2][0] = grid[xPlusOne][yMinusOne] == toFind;
                subGrid[2][1] = grid[xPlusOne][y] == toFind;
                subGrid[2][2] = grid[xPlusOne][yPlusOne] == toFind;
            }
        }
        return graphicGrid;
    }

    private int getSquareStartX(int x) {
        if (x == 0) {
            return 0;
        } else if (x == this.tilesX) {
            return this.xSize;
        } else {
            return (int) Math.round(this.stepX * x);
        }
    }

    private int getSquareStartY(int y) {
        if (y == 0) {
            return 0;
        } else if (y == this.tilesY) {
            return this.ySize;
        } else {
            return (int) Math.round(this.stepY * y);
        }
    }

    private int getSquareSizeX(int x) {
        return getSquareStartX(x + 1) - getSquareStartX(x);
    }

    private int getSquareSizeY(int y) {
        return getSquareStartY(y + 1) - getSquareStartY(y);
    }

    private void render(boolean[][][][] graphicGrid, Graphics2D g2,
        MultiColor other, int xoff) {
        for (int x = 0; x < tilesX; x++) {
            int sx = this.getSquareStartX(x) + xoff;
            int ex = this.getSquareStartX(x + 1) + xoff;
            int lx = ex - sx;
            for (int y = 0; y < tilesY; y++) {
                int sy = this.getSquareStartY(y);
                int ey = this.getSquareStartY(y + 1);
                int ly = ey - sy;

                boolean[][] subGrid = graphicGrid[x][y];

                boolean middle = subGrid[1][1];
                Color mainColor = middle ? color.getMainColor()
                    : other.getMainColor();
                Color paddingColor = middle ? color.getSideColor()
                    : other.getSideColor();
                g2.setColor(mainColor);
                g2.fillRect(
                    sx,
                    sy,
                    lx,
                    ly);
                g2.setColor(paddingColor);

                // top-left
                if (subGrid[0][0] && !skipCorners) {
                    g2.fillRect(
                        sx,
                        sy,
                        paddingSize,
                        paddingSize);
                }

                // top-center
                if (subGrid[1][0]) {
                    g2.fillRect(
                        sx,
                        sy,
                        lx,
                        paddingSize);
                }

                // top-right
                if (subGrid[2][0] && !skipCorners) {
                    g2.fillRect(
                        ex - paddingSize,
                        sy,
                        paddingSize,
                        paddingSize);
                }

                // center-left 
                if (subGrid[0][1]) {
                    g2.fillRect(
                        sx,
                        sy,
                        paddingSize,
                        ly);
                }

                // center-right
                if (subGrid[2][1]) {
                    g2.fillRect(
                        ex - paddingSize,
                        sy,
                        paddingSize,
                        ly);
                }

                // bottom-left 
                if (subGrid[0][2] && !skipCorners) {
                    g2.fillRect(
                        sx,
                        ey - paddingSize,
                        paddingSize,
                        paddingSize);
                }

                // bottom-center 
                if (subGrid[1][2]) {
                    g2.fillRect(
                        sx,
                        ey - paddingSize,
                        lx,
                        paddingSize);
                }

                // bottom-right 
                if (subGrid[2][2] && !skipCorners) {
                    g2.fillRect(
                        ex - paddingSize,
                        ey - paddingSize,
                        paddingSize,
                        paddingSize);
                }
            }
        }
    }

    @Override
    public MultiColor getColor() {
        return color;
    }

    @Override
    public boolean usesSideColor() {
        return true;
    }

    @Override
    public int sizeX() {
        return xSize;
    }

    @Override
    public int sizeY() {
        return ySize;
    }

    @Override
    public void initPattern(MultiColor other) {
        this.grid = removeUnneededFields(createGrid());
    }

    @Override
    public void renderPattern(MultiColor other, Graphics2D target, int xOffset) {
        boolean[][][][] pre = this.preRender(grid);
        this.render(pre, target, other, xOffset);
    }

}
