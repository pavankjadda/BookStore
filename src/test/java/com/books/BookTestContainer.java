package com.books;

import com.books.model.Book;
import com.books.service.BookService;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookStoreApplication.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = {BookTestContainer.Initializer.class})
public class BookTestContainer
{
    @ClassRule
    public static MySQLContainer mySQLContainer = new MySQLContainer()
                                                    .withDatabaseName("test")
                                                    .withUsername("root")
                                                    .withPassword("bcmc1234");
                                                    //.withStartupTimeout(Duration.ofSeconds(600));

    @Autowired
    MockMvc mockMvc;

    /*@Autowired
    BookRepository bookRepository;*/

    @Autowired
    BookService bookService;

  /*  @Before
    public void setMysql()
    {
        mySQLContainer.start();
    }*/

    @Test
    public void getDatabaseName()
    {
        System.out.println(mySQLContainer.getDatabaseName());
    }


    @Test
    public void insertBook() throws Exception
    {
        Book book=createBook("Java Edition 9",200,12.23,"Craig");
        mockMvc.perform(get("/api/book/list").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(content().
                contentTypeCompatibleWith(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$[0].title", is(book.getTitle())));
    }

    private Book createBook(String title, Integer numberOfPages, Double cost, String author)
    {
        Book book=new Book(title,numberOfPages,cost,author);
        bookService.saveAndFlush(book);
        return book;
    }



    //Close MySql connection once you we finished our work
    @After
    public void after()
    {
        mySQLContainer.stop();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>
    {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext)
        {
            TestPropertyValues.of(
                    "spring.datasource.url=" + mySQLContainer.getJdbcUrl(),
                    "spring.datasource.username=" + mySQLContainer.getUsername(),
                    "spring.datasource.password=" + mySQLContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }
}
