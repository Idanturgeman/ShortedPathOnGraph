
package Ex1;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;


public class node_test {

    WGraph_DS.NodeInfo n1;
    WGraph_DS.NodeInfo n2;
    WGraph_DS.NodeInfo n3;

    @BeforeEach
    void set(){

        n1 = new WGraph_DS.NodeInfo(1);
        n2 = new WGraph_DS.NodeInfo(2);
        n3 = new WGraph_DS.NodeInfo(3);

        n1.addNi(n1, 5);
        n1.addNi(n2, 3);
        n3.addNi(n1, 10);
        n3.addNi(n1, 0);
    }

    @Test
    void Edge(){
        n2.setEdge(n1, 14);

        Assertions.assertEquals(10, n1.getEdge(n3));
        Assertions.assertEquals(10, n3.getEdge(n1));
        Assertions.assertEquals(-1, n3.getEdge(n2));
        Assertions.assertEquals(-1, n2.getEdge(n3));
        Assertions.assertEquals(-1, n1.getEdge(n1));
        Assertions.assertEquals(14, n1.getEdge(n2));
        Assertions.assertEquals(14, n2.getEdge(n1));

        System.out.println("how about the edge!");
    }



    @Test
    void Add(){
        Collection<node_info> nis = n1.getNis();

        Assertions.assertEquals(2, nis.size());
        Assertions.assertTrue(nis.contains(n3));
        Assertions.assertTrue(nis.contains(n2));
        Assertions.assertFalse(nis.contains(n1));
        nis = n2.getNis();
        Assertions.assertEquals(1, nis.size());

        System.out.println("we got the adding!");
    }





        @Test
        void Remove(){
        n1.removeNi(n2);

        Collection<node_info> nis = n1.getNis();
        Assertions.assertEquals(1, nis.size());
        n3.removeNi(n2);
        nis = n2.getNis();
        Assertions.assertTrue(nis.isEmpty());

        System.out.println("professional removing");
    }
}
