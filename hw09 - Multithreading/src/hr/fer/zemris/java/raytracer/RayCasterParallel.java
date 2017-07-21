package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.*;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

/**
 * A program which demonstrates how a ray-tracer works
 * in a simple environment, using parallelization.
 *
 * @author Luka Čupić
 */
public class RayCasterParallel {

    /**
     * The main method.
     *
     * @param args command line arguments; not used in this program
     */
    public static void main(String[] args) {
        RayTracerViewer.show(getIRayTracerProducer(),
            new Point3D(10, 0, 0),
            new Point3D(0, 0, 0),
            new Point3D(0, 0, 10),
            20, 20
        );
    }

    /**
     * Implements the job which will be performed by threads.
     *
     * @author Luka Čupić
     */
    public static class CalculationJob extends RecursiveAction {

        /**
         * Screen width.
         */
        private int width;

        /**
         * Screen height.
         */
        private int height;

        /**
         * Position of the human observer.
         */
        private Point3D eye;

        /**
         * The corner of the screen.
         */
        private Point3D corner;

        /**
         * Length of the horizontal component.
         */
        private double horizontal;

        /**
         * Length of the vertical component.
         */
        private double vertical;

        /**
         * Represents the x-axis unit vector.
         */
        private Point3D i;

        /**
         * Represents the y-axis unit vector.
         */
        private Point3D j;

        /**
         * Represents the scene.
         */
        private Scene scene;

        /**
         * The RGB color vector.
         */
        private short[] rgb;

        /**
         * The Red color array.
         */
        short[] red;

        /**
         * The Green color array.
         */
        short[] green;

        /**
         * The Blue color array.
         */
        short[] blue;

        /**
         * The minimum value of y for which this job is to be
         * executed.
         */
        int yMin;

        /**
         * The maximum value of y for which this job is to be
         * executed.
         */
        int yMax;

        /**
         * The threshold for number of blocks per y direction
         * for which the computation shall be performed directly
         */
        static final int threshold = 16;

        /**
         * The constructor.
         *
         * @param width      screen width
         * @param height     Screen height
         * @param eye        position of the human observer
         * @param corner     the corner of the screen
         * @param horizontal length of the horizontal component
         * @param vertical   length of the vertical component
         * @param i          represents the x-axis unit vector
         * @param j          represents the y-axis unit vector
         * @param scene      represents the scene
         * @param rgb        the RGB color vector
         * @param red        the Red color array
         * @param green      the Green color array
         * @param blue       the Blue color array
         * @param yMin       the minimum value of y for which this job is to be
         *                   executed
         * @param yMax       the maximum value of y for which this job is to be
         *                   executed
         */

        public CalculationJob(int width, int height, Point3D eye, Point3D corner,
                              double horizontal, double vertical, Point3D i, Point3D j,
                              Scene scene, short[] rgb, short[] red, short[] green, short[] blue,
                              int yMin, int yMax) {
            this.width = width;
            this.height = height;
            this.eye = eye;
            this.corner = corner;
            this.horizontal = horizontal;
            this.vertical = vertical;
            this.i = i;
            this.j = j;
            this.scene = scene;
            this.rgb = rgb;
            this.red = red;
            this.green = green;
            this.blue = blue;
            this.yMin = yMin;
            this.yMax = yMax;
        }

        @Override
        protected void compute() {
            if (yMax - yMin + 1 <= threshold) {
                computeDirect();
                return;
            }
            invokeAll(
                new CalculationJob(width, height - 1, eye, corner,
                    horizontal, vertical, i, j, scene, rgb, red, green, blue,
                    yMin, yMin + (yMax - yMin) / 2),
                new CalculationJob(width, height - 1, eye, corner,
                    horizontal, vertical, i, j, scene, rgb, red, green, blue,
                    yMin + (yMax - yMin) / 2 + 1, yMax)
            );
        }

        /**
         * Computes the job starting from screen row {@link #yMin} and extents
         * to {@link #yMax}.
         */
        public void computeDirect() {
            // Otkomentirati sljedeći redak da se vidi kako je napravljena dekompozicija:
            // System.out.println("Racunam od " + yMin + " do " + yMax);

            for (int y = yMin; y <= yMax; y++) {
                for (int x = 0; x < width; x++) {
                    Point3D screenPoint = RayCaster.mapToCoordinateSystem(
                        corner, x, y, width, height,
                        horizontal, vertical, i, j
                    );
                    Ray ray = Ray.fromPoints(eye, screenPoint);
                    RayCaster.tracer(scene, ray, rgb);

                    int offset = y * width + x;
                    red[offset] = rgb[0] > 255 ? 255 : rgb[0];
                    green[offset] = rgb[1] > 255 ? 255 : rgb[1];
                    blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
                }
            }
        }
    }

    /**
     * Creates and returns a new ray tracer producer.
     *
     * @return a new ray tracer producer
     */
    private static IRayTracerProducer getIRayTracerProducer() {
        return new IRayTracerProducer() {
            @Override
            public void produce(Point3D eye, Point3D view, Point3D viewUp, double horizontal, double vertical,
                                int width, int height, long requestNo, IRayTracerResultObserver observer) {
                System.out.println("Zapocinjem izracun...");
                short[] red = new short[width * height];
                short[] green = new short[width * height];
                short[] blue = new short[width * height];

                final int noOfTracks = 8 * Runtime.getRuntime().availableProcessors();
                int ysPerTrack = height / noOfTracks;

                Point3D j = RayCaster.getJVector(eye, view, viewUp);
                Point3D i = RayCaster.getIVector(eye, view, j);
                Point3D corner = RayCaster.getScreenCorner(view, horizontal, vertical, i, j);
                Scene scene = RayTracerViewer.createPredefinedScene();
                short[] rgb = new short[3];

                ForkJoinPool pool = new ForkJoinPool();
                for (int k = 0; k < noOfTracks; k++) {
                    int yMin = k * ysPerTrack;
                    int yMax = (k + 1) * ysPerTrack - 1;
                    if (k == noOfTracks - 1) {
                        yMax = height - 1;
                    }
                    pool.invoke(new CalculationJob(width, height - 1, eye, corner,
                        horizontal, vertical, i, j, scene, rgb, red, green, blue, yMin, yMax
                    ));
                }
                pool.shutdown();

                System.out.println("Izračuni gotovi...");
                observer.acceptResult(red, green, blue, requestNo);
                System.out.println("Dojava gotova...");
            }
        };
    }
}
