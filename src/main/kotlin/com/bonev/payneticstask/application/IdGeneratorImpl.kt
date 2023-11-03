package com.bonev.payneticstask.application

import com.bonev.payneticstask.domain.IdGenerator
import org.springframework.stereotype.Service
import java.util.*

@Service
class IdGeneratorImpl: IdGenerator {
    override fun generateUUID(): UUID {
        return UUID.randomUUID()
    }
}