import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        CheckUtil.checkNull(wordnet);
        this.wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (!CheckUtil.checkLen(Arrays.asList(nouns))) {
            throw new java.lang.IllegalArgumentException();
        }
        int[] dist = new int[nouns.length];
        String noun;
        for (int i = 0; i < nouns.length; i++) {
            noun = nouns[i];
            for (int j = 0; j < nouns.length; j++) {
                dist[j] += this.wordNet.distance(noun, nouns[j]);
            }
        }
        int nounIndex = 0;
        noun = nouns[nounIndex];
        for (int k = 1; k < nouns.length; k++) {
            if (dist[k] > dist[nounIndex]) {
                nounIndex = k;
                noun = nouns[nounIndex];
            }
        }
        return noun;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
