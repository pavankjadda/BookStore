package com.books.web;

import org.springframework.web.bind.annotation.RequestMapping;


//@Controller
public class ForwardController
{

    @RequestMapping(value = "/**/{[path:[^.]*}")
    public String redirect()
    {
        // Forward to home page so that route is preserved.
        return "forward:/";
    }
}
