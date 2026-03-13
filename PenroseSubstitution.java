package com.max.hexopt.core.aperiodic;

import com.max.hexopt.core.geometry.SpatialVertexRegistry;
import com.max.hexopt.core.topology.MeshBuilder;
import com.max.hexopt.core.topology.TwinEdgeLinker;

import java.util.ArrayList;
import java.util.List;

public class PenroseSubstitution {

    private static final double PHI = (1 + Math.sqrt(5)) / 2;

    private final SpatialVertexRegistry registry;
    private final MeshBuilder meshBuilder;
    private final RhombMeshFactory rhombFactory;

    public PenroseSubstitution(SpatialVertexRegistry registry,
                               MeshBuilder meshBuilder) {
        this.registry = registry;
        this.meshBuilder = meshBuilder;
        this.rhombFactory = new RhombMeshFactory(registry, meshBuilder);
    }

    public void substitute(RhombTile tile) {

        switch (tile.type) {
            case THICK -> substituteThick(tile);
            case THIN -> substituteThin(tile);
        }

        // Link all twins after adding new rhombs
        TwinEdgeLinker.linkTwins(meshBuilder.build().edges());
    }

    private void substituteThick(RhombTile tile) {

        // vertices of original rhomb
        Vertex A = tile.a;
        Vertex B = tile.b;
        Vertex C = tile.c;
        Vertex D = tile.d;

        // Compute new points using golden ratio
        Vertex P = registry.getOrCreate(A.x() + (B.x() - A.x()) / PHI,
                                        A.y() + (B.y() - A.y()) / PHI);

        Vertex Q = registry.getOrCreate(D.x() + (C.x() - D.x()) / PHI,
                                        D.y() + (C.y() - D.y()) / PHI);

        // Subdivide: 2 THICK, 3 THIN rhombs
        rhombFactory.createRhomb(A.x(), A.y(), edgeLength(A,B)/PHI, angle(A,B), RhombType.THICK);
        rhombFactory.createRhomb(C.x(), C.y(), edgeLength(C,D)/PHI, angle(C,D), RhombType.THICK);

        rhombFactory.createRhomb(P.x(), P.y(), edgeLength(P,B)/PHI, angle(P,B), RhombType.THIN);
        rhombFactory.createRhomb(Q.x(), Q.y(), edgeLength(Q,C)/PHI, angle(Q,C), RhombType.THIN);
        rhombFactory.createRhomb(B.x(), B.y(), edgeLength(B,P)/PHI, angle(B,P), RhombType.THIN);
    }

    private void substituteThin(RhombTile tile) {
        Vertex A = tile.a;
        Vertex B = tile.b;
        Vertex C = tile.c;
        Vertex D = tile.d;

        // Compute new point using golden ratio
        Vertex P = registry.getOrCreate(A.x() + (D.x() - A.x()) / PHI,
                                        A.y() + (D.y() - A.y()) / PHI);

        // Subdivide: 1 THICK, 2 THIN rhombs
        rhombFactory.createRhomb(P.x(), P.y(), edgeLength(A,D)/PHI, angle(A,D), RhombType.THICK);
        rhombFactory.createRhomb(A.x(), A.y(), edgeLength(A,B)/PHI, angle(A,B), RhombType.THIN);
        rhombFactory.createRhomb(D.x(), D.y(), edgeLength(D,C)/PHI, angle(D,C), RhombType.THIN);
    }

    // Helper functions for vector distance & angle
    private double edgeLength(Vertex v1, Vertex v2) {
        return Math.hypot(v2.x() - v1.x(), v2.y() - v1.y());
    }

    private double angle(Vertex v1, Vertex v2) {
        return Math.atan2(v2.y() - v1.y(), v2.x() - v1.x()) * (180.0 / Math.PI);
    }
}
