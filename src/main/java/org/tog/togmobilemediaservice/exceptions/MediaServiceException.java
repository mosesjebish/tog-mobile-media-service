package org.tog.togmobilemediaservice.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediaServiceException extends Exception{
    private Integer errCode;

    public MediaServiceException(Integer errCode, String message) {
        super(message);
        this.errCode = errCode;
    }
}
