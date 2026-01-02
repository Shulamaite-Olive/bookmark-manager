<<<<<<<< HEAD:src/main/java/com/bookmanager/bookmark_manager/BookmarkManagerApplication.java
package com.bookmanager.bookmark_manager;
========
package co.anywhere.bookmark_manager;
>>>>>>>> c03e753 (refactor : updated package name and error code):src/main/java/co/anywhere/bookmark_manager/BookmarkManagerApplication.java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BookmarkManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookmarkManagerApplication.class, args);
	}

}
