package com.books;

import com.books.dto.BookDto;
import com.books.model.Book;
import com.books.service.BookService;
import com.books.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = BookStoreApplication.class)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest
{
    @Autowired
    MockMvc mockMvc;

    @Autowired
    BookService bookService;

    @Test
    public void createPersons_And_GetPersonsTest() throws Exception
    {
        Book book=createBook("Spring Boot Essentials",200,12.23,"Craig");
        mockMvc.perform(get("/api/book/list").contentType(MediaType.APPLICATION_JSON))
                                    .andExpect(status().isOk())
                                    .andExpect(jsonPath("$[0].title", is(book.getTitle())));

    }

    @Test
    public void whenValidInput_createPerson() throws Exception
    {
        BookDto bookDto=new BookDto(1001,"Java Edition 9",250,19.99);
        mockMvc.perform(post("/api/book/save_book").contentType(MediaType.APPLICATION_JSON).content(JsonUtil.toJson(bookDto)));
        List<Book> retrievedBooks = bookService.findAll();

        assertThat(retrievedBooks).extracting(Book::getTitle).contains("Java Edition 9");
    }

    private Book createBook(String title, Integer numberOfPages, Double cost, String author)
    {
        Book book=new Book(title,numberOfPages,cost,author);
        bookService.saveAndFlush(book);
        return book;
    }

}
