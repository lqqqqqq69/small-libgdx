package com.github.lqqqqqq69.utils;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerTool {
    public static void main(String[] args) {
        String inputDir = "assetsnew/Objects";
        String outputDir = "assets/graphics";
        String packFileName = "Objects";

        TexturePacker.process(inputDir, outputDir, packFileName);
       
    }

}
