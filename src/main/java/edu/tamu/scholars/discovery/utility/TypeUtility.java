package edu.tamu.scholars.discovery.utility;

import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Slf4j
public class TypeUtility {

    private static final String ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE = "Default value cannot be null";
    private static final String UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE = "Unsupported default value type";
    private static final String EXCEPTION_MESSAGE_TEMPLATE = "Failed to convert value: {} to type: {}";

    private TypeUtility() {

    }

    @SuppressWarnings("unchecked")
    public static <T> T as(@Nullable String value, @NonNull T defaultValue) {
        if (Objects.isNull(defaultValue)) {
            throw new IllegalArgumentException(ILLEGAL_ARGUMENT_EXCEPTION_MESSAGE);
        }

        T result = defaultValue;

        if (Objects.nonNull(value)) {

            try {
                if (defaultValue instanceof String) {
                    result = (T) value;
                } else if (defaultValue instanceof Integer) {
                    result = (T) Integer.valueOf(value);
                } else if (defaultValue instanceof Long) {
                    result = (T) Long.valueOf(value);
                } else if (defaultValue instanceof Float) {
                    result = (T) Float.valueOf(value);
                } else if (defaultValue instanceof Double) {
                    result = (T) Double.valueOf(value);
                } else if (defaultValue instanceof Boolean) {
                    result = (T) Boolean.valueOf(value);
                } else {
                    throw new UnsupportedOperationException(UNSUPPORTED_OPERATION_EXCEPTION_MESSAGE);
                }
            } catch (ClassCastException | NumberFormatException e) {
                Class<T> type = (Class<T>) defaultValue.getClass();
                log.error(EXCEPTION_MESSAGE_TEMPLATE, value, type, e);
            }
        }

        return result;
    }

}
