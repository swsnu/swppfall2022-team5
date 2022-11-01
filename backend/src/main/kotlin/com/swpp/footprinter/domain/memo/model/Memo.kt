package com.swpp.footprinter.domain.memo.model

import com.swpp.footprinter.common.model.BaseEntity
import com.swpp.footprinter.domain.footprint.model.Footprint
import javax.persistence.*

@Entity
class Memo(

    @Column(name = "content")
    val content: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id", name = "footprintId")
    val footprint: Footprint,

    ) : BaseEntity()
