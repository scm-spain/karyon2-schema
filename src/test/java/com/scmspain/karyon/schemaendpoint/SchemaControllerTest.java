package com.scmspain.karyon.schemaendpoint;

import com.netflix.governator.guice.BootstrapModule;
import com.scmspain.karyon.common.AppServerForTesting;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import netflix.karyon.Karyon;
import netflix.karyon.KaryonServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class SchemaControllerTest {

  private static KaryonServer server;

  @BeforeClass
  public static void setUpBefore() throws Exception {
    server = Karyon.forApplication(AppServerForTesting.class, (BootstrapModule[]) null);
    server.start();
  }

  @AfterClass
  public static void cleanUpAfter() throws Exception {
    server.shutdown();
  }

  @Test
  public void itShouldReturnSuccessCodeStatus() throws Exception {

    String body = RxNetty.createHttpClient("localhost",
        AppServerForTesting.AppServer.DEFAULT_PORT)
          .submit(HttpClientRequest.createGet("/schema"))
          .doOnNext(response -> Assert.assertEquals(HttpResponseStatus.OK, response.getStatus()))
          .flatMap(HttpClientResponse::getContent)
          .map(content -> content.toString(Charset.defaultCharset()))
          .timeout(10, TimeUnit.SECONDS)
          .toBlocking().single();

    Assert.assertEquals("{\"hello\": \"world\"}", body);
  }
}