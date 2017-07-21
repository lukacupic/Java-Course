package hr.fer.zemris.java.raytracer.model;

/**
 * Represents a sphere graphical object which can appear
 * in a scene.
 *
 * @author Luka Čupić
 */
public class Sphere extends GraphicalObject {

    /**
     * Center of the sphere.
     */
    private Point3D center;

    /**
     * Radius of the sphere.
     */
    private double radius;

    /**
     * Diffuse component for the red color.
     */
    private double kdr;

    /**
     * Diffuse component for the green color.
     */
    private double kdg;

    /**
     * Diffuse component for the blue color.
     */
    private double kdb;

    /**
     * Reflective component for the red color.
     */
    private double krr;

    /**
     * Reflective component for the green color.
     */
    private double krg;

    /**
     * Reflective component for the blue color.
     */
    private double krb;

    /**
     * Reflective component coefficient.
     */
    private double krn;

    /**
     * Creates a new sphere.
     *
     * @param center sphere center
     * @param radius sphere radius
     * @param kdr    diffuse component for the red color
     * @param kdg    diffuse component for the green color
     * @param kdb    diffuse component for the blue color
     * @param krr    reflective component for the red color
     * @param krg    reflective component for the green color
     * @param krb    reflective component for the blue color
     * @param krn    reflective component coefficient
     */
    public Sphere(Point3D center, double radius, double kdr, double kdg, double kdb,
                  double krr, double krg, double krb, double krn) {
        this.center = center;
        this.radius = radius;
        this.kdr = kdr;
        this.kdg = kdg;
        this.kdb = kdb;
        this.krr = krr;
        this.krg = krg;
        this.krb = krb;
        this.krn = krn;
    }

    @Override
    public RayIntersection findClosestRayIntersection(Ray ray) {
        Point3D L = center.sub(ray.start);

        double tca = L.scalarProduct(ray.direction);
        if (tca < 0) return null;

        double d = Math.sqrt(L.scalarProduct(L) - tca * tca);
        if (d > radius) return null;

        double thc = Math.sqrt(radius * radius - d * d);

        double t0 = tca - thc;
        double t1 = tca + thc;

        if (t0 > t1) {
            double temp = t0;
            t0 = t1;
            t1 = temp;
        }

        if (t0 < 0) {
            t0 = t1;
            if (t0 < 0) return null;
        }

        Point3D point = ray.start.add(ray.direction.scalarMultiply(t0));
        return new SphereRayIntersection(point, t0, true);
    }

    /**
     * Represents an intersection point of a ray and a sphere
     * object.
     *
     * @author Luka Čupić
     */
    private class SphereRayIntersection extends RayIntersection {

        /**
         * The constructor.
         *
         * @param point    the point of the intersection
         * @param distance the distance from ray origin
         * @param outer    a flag which indicates whether the flag
         *                 is outer or not
         */
        public SphereRayIntersection(Point3D point, double distance, boolean outer) {
            super(point, distance, outer);
        }

        @Override
        public Point3D getNormal() {
            return getPoint().sub(center).normalize();
        }

        @Override
        public double getKdr() {
            return kdr;
        }

        @Override
        public double getKdg() {
            return kdg;
        }

        @Override
        public double getKdb() {
            return kdb;
        }

        @Override
        public double getKrr() {
            return krr;
        }

        @Override
        public double getKrg() {
            return krg;
        }

        @Override
        public double getKrb() {
            return krb;
        }

        @Override
        public double getKrn() {
            return krn;
        }
    }
}