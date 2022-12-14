package com.swpp.footprinter.domain.footprint.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import com.swpp.footprinter.domain.footprint.model.Footprint
import com.swpp.footprinter.domain.footprint.model.QFootprint.footprint
import com.swpp.footprinter.domain.photo.model.QPhoto.photo
import com.swpp.footprinter.domain.place.model.QPlace.place
import com.swpp.footprinter.domain.tag.model.QTag.tag
import com.swpp.footprinter.domain.trace.model.QTrace.trace
import org.springframework.data.jpa.repository.JpaRepository

interface FootprintRepository : JpaRepository<Footprint, Long>, FootprintRepositoryCustom

interface FootprintRepositoryCustom {
    fun findByIdOrNullImproved(id: Long): Footprint?
}

class FootprintRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : FootprintRepositoryCustom {
    override fun findByIdOrNullImproved(id: Long): Footprint? {
        return jpaQueryFactory
            .selectFrom(footprint)
            .join(footprint.tag, tag).fetchJoin()
            .join(footprint.place, place).fetchJoin()
            .join(footprint.trace, trace).fetchJoin()
            .join(footprint.photos, photo).fetchJoin()
            .where(footprint.id.eq(id))
            .fetchOne()
    }
}
