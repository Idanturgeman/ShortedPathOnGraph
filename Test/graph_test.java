import Ex1.WGraph_DS;
import Ex1.node_info;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


public class graph_test {

    WGraph_DS g = new WGraph_DS();

    @BeforeEach
    void set(){
        for(int i = 0; i < 20; i++){
            g.addNode(i);
        }
    }



    @Test
    void getEdge(){
        g.connect(2,3,5);
        Assertions.assertEquals(5,g.getEdge(3,2));
        Assertions.assertEquals(5,g.getEdge(2,3));
        Assertions.assertEquals(-1, g.getEdge(30,1));

        System.out.println("nice edge");
    }


    @Test
    void doesNotHavingEdge(){
        Assertions.assertEquals(-1,g.getEdge(7,5));
        g.connect(5,5,10);
        Assertions.assertEquals(-1,g.getEdge(5,5));
        Assertions.assertEquals(0,g.edgeSize());

        System.err.println("we don't have such an edge");
    }





    @Test
    void UpdateEdge(){
        g.connect(0,1,1);
        Assertions.assertEquals(1,g.getEdge(0,1));
        g.connect(0,1,0);
        Assertions.assertEquals(1,g.getEdge(0,1));
        g.connect(0,1,5);
        Assertions.assertEquals(5,g.getEdge(0,1));

        System.out.println("very nice edge");
    }



    @Test
    void RemoveEdge(){
        g.connect(2,3,5);
        Assertions.assertEquals(5,g.getEdge(3,2));
        Assertions.assertEquals(1,g.edgeSize());
        g.removeEdge(2,3);
        Assertions.assertEquals(-1,g.getEdge(3,2));
        Assertions.assertEquals(0,g.edgeSize());

        System.out.println("professional edge removing");
    }


    @Test
    void EdgeSize(){
        g.connect(5,3,8);
        g.connect(5,4,9);
        g.connect(5,6,11);
        g.connect(5,20,11);
        Assertions.assertEquals(3,g.edgeSize());
        g.removeEdge(5,20);
        g.removeEdge(5,6);
        Assertions.assertEquals(2,g.edgeSize());
        g.removeNode(5);
        Assertions.assertEquals(0,g.edgeSize());

        System.out.println("nice edge size");
    }



    @Test
    void GetNode(){
        node_info n1 = g.getNode(5);
        node_info n2 = g.getNode(1000);
        Assertions.assertNotNull(n1);
        Assertions.assertNull(n2);
        Assertions.assertEquals(20,g.nodeSize());

        System.out.println("we got the node");
    }




    @Test
    void RemoveNode(){
        Assertions.assertNotNull(g.getNode(5));
        Assertions.assertEquals(20,g.nodeSize());
        g.removeNode(5);
        Assertions.assertNull(g.getNode(5));
        Assertions.assertEquals(19,g.nodeSize());

        System.out.println("professional node removing");
    }



    @Test
    void NodeSize(){
        Assertions.assertEquals(20,g.nodeSize());
        g.removeNode(5);
        g.removeNode(5);
        g.removeNode(100);
        Assertions.assertEquals(19,g.nodeSize());

        System.out.println("nice node size");
    }


    @Test
    void print(){
        for(int i = 0; i < 20; i++)
        {
            g.connect(i, i+1, 2*i +1);
        }
        System.out.println(g);
    }


    @Test
    void GetV(){
        ArrayList<node_info> nis = (ArrayList<node_info>) g.getV();
        int key = 0;
        for(node_info ni : nis)
        {
            Assertions.assertEquals(key,ni.getKey());
            key++;
        }
        for(int i = 0; i < 20; i++)
        {
            g.removeNode(i);
        }
        Assertions.assertTrue(nis.isEmpty());
    }



}
