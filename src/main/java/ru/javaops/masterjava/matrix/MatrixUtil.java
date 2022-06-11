package ru.javaops.masterjava.matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * gkislin
 * 03.07.2016
 */
public class MatrixUtil {

    public static int[][] concurrentMultiply(int[][] matrixA, int[][] matrixB, ExecutorService executor) throws InterruptedException, ExecutionException {
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];
        final int[][] matrixBT = tMatrix(matrixB);
        List<Future<Void>> futures = new ArrayList<>();
        for (int i = 0; i < matrixSize; i++) {
            int finalI = i;
            futures.add(executor.submit(() -> {
                for (int j = 0; j < matrixSize; j++) {
                    int sum = 0;
                    for (int k = 0; k < matrixSize; k++) {
                        sum += matrixA[finalI][k] * matrixBT[j][k];
                    }
                    matrixC[finalI][j] = sum;
                }
                return null;
            }));
        }
        while (!futures.isEmpty()) {
            if (futures.get(0).isDone()){
                futures.remove(0);
            }
        }
        return matrixC;
    }

    public static int[][] singleThreadMultiply(int[][] matrixA, int[][] matrixB) {
        final int[][] matrixBT = tMatrix(matrixB);
        final int matrixSize = matrixA.length;
        final int[][] matrixC = new int[matrixSize][matrixSize];

        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                int sum = 0;
                for (int k = 0; k < matrixSize; k++) {
                    sum += matrixA[i][k] * matrixBT[j][k];
                }
                matrixC[i][j] = sum;
            }
        }
        return matrixC;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        /*int[][] ints = create(2);
        System.out.println(Arrays.deepToString(ints));
        int[][] ints1 = create(2);
        System.out.println(Arrays.deepToString(ints1));
        int[][] ints2 = concurrentMultiply(ints, ints1, Executors.newFixedThreadPool(10));
        System.out.println(Arrays.deepToString(ints2));*/
        System.out.println(Runtime.getRuntime().availableProcessors());
    }

    private static int[][] tMatrix(int[][] matrix) {
        int size = matrix.length;
        int[][] tmarrix = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tmarrix[i][j] = matrix[j][i];
            }
        }
        return tmarrix;
    }

    public static int[][] create(int size) {
        int[][] matrix = new int[size][size];
        Random rn = new Random();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = rn.nextInt(10);
            }
        }
        return matrix;
    }

    public static boolean compare(int[][] matrixA, int[][] matrixB) {
        final int matrixSize = matrixA.length;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++) {
                if (matrixA[i][j] != matrixB[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
