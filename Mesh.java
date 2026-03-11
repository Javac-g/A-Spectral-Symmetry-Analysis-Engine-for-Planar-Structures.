package com.max.hexopt.core.topology;

import java.util.*;

public class Mesh {

    private final Map<Integer, Vertex> vertices;
    private final List<HalfEdge> edges;
    private final List<Face> faces;

    Mesh(Map<Integer, Vertex> vertices,
         List<HalfEdge> edges,
         List<Face> faces) {

        this.vertices = Map.copyOf(vertices);
        this.edges = List.copyOf(edges);
        this.faces = List.copyOf(faces);
    }

    public Collection<Vertex> vertices() { return vertices.values(); }
    public List<HalfEdge> edges() { return edges; }
    public List<Face> faces() { return faces; }
}
