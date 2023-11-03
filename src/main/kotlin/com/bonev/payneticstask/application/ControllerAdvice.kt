package com.bonev.payneticstask.application

import com.bonev.payneticstask.domain.ProjectNotFoundException
import com.bonev.payneticstask.domain.ProjectStatusChangeException
import com.bonev.payneticstask.domain.TaskNotFoundException
import com.bonev.payneticstask.domain.TaskStatusChangeException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.lang.IllegalArgumentException

@ControllerAdvice
class ControllerAdvice {
    @ExceptionHandler(IllegalArgumentException::class,
            ProjectNotFoundException::class,
            ProjectStatusChangeException::class,
            TaskNotFoundException::class,
            TaskStatusChangeException::class)
    fun handleExceptions(ex: Exception): ResponseEntity<ResponseDTO> {
        return ResponseEntity(ResponseDTO(-1, null, listOf(ex.message ?: "")), HttpStatus.OK)
    }

}