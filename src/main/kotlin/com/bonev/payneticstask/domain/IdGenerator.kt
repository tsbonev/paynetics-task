package com.bonev.payneticstask.domain

import java.util.*

interface IdGenerator {
    fun generateUUID(): UUID
}