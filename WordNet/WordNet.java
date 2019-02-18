import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.DirectedCycle;
import java.util.HashMap;

public class WordNet {
    private final HashMap<Integer, String[]> intMap;
    private final HashMap<String, Bag<Integer>> strMap;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new java.lang.IllegalArgumentException("input file can not be null");
        Digraph graph;
        intMap = new HashMap<Integer, String[]>();
        strMap = new HashMap<String, Bag<Integer>>();
        graph = makeGraph(synsets);
        DirectedCycle dc = new DirectedCycle(graph);
        if (dc.hasCycle() == true)
            throw new IllegalArgumentException("graph provided contains a cycle");
        addHyperNyms(graph, hypernyms);
        sap = new SAP(graph);
    }

    private void addHyperNyms(Digraph graph, String hypernyms) {
        String line;
        In in = new In(hypernyms);
        String[] splitLine;

        while ((line = in.readLine()) != null) {
            splitLine = line.split(",");
            if (splitLine.length < 2)
                continue;
            for(int i = 1; i < splitLine.length; i++) {
                graph.addEdge(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[i]));
            }
        }
    }

    private Digraph makeGraph(String synsets) {
        int count;
        String line;
        String[] splitStr;
        int id;

        count = 0;
        In in = new In(synsets);
        while ((line = in.readLine()) != null) {
            splitStr = line.split(",");
            String[] nouns;
            if (splitStr.length < 2)
                continue;
            id = Integer.parseInt(splitStr[0]);
            nouns = splitStr[1].split(" ");
            intMap.put(id, nouns);
            for(String noun : nouns) {
                if (strMap.containsKey(noun)) {
                    strMap.get(noun).add(id);
                }
                else {
                    Bag<Integer> ids = new Bag<Integer>();
                    ids.add(id);
                    strMap.put(noun, ids);
                }
            }
            count++;
        }
        Digraph graph = new Digraph(count);
        return graph;
    }
    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return (strMap.keySet());
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new java.lang.IllegalArgumentException("String word can not be null");
        return (strMap.containsKey(word));
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new java.lang.IllegalArgumentException("arguments can not be null");
        if (strMap.containsKey(nounA) == false || strMap.containsKey(nounB) == false) {
            throw new java.lang.IllegalArgumentException("noun given is not a valid word");
        }
        return sap.length(strMap.get(nounA), strMap.get(nounB));

    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || nounB == null)
            throw new java.lang.IllegalArgumentException("arguments can not be null");
        if (strMap.containsKey(nounA) == false || strMap.containsKey(nounB) == false) {
            throw new java.lang.IllegalArgumentException("noun given is not a valid word");
        }
        return (intMap.get(sap.ancestor(strMap.get(nounA), strMap.get(nounB)))[0]);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // for testing
    }
}