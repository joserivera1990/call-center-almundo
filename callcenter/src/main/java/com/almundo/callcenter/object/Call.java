package com.almundo.callcenter.object;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Call {

    private Integer durationInSeconds;

    private String id;

    private static final Logger logger = LoggerFactory.getLogger(Call.class);

    /**
     * Creates a new Call with duration measured in seconds
     *
     * @param durationInSeconds duration in seconds must be equal or greater than zero
     */
    public Call(final Integer durationInSeconds) {
        Validate.notNull(durationInSeconds);
        Validate.isTrue(durationInSeconds >= 0);
        this.id = UUID.randomUUID().toString();
        this.durationInSeconds = durationInSeconds;
    }

    public void start() throws InterruptedException {

        logger.info("Started -> " + this);
        TimeUnit.SECONDS.sleep(this.durationInSeconds);
        logger.info("Finished -> " + this);
    }

    @Override
    public String toString() {

        return "Call [id=" + id + ", durationInSeconds=" + durationInSeconds + "]";
    }
}

