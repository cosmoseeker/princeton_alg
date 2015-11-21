import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BaseballElimination {
    private final Map<String, Integer> teamMap = new LinkedHashMap<String, Integer>();
    private final List<String> teamName = new ArrayList<String>();
    private final int teamNum;
    private final int[] win;
    private final int[] lose;
    private final int[] remain;
    private final int[][] game;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        CheckUtil.checkNull(filename);
        In in = new In(filename);
        teamNum = in.readInt();
        win = new int[teamNum];
        lose = new int[teamNum];
        remain = new int[teamNum];
        game = new int[teamNum][teamNum];
        String name;
        for (int i = 0; i < teamNum; ++i) {
            name = in.readString();
            teamMap.put(name, i);
            teamName.add(name);
            win[i] = in.readInt();
            lose[i] = in.readInt();
            remain[i] = in.readInt();
            for (int j = 0; j < teamNum; ++j) {
                game[i][j] = in.readInt();
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return this.teamNum;
    }

    // all teams
    public Iterable<String> teams() {
        return this.teamMap.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        checkTeam(team);
        return win[teamMap.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        checkTeam(team);
        return lose[teamMap.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        checkTeam(team);
        return remain[teamMap.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        checkTeam(team1);
        checkTeam(team2);
        return game[teamMap.get(team1)][teamMap.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (certificateOfElimination(team) == null) {
            return false;
        }
        return true;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        checkTeam(team);
        Iterable<String> teams = null;
        teams = tivial(team);
        if (teams == null) {
            teams = nonTrivial(team);
        }
        return teams;
    }

    private Iterable<String> tivial(String team) {
        int teamNo = teamMap.get(team);
        int wins = win[teamNo] + remain[teamNo];
        for (int i = 0; i < teamNum; ++i) {
            if (wins < win[i]) {
                return Arrays.asList(teamName.get(i));
            }
        }
        return null;
    }

    private Iterable<String> nonTrivial(String team) {
        int teamNo = teamMap.get(team);
        int wins = win[teamNo] + remain[teamNo];
        Set<String> cert = null;
        int gameNum = ((this.teamNum - 1) * (this.teamNum - 2)) / 2;
        FlowNetwork flow = new FlowNetwork(gameNum + teamNum + 1);
        initFlow(flow, teamNo, wins);
        // find cert teams
        FordFulkerson ff = new FordFulkerson(flow, 0, flow.V() - 1);
        gameNum++;
        for (int gameIdx = 1; gameIdx < gameNum; gameIdx++) {
            if (ff.inCut(gameIdx)) {
                for (FlowEdge edge : flow.adj(gameIdx)) {
                    int teamIdx = edge.other(gameIdx) - gameNum;
                    if (teamIdx >= 0) {
                        if (edge.flow() < edge.capacity()) {
                            if (cert == null) {
                                cert = new HashSet<String>();
                            }
                            if (teamIdx < teamNo) {
                                cert.add(teamName.get(teamIdx));
                            } else {
                                cert.add(teamName.get(teamIdx + 1));
                            }
                        }
                    }
                }
            }
        }
        return cert;
    }

    private void initFlow(FlowNetwork flow, int teamNo, int wins) {
        // init source to games
        int gameIndex = 1;
        for (int i = 0; i < teamNum - 1; i++) {
            for (int j = i + 1; j < teamNum; j++) {
                if (i != teamNo && j != teamNo) {
                    flow.addEdge(new FlowEdge(0, gameIndex++, game[i][j]));
                }
            }
        }
        // init game to team
        int gameNum = gameIndex;
        gameIndex = 1;
        for (int i = 0; i < teamNum; i++) {
            for (int j = i + 1; j < teamNum; j++) {
                if (i != teamNo && j != teamNo) {
                    int team1 = i > teamNo ? i - 1 : i;
                    int team2 = j > teamNo ? j - 1 : j;
                    flow.addEdge(new FlowEdge(gameIndex, gameNum + team1,
                            Double.POSITIVE_INFINITY));
                    flow.addEdge(new FlowEdge(gameIndex, gameNum + team2,
                            Double.POSITIVE_INFINITY));
                    gameIndex++;
                }
            }
        }
        // init team to sink
        int sink = flow.V() - 1;
        for (int i = 0; i < teamNum; i++) {
            int teamIndex;
            if (i != teamNo) {
                if (i < teamNo) {
                    teamIndex = i;
                } else {
                    teamIndex = i - 1;
                }
                if (wins - win[i] >= 0) {
                    flow.addEdge(new FlowEdge(gameNum + teamIndex, sink, wins
                            - win[i]));
                } else {
                    flow.addEdge(new FlowEdge(gameNum + teamIndex, sink, 0));
                }
            }
        }
        // StdOut.print(flow.toString());
    }

    private void checkTeam(String team) {
        if (!teamMap.containsKey(team)) {
            throw new java.lang.IllegalArgumentException();
        }
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}
