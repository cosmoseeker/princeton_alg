import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        while (!StdIn.isEmpty()) {
            /*String input = StdIn.readString();
            rq.enqueue(input);*/
            //Only store k items in queue to limit memory. Get bonus!
            String input = StdIn.readString();
            if (rq.size() >= k && k > 0) {//k can be 0!!! Ensure dequeue() can be done 
                rq.dequeue();
            }
            rq.enqueue(input);
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(rq.dequeue());
        }
    }
}
