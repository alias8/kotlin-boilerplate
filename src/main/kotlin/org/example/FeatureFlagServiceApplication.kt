package org.example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FeatureFlagServiceApplication

fun main(args: Array<String>) {
    runApplication<FeatureFlagServiceApplication>(*args)
}
