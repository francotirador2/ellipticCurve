package echapackage;

import java.math.BigInteger;

public class Point {

    private BigInteger x, y;

    public Point(Point P) {
        this.x = P.getX();
        this.y = P.getY();
    }

    public Point(BigInteger x, BigInteger y) {
        this.x = x;
        this.y = y;
    }

    public Point(int x, int y) {
        this.x = BigInteger.valueOf(x);
        this.y = BigInteger.valueOf(y);
    }

    public BigInteger getX() { return this.x; }
    public BigInteger getY() { return this.y; }
    
    public boolean equals(Object b) {
        Point B = (Point)b;
        return (this.x.equals(B.getX()) && this.y.equals(B.getY()));
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }
}