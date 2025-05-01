package com._dengz.mungcourse.entity;

public enum RepeatDay {
    MON, TUE, WED, THU, FRI, SAT, SUN;

    public static RepeatDay fromJavaDayOfWeek(java.time.DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case MONDAY:
                return MON;
            case TUESDAY:
                return TUE;
            case WEDNESDAY:
                return WED;
            case THURSDAY:
                return THU;
            case FRIDAY:
                return FRI;
            case SATURDAY:
                return SAT;
            case SUNDAY:
                return SUN;
            default:
                throw new IllegalArgumentException("Invalid java.time.DayOfWeek: " + dayOfWeek);
        }
    }
}