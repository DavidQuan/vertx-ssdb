package ssdb;

import com.topifish.vertx.ssdb.SSDBClient;
import com.topifish.vertx.ssdb.SSDBPool;
import com.topifish.vertx.ssdb.models.SSDBOptions;
import com.topifish.vertx.ssdb.util.F;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.RunTestOnContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author FanYongquan
 * @copyright (c) topifish game studio, create on 2017/7/10.
 */
@RunWith(VertxUnitRunner.class)
public class TestSuite
{
    @Rule
    public RunTestOnContext runCtx = new RunTestOnContext();

    private SSDBPool pool;

    private void printf(String format, Object... args)
    {
        System.out.printf(format + "\n", args);
    }

    @Before
    public void setUp(TestContext testContext)
    {
        SSDBOptions options = new SSDBOptions("127.0.0.1", 8888, "123fdafdasfdasfdsafdasfa");
        this.pool = new SSDBPool(runCtx.vertx(), options);
    }

    @After
    public void shutdown(TestContext testContext)
    {
        pool.close(event -> testContext.assertTrue(event.succeeded()));
    }

    @Test
    public void testConnect(TestContext testContext)
    {
        Async async = testContext.async();
        pool.getClient(event -> {
            SSDBClient client = event.result();
            client.dbsize(event1 -> {
                testContext.assertTrue(event1.succeeded());
                printf("dbsize : %d", event1.result());
                async.complete();
            });
        });
    }

    @Test
    public void testAutoClose(TestContext testContext)
    {
        Async async = testContext.async();
        pool.getClient(event -> {
            SSDBClient client = event.result();
            client.dbsize(event1 -> {
                testContext.assertTrue(event1.succeeded());
                printf("dbsize : %d", event1.result());
                client.dbsize(event2 -> {
                    testContext.assertFalse(event1.failed());
                    async.complete();
                });
            });
        });
    }

    @Test
    public void testAll(TestContext testContext)
    {
        Async async = testContext.async();
        pool.getClient(event -> {
            SSDBClient client = event.result();
            client.beginBatch()
                  .set("a", "a", F.noneHandle())
                  .get("a", F.noneHandle())
                  .setx("k", "k", 10, F.noneHandle())
                  .scan(10, F.noneHandle())
                  .setbit("b", 0, 0, F.noneHandle())
                  .getbit("b", 0, F.noneHandle())
                  .del("a", F.noneHandle())
                  .exists("a", F.noneHandle())
                  .expire("a", 10, F.noneHandle())
                  .hclear("k", F.noneHandle())
                  .endBatch(ev -> {
                      client.close(F.succeededFuture());
                      async.complete();
                  });
        });
    }
}
