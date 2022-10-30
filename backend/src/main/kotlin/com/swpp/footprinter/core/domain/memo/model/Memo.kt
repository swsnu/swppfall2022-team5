package com.swpp.footprinter.core.domain.memo.model

import com.swpp.footprinter.core.common.model.BaseEntity
import com.swpp.footprinter.core.domain.footprint.model.Footprint
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
class Memo(

    @Column(name = "content")
    val content: String,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "footprint")
    val footprint: Footprint,

) : BaseEntity()
