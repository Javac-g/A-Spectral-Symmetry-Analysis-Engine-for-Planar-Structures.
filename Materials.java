package com.denysov.miner.render;


import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

public final class Materials {

    public static final PhongMaterial HIDDEN = new PhongMaterial(Color.web("#1d3557"));
    public static final PhongMaterial REVEALED = new PhongMaterial(Color.web("#d9d9d9"));
    public static final PhongMaterial MINE = new PhongMaterial(Color.web("#c1121f"));
    public static final PhongMaterial FLAG = new PhongMaterial(Color.web("#f4a261"));

    private Materials() {
    }
}