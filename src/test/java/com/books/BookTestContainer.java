package com.books;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.testcontainers.containers.MySQLContainer;

public class BookTestContainer
{
    @Rule
    public MySQLContainer mysql = new MySQLContainer();

    @Before
    public void setMysql()
    {
        mysql.start();
    }

    @Test
    public void checkMySqlStatus()
    {
        System.out.println(mysql.getDatabaseName());
    }
}
