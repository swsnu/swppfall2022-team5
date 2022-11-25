package com.swpp.footprinter.domain.trace.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.user.model.User
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["traceId", "userId"])])
class TraceLike(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "traceId")
    val trace: Trace,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "userId")
    val user: User
) : BaseEntity()
