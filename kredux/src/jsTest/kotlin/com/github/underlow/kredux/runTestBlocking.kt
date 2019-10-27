package com.github.underlow.kredux

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

actual fun runTestBlocking(block: suspend () -> Unit): Unit {
    GlobalScope.launch { block() }
}