package com.github.underlow.kredux

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.promise

actual fun runTestBlocking(block: suspend () -> Unit): dynamic = GlobalScope.promise { block() }
