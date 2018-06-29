package com.study.common.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class LocalDateUtil {

	// 获得时区
	private static ZoneId zone = ZoneId.systemDefault();

	private LocalDateUtil() {

	}

	public static LocalDateTime parse(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), zone);
	}

	public static Date parse(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(zone).toInstant());
	}

	public static String dateToString(Date date, String formatPattern) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), zone);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatPattern);
		return localDateTime.format(dateTimeFormatter);
	}

	public static LocalDateTime dateToLocalDateTime(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), zone);
	}

	public static Date plus(Date date, long amountToAdd, ChronoUnit chronoUnit) {
		return parse(dateToLocalDateTime(date).plus(amountToAdd, chronoUnit));
	}

	public static Duration between(LocalDateTime startInclusive, LocalDateTime endExclusive) {
		return Duration.between(startInclusive, endExclusive);
	}

	public static Duration getSurplusToEndTime(int hourOfDay, int minute, int second) {
		return between(LocalDateTime.now(zone), getEndTime(hourOfDay, minute, second));
	}

	public static boolean isInRangeTime(LocalDateTime localDateTime, String startTime, String endTime) {
		LocalTime localTimeStart = getLocalTime(startTime);
		LocalTime localTimeEnd = getLocalTime(endTime);
		LocalTime localTimeNow = localDateTime.toLocalTime();
		if (localTimeStart.isBefore(localTimeEnd)) {
			return localTimeNow.isAfter(localTimeStart) && localTimeNow.isBefore(localTimeEnd);
		} else {
			return localTimeNow.isAfter(localTimeStart) || localTimeNow.isBefore(localTimeEnd);
		}
	}
	
	public static Duration getDurationEndInRangeTime(LocalDateTime localDateTime, String startTime, String endTime) {
		LocalTime localTimeStart = getLocalTime(startTime);
		LocalTime localTimeEnd = getLocalTime(endTime);
		LocalTime localTimeNow = localDateTime.toLocalTime();
		if (localTimeStart.isBefore(localTimeEnd)) {
			if(localTimeNow.isAfter(localTimeStart) && localTimeNow.isBefore(localTimeEnd)) {
				return Duration.between(localDateTime, LocalDateTime.of(localDateTime.toLocalDate(), localTimeEnd));
			}else {
				return null;
			}
		} else {
			if(localTimeNow.isBefore(localTimeEnd)) {
				return Duration.between(localDateTime, LocalDateTime.of(localDateTime.toLocalDate(), localTimeEnd)); 
			}
			
			if(localTimeNow.isAfter(localTimeStart)) {
				return Duration.between(localDateTime, LocalDateTime.of(localDateTime.toLocalDate().plusDays(1), localTimeEnd));
			}
			
			return null;
		}
	}
	

	/**
	 * 格式 HH:mm:ss
	 * 
	 * @param time
	 * @return
	 */
	private static LocalTime getLocalTime(String time) {
		String[] times = time.split(":");
		return LocalTime.of(Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]));
	}

	private static LocalDateTime getEndTime(int hourOfDay, int minute, int second) {
		LocalDateTime localDateTime = LocalDateTime.now(zone);
		int day = hourOfDay / 24;
		localDateTime = localDateTime.plusDays(day);
		return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.of(hourOfDay % 24, minute, second));

	}


}
