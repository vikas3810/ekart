package com.ekart.exception;

import java.io.Serial;

public class FileFormatNotSupportedException extends Exception {


    @Serial
    private static final long serialVersionUID = 1L;

    public FileFormatNotSupportedException() {

    }

    public FileFormatNotSupportedException(String msg) {
        super(msg);
    }


}
