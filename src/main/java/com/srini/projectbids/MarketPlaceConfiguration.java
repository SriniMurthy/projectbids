package com.srini.projectbids;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.validation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class MarketPlaceConfiguration extends Configuration {
    static {
        //System.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");
        System.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
    }
    private static final Logger log = LoggerFactory.getLogger(MarketPlaceConfiguration.class);


    //  @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory database = new DataSourceFactory();

  /**
   * @return An instance of database factory deserialized from the
   * configuration file passed as a command-line argument to the application.
   */

    public DataSourceFactory getDataSourceFactory() {
        try {
            log.debug( " DataSourceFactory really created?" + (database != null) +" " );
        }catch (Throwable th) {
            log.error(" ERROR CREATING DATASOURCEFACTORY",th);
        }
        return database;
    }
}
