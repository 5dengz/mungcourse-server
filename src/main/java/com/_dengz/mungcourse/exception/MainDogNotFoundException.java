package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.MAIN_DOG_NOT_FOUND;

public class MainDogNotFoundException extends CustomBaseException {
  public MainDogNotFoundException() {
    super(MAIN_DOG_NOT_FOUND.getMessage());
  }

  @Override
  public int getStatusCode() {
    return MAIN_DOG_NOT_FOUND.getStatus();
  }

  @Override
  public String getErrorCode() {
    return MAIN_DOG_NOT_FOUND.name();
  }
}
