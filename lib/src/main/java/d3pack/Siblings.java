package d3pack;

import d3pack.LCG;

import java.util.ArrayList;

import d3pack.Array;
import d3pack.Circle;

/**
 * Siblings
 */
public class Siblings {

    /**
     * place circle c according to a and b
     * 
     * @param b reference circle 1
     * @param a reference circle 2
     * @param c the circle to place
     */
    static void place(Circle b, Circle a, Circle c) {
        double dx = b.x - a.x, x, a2,
                dy = b.y - a.y, y, b2,
                d2 = dx * dx + dy * dy;
        if (d2 > 0) {
            a2 = a.r + c.r;
            a2 *= a2;
            b2 = b.r + c.r;
            b2 *= b2;
            if (a2 > b2) {
                x = (d2 + b2 - a2) / (2 * d2);
                y = Math.sqrt(Math.max(0, b2 / d2 - x * x));
                c.x = b.x - x * dx - y * dy;
                c.y = b.y - x * dy + y * dx;
            } else {
                x = (d2 + a2 - b2) / (2 * d2);
                y = Math.sqrt(Math.max(0, a2 / d2 - x * x));
                c.x = a.x + x * dx - y * dy;
                c.y = a.y + x * dy + y * dx;
            }
        } else {
            c.x = a.x + c.r;
            c.y = a.y;
        }
    }

    /**
     * decide if two circles intersect
     * 
     * @param a circle a
     * @param b circle b
     * @return
     */
    static boolean intersects(Circle a, Circle b) {
        double dr = a.r + b.r - 1e-6, dx = b.x - a.x, dy = b.y - a.y;
        return dr > 0 && dr * dr > dx * dx + dy * dy;
    }

    /**
     * Node class for double linked list
     */
    static class Node {
        Circle value;
        Node next;
        Node prev;

        Node(Circle circle) {
            this.value = circle;
            this.next = null;
            this.prev = null;
        }
    }

    /**
     * a score function for node
     * 
     * @param node
     * @return
     */
    static double score(Node node) {
        Circle a = node.value;
        Circle b = node.next.value;
        double ab = a.r + b.r,
                dx = (a.x * b.r + b.x * a.r) / ab,
                dy = (a.y * b.r + b.y * a.r) / ab;
        return dx * dx + dy * dy;
    }

    /**
     * pack sibling circles together
     * @param circles circles to pack
     * @return radius of the last packed circle
     */
    static double packSiblingsRandom(Circle[] circles) {
        if (circles.length < 1)
            return 0.0;

        Circle a, b, c;
        int n = circles.length;
        int i;
        Node j, k;
        double sj, sk;

        a = circles[0];
        a.x = 0;
        a.y = 0;
        if (!(n > 1))
            return a.r;

        b = circles[1];
        a.x = -b.r;
        b.x = a.r;
        b.y = 0;
        if (!(n > 2))
            return a.r + b.r;

        c = circles[2];
        place(b, a, c);

        Node na, nb, nc;
        na = new Node(a);
        nb = new Node(b);
        nc = new Node(c);
        na.next = nc.prev = nb;
        nb.next = na.prev = nc;
        nc.next = nb.prev = na;

        pack: for (i = 3; i < n; i++) {
            c = circles[i];
            place(na.value, nb.value, c);
            nc = new Node(c);

            j = nb.next;
            k = na.prev;
            sj = nb.value.r;
            sk = na.value.r;
            do {
                if (sj <= sk) {
                    if (intersects(j.value, nc.value)) {
                        nb = j;
                        na.next = nb;
                        nb.prev = na;
                        --i;
                        continue pack;
                    }
                    sj += j.value.r;
                    j = j.next;
                } else {
                    if (intersects(k.value, nc.value)){
                        na = k;
                        na.next = nb;
                        nb.prev = na;
                        --i;
                        continue pack;
                    }
                    sk += k.value.r;
                    k = k.prev;
                }
            } while (j != k.next);

            nc.prev = na;
            nc.next = nb;
            // nb = nc;
            // nb.prev = nb;
            // na.next = nb.prev;
            na.next = nb.prev = nb = nc; // ?

            double aa = score(na);
            double ca;
            while ((nc = nc.next) != nb) {
                if ((ca = score(nc)) < aa) {
                    na = nc;
                    aa = ca;
                }
            }
            nb = na.next;
        }

        ArrayList<Circle> arr = new ArrayList<Circle>();
        arr.add(nb.value);
        nc = nb;
        while ((nc = nc.next) != nb) {
            arr.add(nc.value);
        }

        c = Enclose.packEncloseRandom(arr.toArray(new Circle[]{}));

        for (i = 0; i < n; i++) {
            a = circles[i];
            a.x -= c.x;
            a.y -= c.y;
        }

        return c.r;
    }

    /**
     * pack sibling circles together
     * @param circles circles to pack
     * @return the packed circles
     */
    static Circle[] packSiblings(Circle[] circles){
        packSiblingsRandom(circles);
        return circles;
    }
}