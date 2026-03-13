package com.max.hexopt.core.spectral;

import java.util.ArrayList;
import java.util.List;

public final class SpectralPeaks {

    public static List<int[]> findPeaks(double[][] magnitude, double thresholdRatio) {

        double max = 0;
        int N = magnitude.length;

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (magnitude[i][j] > max) max = magnitude[i][j];

        double threshold = max * thresholdRatio;
        List<int[]> peaks = new ArrayList<>();

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (magnitude[i][j] >= threshold)
                    peaks.add(new int[]{i, j});

        return peaks;
    }
}
