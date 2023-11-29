package d3pack;
import java.util.function.*;
/**
 * Constant
 */
public class Constant {

    
    /** 
     * @return integer 0
     */
    static int constantZero(){
        return 0;
    }

    /** 
     * @return double 0.0
     */
    static double constantZeroDouble(){
        return 0.0;
    }

    /**
     * 
     * @param x the constant
     * @return a IntSupplier that return x
     */
    static IntSupplier constant(int x) {
        return () -> x;
    }

    /**
     * 
     * @param x the constant
     * @return a DoubleSupplier that return x
     */
    static DoubleSupplier constantDouble(double x) {
        return () -> x;
    }
    
}