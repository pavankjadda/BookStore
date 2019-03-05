package com.books.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GenericController
{
    @RequestMapping(value = {"/","/home"})
    public ModelAndView redirectToHome()
    {
        return new ModelAndView("redirect:index.html");
    }
}
