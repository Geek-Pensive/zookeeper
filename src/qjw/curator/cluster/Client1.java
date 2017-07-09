package qjw.curator.cluster;

/**
 * 每次启动客户端，都会将数据库(zookeeper的数据同步到程序中，并打印出来)
 * @author PC
 */
public class Client1 {

	public static void main(String[] args) throws Exception{
		
		CuratorWatcher watcher = new CuratorWatcher();
		System.out.println("c1 start ..");
		Thread.sleep(100000000);
	}
}
