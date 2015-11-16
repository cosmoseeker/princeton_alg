import java.util.HashSet;

public class Query {
    private final HashSet<Integer> v;
    private final HashSet<Integer> w;

    Query(Iterable<Integer> v, Iterable<Integer> w) {
        this.v = convertIter2Set(v);
        this.w = convertIter2Set(w);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + v.hashCode() + w.hashCode();
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
        if (v.equals(other.v) && w.equals(other.w)) {
            return true;
        }
        if (v.equals(other.w) && w.equals(other.v)) {
            return true;
        }
        return false;
    }

    private HashSet<Integer> convertIter2Set(Iterable<Integer> v) {
        HashSet<Integer> set = new HashSet<Integer>();
        for (Integer i : v) {
            set.add(i);
        }
        return set;
    }
}
