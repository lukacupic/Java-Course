package hr.fer.zemris.java.raytracer;

import hr.fer.zemris.java.raytracer.model.*;
import hr.fer.zemris.java.raytracer.viewer.RayTracerViewer;

/**
 * A program which demonstrates how a ray-tracer works
 * in a simple environment.
 *
 * @author Luka Čupić
 */
public class RayCaster {

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
     * A helper method for tracing the specified ray.
     *
     * @param scene the scene
     * @param ray   the ray to trace
     * @param rgb   the rgb color vector
     */
    protected static void tracer(Scene scene, Ray ray, short[] rgb) {
        rgb[0] = 0;
        rgb[1] = 0;
        rgb[2] = 0;

        RayIntersection S = findClosestIntersection(scene, ray);
        if (S == null) return;

        determineColor(scene, ray, S, rgb);
    }

    /**
     * Determines the color which will be displayed on the screen, where
     * the color represents the color of the point where the ray intersects
     * a object in the given scene.
     *
     * @param scene the scene
     * @param ray   the ray which intersects the object
     * @param S     the intersection point
     * @param rgb   array of three elements, holding red, green and blue
     *              values
     */
    private static void determineColor(Scene scene, Ray ray, RayIntersection S, short[] rgb) {
        rgb[0] = 15;
        rgb[1] = 15;
        rgb[2] = 15;

        for (LightSource ls : scene.getLights()) {
            Point3D lsPoint = ls.getPoint();
            Ray lightRay = new Ray(lsPoint, S.getPoint().sub(lsPoint).normalize());
            RayIntersection lightS = findClosestIntersection(scene, lightRay);
            if (lightS != null && Math.abs(lsPoint.sub(lightS.getPoint()).norm() -
                lsPoint.sub(S.getPoint()).norm()) > 10E-9) {
                continue;
            }

            Point3D l = S.getPoint().sub(ls.getPoint()).normalize().negate();
            Point3D n = S.getNormal().normalize();
            double ln = l.scalarProduct(n);
            if (ln < 0) ln = 0;

            Point3D v = ray.start.sub(S.getPoint()).normalize();
            Point3D r = l.sub(n.scalarMultiply(2 * ln)).normalize();
            double rv = r.scalarProduct(v);

            int exp = 100;
            rgb[0] += ls.getR() * (S.getKrr() * Math.pow(rv, exp) + S.getKdr() * ln);
            rgb[1] += ls.getG() * (S.getKrg() * Math.pow(rv, exp) + S.getKdg() * ln);
            rgb[2] += ls.getB() * (S.getKrb() * Math.pow(rv, exp) + S.getKdb() * ln);
        }
    }

    /**
     * Returns a {@link RayIntersection} object representing the intersection
     * of the given ray with any object from the given scene.
     *
     * @param scene the scene
     * @param ray   the ray
     * @return the intersection object representing the intersection, or null
     * if no intersections with any of the objects were found
     */
    private static RayIntersection findClosestIntersection(Scene scene, Ray ray) {
        RayIntersection closest = null;
        double minDist = -1;
        for (GraphicalObject o : scene.getObjects()) {
            RayIntersection i = o.findClosestRayIntersection(ray);
            if (i == null) continue;

            double dist = i.getDistance();
            if (dist < minDist || minDist == -1) {
                minDist = dist;
                closest = i;
            }
        }
        return closest;
    }

    /**
     * Maps the given point to the coordinate system.
     *
     * @param corner     screen corner
     * @param x          x point
     * @param y          y point
     * @param width      width of the screen
     * @param height     height of the screen
     * @param horizontal width of the plane
     * @param vertical   height of the plane
     * @param i          y direction unit vector
     * @param j          x direction unit vector
     * @return a 3D point representing the screen corner
     * in 3D space
     */
    protected static Point3D mapToCoordinateSystem(Point3D corner, int x, int y, int width, int height,
                                                   double horizontal, double vertical, Point3D i, Point3D j) {
        Point3D iComp = i.scalarMultiply((double) x / (width - 1) * horizontal);
        Point3D jComp = j.scalarMultiply((double) y / (height - 1) * vertical);
        return corner.add(iComp).sub(jComp);
    }


    /**
     * Returns a point representing the upper-left corner of the screen.
     *
     * @param G          the view position
     * @param horizontal width of the plane
     * @param vertical   height of the plane
     * @param i          y direction unit vector
     * @param j          x direction unit vector
     * @return a point representing the upper-left corner of the screen
     */
    protected static Point3D getScreenCorner(Point3D G, double horizontal, double vertical,
                                             Point3D i, Point3D j) {
        Point3D iComp = i.scalarMultiply(horizontal / 2);
        Point3D jComp = j.scalarMultiply(vertical / 2);
        return G.sub(iComp).add(jComp);
    }

    /**
     * Returns the x direction unit vector, i.
     *
     * @param O the eye position
     * @param G the view position
     * @param j x direction unit vector
     * @return the x direction unit vector, i.
     */
    protected static Point3D getIVector(Point3D O, Point3D G, Point3D j) {
        Point3D OG = G.sub(O).normalize();
        return OG.vectorProduct(j).normalize();
    }

    /**
     * Returns the y direction unit vector, j.
     *
     * @param O  the eye position
     * @param G  the view position
     * @param VU the view-up vector
     * @return the y direction unit vector, j.
     */
    protected static Point3D getJVector(Point3D O, Point3D G, Point3D VU) {
        Point3D OG = G.sub(O).normalize();
        return VU.sub(OG.scalarMultiply(OG.scalarProduct(VU))).normalize();
    }

    /**
     * Creates and returns a new ray tracer producer.
     *
     * @return a new ray tracer producer
     */
    private static IRayTracerProducer getIRayTracerProducer() {
        return new IRayTracerProducer() {
            @Override
            public void produce(Point3D eye, Point3D view, Point3D viewUp,
                                double horizontal, double vertical, int width, int height,
                                long requestNo, IRayTracerResultObserver observer) {
                System.out.println("Zapocinjem izracun...");
                short[] red = new short[width * height];
                short[] green = new short[width * height];
                short[] blue = new short[width * height];

                Point3D j = getJVector(eye, view, viewUp);
                Point3D i = getIVector(eye, view, j);
                Point3D corner = getScreenCorner(view, horizontal, vertical, i, j);
                Scene scene = RayTracerViewer.createPredefinedScene();
                short[] rgb = new short[3];
                int offset = 0;
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        Point3D screenPoint = mapToCoordinateSystem(
                            corner, x, y, width, height,
                            horizontal, vertical, i, j
                        );
                        Ray ray = Ray.fromPoints(eye, screenPoint);
                        tracer(scene, ray, rgb);

                        red[offset] = rgb[0] > 255 ? 255 : rgb[0];
                        green[offset] = rgb[1] > 255 ? 255 : rgb[1];
                        blue[offset] = rgb[2] > 255 ? 255 : rgb[2];
                        offset++;
                    }
                }
                System.out.println("Computation completed!");
                observer.acceptResult(red, green, blue, requestNo);
                System.out.println("Notifying completed!");
            }
        };
    }
}