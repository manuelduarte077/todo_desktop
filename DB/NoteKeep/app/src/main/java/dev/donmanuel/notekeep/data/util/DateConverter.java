package dev.donmanuel.notekeep.data.util;

import androidx.room.TypeConverter;

import java.util.Date;

/**
 * Type converter for Room to convert between Date and Long
 */
public class DateConverter {

    /**
     * Convert from timestamp to Date
     *
     * @param value The timestamp
     * @return The Date object
     */
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Convert from Date to timestamp
     *
     * @param date The Date object
     * @return The timestamp
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
