package com.indra.cajko.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public interface LoggingUtils {
    default Logger logger() {
        return LogManager.getLogger(this.getClass());
    }

}