public record Point(double x, double y) {

    public Point rotate(double angleRadians) {
        double cos = Math.cos(angleRadians);
        double sin = Math.sin(angleRadians);

        return new Point(
            x * cos - y * sin,
            x * sin + y * cos
        );
    }

    public Point translate(double dx, double dy) {
        return new Point(x + dx, y + dy);
    }
}
