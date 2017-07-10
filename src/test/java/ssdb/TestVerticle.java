package ssdb;

import com.topifish.vertx.ssdb.SSDBClient;
import com.topifish.vertx.ssdb.models.SSDBOptions;
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
        SSDBOptions options = new SSDBOptions("127.0.0.1", 8888);

        SSDBClient client = SSDBClient.create(vertx, options);
        client.dbsize(event -> {
            if (event.failed()) {
                System.out.println(event.cause());
                return;
            }
            System.out.printf("dbsize %d\n", event.result());
        });
    }

    public static void main(String[] args)
    {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new TestVerticle());
    }
}
