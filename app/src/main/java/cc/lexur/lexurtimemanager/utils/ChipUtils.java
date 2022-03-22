package cc.lexur.lexurtimemanager.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;

public class ChipUtils {
    public static ColorStateList setChipColor(int color){
        //状态
        int[][] states = new int[1][];
//        //按下
//        states[0] = new int[] {android.R.attr.state_pressed};
        //默认
        states[0] = new int[] {};
        int[] colors = new int[] {color};

        ColorStateList colorStateList = new ColorStateList(states,colors);
        return colorStateList;
    }
}
