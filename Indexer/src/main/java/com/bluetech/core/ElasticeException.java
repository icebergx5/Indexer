/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bluetech.core;

/**
 *
 * @author yasin
 */
public class ElasticeException extends RuntimeException {

    public ElasticeException() {
    }

    public ElasticeException(String message) {
        super(message);
    }

    public ElasticeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElasticeException(Throwable cause) {
        super(cause);
    }

    public ElasticeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
