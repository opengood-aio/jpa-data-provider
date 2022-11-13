package app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["io.opengood.data.jpa.provider", "app", "test.data"])
class TestApplication

fun main(args: Array<String>) {
    runApplication<TestApplication>(*args)
}
