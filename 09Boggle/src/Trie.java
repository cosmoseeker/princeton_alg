class TrieNode {
    // Initialize your data structure here.
    private static final char BASE = 'A';
    private static final int RADIX = 26;
    private TrieNode[] node = new TrieNode[RADIX];
    private boolean leaf = false;

    public TrieNode() {
    }

    public TrieNode getNode(char c) {
        return this.node[c - BASE];
    }

    public void setNode(char c, TrieNode node) {
        this.node[c - BASE] = node;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}

public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    public Trie(String[] dictionary) {
        root = new TrieNode();
        for (String word : dictionary) {
            this.insert(word);
        }
    }

    // Inserts a word into the trie.
    public void insert(String word) {
        TrieNode node = this.root, next;
        char[] cword = word.toCharArray();
        int len = word.length();
        for (int i = 0; i < len; i++) {
            char c = cword[i];
            next = node.getNode(c);
            if (next == null) {
                next = new TrieNode();
                node.setNode(c, next);
            }
            node = next;
        }
        node.setLeaf(true);
    }

    // Returns if the word is in the trie.
    public boolean search(String word) {
        TrieNode node = this.root;
        char[] cword = word.toCharArray();
        int len = word.length();
        for (int i = 0; i < len; i++) {
            node = node.getNode(cword[i]);
            if (node == null) {
                return false;
            }
        }
        return node.isLeaf();
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {
        TrieNode node = this.root;
        char[] cword = prefix.toCharArray();
        int len = prefix.length();
        for (int i = 0; i < len; i++) {
            node = node.getNode(cword[i]);
            if (node == null) {
                return false;
            }
        }
        return true;
    }

    public int checkWord(String word) {
        TrieNode node = this.root;
        char[] cword = word.toCharArray();
        int len = word.length();
        for (int i = 0; i < len; i++) {
            node = node.getNode(cword[i]);
            if (node == null) {
                return 0;
            }
        }
        if (node.isLeaf())
            return 1;
        else
            return -1;
    }
}