import Ex1.WGraph_Algo;
import Ex1.WGraph_DS;
import Ex1.node_info;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class graph_algo_test {

    WGraph_DS g = new WGraph_DS();
    WGraph_Algo ga = new WGraph_Algo(g);

    @BeforeEach
    void set(){
        for(int i = 0; i < 10; i++)
        {
            g.addNode(i);
        }
        for(int i = 0; i < 10; i++)
        {
            g.connect(i, i+1, 2*i +1);
        }
        ga.init(g);
    }


    @Test
    void Copy1(){
        WGraph_DS g0 = new WGraph_DS();
        ga.init(g0);
        WGraph_DS g1 = (WGraph_DS) ga.copy();
        Assertions.assertEquals(0,g1.nodeSize());
        Assertions.assertEquals(0,g1.edgeSize());

        System.out.println("first copy succeeded");

    }



    @Test
    void Copy2(){
        WGraph_DS gc = (WGraph_DS) ga.copy();
        Assertions.assertEquals(g.edgeSize(),gc.edgeSize());
        Assertions.assertEquals(g.nodeSize(),gc.nodeSize());

        for(int i = 0; i < g.nodeSize(); i++)
        {
            Assertions.assertEquals(g.getEdge(i,i+1),gc.getEdge(i,i+1));
        }

        System.out.println("second copy succeeded");
    }




    @Test
    void Connected1(){
        Assertions.assertTrue(ga.isConnected());

        System.out.println("first connected succeeded");
    }

    @Test
    void Connected2(){
        g.connect(0,2,2);
        g.removeEdge(0,1);
        Assertions.assertTrue(ga.isConnected());
        g.removeEdge(2,0);
        Assertions.assertFalse(ga.isConnected());

        System.out.println("second connected succeeded");
    }


    @Test
    void Connected3(){
        g.removeNode(0);
        Assertions.assertTrue(ga.isConnected());
        g.removeNode(5);
        Assertions.assertFalse(ga.isConnected());

        System.out.println("third connected succeeded");
    }



    @Test
    void Connected4(){

        WGraph_DS g0 = new WGraph_DS();
        ga.init(g0);
        Assertions.assertTrue(ga.isConnected());

    }





    @Test
    void shortedDist1(){
        WGraph_DS g0 = new WGraph_DS();
        ga.init(g0);
        Assertions.assertEquals(-1,ga.shortestPathDist(1,2));

        System.out.println("first finding shorted path succeeded");
    }



    @Test
    void shortedDist2(){
        Assertions.assertEquals(21,ga.shortestPathDist(2,5));
        g.removeNode(3);
        Assertions.assertEquals(-1,ga.shortestPathDist(2,5));

        System.out.println("second finding shorted path succeeded");
    }

    @Test
    void shortedDist3(){

        Assertions.assertEquals(0,ga.shortestPathDist(5,5));
        Assertions.assertEquals(-1,ga.shortestPathDist(0,10));

    }




    @Test
    void shortedDist4(){
        g.connect(2,7,1);
        g.connect(3,7,1);
        g.connect(3,6,7);
        g.connect(4,6,1);

        Assertions.assertEquals(18,ga.shortestPathDist(2,5));
        Assertions.assertEquals(18,ga.shortestPathDist(5,2));
    }


    @Test
    void shortedDist5(){
        g.connect(2,7,3);
        g.connect(6,7,1);
        g.connect(6,5,1);
        g.connect(2,3,2);
        g.connect(3,4,2);

        Assertions.assertEquals(5,ga.shortestPathDist(2,5));
    }









    @Test
    void Save1(){
        String file = "messi.txt";
        Assertions.assertTrue(ga.save(file));

        System.out.println("first save succeeded");
    }




    @Test
    void Save2(){
        WGraph_DS g0 = new WGraph_DS();
        ga.init(g0);
        Assertions.assertTrue(ga.save("messi.txt"));

        System.out.println("second save succeeded");
    }





    @Test
    void Load1(){
        String file = "messi.txt";
        ga.save(file);
        g.removeNode(5);

        Assertions.assertTrue(ga.load(file));
        Assertions.assertTrue(ga.isConnected());

        System.out.println("first load succeeded");
    }

    @Test
    void Load2(){
        WGraph_DS g0 = new WGraph_DS();
        ga.init(g0);
        ga.save("messi.txt");
        Assertions.assertTrue(ga.load("messi.txt"));

        System.out.println("second load succeeded");
    }





    @Test
    void shortedDistPath1(){
        List<node_info> list = ga.shortestPath(0,9);

        for(int i = 0; i < 10; i++)
        {
            Assertions.assertEquals(i, list.get(i).getKey());
        }
        System.out.println("test");
    }







    @Test
    void shortedDistPath2(){
        List<node_info> list = ga.shortestPath(5,5);

        Assertions.assertEquals(5,list.get(0).getKey());
        Assertions.assertNull(ga.shortestPath(-1,100));

        System.out.println("path");

    }



    @Test
    void shortedDistPath3(){
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

        System.out.println("succeeded");

    }
}
