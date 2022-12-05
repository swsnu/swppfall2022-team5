package com.swpp.footprinter.domain.trace.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.common.utils.ImageUrlUtil
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

    @Column(name = "public", nullable = false)
    var public: Boolean = true,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "userId")
    val owner: User,

    @OneToMany(mappedBy = "trace", cascade = [CascadeType.ALL])
    val footprints: MutableSet<Footprint>,

    @Column(name = "likes_count", columnDefinition = "integer default 0")
    var likesCount: Int = 0,

    @Column(name = "view_count", columnDefinition = "integer default 0")
    var viewCount: Int = 0,

) : BaseEntity() {
    fun toResponse(): TraceResponse {
        return TraceResponse(
            id = id!!,
            date = traceDate,
            title = traceTitle,
            ownerId = owner.id
        )
    }

    fun toDetailResponse(imageUrlUtil: ImageUrlUtil): TraceDetailResponse {
        return TraceDetailResponse(
            id = id!!,
            date = traceDate,
            title = traceTitle,
            ownerName = owner.username,
            footprints = footprints.map { it.toResponse(imageUrlUtil) },
            viewCount = viewCount
        )
    }
}
