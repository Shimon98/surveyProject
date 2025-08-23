package org.example.util;

public class TimeUtils {

    public static final long SECOND_MS = 1_000L;
    public static final long MINUTE_MS = 60_000L;
    public static final long HOUR_MS   = 3_600_000L;

    private TimeUtils() {}
    public static long now() { return System.currentTimeMillis(); }
    public static long minutesToMs(long m) { return m * MINUTE_MS; }
}