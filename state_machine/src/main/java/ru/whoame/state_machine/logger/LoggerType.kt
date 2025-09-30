package ru.whoame.state_machine.logger

/**
 * Defines the logging level for the state machine operations.
 *
 * This enum controls what type of information should be logged during
 * state machine execution, allowing for different levels of verbosity
 * based on debugging needs or production requirements.
 **/
enum class LoggerType {

    /**
     * Logs all state machine operations including state transitions,
     * events, and detailed execution information.
     **/
    FULL,

    /**
     * Logs only error conditions and exceptions that occur during
     * state machine execution.
     **/
    ERROR,

    /**
     * Disables all logging for the state machine operations.
     **/
    NONE

}
