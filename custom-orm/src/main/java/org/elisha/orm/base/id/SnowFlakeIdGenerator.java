package org.elisha.orm.base.id;



import org.elisha.orm.base.utiil.RuntimeUtils;

import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: SnowFlakeIdGenerator
 * @Author <a href="mailto:zfy_zang@163.com">Vincent</a>
 * @Modify
 * @since
 */
public class SnowFlakeIdGenerator implements IdGenerator {


	private static final ReentrantLock REENTRANTLOCK = new ReentrantLock();

	/**
	 * 机器码
	 */
	private static final long MACHINE_CODE  =  RuntimeUtils.getMachineCode();

	/**
	 * 数据id位移数
	 */
	private static final int DATA_ID_SHIFT = 12;


	/**
	 * 机器码位移数
	 */
	private static final int MACHINE_CODE_SHIFT = 5 + DATA_ID_SHIFT;


	/**
	 * 时间戳位移数
	 */
	private static final int TIME_SHIFT = 5 + MACHINE_CODE_SHIFT;


	/**
	 * 时间戳初始值
	 */
	private static final long INIT_TIME = 1605101946490L;

	/**
	 * 序列号最大值
	 */
	private static final long SEQUENCE_MAX = ~(-1 << DATA_ID_SHIFT);


	/**
	 * 数据id最大值
	 */
	private static final int DATA_ID_MAX = ~(-1 << 5);

	/**
	 * 上一个时间戳
	 */
	private long leftTime = INIT_TIME;

	/**
	 * 序列号
	 */
	private long sequence = 0;

	/**
	 * 随机数生成
	 */
	private Random random = new Random();

	/**
	 * 生成
	 * @return
	 */
	@Override
	public Long generate() {
		return generate(random.nextInt(DATA_ID_MAX));
	}


	/**
	 * 生成
	 * @param dataId 数据id
	 * @return
	 */
	@Override
	public Long generate(int dataId) {
		long currentTime = RuntimeUtils.getTimestamp();
		//判断当前时间是否小于上一次时间
		if (currentTime < leftTime){
			throw new IllegalStateException("当前时间错误，请先矫正当前宿主时间");
		}
		long sequenceNum;
		REENTRANTLOCK.lock();
		try {
			//如果是同一时间
			if (leftTime == currentTime) {
				//判断序列号达到最大值则
				if (sequence > SEQUENCE_MAX) {
					throw new IllegalStateException("生成id的qps过大");
				} else {
					sequence++;
				}
			} else {
				//刷新上一次时间
				leftTime = currentTime;
			}
			sequenceNum = sequence;
		}finally {
			REENTRANTLOCK.unlock();
		}
		return ((currentTime - INIT_TIME) << TIME_SHIFT)|
				(MACHINE_CODE << MACHINE_CODE_SHIFT)|
				(dataId << DATA_ID_SHIFT) |
				sequenceNum;
	}


	public static void main(String[] args) {
		System.out.println(SEQUENCE_MAX);
		System.out.println(RuntimeUtils.getTimestamp());
		IdGenerator idGenerator = new SnowFlakeIdGenerator();
		Set<Long> set = new ConcurrentSkipListSet<>();

		for (int j =0 ; j <  1000 ; j ++){
			set.add(idGenerator.generate());
		}

		System.out.println( "size:" + set.size());
		System.out.println("data:" + set);
	}
}
