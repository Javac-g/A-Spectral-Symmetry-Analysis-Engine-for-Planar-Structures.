package com.max.hexopt.core.topology;

import java.util.*;

public class MeshBuilder {

    private int vertexCounter = 0;
    private int faceCounter = 0;

    private final Map<Integer, Vertex> vertices = new HashMap<>();
    private final List<HalfEdge> edges = new ArrayList<>();
    private final List<Face> faces = new ArrayList<>();

    public Vertex createVertex(double x, double y) {
        Vertex v = new Vertex(vertexCounter++, x, y);
        vertices.put(v.id(), v);
        return v;
    }

    public Face createFace(List<Vertex> polygonVertices) {

        if (polygonVertices.size() < 3) {
            throw new IllegalArgumentException("Face requires 3+ vertices");
        }

        Face face = new Face(faceCounter++);
        faces.add(face);

        int n = polygonVertices.size();
        List<HalfEdge> faceEdges = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            HalfEdge e = new HalfEdge();
            e.setOrigin(polygonVertices.get(i));
            e.setFace(face);
            edges.add(e);
            faceEdges.add(e);
        }

        // close cycle
        for (int i = 0; i < n; i++) {
            faceEdges.get(i)
                     .setNext(faceEdges.get((i + 1) % n));
        }

        face.setEdge(faceEdges.get(0));

        return face;
    }

    public void linkTwins(HalfEdge a, HalfEdge b) {
        a.setTwin(b);
        b.setTwin(a);
    }

    public Mesh build() {
        MeshValidator.validate(vertices.values(), edges, faces);
        return new Mesh(vertices, edges, faces);
    }
}
