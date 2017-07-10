package ssdb;

import com.topifish.vertx.ssdb.SSDBClient;
import com.topifish.vertx.ssdb.models.SSDBOptions;
import com.topifish.vertx.ssdb.pool.SSDBPool;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/10.
 */
public class TestVerticle extends AbstractVerticle
{
    @Override
    public void start() throws Exception
    {
        super.start();
        SSDBOptions options = new SSDBOptions("127.0.0.1", 8888, "123fdafdasfdasfdsafdasfa");

        SSDBPool pool = new SSDBPool(vertx, options);
        pool.getClient(event -> {
            SSDBClient client = event.result();
            client.setAutoClose(false)
                  .dbsize(event1 -> {
                      if (event1.failed()) {
                          System.out.println(event1.cause());
                          return;
                      }
                      System.out.printf("dbsize %d\n", event1.result());
                      client.qsize("aaaa", event2 -> {
                          System.out.println(event2.result());
                      });
                  });
        });
    }

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new TestVerticle());
    }
}
