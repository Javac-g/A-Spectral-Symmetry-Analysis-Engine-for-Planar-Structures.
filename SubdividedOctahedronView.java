package com.denysov.miner.render;


import com.denysov.miner.geometry.polyhedra.TriangleCellData;
import com.denysov.miner.geometry.polyhedra.TriangleSubdivision;
import com.denysov.miner.geometry.polyhedra.Vec3;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import java.util.ArrayList;
import java.util.List;

public class SubdividedOctahedronView {

    private final List<TriangleCellData> cells = new ArrayList<>();

    public Group build(int level, double size) {
        Group root = new Group();

        Vec3 top = new Vec3(0, -size, 0);
        Vec3 left = new Vec3(-size, 0, 0);
        Vec3 back = new Vec3(0, 0, -size);
        Vec3 right = new Vec3(size, 0, 0);
        Vec3 front = new Vec3(0, 0, size);
        Vec3 bottom = new Vec3(0, size, 0);

        List<List<Vec3>> faces = List.of(
                List.of(top, left, back),
                List.of(top, back, right),
                List.of(top, right, front),
                List.of(top, front, left),

                List.of(bottom, back, left),
                List.of(bottom, right, back),
                List.of(bottom, front, right),
                List.of(bottom, left, front)
        );

        PhongMaterial upMat = new PhongMaterial(Color.web("#4ffbdf"));
        PhongMaterial downMat = new PhongMaterial(Color.web("#00d2fc"));
        PhongMaterial openMat = new PhongMaterial(Color.web("#fefedf"));

        for (List<Vec3> face : faces) {
            List<TriangleCellData> faceCells =
                    TriangleSubdivision.subdivide(face.get(0), face.get(1), face.get(2), level);

            for (TriangleCellData cell : faceCells) {
                cell.getMesh().setMaterial(cell.isUp() ? upMat : downMat);

                cell.getMesh().setOnMouseClicked(e -> {
                    cell.getMesh().setMaterial(openMat);
                    e.consume();
                });

                cells.add(cell);
                root.getChildren().add(cell.getMesh());
            }
        }

        return root;
    }

    public List<TriangleCellData> getCells() {
        return cells;
    }
}
