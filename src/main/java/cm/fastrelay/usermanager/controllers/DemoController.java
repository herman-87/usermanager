package cm.fastrelay.usermanager.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
    @GetMapping("/public/hello")
    public String sayHello() {
        return "Hello i am user microservice";
    }
}
