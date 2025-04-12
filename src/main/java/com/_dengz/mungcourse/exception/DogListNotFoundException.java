package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.DOG_LIST_NOT_FOUND;

public class DogListNotFoundException extends CustomBaseException {
  public DogListNotFoundException() {
    super(DOG_LIST_NOT_FOUND.getMessage());
  }

  @Override
  public int getStatusCode() {
    return DOG_LIST_NOT_FOUND.getStatus();
  }

  @Override
  public String getErrorCode() {
    return DOG_LIST_NOT_FOUND.name();
  }
}