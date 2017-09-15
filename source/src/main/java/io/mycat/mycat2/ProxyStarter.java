package io.mycat.mycat2;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.mycat.proxy.BufferPool;
import io.mycat.proxy.MycatReactorThread;
import io.mycat.proxy.NIOAcceptor;
import io.mycat.proxy.ProxyRuntime;
import io.mycat.proxy.man.AdminCommandResovler;
import io.mycat.proxy.man.ClusterNode;
import io.mycat.proxy.man.MyCluster;

public class ProxyStarter {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProxyStarter.class);

	public static final ProxyStarter INSTANCE = new ProxyStarter();

	private ProxyStarter(){}

	public void start() throws IOException {
		ProxyRuntime runtime = ProxyRuntime.INSTANCE;
		MycatConfig conf = (MycatConfig) runtime.getProxyConfig();

		// 启动NIO Acceptor
		NIOAcceptor acceptor = new NIOAcceptor(new BufferPool(1024 * 10));
		acceptor.start();
		runtime.setAcceptor(acceptor);

		if (conf.isClusterEnable()) {
			// 集群开启状态，需要等集群启动，主节点确认完配置才能提供服务
			acceptor.startServerChannel(conf.getClusterIP(), conf.getClusterPort(), true);
			runtime.setAdminCmdResolver(new AdminCommandResovler());
			MyCluster cluster = new MyCluster(acceptor.getSelector(), conf.getMyNodeId(), ClusterNode.parseNodesInf(conf.getAllNodeInfs()));
			runtime.setMyCLuster(cluster);
			cluster.initCluster();
		} else {
			// 未配置集群，直接启动
			startProxy();
		}
	}

	public void startProxy() throws IOException {
		ProxyRuntime runtime = ProxyRuntime.INSTANCE;
		MycatConfig conf = (MycatConfig) runtime.getProxyConfig();

		// 开启mycat服务
		ConfigLoader.INSTANCE.loadAll(conf);
		NIOAcceptor acceptor = runtime.getAcceptor();
		acceptor.startServerChannel(conf.getBindIP(), conf.getBindPort(), false);
		startReactor();
		// 初始化
		init(conf);
	}

	public void stopProxy() {
		ProxyRuntime runtime = ProxyRuntime.INSTANCE;
		NIOAcceptor acceptor = runtime.getAcceptor();
		acceptor.stopServerChannel(false);
	}

	private void startReactor() throws IOException {
		// Mycat 2.0 Session Manager
		MycatReactorThread[] nioThreads = (MycatReactorThread[]) MycatRuntime.INSTANCE.getReactorThreads();
		int cpus = nioThreads.length;
		for (int i = 0; i < cpus; i++) {
			MycatReactorThread thread = new MycatReactorThread(new BufferPool(1024 * 10));
			thread.setName("NIO_Thread " + (i + 1));
			thread.start();
			nioThreads[i] = thread;
		}
	}

	private void init(MycatConfig conf) {
		// 初始化连接
		conf.getMysqlRepMap().forEach((key, value) -> {
			value.initMaster();
			value.getMysqls().forEach(metaBean -> {
				try {
					metaBean.init(value);
				} catch (IOException e) {
					LOGGER.error("error to init metaBean: {}", metaBean.getHostName());
				}
			});
		});
	}
}
