package org.motechproject.tasks.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class StatusController {

    @RequestMapping("/status")
    @ResponseBody
    public String status() {
        return "Tasks - OK ";
    }

}
