package com.ams.core.handler

import com.ams.core.model.ClassesModel
import com.ams.core.model.PageableModel
import com.ams.core.repository.ClassSchedulesRepository
import com.ams.core.repository.ClassesRepository
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters.fromValue
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.ServerResponse.ok
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono

@Component
class ClassesHandler(
    val classSchedulesHandler: ClassSchedulesHandler,
    val classesRepository: ClassesRepository,
    val classSchedulesRepository: ClassSchedulesRepository
) {

    fun getOne(request: ServerRequest): Mono<ServerResponse> =
        classesRepository
            .findById(request.pathVariable("id").toLong())
            .flatMap { ok().body(fromValue(ClassesModel.of(it))) }

    fun getAll(request: ServerRequest): Mono<ServerResponse> =
        classesRepository
            .findAllBy(PageableModel.toPageRequest(request))
            .map { ClassesModel.of(it) }
            .collectList()
            .zipWith(classesRepository.count())
            .flatMap { ok().body(fromValue(ClassesModel.of(request, it.t1, it.t2))) }

    fun save(request: ServerRequest): Mono<ServerResponse> =
        classesRepository
            .saveAll(request.bodyToMono(ClassesModel::class.java).map { it.toEntity() })
            .flatMap { classSchedulesHandler.save(it) }
            .flatMap { ok().body(fromValue(it)) }
            .single()

    fun update(request: ServerRequest): Mono<ServerResponse> =
        request.bodyToMono(ClassesModel::class.java)
            .flatMap { classesRepository.findById(it.id).flatMap { old -> old.update(it) } }
            .flatMap { classesRepository.save(it) }
            .flatMap { ok().body(fromValue(it)) }
            .single()

    fun delete(request: ServerRequest): Mono<ServerResponse> =
        classesRepository
            .deleteById(request.queryParam("id").orElseThrow().toLong())
            .flatMap { ok().build() }

}