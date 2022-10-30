package com.swpp.footprinter.core.domain.trace.model

import com.swpp.footprinter.core.common.model.BaseEntity
import com.swpp.footprinter.core.domain.footprint.model.Footprint
import com.swpp.footprinter.core.domain.user.model.User
import javax.persistence.*

@Entity
class Trace(

    @Column(name = "trace_title", nullable = false)
    val traceTitle: String,

    @Column(name = "trace_date", nullable = false)
    val traceDate: String,

    @OneToMany(mappedBy = "trace")
    val footprintList: MutableList<Footprint>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val owner: User,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val likedUser: MutableList<User>,

) : BaseEntity()
