package lightstomp.util;

/**
 * Holds two values of the same type together
 */
public class Pair<T> {
    public Pair(T v, T v2){
        right = v;
        left = v2;
    }
    public final T right;
    public final T left;
}