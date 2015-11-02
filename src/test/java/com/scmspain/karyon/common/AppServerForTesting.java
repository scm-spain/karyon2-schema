package com.scmspain.karyon.common;

import com.netflix.governator.annotations.Modules;
import com.scmspain.karyon.schemaendpoint.module.SchemaEndPointModule;
import netflix.karyon.KaryonBootstrap;
import netflix.karyon.archaius.ArchaiusBootstrap;
import scmspain.karyon.restrouter.KaryonRestRouterModule;

@ArchaiusBootstrap()
@KaryonBootstrap(name = "AppServer")
@Modules(
    include = {
        AppServerForTesting.AppServer.class,
        SchemaEndPointModule.class
    })
public interface AppServerForTesting {
  class AppServer extends KaryonRestRouterModule {
    public static final int DEFAULT_PORT = 8000;
    public static final int DEFAULT_THREADS_POOL_SIZE = 20;

    @Override
    protected void configureServer() {
      server()
        .port(DEFAULT_PORT)
        .threadPoolSize(DEFAULT_THREADS_POOL_SIZE);
    }

    @Override
    protected void configure() {
      super.configure();
    }
  }

}
