package ru.whoame.state_machine.logger

/**
 * State machine logger settings.
 *
 * @param tag Name of the class where the state machine is running
 * @param type Logging level for the state machine (default is [LoggerType.NONE])
 **/
data class LoggerSettings(
    val tag: String,
    val type: LoggerType = LoggerType.NONE,
)
