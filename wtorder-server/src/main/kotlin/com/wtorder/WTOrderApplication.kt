package com.wtorder

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WTOrderApplication

fun main(args: Array<String>) {
    runApplication<WTOrderApplication>(*args)
}
