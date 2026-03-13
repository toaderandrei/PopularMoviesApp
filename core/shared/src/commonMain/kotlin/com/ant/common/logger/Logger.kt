package com.ant.common.logger

/** Platform-agnostic logging abstraction used throughout the application. */
interface Logger {

    /** Log a verbose message with optional format args.  */
    fun v(message: String)

    /** Log a debug message.  */
    fun d(message: String)

    /** Log an info message.  */
    fun i(message: String)

    /** Log an error with a message. */
    fun e(t: Throwable, message: String)

    /** Log an error. */
    fun e(t: Throwable)
}