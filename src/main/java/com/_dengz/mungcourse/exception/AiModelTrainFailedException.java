package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.AI_MODEL_TRAIN_FAILED;

public class AiModelTrainFailedException extends CustomBaseException {
    public AiModelTrainFailedException() {
      super(AI_MODEL_TRAIN_FAILED.getMessage());
    }

    @Override
    public int getStatusCode() {
      return AI_MODEL_TRAIN_FAILED.getStatus();
    }

    @Override
    public String getErrorCode() {
      return AI_MODEL_TRAIN_FAILED.name();
    }
}
