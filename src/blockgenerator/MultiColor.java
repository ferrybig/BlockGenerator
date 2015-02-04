/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package blockgenerator;

import java.awt.Color;

public class MultiColor {
    private final Color mainColor;
    private final Color sideColor;

    public MultiColor(Color mainColor, Color sideColor) {
        this.mainColor = mainColor;
        this.sideColor = sideColor;
    }

    public Color getMainColor(int x) {
        return mainColor;
    }

    public Color getSideColor(int x) {
        return sideColor;
    }

}
