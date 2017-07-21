package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexPolynomial;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * A simple demonstration program for viewing Newton-Raphson
 * iteration-based fractals.
 *
 * @author Luka Čupić
 */
public class Newton {

    /**
     * The main method.
     *
     * @param args command line arguments; not used in this program
     */
    public static void main(String[] args) {
        System.out.println("Welcome to Newton-Raphson iteration-based " +
            "fractal viewer.");
        System.out.println("Please enter at least two roots, one root " +
            "per line. Enter 'done' when done.");

        Scanner sc = new Scanner(System.in);

        List<Complex> roots = new ArrayList<>();

        int index = 1;
        System.out.print("Root " + index++ + ": ");
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            if ("done".equals(line)) {
                System.out.println("Image of fractal will appear shortly. " + "Thank you.");
                break;
            }
            try {
                roots.add(Complex.parse(line));
            } catch (NumberFormatException ex) {
                System.out.println("Newton would be sad if he knew you won't enter a valid " +
                    "complex number :(");
            }
            System.out.print("Root " + index++ + ": ");
        }
        FractalViewer.show(new NewtonFractalProducer(new ComplexRootedPolynomial(
            roots.toArray(new Complex[0])
        )));
        sc.close();
    }

    /**
     * Implements the job which will be performed by threads.
     *
     * @author Luka Čupić
     */
    public static class CalculationJob implements Callable<Void> {

        /**
         * Minimal value for the real component.
         */
        double reMin;

        /**
         * Maximal value for the real component.
         */
        double reMax;

        /**
         * Minimal value for the imaginary component.
         */
        double imMin;

        /**
         * Maximal value for the imaginary component.
         */
        double imMax;

        /**
         * Width of the screen.
         */
        int width;

        /**
         * Height of the screen.
         */
        int height;

        /**
         * Minimal value for y.
         */
        int yMin;

        /**
         * Maximal value for y.
         */
        int yMax;

        /**
         * Number of iterations.
         */
        int m;

        /**
         * The array to store the result in.
         */
        short[] data;

        /**
         * The convergence threshold.
         */
        double convergenceThreshold;

        /**
         * The root threshold.
         */
        double rootThreshold;

        /**
         * The rooted, explicit, type of polynomial used for calculation.
         */
        ComplexRootedPolynomial pol1;

        /**
         * The (implicit type of) polynomial used for calculation.
         */
        ComplexPolynomial pol2;

        /**
         * The constructor.
         *
         * @param reMin      minimal value for the real component
         * @param reMax      maximal value for the real component
         * @param imMin      minimal value for the imaginary component
         * @param imMax      maximal value for the imaginary component
         * @param width      width of the screen
         * @param height     height of the screen
         * @param yMin       minimal value for y
         * @param yMax       maximal value for y
         * @param m          number of iterations
         * @param data       the array to store the result in
         * @param polynomial the polynomial used for calculation
         */
        public CalculationJob(double reMin, double reMax, double imMin,
                              double imMax, int width, int height, int yMin, int yMax,
                              int m, short[] data, ComplexRootedPolynomial polynomial) {
            this.reMin = reMin;
            this.reMax = reMax;
            this.imMin = imMin;
            this.imMax = imMax;
            this.width = width;
            this.height = height;
            this.yMin = yMin;
            this.yMax = yMax;
            this.m = m;
            this.data = data;
            this.pol1 = polynomial;
            this.pol2 = pol1.toComplexPolynomial();
            this.convergenceThreshold = 0.001;
            this.rootThreshold = 0.002;
        }

        @Override
        public Void call() {
            for (int y = yMin; y <= yMax; y++) {
                for (int x = 0; x < width; x++) {
                    Complex zn = mapToComplexPlain(x, y, reMin, reMax, imMin, imMax);
                    Complex zn1;
                    double module;
                    int i = 0;
                    do {
                        Complex numerator = pol1.apply(zn);
                        Complex denominator = pol2.derive().apply(zn);
                        Complex fraction = numerator.divide(denominator);
                        zn1 = zn.sub(fraction);
                        module = zn1.sub(zn).module();
                        zn = zn1;
                        i++;
                    } while (module > convergenceThreshold && i < m);
                    int index = pol1.indexOfClosestRootFor(zn, rootThreshold);
                    data[y * width + x] = (index == -1) ? 0 : (short) (index + 1);
                }
            }
            return null;
        }

        /**
         * Maps the specified point to the complex plane.
         *
         * @param reMin minimal value for the real component
         * @param reMax maximal value for the real component
         * @param imMin minimal value for the imaginary component
         * @param imMax maximal value for the imaginary component
         * @return a complex number, representing the given point
         */
        private Complex mapToComplexPlain(int x, int y, double reMin, double reMax,
                                          double imMin, double imMax) {
            double re = x / (double) width * (reMax - reMin) + reMin;
            double im = (height - 1 - y) / (double) height * (imMax - imMin) + imMin;
            return new Complex(re, im);
        }
    }

    /**
     * Represents a fractal producer for producing fractals based on
     * the Newton–Raphson method.
     *
     * @author Luka Čupić
     */
    public static class NewtonFractalProducer implements IFractalProducer {

        /**
         * Represents the polynomial of type 1.
         */
        private ComplexRootedPolynomial pol1;

        /**
         * Represents the polynomial of type 2.
         */
        private ComplexPolynomial pol2;

        /**
         * Represents a pool of threads.
         */
        private ExecutorService pool;

        /**
         * Holds the number of processors for this machine.
         */
        private int noOfProcessors;

        /**
         * Creates a new instance of this class.
         */
        public NewtonFractalProducer(ComplexRootedPolynomial polynomial) {
            pol1 = polynomial;
            pol2 = polynomial.toComplexPolynomial();
            noOfProcessors = Runtime.getRuntime().availableProcessors();
            pool = Executors.newFixedThreadPool(noOfProcessors, r -> {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            });
        }

        @Override
        public void produce(double reMin, double reMax, double imMin, double imMax,
                            int width, int height, long requestNo, IFractalResultObserver observer) {
            System.out.println("Zapocinjem izracun...");

            int m = 16 * 16 * 16;
            short[] data = new short[width * height];
            final int noOfTracks = 8 * noOfProcessors;
            int ysPerTrack = height / noOfTracks;

            List<Future<Void>> results = new ArrayList<>();

            for (int i = 0; i < noOfTracks; i++) {
                int yMin = i * ysPerTrack;
                int yMax = (i + 1) * ysPerTrack - 1;
                if (i == noOfTracks - 1) {
                    yMax = height - 1;
                }
                CalculationJob job = new CalculationJob(
                    reMin, reMax, imMin, imMax,
                    width, height, yMin, yMax, m, data,
                    pol1
                );
                results.add(pool.submit(job));
            }
            for (Future<Void> job : results) {
                while (true) {
                    try {
                        job.get();
                        break;
                    } catch (InterruptedException | ExecutionException e) {
                    }
                }
            }
            System.out.println("Izračuni gotovi...");
            observer.acceptResult(data, (short) (pol2.order() + 1), requestNo);
        }
    }
}