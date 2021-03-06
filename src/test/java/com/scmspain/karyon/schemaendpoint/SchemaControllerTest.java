package com.scmspain.karyon.schemaendpoint;

import com.netflix.governator.guice.BootstrapModule;
import com.scmspain.karyon.common.AppServerForTesting;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.RxNetty;
import io.reactivex.netty.protocol.http.client.HttpClientRequest;
import io.reactivex.netty.protocol.http.client.HttpClientResponse;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;
import io.reactivex.netty.protocol.http.client.HttpResponseHeaders;
import netflix.karyon.Karyon;
import netflix.karyon.KaryonServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
          .doOnNext(response -> assertEquals(HttpResponseStatus.OK, response.getStatus()))
          .flatMap(HttpClientResponse::getContent)
          .map(content -> content.toString(Charset.defaultCharset()))
          .timeout(10, TimeUnit.SECONDS)
          .toBlocking().single();

    assertEquals("{\"hello\": \"world\"}", body);
  }

  @Test
  public void itShouldReturnCorsHeadersWhenRequestUsingOptions() throws Exception {

    RxNetty.createHttpClient("localhost",
        AppServerForTesting.AppServer.DEFAULT_PORT)
          .submit(HttpClientRequest.create(HttpMethod.OPTIONS, "/schema"))
          .doOnNext(response -> {
              HttpResponseHeaders headers = response.getHeaders();
              assertTrue(headers.contains("Access-Control-Allow-Origin"));
              assertTrue(headers.contains("Access-Control-Allow-Methods"));
              assertTrue(headers.contains("Access-Control-Allow-Headers"));
            }
          )
          .timeout(10, TimeUnit.SECONDS)
          .toBlocking().single();
  }
}