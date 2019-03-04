package com.books;

import com.books.service.BookService;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookStoreApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration-test.yml")
public class BookTestContainer
{
    @Rule
    public MySQLContainer mysql = new MySQLContainer();

    @Autowired
    MockMvc mockMvc;

    /*@Autowired
    BookRepository bookRepository;*/

    @Autowired
    BookService bookService;

    @Before
    public void setMysql()
    {
        mysql.start();
    }

    @Test
    public void getDatabaseName()
    {
        System.out.println(mysql.getDatabaseName());
    }

    //Close MySql connection once you we finished our work
    @AfterClass
    void after()
    {
        mysql.stop();
    }
}
