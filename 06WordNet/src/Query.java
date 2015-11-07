import java.util.HashSet;


public class Query {
    private final HashSet<Integer> v;
    private final HashSet<Integer> w;

    Query(Iterable<Integer> v, Iterable<Integer> w){
        this.v = convertIter2Set(v);
        this.w = convertIter2Set(w);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((v == null) ? 0 : v.hashCode());
        result = prime * result + ((w == null) ? 0 : w.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Query other = (Query) obj;
        if (v == null) {
            if (other.v != null)
                return false;
        } else if (!v.equals(other.v))
            return false;
        if (w == null) {
            if (other.w != null)
                return false;
        } else if (!w.equals(other.w))
            return false;
        if (v == null) {
            if (other.w != null)
                return false;
        } else if (!v.equals(other.w))
            return false;
        if (w == null) {
            if (other.v != null)
                return false;
        } else if (!w.equals(other.v))
            return false;
        return true;
    }

    private HashSet<Integer> convertIter2Set(Iterable<Integer> v) {
        HashSet<Integer> set = new HashSet<Integer>();
        for(Integer i : v){
            set.add(i);
        }
        return set;
    }
}
