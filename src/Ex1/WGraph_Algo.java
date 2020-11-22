package Ex1;

import java.io.File;
import java.io.FileWriter;
import java.util.*;


/** a class containing algorithms to manipulate and check the WGraph_DS graphs. */
public class WGraph_Algo  implements weighted_graph_algorithms{

    WGraph_DS myGraph;
    NodeComparator nodeComp = new NodeComparator();

    /** create an empty object of WGraph_Algo */
    public WGraph_Algo(){}

    /** create an object of WGraph_Algo with the inputted graph. */
    public WGraph_Algo(weighted_graph g){
        myGraph = (WGraph_DS) g;
    }

    /** sets a new graph for the object to work on. */
    @Override
    public void init(weighted_graph g) {
        myGraph = (WGraph_DS) g;
    }

    /** return the current graph. */
    @Override
    public weighted_graph getGraph() {
        return myGraph;
    }

    /** returns a copy of the of the main graph.
      * @return weighted_graph
     */
    @Override
    public weighted_graph copy() {
        WGraph_DS newGr = new WGraph_DS();
        Collection<node_info> nodes =  myGraph.getV();
        for(Iterator<node_info> node = nodes.iterator(); node.hasNext();){
            newGr.addNode(node.next().getKey());
        }
        for(node_info node: nodes){
            ArrayList<node_info> nis = (ArrayList<node_info>) myGraph.getV(node.getKey());
            int srcKey = node.getKey();
            for(node_info ni : nis){
                int key = ni.getKey();
                newGr.connect(srcKey, key, myGraph.getEdge(srcKey, key));
            }
        }
        return newGr;
    }

    /** return if the graph is connected.
     * if from every node you can reach any node,
     * the graph is connected.
     * @return
     */
    @Override
    public boolean isConnected() {
        clearTag();
        LinkedList<node_info> q = new LinkedList<node_info>();
        List<node_info> nodes = (List<node_info>) myGraph.getV();
        if(nodes.size() == 0 || nodes.size() == 1){
            return true;
        }
        node_info src = nodes.get(0);
        q.add(src);
        while(!q.isEmpty()){
            src = q.remove();
            Collection<node_info> nis = myGraph.getV(src.getKey());
            for(node_info ni : nis){
                if(ni.getTag() == 0){
                    q.add(ni);
                }
                ni.setTag(1);
            }
        }
        for(Iterator<node_info> node1 = nodes.iterator(); node1.hasNext();){
            if(node1.next().getTag() == 0){
                return false;
            }
        }
        return true;
    }

    /** returns the shortest distance between src node to the dest node.
     * if there is no path between the nodes returns -1.
     * @param src - start node
     * @param dest - end (target) node
     * @return
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        if(src == dest){
            return 0;
        }
        this.clearTag();
        node_info node, nDest;
        node = myGraph.getNode(src);
        nDest = myGraph.getNode(dest);
        if(node != null && nDest != null){
            node.setInfo("");
            PriorityQueue<node_info> que = new PriorityQueue<node_info>(myGraph.nodeSize(), nodeComp);
            que.add(node);
            while(!que.isEmpty()){
                node = que.remove();
                if(node.getKey() == dest){
                    return node.getTag();
                }
                List<node_info> nis = (List<node_info>)myGraph.getV(node.getKey());
                for(node_info ni : nis){
                    double dist = node.getTag() + myGraph.getEdge(node.getKey(), ni.getKey());
                    if((dist < ni.getTag() || ni.getTag() == 0 )&& ni.getKey() != src){
                        if(dist < ni.getTag()){
                            que.remove(ni);
                        }
                        ni.setInfo(node.getInfo() + ni.getKey() + ",");
                        ni.setTag(dist);
                        que.add(ni);
                    }
                }
            }
        }
        return -1;
    }

    /** return a list of nodes that are the shortest path from src node to dest node.
     * in case of none existing path return null.
     * @param src - start node
     * @param dest - end (target) node
     * @return List<node_info> or null if none existing path
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if(this.shortestPathDist(src, dest) != -1){
            List<node_info> path = new LinkedList<node_info>();
            node_info node = myGraph.getNode(src);
            path.add(node);
            if(src == dest){
                return path;
            }
            String info = myGraph.getNode(dest).getInfo();
            while(!info.isEmpty()){
                int divider = info.indexOf(",");
                String key = info.substring(0,divider);
                node = myGraph.getNode(Integer.parseInt(key));
                path.add(node);
                info = info.substring(divider+1);
            }
            return path;
        }
        return null;
    }

    /** save the init graph to a text file for later use.
     * @param file - the file name (may include a relative path).
     * @return if the function was successful in saving the file.
     */
    @Override
    public boolean save(String file) {
        try{
            FileWriter writer = new FileWriter(file);
            String data = myGraph.toSave();
            writer.write(data);
            writer.close();
        }
        catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /** loads a graph from a saved file.
     * @param file - file name
     * @return if the loading was successful.
     */
    @Override
    public boolean load(String file) {
        WGraph_DS newGr = new WGraph_DS();
        try{
            File saveFile = new File(file);
            Scanner reader = new Scanner(saveFile);
            while(reader.hasNextLine()){
                String data = reader.nextLine();
                if(!data.isEmpty()){
                    int end = data.indexOf(']');
                    int node = Integer.parseInt(data.substring(1,end));
                    newGr.addNode(node);
                    data = data.substring(end+1);
                    while(!data.isEmpty()){
                        end = data.indexOf(']');
                        int divider = data.indexOf('|');
                        int ni = Integer.parseInt(data.substring(1,divider));
                        double weight = Double.parseDouble(data.substring(divider+1,end));
                        newGr.addNode(ni);
                        newGr.connect(node, ni, weight);
                        data = data.substring(end+1);
                    }
                }
            }
            myGraph = newGr;        //sets the new graph as the init graph
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private void clearTag() {
        List<node_info> nodes =  (List<node_info>)myGraph.getV();
        for (Iterator<node_info> node = nodes.iterator(); node.hasNext();) {
            node.next().setTag(0);
        }
    }

    /** returns the init graph info. */
    public String toString(){
        return myGraph.toString();
    }


    private class NodeComparator implements Comparator<node_info>{

        @Override
        public int compare(node_info o1, node_info o2) {
            return (int) (o1.getTag() - o2.getTag());
        }
    }
}
