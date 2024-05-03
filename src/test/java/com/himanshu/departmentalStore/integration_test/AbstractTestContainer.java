package com.himanshu.departmentalStore.integration_test;

import org.testcontainers.containers.MySQLContainer;
abstract class AbstractTestContainer {

    static final MySQLContainer MY_SQL_CONTAINER;

    static {
        MY_SQL_CONTAINER = new MySQLContainer("mysql:latest");
        MY_SQL_CONTAINER.start();
    }
}
