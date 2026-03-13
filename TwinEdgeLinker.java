package com.max.hexopt.core.topology;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class TwinEdgeLinker {

    public static void linkTwins(List<HalfEdge> edges) {
        Map<Long, HalfEdge> edgeMap = new HashMap<>();

        for (HalfEdge e : edges) {
            Vertex from = e.origin();
            Vertex to = e.next().origin();

            long key = canonicalEdgeKey(from, to);

            if (edgeMap.containsKey(key)) {
                HalfEdge twin = edgeMap.get(key);
                e.setTwin(twin);
                twin.setTwin(e);
                edgeMap.remove(key); // done linking
            } else {
                edgeMap.put(key, e);
            }
        }

        if (!edgeMap.isEmpty()) {
            System.out.println("Warning: " + edgeMap.size() + " edges have no twin");
        }
    }

    private static long canonicalEdgeKey(Vertex v1, Vertex v2) {
        int min = Math.min(v1.id(), v2.id());
        int max = Math.max(v1.id(), v2.id());
        return (((long) min) << 32) | (max & 0xffffffffL);
    }
}
