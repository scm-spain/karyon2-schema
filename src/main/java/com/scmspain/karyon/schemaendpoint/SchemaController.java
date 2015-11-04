package com.scmspain.karyon.schemaendpoint;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.reactivex.netty.protocol.http.server.HttpServerResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.ws.rs.HttpMethod;
import rx.Observable;
import scmspain.karyon.restrouter.annotation.Endpoint;
import scmspain.karyon.restrouter.annotation.Path;

@Endpoint
public class SchemaController {

  private static final String SCHEMA_FILE = "schema.json";

  private static final String CONTENT_TYPE_HEADER = "Content-Type";
  private static final String CONTENT_TYPE = "application/json";

  private static final String ORIGIN_HEADER = "Access-Control-Allow-Origin";
  private static final String METHODS_HEADER = "Access-Control-Allow-Methods";
  private static final String ALLOWED_HEADERS_HEADER = "Access-Control-Allow-Headers";

  private static final String ORIGIN = "*";
  private static final String METHODS = "GET, OPTIONS";
  private static final String HEADERS = "content-type, accept";

  @Path(value = "/schema", method = HttpMethod.OPTIONS)
  public Observable<Void> optionsSchema(HttpServerResponse<ByteBuf> response) {
    addJsonHeaders(response);
    response.setStatus(HttpResponseStatus.NO_CONTENT);
    return Observable.empty();
  }

  @Path(value = "/schema", method = HttpMethod.GET)
  public Observable<Void> getSchema(HttpServerResponse<ByteBuf> response) {
    InputStream schema = findSchema();

    if (null == schema) {
      return response404(response);
    }

    return responseWithSchema(addJsonHeaders(response), schema);

  }

  private InputStream findSchema() {

    return Thread.currentThread()
      .getContextClassLoader()
      .getResourceAsStream(SCHEMA_FILE);
  }

  private Observable<Void> responseWithSchema(
      HttpServerResponse<ByteBuf> response,
      InputStream schema
  ) {

    InputStreamReader schemaStreamReader = new InputStreamReader(schema);
    BufferedReader schemaBufferedReader = new BufferedReader(schemaStreamReader);
    StringBuilder fileContent = new StringBuilder();
    String line;

    try {
      while ((line = schemaBufferedReader.readLine()) != null) {
        fileContent.append(line);
      }
    } catch (IOException error) {
      fileContent.append("");
    }

    response.getHeaders().setContentLength(fileContent.length());
    response.getHeaders().removeTransferEncodingChunked();

    return response.writeStringAndFlush(fileContent.toString());
  }

  private Observable<Void> response404(HttpServerResponse<ByteBuf> response) {

    response.setStatus(HttpResponseStatus.NOT_FOUND);
    return Observable.empty();
  }

  private HttpServerResponse<ByteBuf> addJsonHeaders(HttpServerResponse<ByteBuf> response) {

    response.getHeaders().add(CONTENT_TYPE_HEADER, CONTENT_TYPE);
    response.getHeaders().add(ORIGIN_HEADER, ORIGIN);
    response.getHeaders().add(METHODS_HEADER, METHODS);
    response.getHeaders().add(ALLOWED_HEADERS_HEADER, HEADERS);

    return response;
  }

}