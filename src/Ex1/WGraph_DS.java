package Ex1;

import java.util.*;

/** a class that simulate a weighted unidirectional graph.*/
public class WGraph_DS implements weighted_graph {


    static class NodeInfo implements node_info {

        private int _key;
        private double _tag = 0;
        private String _info;

        private HashMap<node_info, Double> edgeMap = new HashMap<node_info, Double>();
        private ArrayList<node_info> neiList = new ArrayList<node_info>() {
        };


        public NodeInfo(int key) {

            _key = key;
        }


        @Override
        public int getKey() {

            return _key;
        }

        @Override
        public String getInfo() {

            return _info;
        }

        @Override
        public void setInfo(String s) {

            _info = s;
        }

        @Override
        public double getTag() {

            return _tag;
        }

        @Override
        public void setTag(double t) {

            _tag = t;
        }



        public boolean hasnei(node_info node) {

            return neiList.contains(node);
        }


        /** adds a node as neighbor and created a weighted edge.*/
        public void addNi(NodeInfo node, double weight) {

            if (node != null && !edgeMap.containsKey(node) && _key != node.getKey())
            {
                edgeMap.put(node, weight);
                neiList.add(node);
                node.addNi(this, weight);
            }
        }


        public Collection<node_info> getNis() {

            return neiList;
        }


        /** remove the node from list of neighbors and remove the edge.*/
        public void removeNi(NodeInfo node) {

            if (node != null && edgeMap.containsKey(node))
            {
                edgeMap.remove(node);
                neiList.remove(node);
                node.removeNi(this);
            }
        }


        /** update the weight of the edge between this node and the inputted node.*/
        public void setEdge(node_info ni, double weight) {

            if (neiList.contains(ni) && edgeMap.get(ni) != weight)
            {
                edgeMap.replace(ni, weight);
                NodeInfo niTemp = (NodeInfo) ni;
                niTemp.setEdge(this, weight);
            }
        }




        /** return the weight of the edge between this node and the inputted node.*/
        public double getEdge(node_info ni) {

            if (edgeMap.containsKey(ni))
            {
                return edgeMap.get(ni);
            }
            return -1;
        }




        /** return a string that hold all the information of the node.*/
        public String toString() {
            String info = "[" + _key + "]:";

            for (node_info ni : neiList)
            {
                info += " [" + ni.getKey() + "," + this.getEdge(ni) + "]";
            }
            info += ". tag = " + _tag + ".";
            return info;
        }

        /** return a string with the nodes information, for saving the node info in a file.*/
        public String forSaving() {
            String info = "[" + _key + "]";

            for (node_info ni : neiList)
            {
                info += "[" + ni.getKey() + "|" + this.getEdge(ni) + "]";
            }
            return info;
        }
    }




    private int edgeSize = 0;
    private int MC = 0;

    private ArrayList<node_info> nodeList = new ArrayList<node_info>();

    private HashMap<Integer, NodeInfo> nodeListMap = new HashMap<Integer, NodeInfo>();






    /** return the node by its node_id (key).
     *  if the node does not exist return null.
     * @param key - the node_id
     * @return node_info, null if none
     */
    @Override
    public node_info getNode(int key) {

        if (nodeListMap.containsKey(key))
        {
            return nodeListMap.get(key);
        }
        return null;
    }

    /** checks if the edge between the the 2 nodes exist.
     *  the nodes are called by their id's (key)
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public boolean hasEdge(int node1, int node2) {

        if (nodeListMap.containsKey(node1) && nodeListMap.containsKey(node2))
        {
            NodeInfo NODE1 = (NodeInfo) this.getNode(node1);
            NodeInfo NODE2 = (NodeInfo) this.getNode(node2);
            return NODE1.hasnei(NODE2);
        }
        return false;
    }

    /** return the weight of the edge between the 2 nodes.
     * if the edge does not exist return -1.
     * the nodes are called by their id's (key)
     * @param node1
     * @param node2
     * @return
     */
    @Override
    public double getEdge(int node1, int node2) {

        if (nodeListMap.containsKey(node1) && nodeListMap.containsKey(node2))
        {
            NodeInfo NODE1 = (NodeInfo) this.getNode(node1);
            NodeInfo NODE2 = (NodeInfo) this.getNode(node2);
            return NODE1.getEdge(NODE2);
        }
        return -1;
    }

    /** adds a node to the graph,
     * any number can be the key of the node.
     * if the node already exist nothing changes.
     * @param key
     */
    @Override
    public void addNode(int key) {

        if (!nodeListMap.containsKey(key))
        {
            NodeInfo node = new NodeInfo(key);
            nodeListMap.put(key, node);
            nodeList.add(node);
            MC++;
        }
    }

    /** connect 2 node in the graph to create an edge,
     *  the function set the weight of the new edge.
     *  if the edge already exist simply set the new weight to the existing edge.
     *  the weight cannot be equal or smaller than 0.
     *  a node cannot be connected to himself.
     *  in case of invalid weight or nodes nothing changes.
     * @param node1
     * @param node2
     * @param w
     */
    @Override
    public void connect(int node1, int node2, double w) {

        if (node1 != node2 && w > 0 && nodeListMap.containsKey(node1) && nodeListMap.containsKey(node2))
        {
            NodeInfo NODE1 = nodeListMap.get(node1);
            NodeInfo NODE2 = nodeListMap.get(node2);

            if(NODE1.hasnei(NODE2))
            {
                NODE1.setEdge(NODE2, w);
            }
            else
                {
                NODE1.addNi(NODE2, w);
                edgeSize++;
            }
            MC++;
        }
    }

    /** return a collection of all the nodes in the graph.
     * @return Collection<node_info>
     */
    @Override
    public Collection<node_info> getV() {

        return nodeList;
    }

    /** return a collection all the neighboring nodes of a specific node
     *  the node is called by its id (key)
     * @param node_id
     * @return Collection<node_info>
     */
    @Override
    public Collection<node_info> getV(int node_id) {

        return nodeListMap.get(node_id).getNis();
    }

    /** remove a node from the graph.
     * also break all edge connected to the node.
     * if the node does not exist nothing changes.
     * the node is called by its id (key)
     * @param key
     * @return the removed node.
     */
    @Override
    public node_info removeNode(int key) {

        if (nodeListMap.containsKey(key))
        {
            NodeInfo node = (NodeInfo) this.getNode(key);
            Collection<node_info> nis = node.getNis();

            if (!nis.isEmpty())
            {
                ArrayList<node_info> nisTemp = new ArrayList<node_info>(nis);

                for (node_info ni : nisTemp)
                {
                    NodeInfo niTemp = (NodeInfo) ni;
                    niTemp.removeNi(node);
                    edgeSize--;
                }
            }
            nodeList.remove(node);
            nodeListMap.remove(node.getKey());
            MC++;
            return node;
        }
        return null;
    }

    /** remove the edge between the 2 nodes.
     *  if the edge or the nodes do not exist nothing changes.
     *  the nodes are called by their id (key)
     * @param node1
     * @param node2
     */
    @Override
    public void removeEdge(int node1, int node2) {

        if (hasEdge(node1, node2))
        {
            NodeInfo NODE1 = (NodeInfo) this.getNode(node1);
            NodeInfo NODE2 = (NodeInfo) this.getNode(node2);
            NODE1.removeNi(NODE2);
            edgeSize--;
            MC++;
        }
    }

    /** return the number of nodes in the graph.
     * @return
     */
    @Override
    public int nodeSize() {

        return nodeList.size();
    }

    /** return the number of edges in the graph.
     * @return
     */
    @Override
    public int edgeSize() {

        return edgeSize;
    }

    /** return the number of modification done to the graph.
     * @return
     */
    @Override
    public int getMC() {

        return MC;
    }

    /** a toString function for the graph.
     * prints the number of nodes edges in the graph,
     * along with a list of every node and his neighbors edges and tags.
     * @return
     */
    public String toString(){
        String sGraph = "nodes: "+nodeList.size()+", edges: "+edgeSize;
        Collection<node_info> graphList = this.getV();

        for(Iterator<node_info> node = graphList.iterator(); node.hasNext();)
        {
            NodeInfo nodeI = (NodeInfo) node.next();
            sGraph += "\n" + nodeI.toString();
        }
        return sGraph;
    }

    /** create a string of information from the graph
     *  for the purpose of storing it in a save file.
     *  mainly used for the WGraph_Algo class.
     * @return
     */
    public String forSaving(){
        String sGraph = "";
        Collection<node_info> graphList = this.getV();

        for(Iterator<node_info> node = graphList.iterator(); node.hasNext();)
        {
            NodeInfo nodeI = (NodeInfo) node.next();
            sGraph += nodeI.forSaving() + "\n";
        }
        return sGraph;
    }





    /**  creates an empty graph */
    public WGraph_DS() { }

}
