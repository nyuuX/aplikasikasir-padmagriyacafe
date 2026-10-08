package com.padmagriya.util;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatUtil {
    private static final Locale INDONESIA = Locale.forLanguageTag("id-ID");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", INDONESIA);

    public static String rupiah(BigDecimal value) {
        if (value == null) {
            value = BigDecimal.ZERO;
        }
        NumberFormat format = NumberFormat.getCurrencyInstance(INDONESIA);
        format.setMaximumFractionDigits(0);
        return format.format(value);
    }

    public static String tanggal(LocalDateTime value) {
        return value == null ? "-" : value.format(DATE_TIME);
    }
}
