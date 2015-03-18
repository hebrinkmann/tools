package de.henningbrinkmann.timezones;

import java.util.Comparator;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

class TimeZoneKey implements Comparable<TimeZoneKey> {
  private TimeZone zone = null;

  @SuppressWarnings("unused")
  private TimeZoneKey() {
  }

  public TimeZoneKey(TimeZone zone) {
    this.zone = zone;
  }

  @Override
  public String toString() {
    return String.format("%d - %d - %b", zone.getRawOffset(), zone.getDSTSavings(), zone.useDaylightTime());
  }

  @Override
  public int compareTo(TimeZoneKey o) {
    if (o == null) {
      return -1;
    }

    if (zone.getRawOffset() < o.zone.getRawOffset()) {
      return -1;
    }

    if (zone.getRawOffset() > o.zone.getRawOffset()) {
      return 1;
    }

    if (zone.getDSTSavings() < o.zone.getDSTSavings()) {
      return -1;
    }

    if (zone.getDSTSavings() > o.zone.getDSTSavings()) {
      return 1;
    }

    return 0;
  }
}

@SuppressWarnings("serial")
class TimeZoneMap extends TreeMap<TimeZoneKey, SortedSet<TimeZone>> {
  private static final String LF = System.getProperty("line.separator");
  private static final Comparator<TimeZone> timezoneComp = new Comparator<TimeZone>() {

    @Override
    public int compare(TimeZone o1, TimeZone o2) {
      if (o1 == o2) {
        return 0;
      }

      if (o1 == null && o2 != null) {
        return -1;
      }

      if (o1 != null && o2 == null) {
        return 1;
      }

      int tmp = o1.getDisplayName().compareTo(o2.getDisplayName());

      if (tmp != 0) {
        return tmp;
      }

      return o1.getID().compareTo(o2.getID());
    }
  };

  public void put(TimeZone zone) {
    TimeZoneKey key = new TimeZoneKey(zone);

    SortedSet<TimeZone> zones = this.get(key);

    if (zones == null) {
      zones = new TreeSet<TimeZone>(timezoneComp);
      this.put(key, zones);
    }

    zones.add(zone);
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    int count = 0;

    for (TimeZoneKey key : keySet()) {
      SortedSet<TimeZone> zones = get(key);

      builder.append(LF);
      builder.append(key).append(LF);
      builder.append("-------------------").append(LF);

      for (TimeZone zone : zones) {
        builder.append(String.format("|%s|%s", zone.getID(), zone.getDisplayName())).append(LF);
        count++;
      }
    }

    builder.append(LF).append(keySet().size()).append(" groups").append(LF);
    builder.append(count).append(" time zones");

    return builder.toString();
  }
}

public class Timezones {

  public static void main(String[] args) {
    TimeZoneMap timeZoneMap = new TimeZoneMap();

    for (String id : TimeZone.getAvailableIDs()) {
      TimeZone timeZone = TimeZone.getTimeZone(id);

      timeZoneMap.put(timeZone);
    }

    System.out.print(timeZoneMap);
  }

  private static void printTimeZone(TimeZone timeZone) {
    StringBuilder builder = new StringBuilder();
    builder
        .append("|")
        .append("|")
        .append(timeZone.getRawOffset())
        .append("|")
        .append(timeZone.getID())
        .append("|")
        .append(timeZone.getDisplayName(false, TimeZone.SHORT))
        .append("|")
        .append(timeZone.getDisplayName(true, TimeZone.SHORT));
    System.out.println(builder.toString());
  }

}
