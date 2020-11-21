import Ex1.WGraph_Algo;
import Ex1.WGraph_DS;
import Ex1.node_info;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class graph_algo_test {

    WGraph_DS g = new WGraph_DS();       //the graph that ga will work on
    WGraph_Algo ga = new WGraph_Algo(g);  //the object of the algorithms

    @BeforeEach
    void setUp(){
        for(int i = 0; i < 10; i++){   //add nodes to the graph
            g.addNode(i);
        }
        for(int i = 0; i < 10; i++){   //create edges in the graph
            g.connect(i, i+1, 2*i +1);
        }
        ga.init(g);
    }

    @Test
    void testCon1(){
        Assertions.assertTrue(ga.isConnected());
    }  //in the set up the graph is made connected

    @Test
    void testCon2(){
        WGraph_DS g0 = new WGraph_DS();
        ga.init(g0);
        Assertions.assertTrue(ga.isConnected());    //test if an empty graph is considered connected
    }

    @Test
    void testCon3(){
        g.removeNode(0);                       //test connectivity after removal of a none effecting node
        Assertions.assertTrue(ga.isConnected());
        g.removeNode(5);                       //test connectivity after splitting the graph to 2
        Assertions.assertFalse(ga.isConnected());
    }

    @Test
    void testCon4(){
        g.connect(0,2,2);
        g.removeEdge(0,1);
        Assertions.assertTrue(ga.isConnected());   //test connectivity after removing a none effecting edge
        g.removeEdge(2,0);
        Assertions.assertFalse(ga.isConnected());  //test connectivity after splitting the graph
    }

    @Test
    void testCopy1(){
        WGraph_DS gc = (WGraph_DS) ga.copy();                 //create a copy of g
        Assertions.assertEquals(g.edgeSize(),gc.edgeSize());  //compare the values of g tp it's copy
        Assertions.assertEquals(g.nodeSize(),gc.nodeSize());
        for(int i = 0; i < g.nodeSize(); i++){
            Assertions.assertEquals(g.getEdge(i,i+1),gc.getEdge(i,i+1));
        }
    }

    @Test
    void testCopy2(){
        WGraph_DS g0 = new WGraph_DS();
        ga.init(g0);
        WGraph_DS g1 = (WGraph_DS) ga.copy();               //check if it possible to copy an empty graph
        Assertions.assertEquals(0,g1.nodeSize());   //check values
        Assertions.assertEquals(0,g1.edgeSize());
    }

    @Test
    void testSave1(){
        String file = "save.txt";
        Assertions.assertTrue(ga.save(file));
    }

    @Test
    void testSave2(){
        WGraph_DS g0 = new WGraph_DS();                 //test saving an empty graph
        ga.init(g0);
        Assertions.assertTrue(ga.save("save.txt"));
    }

    @Test
    void testLoad1(){
        String file = "save.txt";
        ga.save(file);
        g.removeNode(5);
        Assertions.assertTrue(ga.load(file));
        Assertions.assertTrue(ga.isConnected());
    }

    @Test
    void testLoad2(){
        WGraph_DS g0 = new WGraph_DS();     //test loading an empty graph
        ga.init(g0);
        ga.save("save.txt");
        Assertions.assertTrue(ga.load("save.txt"));
    }

    @Test
    void testDist1(){
        Assertions.assertEquals(21,ga.shortestPathDist(2,5));  //test distance with a valid path
        g.removeNode(3);
        Assertions.assertEquals(-1,ga.shortestPathDist(2,5));  //test distance with an invalid path
    }

    @Test
    void testDist2(){
        Assertions.assertEquals(0,ga.shortestPathDist(5,5));   //test distance with a single node
        Assertions.assertEquals(-1,ga.shortestPathDist(0,10)); //test distance with invalid arguments
    }

    @Test
    void testDist3(){  //test distance with an empty graph
        WGraph_DS g0 = new WGraph_DS();
        ga.init(g0);
        Assertions.assertEquals(-1,ga.shortestPathDist(1,2));
    }

    @Test
    void testDist4(){   //test distance with a more complex graph
        g.connect(2,7,3);
        g.connect(6,7,1);
        g.connect(6,5,1);
        g.connect(2,3,2);
        g.connect(3,4,2);
        Assertions.assertEquals(5,ga.shortestPathDist(2,5));
    }

    @Test
    void testDist5(){
        g.connect(2,7,1);
        g.connect(3,7,1);
        g.connect(3,6,7);
        g.connect(4,6,1);
        Assertions.assertEquals(18,ga.shortestPathDist(2,5));
        Assertions.assertEquals(18,ga.shortestPathDist(5,2));
    }

    @Test
    void testPath1(){
        List<node_info> list = ga.shortestPath(0,9);
        for(int i = 0; i < 10; i++){
            Assertions.assertEquals(i, list.get(i).getKey());
        }
    }

    @Test
    void testPath2(){  //test path with a more complex graph
        g.connect(2,7,1);
        g.connect(3,7,1);
        g.connect(3,6,7);
        g.connect(4,6,1);
        List<node_info> list = ga.shortestPath(2,5);
        Assertions.assertEquals(2,list.get(0).getKey());
        Assertions.assertEquals(7,list.get(1).getKey());
        Assertions.assertEquals(3,list.get(2).getKey());
        Assertions.assertEquals(4,list.get(3).getKey());
        Assertions.assertEquals(5,list.get(4).getKey());
    }

    @Test
    void testPath3(){
        List<node_info> list = ga.shortestPath(5,5);    //test path with a single node
        Assertions.assertEquals(5,list.get(0).getKey());
        Assertions.assertNull(ga.shortestPath(-1,100)); //test path with invalid arguments
    }
}
