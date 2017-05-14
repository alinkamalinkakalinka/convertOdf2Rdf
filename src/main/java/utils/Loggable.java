package utils;

/**
 * Created by aarunova on 3/14/17.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Loggable {

    default Logger logger() {
        return LoggerFactory.getLogger(this.getClass());
    }

}