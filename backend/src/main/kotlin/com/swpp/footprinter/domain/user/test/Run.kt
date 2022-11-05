package com.swpp.footprinter.domain.user.test

import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DataInitializer(private val userRepo: UserRepository) : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        println("This should be printing to the console but it is not")

        listOf("User1", "User2", "User3").forEach {
            userRepo.save(User(username = it, email = "$it@snu.ac.kr", myTrace = listOf()))
        }
        userRepo.findAll().forEach { println(it.username) }
    }
}
