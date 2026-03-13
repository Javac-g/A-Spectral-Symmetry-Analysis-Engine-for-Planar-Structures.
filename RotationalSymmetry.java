package com.max.hexopt.core.symmetry;

import java.util.List;

public final class RotationalSymmetry {

    /**
     * Compute symmetry order from peak angles
     * @param peakAngles angles in degrees [0-360)
     * @param tolerance angle tolerance
     * @return detected order (e.g., 10 for 10-fold)
     */
    public static int detectOrder(List<Double> peakAngles, double tolerance) {

        for (int order = 2; order <= 12; order++) {
            boolean matches = peakAngles.stream().allMatch(
                angle -> {
                    double mod = angle % (360.0 / order);
                    return mod < tolerance || (360.0 / order - mod) < tolerance;
                }
            );
            if (matches) return order;
        }
        return 1; // trivial / no symmetry
    }
}
