package com.swpp.footprinter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FootprinterApplication

fun main(args: Array<String>) {
    runApplication<FootprinterApplication>(*args)
}
