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
	
	/**
	 * 获得当前时间
	 * @return
	 */
	public static LocalDateTime now() {
		return LocalDateTime.now(zone);
	}


	/**
	 * date 转 localDateTime对象
	 * @param date
	 * @return
	 */
	public static LocalDateTime parse(Date date) {
		return LocalDateTime.ofInstant(date.toInstant(), zone);
	}
	
	/**
	 * localDateTime 转 date对象
	 * @param localDateTime
	 * @return
	 */
	public static Date parse(LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(zone).toInstant());
	}

	/**
	 * 时间转字符串
	 * @param date 时间
	 * @param formatPattern 格式化方式 
	 * @return
	 */
	public static String dateToString(Date date, String formatPattern) {
		LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(), zone);
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(formatPattern);
		return localDateTime.format(dateTimeFormatter);
	}

	

	/**
	 * 在当前时间 增加
	 * @param date 当前时间
	 * @param amountToAdd 增加的值 
	 * @param chronoUnit 增加的单位
	 * @return
	 */
	public static Date plus(Date date, long amountToAdd, ChronoUnit chronoUnit) {
		return parse(plusToLocal(date, amountToAdd, chronoUnit));
	}

	public static LocalDateTime plusToLocal(Date date, long amountToAdd, ChronoUnit chronoUnit) {
		return parse(date).plus(amountToAdd, chronoUnit);
	}

	/**
	 * 获得 相差的Duration对象
	 * @param startInclusive 开始时间
	 * @param endExclusive 结束时间
	 * @return
	 */
	public static Duration between(LocalDateTime startInclusive, LocalDateTime endExclusive) {
		return Duration.between(startInclusive, endExclusive);
	}
	
	/**
	 * 获得 相差的Duration对象
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public static Duration between(Date startDate, Date endDate) {
		return between(parse(startDate), parse(endDate));
	}

	/**
	 * 获得 相差的Duration对象
	 * @param hourOfDay 小时(如果大于24 那么就是跨天)
	 * @param minute 分钟
	 * @param second 秒
	 * @return
	 */
	public static Duration getSurplusToEndTime(int hourOfDay, int minute, int second) {
		return between(now(), getEndTime(hourOfDay, minute, second));
	}

	/**
	 * 获取 一段时间后是否在 特定时间范围内
	 * 
	 * @param localDateTime
	 *            当前时间
	 * @param startTime
	 *            判断开始时间 HH:mm:ss
	 * @param endTime
	 *            判断结束时间 HH:mm:ss
	 * @return
	 */
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

	/**
	 * 获取 一段时间后是否在 特定时间范围内，如果是，那么返回 Duration对象，否则，返回null
	 * 
	 * @param localDateTime
	 *            当前时间
	 * @param startTime
	 *            判断开始时间 HH:mm:ss
	 * @param endTime
	 *            判断结束时间 HH:mm:ss
	 * @return
	 */
	public static Duration getDurationEndInRangeTime(LocalDateTime localDateTime, String startTime, String endTime) {
		LocalTime localTimeStart = getLocalTime(startTime);
		LocalTime localTimeEnd = getLocalTime(endTime);
		LocalTime localTimeNow = localDateTime.toLocalTime();
		if (localTimeStart.isBefore(localTimeEnd)) {
			if (localTimeNow.isAfter(localTimeStart) && localTimeNow.isBefore(localTimeEnd)) {
				return Duration.between(localDateTime, LocalDateTime.of(localDateTime.toLocalDate(), localTimeEnd));
			} else {
				return null;
			}
		} else {
			if (localTimeNow.isBefore(localTimeEnd)) {
				return Duration.between(localDateTime, LocalDateTime.of(localDateTime.toLocalDate(), localTimeEnd));
			}

			if (localTimeNow.isAfter(localTimeStart)) {
				return Duration.between(localDateTime,
						LocalDateTime.of(localDateTime.toLocalDate().plusDays(1), localTimeEnd));
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
		LocalDateTime localDateTime = now();
		int day = hourOfDay / 24;
		localDateTime = localDateTime.plusDays(day);
		return LocalDateTime.of(localDateTime.toLocalDate(), LocalTime.of(hourOfDay % 24, minute, second));

	}

}
