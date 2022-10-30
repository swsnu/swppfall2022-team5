package com.swpp.footprinter.core.domain.user.model

import com.swpp.footprinter.core.common.model.BaseEntity
import com.swpp.footprinter.core.domain.trace.model.Trace
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.ManyToMany
import javax.persistence.OneToMany

@Entity
class User(

    @Column(name = "username")
    val username: String,

    @Column(name = "email")
    val email: String,

    @OneToMany(mappedBy = "trace")
    val myTraces: List<Trace> = listOf(),

    @ManyToMany(mappedBy = "trace")
    val likedTraces: List<Trace> = listOf(),

    @ManyToMany(mappedBy = "user")
    val followedUser: List<User> = listOf(),

    @ManyToMany(mappedBy = "user")
    val followingUser: List<User> = listOf(),

) : BaseEntity()
