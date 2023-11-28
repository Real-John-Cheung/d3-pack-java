package d3pack;

/**
 * LCG
 */
public class LCG {

    static long s = 1;
    static final int a = 1664525;
    static final int c = 1013904223;
    static final long m = 4294967296L;

    
    /** 
     * @return double
     */
    static double next() {
        LCG.s = (LCG.a * LCG.s + LCG.c) % LCG.m;
        return (double) LCG.s / (double) LCG.m;
    }
}