package com.swpp.footprinter.test

import com.swpp.footprinter.domain.auth.dto.SignUpRequest
import com.swpp.footprinter.domain.auth.service.AuthService
import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.model.Tag
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.user.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
@Transactional
@Profile("!test")
class DataInitializer(
    private val userRepo: UserRepository,
    private val authService: AuthService,
    private val tagRepo: TagRepository,
) : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        listOf("User1", "User2", "User3").forEach {
            if (!userRepo.existsByUsername(it)) {
                authService.createUser(SignUpRequest(username = it, password = "encoded-password"))
            }
        }
        userRepo.findAll().forEach { println(it.username) }

        for (tagCode in TAG_CODE.values()) {
            if (!tagRepo.existsByTagCode(tagCode)) {
                tagRepo.save(Tag(tagCode, mutableSetOf()))
            }
        }
    }
}
