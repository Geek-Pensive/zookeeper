package qjw.zkclient.watcher;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.ZkConnection;

public class ZkClientWatcher1 {

	/** zookeeper地址 */
	static final String CONNECT_ADDR = "192.168.142.135:2181,192.168.142.136:2181,192.168.142.138:2181";
	/** session超时时间 */
	static final int SESSION_OUTTIME = 50000;//ms 
	
	
	public static void main(String[] args) throws Exception {
		ZkClient zkc = new ZkClient(new ZkConnection(CONNECT_ADDR), SESSION_OUTTIME);
		
		//对父节点添加监听子节点变化。只监听super下所有的节点增加和删除，  parentPath为变化的节点
		zkc.subscribeChildChanges("/super", new IZkChildListener() {
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
				System.out.println("parentPath: " + parentPath);
				System.out.println("currentChilds: " + currentChilds);
			}
		});
		
		Thread.sleep(3000);
		
		zkc.createPersistent("/super");
		Thread.sleep(1000);
		
		zkc.createPersistent("/super" + "/" + "c1", "c1内容");
		Thread.sleep(1000);
		
		zkc.createPersistent("/super" + "/" + "c2", "c2内容");
		Thread.sleep(1000);	
		
		// 更新操作并不会监听
		zkc.writeData("/super" + "/" + "c1", "c2新内容");
		Thread.sleep(1000);
		
		zkc.delete("/super/c2");
		Thread.sleep(1000);	
		
//		zkc.deleteRecursive("/super");
		Thread.sleep(Integer.MAX_VALUE);
		
		
	}
}
