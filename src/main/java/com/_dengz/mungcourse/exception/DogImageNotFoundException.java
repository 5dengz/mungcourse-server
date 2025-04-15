package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.DOG_IMAGE_NOT_FOUND;

public class DogImageNotFoundException extends CustomBaseException {
  public DogImageNotFoundException() {
    super(DOG_IMAGE_NOT_FOUND.getMessage());
  }

  @Override
  public int getStatusCode() {
    return DOG_IMAGE_NOT_FOUND.getStatus();
  }

  @Override
  public String getErrorCode() {
    return DOG_IMAGE_NOT_FOUND.name();
  }
}
