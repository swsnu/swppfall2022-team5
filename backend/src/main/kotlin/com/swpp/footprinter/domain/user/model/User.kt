package com.swpp.footprinter.domain.user.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.trace.model.Trace
import com.swpp.footprinter.domain.user.dto.UserResponse
import javax.persistence.*

@Entity
class User(
    @Column(name = "username", unique = true)
    val username: String,

    @Column(name = "password")
    val password: String,

    @Column(columnDefinition = "integer default 0")
    var followingCount: Int = 0,

    @Column(columnDefinition = "integer default 0")
    var followerCount: Int = 0,

    @OneToMany(mappedBy = "owner", cascade = [CascadeType.ALL])
    val myTrace: MutableSet<Trace> = mutableSetOf(),

) : BaseEntity() {
    fun toResponse() = UserResponse(username, followingCount, followerCount, traceCount = myTrace.size)
}

@Entity
class LikedTrace(

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "userId")
    val user: User,

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "traceId")
    val trace: Trace,

) : BaseEntity()
