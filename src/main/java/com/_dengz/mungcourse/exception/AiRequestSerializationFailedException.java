package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.AI_REQUEST_SERIALIZATION_FAILED;

public class AiRequestSerializationFailedException extends CustomBaseException {
  public AiRequestSerializationFailedException() {
    super(AI_REQUEST_SERIALIZATION_FAILED.getMessage());
  }

  @Override
  public int getStatusCode() {
    return AI_REQUEST_SERIALIZATION_FAILED.getStatus();
  }

  @Override
  public String getErrorCode() {
    return AI_REQUEST_SERIALIZATION_FAILED.name();
  }
}
