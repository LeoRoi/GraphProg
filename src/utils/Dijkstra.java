package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * Dijkstra shortest path algorithm
 */
public class Dijkstra {

    // declare a map with edges & nodes
    private Map<String, Node> map;
    public boolean ok = true;

    public static class Edge {
        String from, to;
        int weight;

        public Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    /**
     * One vertex of the map, complete with mappings to neighbouring vertices
     */
    public static class Node implements Comparable<Node> {
        String name;
        int weight = Integer.MAX_VALUE;
        Node previous = null;
        Map<Node, Integer> neighbors = new HashMap<>();

        Node(String name) {
            this.name = name;
        }

        private void print() {
            if (this == this.previous) {
                System.out.printf("%s", this.name);
            }
            else if (this.previous == null) {
                System.out.printf("%s(unreached)", this.name);
            }
            else {
                this.previous.print();
                System.out.printf(" -> %s(%d)", this.name, this.weight);
            }
        }

        public int compareTo(Node other) {
            if (weight == other.weight)
                return name.compareTo(other.name);

            return Integer.compare(weight, other.weight);
        }

        @Override
        public String toString() {
            return "(" + name + ", " + weight + ")";
        }
    }

    /**
     * init map
     * @param edges all connections
     */
    public Dijkstra(Edge[] edges) {
        map = new HashMap<>(edges.length);

        // add nodes
        for (Edge temp : edges) {
            if (!map.containsKey(temp.from))
                map.put(temp.from, new Node(temp.from));
            if (!map.containsKey(temp.to))
                map.put(temp.to, new Node(temp.to));
        }

        // find neighbors
        for (Edge temp : edges) {
            map.get(temp.from).neighbors.put(map.get(temp.to), temp.weight);
        }
    }

    /**
     * actual procedure
     * @param startName here it begins
     */
    public void dijkstra(String startName) {
        if (!map.containsKey(startName)) {
            System.err.printf("Dijkstra doesn't contain this start cell!\n");
            AlertBox.display("Dijkstra error", "Non-existing start cell!");
            ok = false;
            return;
        }

        Node start = map.get(startName);
        NavigableSet<Node> node = new TreeSet<>();

        // set-up vertices
        for (Node temp : map.values()) {
            temp.previous = temp == start ? start : null;
            temp.weight = temp == start ? 0 : Integer.MAX_VALUE;
            node.add(temp);
        }
        dijkstra(node);
    }

    /**
     * procedure 2: with binary heap
     * @param node to begin
     */
    private void dijkstra(final NavigableSet<Node> node) {
        Node u, v;

        while (!node.isEmpty()) {

            // shortest edge
            u = node.pollFirst();

            // ignore u if unreachable
            if (u.weight == Integer.MAX_VALUE)
                break;

            // compare neighbor-distances
            for (Map.Entry<Node, Integer> a : u.neighbors.entrySet()) {
                v = a.getKey();

                final int alternateDist = u.weight + a.getValue();
                // any other shorter paths?
                if (alternateDist < v.weight) {
                    node.remove(v);
                    v.weight = alternateDist;
                    v.previous = u;
                    node.add(v);
                }
            }
        }
    }

    public void printPath(String endName) {
        if (!map.containsKey(endName)) {
            System.err.printf("Dijkstra doesn't contain this end cell!\n");
            AlertBox.display("Dijkstra error", "Non-existing end cell!");
            ok = false;
            return;
        }

        map.get(endName).print();
        System.out.println();
    }
}
