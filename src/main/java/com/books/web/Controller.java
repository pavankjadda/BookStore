package com.books.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@org.springframework.stereotype.Controller
public class Controller
{
    @GetMapping(value = {"/index","index.html"})
    public ModelAndView getIndexPage()
    {
        return new ModelAndView("index.html");
    }

    @GetMapping(value = {"/books","/books.html"})
    public ModelAndView getAllBooks()
    {
        return new ModelAndView("books.html");
    }

    @GetMapping(value = {"/books","/save_book.html"})
    public ModelAndView saveBook()
    {
        return new ModelAndView("save_book.html");
    }


    @GetMapping(value = {"/books","/view_book.html"})
    public ModelAndView viewBook()
    {
        return new ModelAndView("view_book.html");
    }
}
