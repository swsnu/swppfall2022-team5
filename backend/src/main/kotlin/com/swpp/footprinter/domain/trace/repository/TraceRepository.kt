package com.swpp.footprinter.domain.trace.repository

import com.querydsl.core.BooleanBuilder
import com.querydsl.jpa.impl.JPAQueryFactory
import com.swpp.footprinter.domain.footprint.model.QFootprint.footprint
import com.swpp.footprinter.domain.photo.model.QPhoto.photo
import com.swpp.footprinter.domain.place.dto.PlaceRequest
import com.swpp.footprinter.domain.place.model.QPlace.place
import com.swpp.footprinter.domain.tag.TAG_CODE
import com.swpp.footprinter.domain.tag.model.QTag.tag
import com.swpp.footprinter.domain.trace.model.QTrace.trace
import com.swpp.footprinter.domain.trace.model.Trace
import com.swpp.footprinter.domain.user.model.QUser.user
import com.swpp.footprinter.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TraceRepository : JpaRepository<Trace, Long>, TraceRepositoryCustom {
//    fun findByOwnerAndTraceDate(owner: User, traceDate: String): Trace?
    fun findTracesByTraceDate(traceDate: String): MutableList<Trace>
    fun findTraceAllByOwner(user: User): MutableList<Trace>
    fun existsByTraceTitle(title: String): Boolean
    fun findByTraceTitle(title: String): Trace?
}

interface TraceRepositoryCustom {
    fun findByIdOrNullEfficiently(id: Long): Trace?
    fun findByOwnerAndTraceDate(owner: User, traceDate: String): Trace?
    fun getTracesWithOptions(
        usernameList: List<String>,
        tagList: List<TAG_CODE>,
        dateList: List<String>,
        placeList: List<PlaceRequest>,
    ): List<Trace>

    fun getTracesOfUser(
        username: String,
        isInclude: Boolean,
        isConsiderPublic: Boolean,
    ): List<Trace>
}

class TraceRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
) : TraceRepositoryCustom {
    override fun findByIdOrNullEfficiently(id: Long): Trace? {
        val trace = jpaQueryFactory.selectFrom(trace)
            .join(trace.owner, user).fetchJoin()
            .join(trace.footprints, footprint).fetchJoin()
            .join(footprint.place, place).fetchJoin()
            .join(footprint.tag, tag).fetchJoin()
            .where(trace.id.eq(id))
            .fetchOne()

        if (trace != null) {
            jpaQueryFactory.selectFrom(footprint)
                .join(footprint.photos, photo).fetchJoin()
                .where(footprint.trace.eq(trace))
                .fetch()
        }

        return trace
    }

    override fun findByOwnerAndTraceDate(owner: User, traceDate: String): Trace? {
        val trace = jpaQueryFactory.selectFrom(trace)
            .join(trace.owner, user).fetchJoin()
            .join(trace.footprints, footprint).fetchJoin()
            .join(footprint.place, place).fetchJoin()
            .join(footprint.tag, tag).fetchJoin()
            .where(
                user.eq(owner).and(
                    trace.traceDate.eq(traceDate)
                )
            )
            .fetchOne()

        if (trace != null) {
            jpaQueryFactory.selectFrom(footprint)
                .join(footprint.photos, photo).fetchJoin()
                .where(footprint.trace.eq(trace))
                .fetch()
        }

        return trace
    }

    override fun getTracesWithOptions(
        usernameList: List<String>,
        tagList: List<TAG_CODE>,
        dateList: List<String>,
        placeList: List<PlaceRequest>
    ): List<Trace> {
        val booleanBuilder = BooleanBuilder()
        booleanBuilder.and(trace.isPublic.eq(true))
        if (usernameList.isNotEmpty()) {
            booleanBuilder.and(
                user.username.`in`(usernameList)
            )
        }
        if (tagList.isNotEmpty()) {
            booleanBuilder.and(
                tag.tagCode.`in`(tagList)
            )
        }
        if (dateList.isNotEmpty()) {
            booleanBuilder.and(
                trace.traceDate.`in`(dateList)
            )
        }
        if (placeList.isNotEmpty()) {
            booleanBuilder.and(
                place.name.`in`(placeList.map { it.name })
                    .and(place.address.`in`(placeList.map { it.address }))
            )
        }

        val traces = jpaQueryFactory
            .selectFrom(trace)
            .join(trace.owner, user).fetchJoin()
            .join(trace.footprints, footprint).fetchJoin()
            .join(footprint.place, place).fetchJoin()
            .join(footprint.tag, tag).fetchJoin()
            .where(booleanBuilder)
            .orderBy(trace.traceDate.desc())
            .fetch()

        jpaQueryFactory.selectFrom(footprint)
            .join(footprint.photos, photo).fetchJoin()
            .where(footprint.trace.`in`(traces))
            .fetch()

        return traces.distinctBy { it.id }
    }

    override fun getTracesOfUser(
        username: String,
        isInclude: Boolean,
        isConsiderPublic: Boolean
    ): List<Trace> {
        val booleanBuilder = BooleanBuilder()
        if (isConsiderPublic) { booleanBuilder.and(trace.isPublic.isTrue()) }
        if (isInclude) {
            booleanBuilder.and(user.username.eq(username))
        } else {
            booleanBuilder.and(user.username.ne(username))
        }

        val traces = jpaQueryFactory
            .selectFrom(trace)
            .join(trace.owner, user).fetchJoin()
            .join(trace.footprints, footprint).fetchJoin()
            .join(footprint.place, place).fetchJoin()
            .join(footprint.tag, tag).fetchJoin()
            .where(booleanBuilder)
            .orderBy(trace.traceDate.desc())
            .fetch()

        jpaQueryFactory.selectFrom(footprint)
            .join(footprint.photos, photo).fetchJoin()
            .where(footprint.trace.`in`(traces))
            .fetch()

        return traces.distinctBy { it.id }
    }
}
