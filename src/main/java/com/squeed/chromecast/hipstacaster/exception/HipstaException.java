package com.squeed.chromecast.hipstacaster.exception;

/**
 * Created with IntelliJ IDEA.
 * User: Erik
 * Date: 2013-10-03
 * Time: 09:41
 * To change this template use File | Settings | File Templates.
 */
public class HipstaException extends Throwable {

    private String message;

    public HipstaException(String message) {
        this.message = message;
    }
}
