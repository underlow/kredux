package com.github.underlow.kredux

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest


@ExperimentalCoroutinesApi
actual fun runTestBlocking(block: suspend () -> Unit) =
    runBlockingTest { block() }
