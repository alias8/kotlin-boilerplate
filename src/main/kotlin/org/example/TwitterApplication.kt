package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TwitterApplication

fun main(args: Array<String>) {
    runApplication<TwitterApplication>(*args)
}
