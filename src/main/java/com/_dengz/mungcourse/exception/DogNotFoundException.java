package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.DOG_NOT_FOUND;

public class DogNotFoundException extends CustomBaseException {
  public DogNotFoundException() {
    super(DOG_NOT_FOUND.getMessage());
  }

  @Override
  public int getStatusCode() {
    return DOG_NOT_FOUND.getStatus();
  }

  @Override
  public String getErrorCode() {
    return DOG_NOT_FOUND.name();
  }
}
