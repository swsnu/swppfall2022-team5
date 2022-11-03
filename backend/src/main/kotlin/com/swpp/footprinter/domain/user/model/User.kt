package com.swpp.footprinter.domain.user.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.trace.model.Trace
import javax.persistence.*

@Entity
class User(

    @Column(name = "username")
    val username: String,

    @Column(name = "email")
    val email: String,

    @OneToMany(mappedBy = "owner")
    val myTrace: List<Trace>,

) : BaseEntity()

@Entity
class UserFollow(

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "followerId")
    val follower: User,

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "followingId")
    val followed: User,

) : BaseEntity()

@Entity
class LikedTrace(

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "userId")
    val user: User,

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "traceId")
    val trace: Trace,

) : BaseEntity()
