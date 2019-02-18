import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;

public class SAP {
    private final Digraph graph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new java.lang.IllegalArgumentException("graph can not be null");
        graph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        int length;

        length = -1;
        if (v < 0 || w < 0 || v >= graph.V() || w >= graph.V())
            throw new IllegalArgumentException("vertex given is out of range");
        BreadthFirstDirectedPaths bfv = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths bfw = new BreadthFirstDirectedPaths(this.graph, w);
        for (int i = 0; i < graph.V(); i++) {
            if (bfv.hasPathTo(i) && bfw.hasPathTo(i))
            {
                if (length < 0 || bfv.distTo(i) + bfw.distTo(i) < length) {
                    length = bfv.distTo(i);
                    length += bfw.distTo(i);
                }
            }
        }
        return (length);
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        int ancestor;
        int length;

        ancestor = -1;
        length = -1;
        if (v < 0 || w < 0 || v >= graph.V() || w >= graph.V())
            throw new IllegalArgumentException("vertex given is out of range");
        BreadthFirstDirectedPaths bfv = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths bfw = new BreadthFirstDirectedPaths(this.graph, w);
        for (int i = 0; i < graph.V(); i++) {
            if (bfv.hasPathTo(i) && bfw.hasPathTo(i)) {
                if (ancestor < 0 || bfv.distTo(i) + bfw.distTo(i) < length) {
                    length = bfv.distTo(i) + bfw.distTo(i);
                    ancestor = i;
                }
            }
        }
        return (ancestor);
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        int length;

        length = -1;
        if (v == null || w == null)
            throw new java.lang.IllegalArgumentException("subset can not be null");
        for (int i : v) {
            if (i < 0 || i >= graph.V())
                throw new IllegalArgumentException("vertex given is out of range");
        }
        for (int i : w) {
            if (i < 0 || i >= graph.V())
                throw new IllegalArgumentException("vertex given is out of range");
        }
        BreadthFirstDirectedPaths bfv = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths bfw = new BreadthFirstDirectedPaths(this.graph, w);
        for (int i = 0; i < graph.V(); i++) {
            if (bfv.hasPathTo(i) && bfw.hasPathTo(i)) {
                if (length == -1 || bfv.distTo(i) + bfw.distTo(i) < length) {
                    length = bfv.distTo(i) + bfw.distTo(i);
                }
            }
        }
        return (length);
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        int length;
        int ancestor;

        length = -1;
        ancestor = -1;
        if (v == null || w == null)
            throw new java.lang.IllegalArgumentException("subset can not be null");
        for (int i : v) {
            if (i < 0 || i >= graph.V())
                throw new IllegalArgumentException("vertex given is out of range");
        }
        for (int i : w) {
            if (i < 0 || i >= graph.V())
                throw new IllegalArgumentException("vertex given is out of range");
        }
        BreadthFirstDirectedPaths bfv = new BreadthFirstDirectedPaths(this.graph, v);
        BreadthFirstDirectedPaths bfw = new BreadthFirstDirectedPaths(this.graph, w);
        for (int i = 0; i < graph.V(); i++)
        {
            if (bfv.hasPathTo(i) && bfw.hasPathTo(i)) {
                if (length == -1 || bfv.distTo(i) + bfw.distTo(i) < length) {
                    length = bfv.distTo(i) + bfw.distTo(i);
                    ancestor = i;
                }
            }
        }
        return (ancestor);
    }

    // do unit testing of this class
    public static void main(String[] args) {
        // empty
    }
}