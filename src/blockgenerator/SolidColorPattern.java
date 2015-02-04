/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blockgenerator;

import java.awt.Graphics2D;

public class SolidColorPattern implements ColorPattern{
    private final int x;
    private final int y;
    
    private final MultiColor color;

    public SolidColorPattern(int x, int y, MultiColor color) {
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public MultiColor getColor() {
        return color;
    }

    @Override
    public boolean usesSideColor() {
        return false;
    }

    @Override
    public int sizeX() {
        return x;
    }

    @Override
    public int sizeY() {
        return y;
    }

    @Override
    public void initPattern(MultiColor other) {
        
    }

    @Override
    public void renderPattern(MultiColor other, Graphics2D target, int xOffset) {
        target.fillRect(xOffset, 0, x, y);
    }
}
