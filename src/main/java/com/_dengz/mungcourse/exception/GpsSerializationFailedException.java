package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.GPS_SERIALIZATION_FAILED;

public class GpsSerializationFailedException extends CustomBaseException {
  public GpsSerializationFailedException() {
    super(GPS_SERIALIZATION_FAILED.getMessage());
  }

  @Override
  public int getStatusCode() {
    return GPS_SERIALIZATION_FAILED.getStatus();
  }

  @Override
  public String getErrorCode() {
    return GPS_SERIALIZATION_FAILED.name();
  }
}