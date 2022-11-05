package com.swpp.footprinter.domain.user.repository

import com.swpp.footprinter.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long>
