package com.violetbeach.daengbu.exception;

import java.text.MessageFormat;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.violetbeach.daengbu.config.PropertiesConfiguration;

@SuppressWarnings("serial")
@Component
public class CustomException {

    private static PropertiesConfiguration propertiesConfig;

    @Autowired
    public CustomException(PropertiesConfiguration propertiesConfig) {
        CustomException.propertiesConfig = propertiesConfig;
    }

    public static RuntimeException throwException(String messageTemplate, String... args) {
        return new RuntimeException(format(messageTemplate, args));
    }

    public static RuntimeException throwException(DtoType dtoType, ExceptionType exceptionType, String... args) {
        String messageTemplate = getMessageTemplate(dtoType, exceptionType);
        return throwException(exceptionType, messageTemplate, args);
    }

    private static RuntimeException throwException(ExceptionType exceptionType, String messageTemplate, String... args) {
        if (ExceptionType.DTO_NOT_FOUND.equals(exceptionType)) {
            return new DtoNotFoundException(format(messageTemplate, args));
        } else if (ExceptionType.DUPLICATE_DTO.equals(exceptionType)) {
            return new DuplicateDtoException(format(messageTemplate, args));
        }
        return new RuntimeException(format(messageTemplate, args));
    }

    private static String getMessageTemplate(DtoType dtoType, ExceptionType exceptionType) {
        return dtoType.name().concat(".").concat(exceptionType.getValue()).toLowerCase();
    }

    private static String format(String template, String... args) {
        Optional<String> templateContent = Optional.ofNullable(propertiesConfig.getConfigValue(template));
        if (templateContent.isPresent()) {
            return MessageFormat.format(templateContent.get(), (Object[]) args);
        }
        return String.format(template, (Object[]) args);
    }

    public static class DtoNotFoundException extends RuntimeException {
        public DtoNotFoundException(String message) {
            super(message);
        }
    }

    public static class DuplicateDtoException extends RuntimeException {
        public DuplicateDtoException(String message) {
            super(message);
        }
    }

}
