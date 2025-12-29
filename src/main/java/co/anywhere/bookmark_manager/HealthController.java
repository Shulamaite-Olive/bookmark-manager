<<<<<<<< HEAD:src/main/java/com/bookmanager/bookmark_manager/HealthController.java
package com.bookmanager.bookmark_manager;
========
package co.anywhere.bookmark_manager;
>>>>>>>> c03e753 (refactor : updated package name and error code):src/main/java/co/anywhere/bookmark_manager/HealthController.java

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}
