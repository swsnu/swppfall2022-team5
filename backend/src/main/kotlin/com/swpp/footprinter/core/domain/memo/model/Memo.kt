package com.swpp.footprinter.core.domain.memo.model

import com.swpp.footprinter.core.common.model.BaseEntity
import javax.persistence.Column

class Memo(

    @Column(name = "content")
    val content: String,

) : BaseEntity()
