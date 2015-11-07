
public class CheckUtil {
    public static void checkNull(Object obj){
        if(obj == null){
            throw new java.lang.NullPointerException();
        }
    }
    public static boolean checkLen(Iterable<?> iter){
        return iter.iterator().hasNext();
    }
}
