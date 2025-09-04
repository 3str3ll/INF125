import java.util.Scanner;

public class Main {
    public static double[][] leerMatriz(Scanner scanner, int n) {
        double[][] matriz = new double[n][n];
        System.out.println("\nIntroduce los elementos de la matriz " + n + "x" + n + ":");

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.printf("Elemento [%d][%d]: ", i + 1, j + 1);
                matriz[i][j] = scanner.nextDouble();
            }
        }
        return matriz;
    }
    public static double[] leerVector(Scanner scanner, int n) {
        double[] vector = new double[n];
        System.out.println("\nIntroduce los elementos del vector término independiente:");
        for (int i = 0; i < n; i++) {
            System.out.printf("b[%d]: ", i + 1);
            vector[i] = scanner.nextDouble();
        }
        return vector;
    }
    public static double calcularDeterminante(double[][] A) {
        int n = A.length;

        if (n == 1) {
            return A[0][0];
        }

        if (n == 2) {
            return A[0][0] * A[1][1] - A[1][0] * A[0][1];
        }

        double det = 0;
        for (int j = 0; j < n; j++) {
            double[][] menor = obtenerMenor(A, 0, j);
            double signo = Math.pow(-1, j);
            det += signo * A[0][j] * calcularDeterminante(menor);
        }
        return det;
    }
    private static double[][] obtenerMenor(double[][] A, int filaEliminar, int columnaEliminar) {
        int n = A.length;
        double[][] menor = new double[n-1][n-1];

        int filaMenor = 0;
        for (int i = 0; i < n; i++) {
            if (i == filaEliminar) continue;

            int columnaMenor = 0;
            for (int j = 0; j < n; j++) {
                if (j == columnaEliminar) continue;

                menor[filaMenor][columnaMenor] = A[i][j];
                columnaMenor++;
            }
            filaMenor++;
        }
        return menor;
    }
    public static double[][] calcularMatrizCofactores(double[][] A) {
        int n = A.length;
        double[][] cofactores = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double[][] menor = obtenerMenor(A, i, j);
                double signo = Math.pow(-1, i + j);
                cofactores[i][j] = signo * calcularDeterminante(menor);
            }
        }
        return cofactores;
    }
    public static double[][] calcularMatrizAdjunta(double[][] A) {
        double[][] cofactores = calcularMatrizCofactores(A);
        return transponerMatriz(cofactores);
    }
    private static double[][] transponerMatriz(double[][] matriz) {
        int n = matriz.length;
        double[][] transpuesta = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                transpuesta[j][i] = matriz[i][j];
            }
        }
        return transpuesta;
    }
    public static double[][] calcularInversa(double[][] A) {
        double determinante = calcularDeterminante(A);

        if (Math.abs(determinante) < 1e-10) {
            throw new ArithmeticException("La matriz no es invertible (determinante = 0)");
        }
        double[][] adjunta = calcularMatrizAdjunta(A);
        int n = A.length;
        double[][] inversa = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inversa[i][j] = (1.0 / determinante) * adjunta[i][j];
            }
        }
        return inversa;
    }
    public static double[] resolverSistema(double[][] A, double[] B) {
        double[][] A_inversa = calcularInversa(A);
        int n = A.length;
        double[] X = new double[n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                X[i] += A_inversa[i][j] * B[j];
            }
        }
        return X;
    }
    public static double[][] realizarOperacionesElementales(double[][] A) {
        int n = A.length;
        double[][] aumentada = new double[n][2*n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                aumentada[i][j] = A[i][j];
                aumentada[i][j + n] = (i == j) ? 1.0 : 0.0;
            }
        }
        for (int i = 0; i < n; i++) {
            double pivote = aumentada[i][i];
            for (int j = 0; j < 2*n; j++) {
                aumentada[i][j] /= pivote;
            }
            for (int k = 0; k < n; k++) {
                if (k != i) {
                    double factor = aumentada[k][i];
                    for (int j = 0; j < 2*n; j++) {
                        aumentada[k][j] -= factor * aumentada[i][j];
                    }
                }
            }
        }
        double[][] inversa = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inversa[i][j] = aumentada[i][j + n];
            }
        }
        return inversa;
    }
    public static void imprimirMatriz(double[][] matriz, String titulo) {
        System.out.println("\n" + titulo + ":");
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                System.out.printf("%12.6f", matriz[i][j]);
            }
            System.out.println();
        }
    }
    public static void imprimirVector(double[] vector, String titulo) {
        System.out.println("\n" + titulo + ":");
        for (int i = 0; i < vector.length; i++) {
            System.out.printf("x%d = %10.6f\n", i + 1, vector[i]);
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("\nIntroduce el tamaño de la matriz cuadrada (n): ");
            int n = scanner.nextInt();
            if (n <= 0) {
                System.out.println("El tamaño debe ser mayor que 0");
                return;
            }
            double[][] A = leerMatriz(scanner, n);
            imprimirMatriz(A, "Matriz A introducida");
            double determinante = calcularDeterminante(A);
            System.out.printf("\nDeterminante |A| = %.6f\n", determinante);
            if (Math.abs(determinante) < 1e-10) {
                System.out.println("La matriz NO es invertible (determinante = 0)");
            } else {
                double[][] inversaAdjunta = calcularInversa(A);
                imprimirMatriz(inversaAdjunta, "Inversa por adjunta: A⁻¹ = (1/|A|) * adj(A)");
                double[][] inversaElemental = realizarOperacionesElementales(A);
                imprimirMatriz(inversaElemental, "Inversa por operaciones elementales");
                System.out.println("\n¿Deseas resolver un sistema de ecuaciones AX = B? (s/n)");
                scanner.nextLine();
                String respuesta = scanner.nextLine();
                if (respuesta.equalsIgnoreCase("s")) {
                    double[] B = leerVector(scanner, n);
                    double[] solucion = resolverSistema(A, B);
                    imprimirVector(solucion, "Solución del sistema AX = B");
                }
            }
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}