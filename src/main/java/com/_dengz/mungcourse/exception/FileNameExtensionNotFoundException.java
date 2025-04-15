package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.FILE_NAME_EXTENSION_NOT_FONUD;

public class FileNameExtensionNotFoundException extends CustomBaseException {
  public FileNameExtensionNotFoundException() {
    super(FILE_NAME_EXTENSION_NOT_FONUD.getMessage());
  }

  @Override
  public int getStatusCode() {
    return FILE_NAME_EXTENSION_NOT_FONUD.getStatus();
  }

  @Override
  public String getErrorCode() {
    return FILE_NAME_EXTENSION_NOT_FONUD.name();
  }
}
