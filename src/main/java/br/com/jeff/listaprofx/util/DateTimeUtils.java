package br.com.jeff.listaprofx.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class DateTimeUtils {

    // Definição do formato padrão
    private static final DateTimeFormatter FORMATTER = 
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    
    private static final DateTimeFormatter FILE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * Converte LocalDateTime para String formatada.
     */
    public static String format(LocalDateTime dateTime) {
        return Optional.ofNullable(dateTime)
                .map(FORMATTER::format)
                .orElse("");
    }

    /**
     * Converte String formatada para LocalDateTime.
     */
    public static LocalDateTime parse(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, FORMATTER);
    }

    /**
     * Retorna o formatter (caso precise em outros lugares).
     */
    public static DateTimeFormatter getFormatter() {
        return FORMATTER;
    }
    
    
    public static String formatForFileName(LocalDateTime dateTime) {
        return Optional.ofNullable(dateTime)
                .map(FILE_FORMATTER::format)
                .orElse("sem_data");
    }
}
