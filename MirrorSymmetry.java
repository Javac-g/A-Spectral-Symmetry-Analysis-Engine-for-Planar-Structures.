package com.max.hexopt.core.symmetry;

import com.max.hexopt.core.topology.Vertex;

import java.util.List;

public final class MirrorSymmetry {

    /**
     * Checks if a candidate axis x = c preserves symmetry
     */
    public static boolean checkVerticalAxis(List<Vertex> vertices, double x0, double tolerance) {
        for (Vertex v : vertices) {
            boolean reflectedExists = vertices.stream().anyMatch(
                u -> Math.abs(u.x() - (2*x0 - v.x())) < tolerance
                     && Math.abs(u.y() - v.y()) < tolerance
            );
            if (!reflectedExists) return false;
        }
        return true;
    }
}
