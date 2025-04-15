package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.DOG_IMAGE_ACCESS_FORBIDDEN;

public class DogImageAccessForbiddenException extends CustomBaseException {
  public DogImageAccessForbiddenException() {
    super(DOG_IMAGE_ACCESS_FORBIDDEN.getMessage());
  }

  @Override
  public int getStatusCode() {
    return DOG_IMAGE_ACCESS_FORBIDDEN.getStatus();
  }

  @Override
  public String getErrorCode() {
    return DOG_IMAGE_ACCESS_FORBIDDEN.name();
  }
}