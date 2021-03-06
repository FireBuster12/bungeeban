package de.lucavinci.bungeeban.util;

/**
 * Represents a timeunit for temporary bans and mutes.
 * Each timeunit has multiple shortcuts by which you can find it.
 */
public enum TimeUnit {

    MILLISECOND(1L, new String[]{"milliseconds", "millisecond", "millis", "milli", "mil", "mils"}),
    SECOND(1000L, new String[]{"seconds", "second", "secs", "sec", "s"}),
    MINUTE(60L * 1000L, new String[]{"minutes", "minute", "mins", "min", "m"}),
    HOUR(60L * 60L * 1000L, new String[]{"hours", "hour", "h"}),
    DAY(24L * 60L * 60L * 1000L, new String[]{"days", "day", "d"}),
    WEEK(7L * 24L * 60L * 60L * 1000L, new String[]{"weeks", "week", "w"}),
    MONTH(30L * 24L * 60L * 60L * 1000L, new String[]{"months", "month", "mon"}),
    YEAR(365L * 24L * 60L * 60L * 1000L, new String[]{"years", "year", "y"});

    private long milliseconds;
    private String[] names;

    private TimeUnit(long milliseconds, String[] names) {
        this.milliseconds = milliseconds;
        this.names = names;
    }

    public long getMilliseconds() {
        return milliseconds;
    }

    public String[] getNames() {
        return names;
    }

    public void setMilliseconds(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public static TimeUnit find(String name) {
        for(TimeUnit tu : TimeUnit.values()) {
            for(String n : tu.getNames()) {
                if(n.equalsIgnoreCase(name)) {
                    return tu;
                }
            }
        }
        return null;
    }

}
