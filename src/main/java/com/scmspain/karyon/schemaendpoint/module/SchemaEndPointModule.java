package com.scmspain.karyon.schemaendpoint.module;

import com.google.inject.AbstractModule;
import com.scmspain.karyon.schemaendpoint.SchemaController;

public class SchemaEndPointModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(SchemaController.class).asEagerSingleton();
  }
}
