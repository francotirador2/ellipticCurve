package echapackage;

import java.math.BigInteger;
import echapackage.Point;
import java.io.*;

public class ECGroup {

    private BigInteger a, b; //coeffs of the equation
    private BigInteger P; //prime
    private BigInteger N; //order of the group

    //ZERO point (not on the plane)
    //set to (P,P) since this point does not occur anywhere else in the group
    public final Point ZERO;

    public Point getRandomPointInField() {
        return this.ZERO;
    }

    public BigInteger computeOrder() {

      BigInteger order = null;
      //String command = "python3 echapackage/python-schoof/naive_schoof.py " + this.P + " " + this.a + " " + this.b;
      String command = "python3 echapackage/python-schoof/reduced_computation_schoof.py " + this.P + " " + this.a + " " + this.b;

      System.out.println(command);
      try {
        Process proc = Runtime.getRuntime().exec(command);
        proc.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = reader.readLine();
        order = new BigInteger(line);

      } catch (Exception e) {
        System.out.println("Exception da bunda " + e);
      }


      return order;
    }

    private BigInteger getSlopeAtPoint(Point Q) {
        BigInteger x = Q.getX(), y = Q.getY();
        BigInteger num = BigInteger.valueOf(3).multiply(x).multiply(x).add(this.a).mod(this.P);
        BigInteger den = BigInteger.valueOf(2).multiply(y).mod(this.P);
        return num.multiply(den.modInverse(this.P)).mod(this.P);
    }

    public ECGroup(BigInteger a, BigInteger b, BigInteger P) {

        this.a = a;
        this.b = b;
        this.P = P;
        this.ZERO = new Point(P,P);
        this.N = computeOrder();
        System.out.println(this.N);
    }

    public Point inverse(Point A) {
        return new Point(A.getX(), P.subtract(A.getY().mod(P)));
    }

    public Point sum(Point A, Point B) {

        //0 + B = B
        if (A.equals(this.ZERO)) return B;

        //A + 0 = A
        if (B.equals(this.ZERO)) return A;

        //A + (-A) = 0
        if (A.equals(this.inverse(B))) return this.ZERO;

        BigInteger Xa = A.getX(), Ya = A.getY();
        BigInteger Xb = B.getX(), Yb = B.getY();



        if (A.equals(B)) { //same point

            //if both at origin
            //System.out.println("HERE\n");
            if (Xa.equals(BigInteger.ZERO) && Ya.equals(BigInteger.ZERO)) return this.ZERO;

            BigInteger m = getSlopeAtPoint(A);
            //System.out.println("Slope at point: " + m);
            BigInteger Xc = m.multiply(m).subtract(Xa).subtract(Xb).mod(P);
            BigInteger Yc = Ya.add(m.multiply(Xc.subtract(Xa))).mod(P);
            return new Point(Xc,Yc);

        } else {

            //vertical line
            if (Xa.equals(Xb)) return this.ZERO;

            BigInteger ma = getSlopeAtPoint(A);
            BigInteger mb = getSlopeAtPoint(B);

            BigInteger num = Yb.subtract(Ya).mod(P);
            BigInteger den = Xb.subtract(Xa).mod(P);
            BigInteger m = num.multiply(den.modInverse(P)).mod(P);

            //if line is tangent to curve at point A
            if (m.equals(ma)) return this.inverse(B);

            //if line is tanget to curve at point B
            if (m.equals(mb)) return this.inverse(A);

            BigInteger Xc = m.multiply(m).subtract(Xa).subtract(Xb).mod(P);
            BigInteger Yc = Ya.add(m.multiply(Xc.subtract(Xa))).mod(P);
            return new Point(Xc,Yc);

        }

    }

  

    public Point multiply(Point A, BigInteger K) {

        String bin = K.toString(2);
        Point Q = new Point(A);
        Point R = this.ZERO;

        //multiply using repeated doubling
        //log K time to multiply with K
        for (int i = bin.length()-1; i>=0; i--) {
            if (bin.charAt(i) == '1') R = this.sum(R,Q);
            Q = this.sum(Q,Q);
        }

        return R;
    }

    public static void main(String args[]) {

        /*BigInteger x = BigInteger.valueOf(-19);
        System.out.println(x.mod(BigInteger.valueOf(5))); */

        ECGroup g2 = new ECGroup(BigInteger.valueOf(2), BigInteger.valueOf(3), BigInteger.valueOf(97));
        //System.out.println(g2.computeOrder());

        //System.out.println(g2.computeOrder());
/*
        ECGroup group = new ECGroup(BigInteger.valueOf(2),BigInteger.valueOf(3),BigInteger.valueOf(97));
        Point P1 = new Point(3,6);
        Point P2 = group.sum(P1,P1);
        Point P3 = group.sum(P2,P1);
        Point P4 = group.sum(P3,P1);
        Point P5 = group.sum(P4,P1);
        Point P6 = group.sum(P5,P1);

        System.out.println(P1);
        System.out.println(P2);
        System.out.println(P3);
        System.out.println(P4);
        System.out.println(P5);
        System.out.println(P6+"\n");

        System.out.println(group.multiply(P1, BigInteger.valueOf(1)));
        System.out.println(group.multiply(P1, BigInteger.valueOf(2)));
        System.out.println(group.multiply(P1, BigInteger.valueOf(3)));
        System.out.println(group.multiply(P1, BigInteger.valueOf(4)));
        System.out.println(group.multiply(P1, BigInteger.valueOf(5))); */

        /* BigInteger x = BigInteger.valueOf(15);
        BigInteger y = x;
        y = y.add(BigInteger.valueOf(10));
        System.out.println(x+" "+y); */
    }
}
