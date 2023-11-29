package d3pack;
import java.util.Arrays;

import javax.crypto.Cipher;

import d3pack.Circle;
import d3pack.LCG;

/**
 * Array
 */
public class Array {
    /**
     * Fisher-yate shuffle
     * @param array
     * @return the shuffled array
     */
    static Circle[] shuffle(Circle[] array){
        int m = array.length;
        int i;
        Circle t;

        while (m > 0) {
            i = (int) Math.floor(LCG.next() * m);
            m--;
            t = array[m];
            array[m] = array[i];
            array[i] = t;

        }
        
        return array;
    }
    
}