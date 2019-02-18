import java.util.HashMap;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.Bag;

public class BaseballElimination {
    private int num;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] games;
    private HashMap<String, Integer> map;
    private HashMap<Integer, String> intStr;
    private HashMap<Integer, Bag<String>> certificates;
    private boolean[] eliminated;

    private void populate_team(In in, int team) {
        String name = in.readString();
        map.put(name, team);
        intStr.put(team, name);
        wins[team] = in.readInt();
        losses[team] = in.readInt();
        remaining[team] = in.readInt();
        for (int i = 0; i < num; i++) {
            games[team][i] = in.readInt();
        }
        eliminated[team] = false;
    }

    private FlowNetwork get_network(int team) {
        int vertices;
        int s;
        int t;

        vertices = 2 + (num) * (num - 1) / 2 + num;
        FlowNetwork network = new FlowNetwork(vertices);
        s = vertices - 2;
        t = vertices - 1;
        for (int i = 0; i < num; i++) {
            FlowEdge edge;
            if (wins[team] + remaining[team] - wins[i] < 0)
                edge = new FlowEdge(i, t, 0);
            else
                edge = new FlowEdge(i, t, wins[team] + remaining[team] - wins[i]);
            network.addEdge(edge);
        }
        int v = num;
        for (int i = 0; i < num; i++) {
            for (int j = i + 1; j < num; j++) {
                network.addEdge(new FlowEdge(s, v, games[i][j]));
                network.addEdge(new FlowEdge(v, i, Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(v, j, Double.POSITIVE_INFINITY));
                v++;
            }
        }
        return (network);
    }

    private void get_elimination(FlowNetwork network,int team) {
        int numV = 2 + (num) * (num - 1) / 2 + num;
        int numGames = 0;
        FordFulkerson ff = new FordFulkerson(network, numV - 2, numV - 1);

        for (int i = 0; i < num; i++) {
            numGames += remaining[i];
        }
        if (ff.value() + 0.0001 < (double)numGames / 2.0) {
            eliminated[team] = true;
            Bag<String> teams = new Bag<String>();
            for (int i = 0; i < num; i++) {
                if (ff.inCut(i)) {
                    if (i != team)
                        teams.add(intStr.get(i));
                }
            }
            certificates.put(team, teams);
        }
    }

    public BaseballElimination(String filename) {
        In in = new In(filename);
        FlowNetwork network;

        num = in.readInt();
        wins = new int[num];
        losses = new int[num];
        remaining = new int[num];
        games = new int[num][num];
        map = new HashMap<String, Integer>();
        intStr = new HashMap<Integer, String>();
        certificates = new HashMap<Integer, Bag<String>>();
        eliminated = new boolean[num];
        for (int team = 0; team < num; team++) {
            populate_team(in, team);
        }
        for (int team = 0; team < num; team++) {
            network = get_network(team);
            get_elimination(network, team);
        }
    }                    // create a baseball division from given filename in format specified below

    public              int numberOfTeams() {
        return wins.length;
    }                    // number of teams

    public Iterable<String> teams() {
        return (map.keySet());
    }                                // all teams

    public              int wins(String team) {
        if (!map.containsKey(team))
            throw new IllegalArgumentException("team given is not valid");
        return (wins[map.get(team)]);
    }                      // number of wins for given team

    public              int losses(String team) {
        if (!map.containsKey(team))
            throw new IllegalArgumentException("team given is not valid");
        return (losses[map.get(team)]);
    }                    // number of losses for given team

    public              int remaining(String team) {
        if (!map.containsKey(team))
            throw new IllegalArgumentException("team given is not valid");
        return (remaining[map.get(team)]);
    }                 // number of remaining games for given team

    public              int against(String team1, String team2) {
        if (!map.containsKey(team1) || !map.containsKey(team2))
            throw new IllegalArgumentException("team given is not valid");
        return (games[map.get(team1)][map.get(team2)]);
    }                                                                    // number of remaining games between team1 and team2

    public          boolean isEliminated(String team) {
        if (!map.containsKey(team))
            throw new IllegalArgumentException("team given is not valid");
        return (eliminated[map.get(team)]);
    }              // is given team eliminated?

    public Iterable<String> certificateOfElimination(String team) {
        if (!map.containsKey(team))
            throw new IllegalArgumentException("team given is not valid");
        if (this.isEliminated(team)) {
            return (certificates.get(map.get(team)));
        }
        return null;
    }  // subset R of teams that eliminates given team; null if not eliminated

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination("testfile.txt");

    }
}
