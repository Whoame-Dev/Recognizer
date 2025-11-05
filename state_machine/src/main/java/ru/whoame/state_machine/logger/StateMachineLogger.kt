package ru.whoame.state_machine.logger

import ru.whoame.state_machine.contract.Transaction
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Internal logger for state machine operations that provides configurable logging
 * for different aspects of state machine lifecycle and event handling.
 *
 * This logger supports different logging levels and can optionally invoke an error
 * listener for exception handling. It formats all log messages with a parent tag
 * for better traceability in complex applications.
 *
 * @param tag The logger tag used to identify this specific logger instance
 * @param parentTag The parent component tag that will be prefixed to all log messages
 * @param type The logging type that determines which messages are logged ([LoggerType])
 * @param errorListener Callback function that will be invoked whenever an error occurs
 **/
internal class StateMachineLogger(
    tag: String,
    private val parentTag: String,
    private val type: LoggerType,
    private val errorListener: (Throwable) -> Unit,
) {

    private val logger = Logger.getLogger(tag)

    /**
     * Logs that the state machine is starting up.
     * Only logged when [LoggerType.FULL] is set.
     **/
    fun starting() {
        if (type == LoggerType.FULL) {
            logger.info("$parentTag: StateMachine is starting up")
        }
    }

    /**
     * Logs that the state machine has completed startup and is ready to process events.
     * Only logged when [LoggerType.FULL] is set.
     **/
    fun started() {
        if (type == LoggerType.FULL) {
            logger.info("$parentTag: StateMachine is started")
        }
    }

    /**
     * Logs when a default domain event is being processed.
     * Only logged when [LoggerType.FULL] is set.
     *
     * @param event The default domain event that was received
     **/
    fun defaultDomainEvent(event: Any) {
        if (type == LoggerType.FULL) {
            logger.info("$parentTag: Default domain event: $event")
        }
    }

    /**
     * Logs when a new UI event is received by the state machine.
     * Only logged when [LoggerType.FULL] is set.
     *
     * @param event The UI event that was received
     **/
    fun newUiEvent(event: Any) {
        if (type == LoggerType.FULL) {
            logger.info("$parentTag: New UI event: $event")
        }
    }

    /**
     * Logs when a new domain event is received by the state machine.
     * Only logged when [LoggerType.FULL] is set.
     *
     * @param event The domain event that was received
     **/
    fun newDomainEvent(event: Any) {
        if (type == LoggerType.FULL) {
            logger.info("$parentTag: New domain event: $event")
        }
    }

    /**
     * Logs when a new transaction is created and processed by the state machine.
     * Only logged when [LoggerType.FULL] is set.
     *
     * @param transaction The transaction that was received
     **/
    fun newTransaction(transaction: Transaction<*, *, *, *>) {
        if (type == LoggerType.FULL) {
            logger.info("$parentTag: New transaction: $transaction")
        }
    }

    /**
     * Logs when a state update is applied to the current state.
     * Only logged when [LoggerType.FULL] is set.
     *
     * @param update The state update that was applied
     **/
    fun newStateUpdate(update: Any) {
        if (type == LoggerType.FULL) {
            logger.info("$parentTag: New state update: $update")
        }
    }

    /**
     * Logs when a side effect is received by the state machine.
     * Only logged when [LoggerType.FULL] is set.
     *
     * @param sideEffect The side effect that was executed
     **/
    fun newSideEffect(sideEffect: Any) {
        if (type == LoggerType.FULL) {
            logger.info("$parentTag: New side effect: $sideEffect")
        }
    }

    /**
     * Logs when the state machine generates a new state.
     * Only logged when [LoggerType.FULL] is set.
     *
     * @param state A new state that was generated in the machine state
     **/
    fun newState(state: Any) {
        if (type == LoggerType.FULL) {
            logger.info("$parentTag: New state: $state")
        }
    }

    /**
     * Logs when a new UI state is emitted for the presentation layer.
     * Only logged when [LoggerType.FULL] is set.
     *
     * @param state The UI state that was emitted
     **/
    fun newUiState(state: Any) {
        if (type == LoggerType.FULL) {
            logger.info("$parentTag: New UI state: $state")
        }
    }

    /**
     * Logs a warning when a domain event is received but no domain event handler is available.
     * Logs when [LoggerType.FULL] or [LoggerType.ERROR] is set.
     *
     * @param event The domain event that was ignored due to missing handler
     **/
    fun domainHandlerIsNull(event: Any) {
        if (type == LoggerType.FULL || type == LoggerType.ERROR) {
            logger.log(
                /* p0 = */ Level.WARNING,
                /* p1 = */ "$parentTag: DomainEventHandler is null, but DomainEvent is here.\n$event is ignored",
            )
        }
    }

    /**
     * Logs a warning when a UI event is received but no UI event handler is available.
     * Logs when [LoggerType.FULL] or [LoggerType.ERROR] is set.
     *
     * @param event The UI event that was ignored due to missing handler
     **/
    fun uiHandlerIsNull(event: Any) {
        if (type == LoggerType.FULL || type == LoggerType.ERROR) {
            logger.log(
                /* p0 = */ Level.WARNING,
                /* p1 = */ "$parentTag: UiEventHandler is null, but UiEvent is here.\n$event is ignored",
            )
        }
    }

    /**
     * Logs a error that occurred during state conversion.
     * Also invokes the error listener.
     * Logs when [LoggerType.FULL] or [LoggerType.ERROR] is set.
     *
     * @param tag Additional tag to identify the specific conversion that failed
     * @param throwable The exception that occurred during state conversion
     **/
    fun convertStateThrowable(tag: String, throwable: Throwable) {
        errorListener.invoke(throwable)
        if (type == LoggerType.FULL || type == LoggerType.ERROR) {
            logger.log(Level.SEVERE, "$parentTag.$tag: Error while state converting", throwable)
        }
    }

    /**
     * Logs a error that occurred during event handling.
     * Also invokes the error listener.
     * Logs when [LoggerType.FULL] or [LoggerType.ERROR] is set.
     *
     * @param tag Additional tag to identify the specific event handler that failed
     * @param throwable The exception that occurred during event handling
     **/
    fun handleEventThrowable(tag: String, throwable: Throwable) {
        errorListener.invoke(throwable)
        if (type == LoggerType.FULL || type == LoggerType.ERROR) {
            logger.log(Level.SEVERE, "$parentTag.$tag: Error while handling event", throwable)
        }
    }

    /**
     * Logs a error that occurred during transaction handling.
     * Also invokes the error listener.
     * Logs when [LoggerType.FULL] or [LoggerType.ERROR] is set.
     *
     * @param throwable The exception that occurred during transaction handling
     **/
    fun handleTransactionThrowable(throwable: Throwable) {
        errorListener.invoke(throwable)
        if (type == LoggerType.FULL || type == LoggerType.ERROR) {
            logger.log(Level.SEVERE, "$parentTag: Error while handling transaction", throwable)
        }
    }

    /**
     * Logs a error that occurred during analytics handling.
     * Also invokes the error listener.
     * Logs when [LoggerType.FULL] or [LoggerType.ERROR] is set.
     *
     * @param throwable The exception that occurred during analytics handling
     **/
    fun handleAnalyticsThrowable(throwable: Throwable) {
        errorListener.invoke(throwable)
        if (type == LoggerType.FULL || type == LoggerType.ERROR) {
            logger.log(Level.SEVERE, "$parentTag: Error while handling analytics", throwable)
        }
    }

}
