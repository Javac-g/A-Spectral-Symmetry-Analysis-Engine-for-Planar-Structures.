package com.max.hexopt.core.spectral;

import com.max.hexopt.core.topology.Vertex;

public final class GridSampler {

    private final int N; // grid resolution
    private final double minX, minY, maxX, maxY;
    private final double[][] grid;

    public GridSampler(List<Vertex> vertices, int N) {
        this.N = N;

        minX = vertices.stream().mapToDouble(Vertex::x).min().orElse(0);
        minY = vertices.stream().mapToDouble(Vertex::y).min().orElse(0);
        maxX = vertices.stream().mapToDouble(Vertex::x).max().orElse(1);
        maxY = vertices.stream().mapToDouble(Vertex::y).max().orElse(1);

        grid = new double[N][N];

        for (Vertex v : vertices) {
            int gx = (int) ((v.x() - minX) / (maxX - minX) * (N - 1));
            int gy = (int) ((v.y() - minY) / (maxY - minY) * (N - 1));
            grid[gx][gy] += 1.0; // count multiple vertices per cell
        }
    }

    public double[][] grid() {
        return grid;
    }
}
