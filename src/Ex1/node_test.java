
package Ex1;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashSet;

//note: must set NodeInfo to public static
//after testing return to private
public class node_test {

    WGraph_DS.NodeInfo n1;   //the node that will be used in the tests
    WGraph_DS.NodeInfo n2;
    WGraph_DS.NodeInfo n3;

    @BeforeEach
    void setUp(){
        n1 = new WGraph_DS.NodeInfo(1);   //initialize the nodes and connect them
        n2 = new WGraph_DS.NodeInfo(2);
        n3 = new WGraph_DS.NodeInfo(3);
        n1.addNi(n1, 5);                  //connect the nodes to create edges
        n1.addNi(n2, 3);
        n3.addNi(n1, 10);
        n3.addNi(n1, 0);
    }

    @Test
    void testAdd(){
        Collection<node_info> nis = n1.getNis();
        Assertions.assertEquals(2, nis.size());   //test if nodes are connected
        Assertions.assertTrue(nis.contains(n2));  //check the content of the list of neighbors
        Assertions.assertTrue(nis.contains(n3));
        Assertions.assertFalse(nis.contains(n1));
        nis = n2.getNis();
        Assertions.assertEquals(1, nis.size());
    }

    @Test
    void testEdge(){
        n2.setEdge(n1, 14);
        Assertions.assertEquals(10, n1.getEdge(n3));   //test if the connection is symmetrical
        Assertions.assertEquals(10, n3.getEdge(n1));
        Assertions.assertEquals(14, n1.getEdge(n2));   //test if updating an edge function properly
        Assertions.assertEquals(14, n2.getEdge(n1));
        Assertions.assertEquals(-1, n3.getEdge(n2));   //test in case of none exiting edge
        Assertions.assertEquals(-1, n2.getEdge(n3));
        Assertions.assertEquals(-1, n1.getEdge(n1));
    }

        @Test
        void testRemove(){
        n1.removeNi(n2);
        Collection<node_info> nis = n1.getNis();
        Assertions.assertEquals(1, nis.size());  //check if the edge was broken
        n3.removeNi(n2);
        nis = n2.getNis();
        Assertions.assertTrue(nis.isEmpty());
    }
}
