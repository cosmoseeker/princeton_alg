import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;

public class WordNet {
    private Map<String, List<Integer>> nouns;
    private Map<Integer, List<String>> synset;
    private Digraph graph;
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        CheckUtil.checkNull(synsets);
        CheckUtil.checkNull(hypernyms);

        parseSynsets(synsets);
        int outNum = parseHypernyms(hypernyms);
        checkDAG(outNum);
        this.sap = new SAP(this.graph);
    }

    private void checkDAG(int outNum) {
        if (this.graph.V() - outNum > 1) { // only one 0 out
            throw new java.lang.IllegalArgumentException();
        }
        if (new DirectedCycle(this.graph).hasCycle()) {
            throw new IllegalArgumentException();
        }
    }

    private int parseHypernyms(String hypernyms) {
        In hyperIn = new In(hypernyms);
        this.graph = new Digraph(this.synset.size());
        int outNum = 0;
        while (hyperIn.hasNextLine()) {
            String[] vec = hyperIn.readLine().split(",");
            int in = Integer.parseInt(vec[0]);
            if (vec.length > 1) {
                outNum++;
                for (int num = 1; num < vec.length; num++) {
                    int out = Integer.parseInt(vec[num]);
                    this.graph.addEdge(in, out);
                }
            }
        }
        return outNum;
    }

    private void parseSynsets(String synsets) {
        nouns = new HashMap<String, List<Integer>>();
        synset = new HashMap<Integer, List<String>>();
        In synIn = new In(synsets);
        while (synIn.hasNextLine()) {
            String[] line = synIn.readLine().split(",");
            int id = Integer.parseInt(line[0]);
            String[] words = line[1].split(" ");
            synset.put(id, Arrays.asList(words));
            for (int i = 0; i < words.length; i++) {
                if (this.isNoun(words[i])) {
                    List<Integer> ids = this.nouns.get(words[i]);
                    ids.add(id);
                } else {
                    List<Integer> ids = new LinkedList<Integer>();
                    ids.add(id);
                    this.nouns.put(words[i], ids);
                }
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return this.nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        CheckUtil.checkNull(word);
        return this.nouns.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        CheckUtil.checkNull(nounA);
        CheckUtil.checkNull(nounB);
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }

        if (nounA.equals(nounB)) {
            return 0;
        }
        List<Integer> idA = this.nouns.get(nounA), idB = this.nouns.get(nounB);
        return this.sap.length(idA, idB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of
    // nounA and nounB in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        CheckUtil.checkNull(nounA);
        CheckUtil.checkNull(nounB);
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException();
        }

        int ancester;
        if (nounA.equals(nounB)) {
            ancester = this.nouns.get(nounA).get(0);
        } else {
            List<Integer> idA = this.nouns.get(nounA);
            List<Integer> idB = this.nouns.get(nounB);
            ancester = this.sap.ancestor(idA, idB);
        }
        List<String> words = this.synset.get(ancester);
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            sb.append(word);
            sb.append(" ");
        }
        return sb.substring(0, sb.length() - 1).toString();
    }

    // do unit testing of this class
    public static void main(String[] args) {
    }
}
