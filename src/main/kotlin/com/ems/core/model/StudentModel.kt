package com.ems.core.model

import com.ems.core.entity.Student
import org.springframework.data.domain.PageImpl
import org.springframework.web.reactive.function.server.ServerRequest

class GetStudentsResponse(
    override val number: Int,
    override val size: Int,
    override val numberOfElements: Int,
    override val totalPages: Int,
    override val totalElements: Long,
    override val first: Boolean,
    override val last: Boolean,
    override val empty: Boolean,
    override val content: MutableList<Student>
) : PageableModel<Student>(number, size, numberOfElements, totalPages, totalElements, first, last, empty, content) {
    companion object {
        fun of(request: ServerRequest, content: List<Student>, totalSize: Long): GetStudentsResponse {
            val pageable = PageImpl(content, toPageRequest(request), totalSize)
            return GetStudentsResponse(
                number = pageable.number,
                size = pageable.size,
                numberOfElements = pageable.numberOfElements,
                totalPages = pageable.totalPages,
                totalElements = pageable.totalElements,
                first = pageable.isFirst,
                last = pageable.isLast,
                empty = pageable.isEmpty,
                content = pageable.content
            )
        }
    }
}