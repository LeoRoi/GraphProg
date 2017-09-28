package utils;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;

/*
https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
with Fibonacci instead of self-balancing binary search tree
it is only E + log N !!!

Assoziative Speicher
http://openbook.rheinwerk-verlag.de/javainsel9/javainsel_13_008.htm

sets
http://openbook.rheinwerk-verlag.de/javainsel9/javainsel_13_005.htm
 */

/**
 * Dijkstra shortest path algorithm
 * O = E * log N
 */
public class Dijkstra {
    private Map<String, Node> map; // HashMap <key, value>
    public boolean ok = true; // status indicator

    /**
     * step 1: constructor
     * @param edges array holds all connections
     */
    public Dijkstra(Edge[] edges) {
        map = new HashMap<>(edges.length); //name & node

        //add nodes in map
        for (Edge temp : edges) {
            if ( !map.containsKey(temp.from) ) //if not already mapped - add!
                map.put(temp.from, new Node(temp.from)); //name as key, node as value
            if ( !map.containsKey(temp.to) )
                map.put(temp.to, new Node(temp.to));
        }

        //add weights to the nodes in the map
        for (Edge temp : edges) {
            map.get( temp.from ).neighbors.put( map.get(temp.to), temp.weight );
            //System.out.println(temp.weight);
        }

        /*
         at this point we've a HashMap with all nodes
         each node has another HashMap with distances to the neighbors
          */
    }

    /**
     * single connection
     * called only by Dijkstra constructor
     */
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
     * single vertex
     */
    public static class Node implements Comparable<Node> {
        String name;
        int weight = Integer.MAX_VALUE; //distance from start-node
        Node prev = null;
        Map<Node, Integer> neighbors = new HashMap<>(); // other (adjacent) nodes & their distances

        Node(String name) {
            this.name = name;
        }

        private void print() {

            //start mark (2 requirements)
            if (this == this.prev) {
                System.out.printf("%s", this.name);
            }
            else if (this.prev == null) {
                System.out.printf("%s(unreached)", this.name);
            }
            else {
                this.prev.print();
                System.out.printf(" -> %s(%d)", this.name, this.weight);
            }
        }

        /**
         * used in step 3 to compare node-weights while shaping the sll
         * @param other node for comparison
         * @return
         */
        @Override
        public int compareTo(Node other) {
            if (weight == other.weight)
                return name.compareTo(other.name);

            //This method returns the value 0 if this Integer is equal to the argument Integer, a value less than 0 if this Integer is numerically less than the argument Integer and a value greater than 0 if this Integer is numerically greater than the argument Integer.
            return Integer.compare(weight, other.weight);
        }

        @Override
        public String toString() {
            return "(" + name + ", " + weight + ")";
        }
    }

    /**
     * step 2: find & mark the start node; compose a TreeSet
     * @param startName node to mark
     */
    public void tree(String startName) {
        if (!map.containsKey(startName)) {
            System.err.printf("Graph doesn't contain this start cell!\n");
            AlertBox.display("Dijkstra error", "Non-existing start cell!");
            ok = false;
            return;
        }

        Node start = map.get(startName);
        NavigableSet<Node> navTreeSet = new TreeSet<>(); //always sorted binary tree

        // after this loop the start node in the treeSet is marked by 0 & has itself as previous node
        for (Node temp : map.values()) { //iterate map values (Nodes)
            temp.prev = temp == start ? start : null; //if(temp == start) temp.prev = start; else start = null;
            temp.weight = temp == start ? 0 : Integer.MAX_VALUE; //loop-distance = 0 V unreachable
            navTreeSet.add(temp);
        }
        //System.out.println(navTreeSet);
        shape(navTreeSet);
    }

    /**
     * step 3: update the neighbor distances
     * @param navTreeSet is final to avoid changes in elements
     */
    private void shape(final NavigableSet<Node> navTreeSet) {
        Node x, y;

        while (!navTreeSet.isEmpty()) {
            x = navTreeSet.pollFirst(); //pop the first least element V return null if there is no such thing = take the nearest neighbor as next

            //skip unreachable nodes
            if (x.weight == Integer.MAX_VALUE)
                break;

            // entrySet() liefert ein Set mit speziellen Map.Entry-Objekten, die gleichzeitig den Schlüssel sowie den Wert speichern
            for (Map.Entry<Node, Integer> temp : x.neighbors.entrySet()) {
                y = temp.getKey(); // y = neighbor node
                final int alternateDist = x.weight + temp.getValue(); //could be the distance to any neighbor

                // update distances & compose a singly linked list (sll) with x-node as root
                if (alternateDist < y.weight) {
                    navTreeSet.remove(y);
                    y.weight = alternateDist;
                    y.prev = x;
                    navTreeSet.add(y);
                }
                //System.out.println(navTreeSet);
            }
        }
    }

    /**
     * follow the sll from tail to head
     * @param endName tail of the sll
     */
    public void printPath(String endName) {
        if (!map.containsKey(endName)) {
            System.err.printf("Graph doesn't have this end-cell!\n");
            AlertBox.display("Dijkstra error", "Non-existing end-cell!");
            ok = false;
            return;
        }

        map.get(endName).print();
        System.out.println();
    }
}
