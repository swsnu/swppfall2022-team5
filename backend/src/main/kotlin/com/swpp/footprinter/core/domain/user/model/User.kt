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

    @OneToMany(mappedBy = "owner")
    val myTraces: List<Trace> = listOf(),

    @ManyToMany(mappedBy = "likedUser")
    val likedTraces: List<Trace> = listOf(),

    @ManyToMany(mappedBy = "followingUser")
    val followedUser: List<User> = listOf(),

    @ManyToMany()
    val followingUser: List<User> = listOf(),

) : BaseEntity()
