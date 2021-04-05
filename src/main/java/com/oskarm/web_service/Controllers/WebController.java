package com.oskarm.web_service.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * A basic web controller, allowing to return an index.html to users.
 * 
 */

@Controller
public class WebController {
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

}
