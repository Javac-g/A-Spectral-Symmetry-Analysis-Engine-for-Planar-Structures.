package com.denysov.miner.game;

import com.denysov.miner.geometry.polyhedra.TriangleCellData;
import com.denysov.miner.geometry.polyhedra.TriangleSubdivision;
import com.denysov.miner.geometry.polyhedra.Vec3;
import com.denysov.miner.topology.PolyhedronDefinition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class GenericPolyBoard {

    private static final double EPS = 1e-6;

    private final PolyhedronDefinition definition;
    private final List<TriangleCell> cells = new ArrayList<>();
    private final Map<Integer, TriangleCell> byId = new HashMap<>();

    private final int subdivisionLevel;
    private final int mineCount;

    private boolean minesPlaced = false;
    private GameState state = GameState.READY;

    public GenericPolyBoard(PolyhedronDefinition definition, int subdivisionLevel, int mineCount) {
        this.definition = definition;
        this.subdivisionLevel = subdivisionLevel;
        this.mineCount = mineCount;

        buildCells();
        connectBySharedEdges();
    }

    private void buildCells() {
        int id = 0;
        List<Vec3> vertices = definition.getVertices();
        List<int[]> faces = definition.getFaces();

        for (int faceId = 0; faceId < faces.size(); faceId++) {
            int[] face = faces.get(faceId);

            if (face.length < 3) {
                continue;
            }

            Vec3 anchor = vertices.get(face[0]);

            for (int i = 1; i < face.length - 1; i++) {
                Vec3 b = vertices.get(face[i]);
                Vec3 c = vertices.get(face[i + 1]);

                List<TriangleCellData> subCells =
                        TriangleSubdivision.subdivide(anchor, b, c, subdivisionLevel);

                for (TriangleCellData data : subCells) {
                    TriangleCell cell = new TriangleCell(
                            id,
                            faceId,
                            data.getRow(),
                            data.getCol(),
                            data.isUp(),
                            data.getMesh(),
                            data.getA(),
                            data.getB(),
                            data.getC()
                    );

                    cells.add(cell);
                    byId.put(id, cell);
                    cell.getMesh().setUserData(id);
                    id++;
                }
            }
        }
    }

    private void connectBySharedEdges() {
        for (int i = 0; i < cells.size(); i++) {
            TriangleCell a = cells.get(i);

            for (int j = i + 1; j < cells.size(); j++) {
                TriangleCell b = cells.get(j);

                if (shareEdge(a, b)) {
                    a.addNeighbor(b);
                    b.addNeighbor(a);
                }
            }
        }
    }

    private boolean shareEdge(TriangleCell a, TriangleCell b) {
        int shared = 0;

        for (Vec3 va : List.of(a.getA(), a.getB(), a.getC())) {
            for (Vec3 vb : List.of(b.getA(), b.getB(), b.getC())) {
                if (samePoint(va, vb)) {
                    shared++;
                    break;
                }
            }
        }

        return shared >= 2;
    }

    private boolean samePoint(Vec3 a, Vec3 b) {
        return a.distanceTo(b) < EPS;
    }

    private void placeMinesAvoidingFirstClick(int safeId) {
        HashSet<Integer> forbidden = new HashSet<>();
        forbidden.add(safeId);

        TriangleCell safe = byId.get(safeId);
        if (safe != null) {
            for (TriangleCell neighbor : safe.getNeighbors()) {
                forbidden.add(neighbor.getId());
            }
        }

        List<TriangleCell> candidates = new ArrayList<>();
        for (TriangleCell cell : cells) {
            if (!forbidden.contains(cell.getId())) {
                candidates.add(cell);
            }
        }

        Collections.shuffle(candidates);

        int placed = 0;
        for (TriangleCell candidate : candidates) {
            if (placed >= mineCount) {
                break;
            }
            candidate.setMine(true);
            placed++;
        }

        calculateNumbers();
        minesPlaced = true;
    }

    private void calculateNumbers() {
        for (TriangleCell cell : cells) {
            int mines = 0;

            for (TriangleCell neighbor : cell.getNeighbors()) {
                if (neighbor.isMine()) {
                    mines++;
                }
            }

            cell.setNeighborMines(mines);
        }
    }

    public void revealCell(int id) {
        if (state == GameState.LOST || state == GameState.WON) {
            return;
        }

        TriangleCell cell = byId.get(id);
        if (cell == null || cell.isFlagged() || cell.isRevealed()) {
            return;
        }

        if (!minesPlaced) {
            placeMinesAvoidingFirstClick(id);
        }

        state = GameState.RUNNING;
        cell.setRevealed(true);

        if (cell.isMine()) {
            revealAllMines();
            state = GameState.LOST;
            return;
        }

        if (cell.getNeighborMines() == 0) {
            FloodFill.revealZeros(cell);
        }

        if (isWin()) {
            state = GameState.WON;
            revealAllMines();
        }
    }

    public void toggleFlag(int id) {
        if (state == GameState.LOST || state == GameState.WON) {
            return;
        }

        TriangleCell cell = byId.get(id);
        if (cell != null) {
            cell.toggleFlag();
        }
    }

    private void revealAllMines() {
        for (TriangleCell cell : cells) {
            if (cell.isMine()) {
                cell.setRevealed(true);
            }
        }
    }

    private boolean isWin() {
        for (TriangleCell cell : cells) {
            if (!cell.isMine() && !cell.isRevealed()) {
                return false;
            }
        }
        return true;
    }

    public PolyhedronDefinition getDefinition() {
        return definition;
    }

    public List<TriangleCell> getCells() {
        return cells;
    }

    public TriangleCell getCell(int id) {
        return byId.get(id);
    }

    public int getSubdivisionLevel() {
        return subdivisionLevel;
    }

    public int getMineCount() {
        return mineCount;
    }

    public boolean isMinesPlaced() {
        return minesPlaced;
    }

    public long getFlagCount() {
        return cells.stream().filter(TriangleCell::isFlagged).count();
    }

    public GameState getState() {
        return state;
    }
}
