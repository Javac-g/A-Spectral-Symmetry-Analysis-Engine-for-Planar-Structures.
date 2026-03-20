package com.denysov.miner.topology;

import com.denysov.miner.geometry.polyhedra.Vec3;

import java.util.ArrayList;
import java.util.List;

public class PolyhedronDefinition {

    private final String name;
    private final List<Vec3> vertices;
    private final List<int[]> faces;

    public PolyhedronDefinition(String name, List<Vec3> vertices, List<int[]> faces) {
        this.name = name;
        this.vertices = List.copyOf(vertices);

        List<int[]> copy = new ArrayList<>();
        for (int[] face : faces) {
            copy.add(face.clone());
        }
        this.faces = List.copyOf(copy);
    }

    public String getName() {
        return name;
    }

    public List<Vec3> getVertices() {
        return vertices;
    }

    public List<int[]> getFaces() {
        return faces;
    }
}
