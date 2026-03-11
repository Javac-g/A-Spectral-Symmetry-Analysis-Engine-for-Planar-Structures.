package com.max.hexopt.core.topology;

public class HalfEdge {

    private Vertex origin;
    private HalfEdge twin;
    private HalfEdge next;
    private Face face;

    public Vertex origin() { return origin; }
    public HalfEdge twin() { return twin; }
    public HalfEdge next() { return next; }
    public Face face() { return face; }

    void setOrigin(Vertex origin) { this.origin = origin; }
    void setTwin(HalfEdge twin) { this.twin = twin; }
    void setNext(HalfEdge next) { this.next = next; }
    void setFace(Face face) { this.face = face; }
}
