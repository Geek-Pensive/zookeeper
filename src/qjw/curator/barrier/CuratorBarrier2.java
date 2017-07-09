package qjw.curator.barrier;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.queue.DistributedQueue;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;

public class CuratorBarrier2 {

	/** zookeeper地址 */
	static final String CONNECT_ADDR = "192.168.142.135:2181,192.168.142.136:2181,192.168.142.138:2181";
	/** session超时时间 */
	static final int SESSION_OUTTIME = 50000;//ms 
	
	static DistributedBarrier barrier = null;
	
	public static void main(String[] args) throws Exception {
		
		for(int i = 0; i < 5; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 10);
						CuratorFramework cf = CuratorFrameworkFactory.builder()
									.connectString(CONNECT_ADDR)
									.sessionTimeoutMs(SESSION_OUTTIME)
									.retryPolicy(retryPolicy)
									.build();
						cf.start();
						barrier = new DistributedBarrier(cf, "/barrier");
						System.out.println(Thread.currentThread().getName() + "设置barrier!");
						barrier.setBarrier();	//设置障碍物
						barrier.waitOnBarrier();	//等待障碍物移除
						System.out.println("---------开始执行程序----------");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			},"t" + i).start();
		}

		Thread.sleep(2000);
		barrier.removeBarrier();	//释放障碍物
		
	}
}
