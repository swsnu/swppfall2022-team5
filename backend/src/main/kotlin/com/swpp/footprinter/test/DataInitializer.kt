package com.swpp.footprinter.test

import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.model.Tag
import com.swpp.footprinter.domain.tag.repository.TagRepository
import com.swpp.footprinter.domain.trace.model.Trace
import com.swpp.footprinter.domain.trace.repository.TraceRepository
import com.swpp.footprinter.domain.user.model.User
import com.swpp.footprinter.domain.user.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
@Transactional
@Profile("!test")
class DataInitializer(
    private val userRepo: UserRepository,
    private val traceRepo: TraceRepository,
    private val tagRepo: TagRepository,
) : ApplicationRunner {

    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        listOf("User1", "User2", "User3").forEach {
            if (!userRepo.existsByUsername(it)) {
                userRepo.save(User(username = it, email = "$it@snu.ac.kr", myTrace = mutableSetOf()))
            }
        }
        userRepo.findAll().forEach { println(it.username) }

        listOf("Trace1", "Trace2", "Trace3").forEach {
            if (!traceRepo.existsByTraceTitle(it)) {
                traceRepo.save(Trace(traceTitle = it, traceDate = "2022-11-05", footprints = mutableSetOf(), owner = userRepo.findByIdOrNull(1)!!))
            }
        }

        for (tagCode in TAG_CODE.values()) {
            if (!tagRepo.existsByTagCode(tagCode)) {
                tagRepo.save(Tag(tagCode, mutableSetOf()))
            }
        }
    }
}
