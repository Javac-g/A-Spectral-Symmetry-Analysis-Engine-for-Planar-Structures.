package com.denysov.miner.game;

import com.denysov.miner.geometry.polyhedra.Vec3;
import javafx.scene.shape.MeshView;

import java.util.ArrayList;
import java.util.List;


public class TriangleCell {

    private final int id;
    private final int faceId;
    private final int row;
    private final int col;
    private final boolean up;
    private final MeshView mesh;

    private final Vec3 a;
    private final Vec3 b;
    private final Vec3 c;

    private boolean mine;
    private boolean revealed;
    private boolean flagged;
    private int neighborMines;

    private final List<TriangleCell> neighbors = new ArrayList<>();

    public TriangleCell(
            int id,
            int faceId,
            int row,
            int col,
            boolean up,
            MeshView mesh,
            Vec3 a,
            Vec3 b,
            Vec3 c
    ) {
        this.id = id;
        this.faceId = faceId;
        this.row = row;
        this.col = col;
        this.up = up;
        this.mesh = mesh;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public int getId() {
        return id;
    }

    public int getFaceId() {
        return faceId;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isUp() {
        return up;
    }

    public MeshView getMesh() {
        return mesh;
    }

    public Vec3 getA() {
        return a;
    }

    public Vec3 getB() {
        return b;
    }

    public Vec3 getC() {
        return c;
    }

    public Vec3 center() {
        return new Vec3(
                (a.x() + b.x() + c.x()) / 3.0,
                (a.y() + b.y() + c.y()) / 3.0,
                (a.z() + b.z() + c.z()) / 3.0
        );
    }

    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean mine) {
        this.mine = mine;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void toggleFlag() {
        if (!revealed) {
            flagged = !flagged;
        }
    }

    public int getNeighborMines() {
        return neighborMines;
    }

    public void setNeighborMines(int neighborMines) {
        this.neighborMines = neighborMines;
    }

    public List<TriangleCell> getNeighbors() {
        return neighbors;
    }

    public void addNeighbor(TriangleCell neighbor) {
        if (neighbor != null && neighbor != this && !neighbors.contains(neighbor)) {
            neighbors.add(neighbor);
        }
    }
}