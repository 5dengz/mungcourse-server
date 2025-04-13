package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.GPS_DESERIALIZATION_FAILED;

public class GpsDeserializationFailedException extends CustomBaseException {
    public GpsDeserializationFailedException() {
        super(GPS_DESERIALIZATION_FAILED.getMessage());
    }

    @Override
    public int getStatusCode() {
        return GPS_DESERIALIZATION_FAILED.getStatus();
    }

    @Override
    public String getErrorCode() {
        return GPS_DESERIALIZATION_FAILED.name();
    }
}