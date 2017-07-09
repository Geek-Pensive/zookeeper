package qjw.zookeeper.base;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;

/**
 * Zookeeper base学习笔记
 * @since 2015-6-13
 */
public class ZookeeperBase {

	/** zookeeper地址 */
	static final String CONNECT_ADDR = "192.168.142.135:2181,192.168.142.136:2181,192.168.142.138:2181";
	/** session超时时间 */
	static final int SESSION_OUTTIME = 5000;//ms 
	/** 信号量，阻塞程序执行，用于等待zookeeper连接成功，发送成功信号.因为Client端连接ZK集群是异步的，可能还没连接上 */
	static final CountDownLatch connectedSemaphore = new CountDownLatch(1);
	
	public static void main(String[] args) throws Exception{
		
		ZooKeeper zk = new ZooKeeper(CONNECT_ADDR, SESSION_OUTTIME, new Watcher(){
			@Override
			public void process(WatchedEvent event) {
				//获取事件的状态
				KeeperState keeperState = event.getState();
				EventType eventType = event.getType();
				//如果是建立连接
				if(KeeperState.SyncConnected == keeperState){
					if(EventType.None == eventType){
						//如果建立连接成功，则发送信号量，让后续阻塞程序向下执行
						System.out.println("zk 连接成功！");
						connectedSemaphore.countDown();
					}
				}
			}
		});

		//进行阻塞
		connectedSemaphore.await();
		
		System.out.println("============开始操作============");
		
		//创建父节点。注意：不可重复创建，重复创建抛异常
//		String res = zk.create("/testRoot", "testRoot".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
//		System.out.println(res);
		
		//没有父节点，不能直接创建子节点，创建子节点:临时节点：本地会话有效，可以用来做分布式锁
//		String ret = zk.create("/testRoot/children", "children data".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
//		System.out.println(ret);
		
		//获取节点信息
//		byte[] data = zk.getData("/testRoot", false, null);
//		System.out.println(new String(data));
//		System.out.println(zk.getChildren("/testRoot", false));
		
		//修改节点的值
//		zk.setData("/testRoot", "modify data root".getBytes(), -1);
//		byte[] data = zk.getData("/testRoot", false, null);
//		System.out.println(new String(data));		
		
		//判断节点是否存在
//		System.out.println(zk.exists("/testRoot/children", false));
		//删除节点
//		zk.delete("/testRoot/children", -1);
//		System.out.println(zk.exists("/testRoot/children", false));
		
		zk.close();
		
	}
	
}
