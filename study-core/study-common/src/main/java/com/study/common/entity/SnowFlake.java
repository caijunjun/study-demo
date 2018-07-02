package com.study.common.entity;

/**
 * 参考 https://github.com/beyondfengyu/SnowFlake
 * 
 * @author caijunjun
 * @date 2018年7月2日
 * 
 *       概述
 *       SnowFlake算法是Twitter设计的一个可以在分布式系统中生成唯一的ID的算法，它可以满足Twitter每秒上万条消息ID分配的请求，这些消息ID是唯一的且有大致的递增顺序。
 * 
 *       原理 SnowFlake算法产生的ID是一个64位的整型，结构如下（每一部分用“-”符号分隔）：
 * 
 *       0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 -
 *       000000000000
 * 
 * 
 * 
 *       1位，不用。二进制中最高位为1的都是负数，但是我们生成的id一般都使用整数，所以这个最高位固定是0 41位，用来记录时间戳（毫秒）。
 * 
 *       41位可以表示241−1个数字， 如果只用来表示正整数（计算机中正数包含0），可以表示的数值范围是：0 至
 *       241−1，减1是因为可表示的数值范围是从0开始算的，而不是1。
 *       也就是说41位可以表示241−1个毫秒的值，转化成单位年则是(241−1)/(1000∗60∗60∗24∗365)=69年
 *       
 *       10位，用来记录工作机器id。
 *       可以部署在210=1024个节点，包括5位datacenterId和5位workerId
 *       5位（bit）可以表示的最大正整数是25−1=31，即可以用0、1、2、3、....31这32个数字，来表示不同的datecenterId或workerId
 *       12位，序列号，用来记录同毫秒内产生的不同id。
 * 
 *       12位（bit）可以表示的最大正整数是212−1=4095，即可以用0、1、2、3、....4094这4095个数字，来表示同一机器同一时间截（毫秒)内产生的4095个ID序号
 *       SnowFlake算法生成的ID大致上是按照时间递增的，用在分布式系统中时，需要注意数据中心标识和机器标识必须唯一，这样就能保证每个节点生成的ID都是唯一的。或许我们不一定都需要像上面那样使用5位作为数据中心标识，5位作为机器标识，可以根据我们业务的需要，灵活分配节点部分，如：若不需要数据中心，完全可以使用全部10位作为机器标识；若数据中心不多，也可以只使用3位作为数据中心，7位作为机器标识。
 */
public class SnowFlake {

	/**
	 * 起始的时间戳
	 */
	private static final long START_STMP = 1530503181154L;

	/**
	 * 每一部分占用的位数
	 */
	private static final long SEQUENCE_BIT = 12; // 序列号占用的位数
	private static final long MACHINE_BIT = 5; // 机器标识占用的位数
	private static final long DATACENTER_BIT = 5;// 数据中心占用的位数

	/**
	 * 每一部分的最大值
	 */
	private static final long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
	private static final long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
	private static final long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

	/**
	 * 每一部分向左的位移
	 */
	private static final long MACHINE_LEFT = SEQUENCE_BIT;
	private static final long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
	private static final long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

	private long datacenterId; // 数据中心
	private long machineId; // 机器标识
	private long sequence = 0L; // 序列号
	private long lastStmp = -1L;// 上一次时间戳

	/**
	 * 每台服务器可以实例化一个
	 * 
	 * @param datacenterId
	 *            数据中心id
	 * @param machineId
	 *            机器标识id
	 */
	public SnowFlake(long datacenterId, long machineId) {
		if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
			throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
		}
		if (machineId > MAX_MACHINE_NUM || machineId < 0) {
			throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
		}
		if (getCurrentTimeMillis() < lastStmp) {
			throw new IllegalArgumentException("Clock moved backwards.  Refusing to generate id");
		}

		this.datacenterId = datacenterId;
		this.machineId = machineId;
	}

	/**
	 * 产生下一个ID
	 *
	 * @return
	 */
	public synchronized long nextId() {
		long currStmp = getCurrentTimeMillis();

		if (currStmp == lastStmp) {
			// 相同毫秒内，序列号自增
			sequence = (sequence + 1) & MAX_SEQUENCE;
			// 同一毫秒的序列数已经达到最大
			if (sequence == 0L) {
				currStmp = getNextMill();
			}
		} else {
			// 不同毫秒内，序列号置为0
			sequence = 0L;
		}

		lastStmp = currStmp;

		return (currStmp - START_STMP) << TIMESTMP_LEFT // 时间戳部分
				| datacenterId << DATACENTER_LEFT // 数据中心部分
				| machineId << MACHINE_LEFT // 机器标识部分
				| sequence; // 序列号部分
	}

	private long getNextMill() {
		long mill = getCurrentTimeMillis();
		while (mill <= lastStmp) {
			mill = getCurrentTimeMillis();
		}
		return mill;
	}

	private long getCurrentTimeMillis() {
		return System.currentTimeMillis();
	}

}