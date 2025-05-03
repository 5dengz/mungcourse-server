package com._dengz.mungcourse.exception;

import com._dengz.mungcourse.exception.common.CustomBaseException;

import static com._dengz.mungcourse.exception.GlobalErrorCode.AI_MODEL_TRAIN_REQUEST_SERIALIZATION_FAILED;

public class AiModelTrainRequestSerializationFailedExcepiton extends CustomBaseException {
    public AiModelTrainRequestSerializationFailedExcepiton() {
        super(AI_MODEL_TRAIN_REQUEST_SERIALIZATION_FAILED.getMessage());
    }

    @Override
    public int getStatusCode() {
        return AI_MODEL_TRAIN_REQUEST_SERIALIZATION_FAILED.getStatus();
    }

    @Override
    public String getErrorCode() {
        return AI_MODEL_TRAIN_REQUEST_SERIALIZATION_FAILED.name();
    }
}
