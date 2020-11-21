package Ex1;

import java.io.File;
import java.io.FileWriter;
import java.util.*;

/** a class containing algorithms to manipulate and check the WGraph_DS graphs. */
public class WGraph_Algo  implements weighted_graph_algorithms{

    WGraph_DS myGraph;      //the main graph the algorithms are working on
    NodeComparator nodeComp = new NodeComparator(); //a custom comparator for to compare between node in the algorithms

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
        WGraph_DS newGr = new WGraph_DS();                                             //initialize the new graph
        Collection<node_info> nodes =  myGraph.getV();                       //get a list of all the nodes in the main graph
        for(Iterator<node_info> node = nodes.iterator(); node.hasNext();){                                                   //for every node in the graph, create a new node for newGr
            newGr.addNode(node.next().getKey());                                              //create a copy of the nodes
        }
        for(node_info node: nodes){                                                    //for every node in the graph, copy its neighbors
            ArrayList<node_info> nis = (ArrayList<node_info>) myGraph.getV(node.getKey());  //get the neighbors of every node in the graph
            int srcKey = node.getKey();
            for(node_info ni : nis){                                                   //for every neighbors a node has, copy its edges
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
    public boolean isConnected() {                                            //utilizes the BFS algorithm
        clearTag();                                                           //clear the tags from previous algorithms
        LinkedList<node_info> q = new LinkedList<node_info>();                        //create a queue to hold the nodes that are checked
        List<node_info> nodes = (List<node_info>) myGraph.getV();
        if(nodes.size() == 0 || nodes.size() == 1){                           //if the graph is empty or is just a node
            return true;                                                      //the graph is connected
        }
        node_info src = nodes.get(0);                                         //the starting node for the algorithm
        q.add(src);
        while(!q.isEmpty()){                                                  //if the queue is empty then we checked every connected node
            src = q.remove();                                                 //remove the node we checked
            Collection<node_info> nis = myGraph.getV(src.getKey());            //get the src neighbors
            for(node_info ni : nis){                                          //for every node in src's neighbors,
                if(ni.getTag() == 0){                                         //check if the node was visited (default 0 = false)
                    q.add(ni);                                                //if not add it to the queue of unvisited nodes
                }
                ni.setTag(1);                                                 //after the node was checked set the tag accordingly
            }
        }
        for(node_info node1 : nodes){                                         //check every node int the graph
            if(node1.getTag() == 0){                                          //if there is a node that wasn't visited
                return false;                                                 //then the algorithm couldn't reach the node because he wasn't connected
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
    public double shortestPathDist(int src, int dest) {                                      //utilize the Dijkstra algorithm
        if(src == dest){                                                                     //if the src is also the dest the distance is 0
            return 0;
        }
        this.clearTag();                                                                     //clear the tags from previous algorithms
        node_info node, nDest;
        node = myGraph.getNode(src);
        nDest = myGraph.getNode(dest);
        if(node != null && nDest != null){                                                   //check that the nodes exist in the graph
            node.setInfo("");                                                                //empty the src node info, mainly to record the path of nodes
            PriorityQueue<node_info> que = new PriorityQueue<node_info>(myGraph.nodeSize(), nodeComp);    //priority queue for the algorithm with class' comp
            que.add(node);
            while(!que.isEmpty()){                                                           //check every node until queue is empty
                node = que.remove();                                                         //remove the node that is being checked
                if(node.getKey() == dest){                                                   //if reached the dest node
                    return node.getTag();                                                    //return the distance calculated and stored in the tag
                }
                Collection<node_info> nis = myGraph.getV(node.getKey());
                for(node_info ni : nis){                                                     //for every neighbor of the checked node
                    double dist = node.getTag() + myGraph.getEdge(node.getKey(), ni.getKey());//set the distance as the current edge weight and node tag
                    if((dist < ni.getTag() || ni.getTag() == 0 )&& ni.getKey() != src){      //if encounter an unvisited node or a shorter path that isn't the src node
                        if(dist < ni.getTag()){                                              //check if encountered a visited node with shorter path
                            que.remove(ni);                                                  //remove the visited node from the queue
                        }                                                                    //so he can be replaced with a the same node with a shorter path
                        ni.setInfo(node.getInfo() + ni.getKey() + ",");                      //record the node in the path, inside the next node
                        ni.setTag(dist);                                                     //store distance that was passed in the tag
                        que.add(ni);                                                         //add the neighboring node to the the queue
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
    public List<node_info> shortestPath(int src, int dest) {     //utilizes the shortestPathDist function
        if(this.shortestPathDist(src, dest) != -1){              //checks if there is a path between the nodes
            LinkedList<node_info> path = new LinkedList<node_info>();    //the list that will be returned
            node_info node = myGraph.getNode(src);
            path.add(node);                                      //adds the first node in the path (src node)
            if(src == dest){                                     //if src is the only node in the path return the path
                return path;
            }
            String info = myGraph.getNode(dest).getInfo();        //get the path that was stored inside the dest info
            while(!info.isEmpty()){                              //extract the nodes from info until there is none
                int divider = info.indexOf(",");                 //the divider between every node key in info
                String key = info.substring(0,divider);          //extract the node key from the path
                node = myGraph.getNode(Integer.parseInt(key));
                path.add(node);                                  //add the extracted node
                info = info.substring(divider+1);                //remove the extracted node from info
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
            FileWriter writer = new FileWriter(file);   //create the save file
            String data = myGraph.toSave();              //get the save info of the graph
            writer.write(data);                         //write the info into the file
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
        WGraph_DS newGr = new WGraph_DS();                               //the new init graph that the information will be loaded to
        try{
            File saveFile = new File(file);                              //get the save file
            Scanner reader = new Scanner(saveFile);                      //read the file
            while(reader.hasNextLine()){                                 //check every line in the file
                String data = reader.nextLine();                         //get the current line
                if(!data.isEmpty()){                                     //check that the data is not null
                    int end = data.indexOf(']');                         //every key and edge is stored inside a "[key|edge]" format
                    int node = Integer.parseInt(data.substring(1,end));  //extract the current node in the line
                    newGr.addNode(node);
                    data = data.substring(end+1);                        //remove from data the extracted node
                    while(!data.isEmpty()){                              //run through every neighboring node and edge inside data until there is none
                        end = data.indexOf(']');
                        int divider = data.indexOf('|');                 //divider between key and edge inside data
                        int ni = Integer.parseInt(data.substring(1,divider));                 //extract the neighboring node key
                        double weight = Double.parseDouble(data.substring(divider+1,end));    //extract the edges weight
                        newGr.addNode(ni);                                                    //create a node with the key
                        newGr.connect(node, ni, weight);                                      //connect it to the main node
                        data = data.substring(end+1);                                         //remove the extracted info from data
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

    //set all the tags in the nodes to 0, as to create a clean environment for the relevant algorithms
    private void clearTag() {
        Collection<node_info> nodes =  myGraph.getV();
        for (node_info node : nodes) {  //set every node in the graph to default tag of 0
            node.setTag(0);
        }
    }

    /** returns the init graph info. */
    public String toString(){
        return myGraph.toString();
    }

    /* a custom comparator for the queue, utilized in the main class
     * as to sort the nodes by their tag size.
     */
    private class NodeComparator implements Comparator<node_info>{

        @Override
        public int compare(node_info o1, node_info o2) {
            return (int) (o1.getTag() - o2.getTag());
        }
    }
}
