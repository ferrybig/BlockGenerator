/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blockgenerator;

import java.awt.Graphics2D;


public interface ColorPattern {

    public MultiColor getColor();

    public boolean usesSideColor();

    public int sizeX();
    
    public int sizeY();

    public void initPattern(MultiColor other);

    public void renderPattern(MultiColor other, Graphics2D target, int xOffset);
    
}
