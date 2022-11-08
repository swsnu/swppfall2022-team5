package com.swpp.footprinter.domain.trace.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.trace.dto.TraceDetailResponse
import com.swpp.footprinter.domain.trace.dto.TraceResponse
import com.swpp.footprinter.domain.user.model.User
import javax.persistence.*

@Entity
class Trace(

    @Column(name = "trace_title", nullable = false)
    val traceTitle: String,

    @Column(name = "trace_date", nullable = false)
    val traceDate: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "userId")
    val owner: User,

    @OneToMany(mappedBy = "trace", cascade = [CascadeType.ALL])
    val footprints: MutableSet<Footprint>,

    ) : BaseEntity() {
    fun toResponse(): TraceResponse {
        return TraceResponse(
            id = id!!,
            date = traceDate,
            title = traceTitle,
            ownerId = owner.id
        )
    }

    fun toDetailResponse(): TraceDetailResponse {
        return TraceDetailResponse(
            id = id!!,
            date = traceDate,
            title = traceTitle,
            ownerId = owner.id,
            footprints = footprints.map { it.toResponse() }
        )
    }
}
