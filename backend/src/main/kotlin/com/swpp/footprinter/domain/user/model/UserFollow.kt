package com.swpp.footprinter.domain.user.model

import com.swpp.footprinter.common.model.BaseEntity
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["followerId", "followingId"])])
class UserFollow(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "followerId")
    val follower: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "followingId")
    val followed: User,
) : BaseEntity()
