package com.max.hexopt.core.topology;

import java.util.*;

public final class MeshValidator {

    public static void validate(Collection<Vertex> vertices,
                                List<HalfEdge> edges,
                                List<Face> faces) {

        validateTwins(edges);
        validateFaceCycles(faces);
        validateOrigins(edges);
    }

    private static void validateTwins(List<HalfEdge> edges) {
        for (HalfEdge e : edges) {
            if (e.twin() != null && e.twin().twin() != e) {
                throw new IllegalStateException("Twin symmetry broken");
            }
        }
    }

    private static void validateFaceCycles(List<Face> faces) {
        for (Face f : faces) {
            HalfEdge start = f.edge();
            HalfEdge current = start;
            int counter = 0;

            do {
                if (current == null)
                    throw new IllegalStateException("Broken face cycle");

                current = current.next();
                counter++;

                if (counter > 10000)
                    throw new IllegalStateException("Infinite loop detected");

            } while (current != start);
        }
    }

    private static void validateOrigins(List<HalfEdge> edges) {
        for (HalfEdge e : edges) {
            if (e.origin() == null)
                throw new IllegalStateException("Edge without origin");
        }
    }
}
