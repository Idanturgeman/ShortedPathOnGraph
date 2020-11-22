import Ex1.WGraph_DS;
import Ex1.node_info;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class graph_test {

    WGraph_DS g = new WGraph_DS();  //the testing graph

    @BeforeEach
    void setUp(){
        for(int i = 0; i < 10; i++){   //add nodes to the graph
            g.addNode(i);
        }
    }

    @Test
    void testGetNode(){
        node_info n1 = g.getNode(5);     //check if getNode returns the right argument
        node_info n2 = g.getNode(1000);
        Assertions.assertNotNull(n1);
        Assertions.assertNull(n2);
        Assertions.assertEquals(10,g.nodeSize());  //test if the size of node is counted correctly
    }

    @Test
    void testEdge(){
        g.connect(2,3,5);
        Assertions.assertEquals(5,g.getEdge(3,2));    //check if the connection is symmetrical
        Assertions.assertEquals(5,g.getEdge(2,3));
        Assertions.assertEquals(-1, g.getEdge(30,1)); //check argument for none existing edges
    }

    @Test
    void testUpdateEdge(){
        g.connect(0,1,1);
        Assertions.assertEquals(1,g.getEdge(0,1));  //test getEdge function
        g.connect(0,1,0);
        Assertions.assertEquals(1,g.getEdge(0,1));  //test if connect does not update in case of invalid arguments
        g.connect(0,1,5);
        Assertions.assertEquals(5,g.getEdge(0,1));  //test of connect update existing edges
    }

    @Test
    void falseEdge(){
        Assertions.assertEquals(-1,g.getEdge(7,5));  //check arguments for none existing edges
        g.connect(5,5,10);
        Assertions.assertEquals(-1,g.getEdge(5,5));  //check result from none valid connection
        Assertions.assertEquals(0,g.edgeSize());     //check that none valid edges weren't counted
    }

    @Test
    void testRemoveEdge(){
        g.connect(2,3,5);
        Assertions.assertEquals(5,g.getEdge(3,2));
        Assertions.assertEquals(1,g.edgeSize());
        g.removeEdge(2,3);
        Assertions.assertEquals(-1,g.getEdge(3,2));   //test that after removal all the relevant
        Assertions.assertEquals(0,g.edgeSize());      //information was updated
    }

    @Test
    void testRemoveNode(){
        Assertions.assertNotNull(g.getNode(5));
        Assertions.assertEquals(10,g.nodeSize());
        g.removeNode(5);                                  //test that after removal all the relevant
        Assertions.assertNull(g.getNode(5));              //information was updated
        Assertions.assertEquals(9,g.nodeSize());
    }

    @Test
    void testNodeSize(){
        Assertions.assertEquals(10,g.nodeSize());
        g.removeNode(5);                               //test remove node with false arguments
        g.removeNode(5);
        g.removeNode(100);
        Assertions.assertEquals(9,g.nodeSize());
    }

    @Test
    void testEdgeSize(){
        g.connect(5,3,8);                  //test remove edge with false arguments
        g.connect(5,4,9);
        g.connect(5,6,11);
        g.connect(5,20,11);
        Assertions.assertEquals(3,g.edgeSize());
        g.removeEdge(5,20);
        g.removeEdge(5,6);
        Assertions.assertEquals(2,g.edgeSize());
        g.removeNode(5);                              //test that all relevant edges were broken
        Assertions.assertEquals(0,g.edgeSize());  //with the removal of the main node
    }

    @Test
    void testGetV(){
        ArrayList<node_info> nis = (ArrayList<node_info>) g.getV();
        int key = 0;
        for(node_info ni : nis){                       //test that getV contains all the nodes
            Assertions.assertEquals(key,ni.getKey());
            key++;
        }
        for(int i = 0; i < 10; i++){
            g.removeNode(i);
        }
        Assertions.assertTrue(nis.isEmpty());
    }

    @Test
    void print(){  //test the printing of the graph
        for(int i = 0; i < 10; i++){
            g.connect(i, i+1, 2*i +1);
        }
        System.out.println(g);
    }


}
