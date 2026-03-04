public class PerimeterCalculator {

    public double calculate(Polygon polygon) {
        List<Point> v = polygon.getVertices();
        int n = v.size();
        double perimeter = 0;

        for (int i = 0; i < n; i++) {
            Point p1 = v.get(i);
            Point p2 = v.get((i + 1) % n);

            perimeter += Math.hypot(
                p2.x() - p1.x(),
                p2.y() - p1.y()
            );
        }

        return perimeter;
    }
}
