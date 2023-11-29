package d3pack;

import d3pack.LCG;
import d3pack.Circle;
import d3pack.Array;

/**
 * Enclose
 */
public class Enclose {

    /**
     * return a new circle randomly pack with circles
     * @param circles
     * @return the new circle
     */
    static Circle packEnclose(Circle[] circles){
        return packEncloseRandom(circles);
    }

    /**
     * return a new circle randomly pack with circles
     * @param circles
     * @return the new circle
     */
    static Circle packEncloseRandom(Circle[] circles){
        int i = 0;
        int n = circles.length;
        Array.shuffle(circles);
        Circle[] b = new Circle[]{};
        Circle p = null, e = null;

        while (i < n) {
            p = circles[i];
            if (e != null && enclosesWeak(e, p)){
                i ++;
            } else {
                b = extendBasis(b, p);
                e = encloseBasis(b);
                i = 0;
            }
        }

        return e;
    }

    /**
     * add a new circle to the basis
     * 
     * @param b current basis
     * @param p the new circle
     * @return the new basis
     */
    static Circle[] extendBasis(Circle[] b, Circle p) {
        int i, j;

        if (enclosesWeakAll(p, b))
            return new Circle[] { p };

        for (i = 0; i < b.length; i++) {
            if (enclosesNot(p, b[i]) && enclosesWeakAll(encloseBasis2(b[i], p), b)) {
                return new Circle[] { b[i], p };
            }
        }

        for (i = 0; i < b.length - 1; i++) {
            for (j = i + 1; j < b.length; j++) {
                if (enclosesNot(encloseBasis2(b[i], b[j]), p)
                        && enclosesNot(encloseBasis2(b[i], p), b[j])
                        && enclosesNot(encloseBasis2(b[j], p), b[i])
                        && enclosesWeakAll(encloseBasis3(b[i], b[j], p), b)) {
                    return new Circle[] { b[i], b[j], p };
                }
            }
        }

        throw new Error();
    }

    /**
     * decide if two circles are not enclosed
     * 
     * @param a circle a
     * @param b circle b
     * @return
     */
    static boolean enclosesNot(Circle a, Circle b) {
        double dr = a.r - b.r, dx = b.x - a.x, dy = b.y - a.y;
        return dr < 0 || dr * dr < dx * dx + dy * dy;
    }

    /**
     * decide if a circle is weakly enclosed with a set of circles
     * 
     * @param a the circle
     * @param b the set of circles
     * @return
     */
    static boolean enclosesWeakAll(Circle a, Circle[] b) {
        for (int i = 0; i < b.length; i++) {
            if (!enclosesWeak(a, b[i])) {
                return false;
            }
        }
        return true;
    }

    /**
     * decide if two circles are weakly enclosed
     * 
     * @param a circle a
     * @param b circle b
     * @return
     */
    static boolean enclosesWeak(Circle a, Circle b) {
        double dr = a.r - b.r + Math.max(a.r, Math.max(b.r, 1)) * 1e-9, dx = b.x - a.x, dy = b.y - a.y;
        return dr > 0 && dr * dr > dx * dx + dy * dy;
    }

    /**
     * return a new circle according to a set of circle
     * 
     * @param b array of the reference circle
     * @return the new circle
     */
    static Circle encloseBasis(Circle[] b) {
        switch (b.length) {
            case 1:
                return encloseBasis1(b[0]);
            case 2:
                return encloseBasis2(b[0], b[1]);
            case 3:
                return encloseBasis3(b[0], b[1], b[2]);
            default:
                return null;
        }
    }

    /**
     * return a copy of the circle
     * 
     * @param a circle
     * @return a copy of the circle
     */
    static Circle encloseBasis1(Circle a) {
        return new Circle(a.x, a.y, a.r);
    }

    /**
     * return a new circle according to two circles
     * 
     * @param a circle a
     * @param b circle b
     * @return the new circle
     */
    static Circle encloseBasis2(Circle a, Circle b) {
        double x1 = a.x, y1 = a.y, r1 = a.r,
                x2 = b.x, y2 = b.y, r2 = b.r,
                x21 = x2 - x1, y21 = y2 - y1, r21 = r2 - r1,
                l = Math.sqrt(x21 * x21 + y21 * y21);
        return new Circle((x1 + x2 + x21 / l * r21) / 2, (y1 + y2 + y21 / l * r21) / 2, (l + r1 + r2) / 2);
    }

    /**
     * return a new circle according to three circles
     * 
     * @param a circle a
     * @param b circle b
     * @param c circle c
     * @return the new circle
     */
    static Circle encloseBasis3(Circle a, Circle b, Circle c) {
        double x1 = a.x, y1 = a.y, r1 = a.r;
        double x2 = b.x, y2 = b.y, r2 = b.r;
        double x3 = c.x, y3 = c.y, r3 = c.r;
        double a2 = x1 - x2,
                a3 = x1 - x3,
                b2 = y1 - y2,
                b3 = y1 - y3,
                c2 = r2 - r1,
                c3 = r3 - r1,
                d1 = x1 * x1 + y1 * y1 - r1 * r1,
                d2 = d1 - x2 * x2 - y2 * y2 + r2 * r2,
                d3 = d1 - x3 * x3 - y3 * y3 + r3 * r3,
                ab = a3 * b2 - a2 * b3,
                xa = (b2 * d3 - b3 * d2) / (ab * 2) - x1,
                xb = (b3 * c2 - b2 * c3) / ab,
                ya = (a3 * d2 - a2 * d3) / (ab * 2) - y1,
                yb = (a2 * c3 - a3 * c2) / ab,
                A = xb * xb + yb * yb - 1,
                B = 2 * (r1 + xa * xb + ya * yb),
                C = xa * xa + ya * ya - r1 * r1,
                r = -(Math.abs(A) > 1e-6 ? (B + Math.sqrt(B * B - 4 * A * C)) / (2 * A) : C / B);
        return new Circle(x1 + xa + xb * r, y1 + ya + yb * r, r);
    }
}