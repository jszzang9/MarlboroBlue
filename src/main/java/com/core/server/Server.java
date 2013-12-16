package com.core.server;

import java.util.concurrent.Executors;

import org.apache.log4j.Level;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import com.core.model.TestDataBox;
import com.util.QueuedLogger.QueuedLogger;

public class Server {
	public void run(int port) {
		QueuedLogger.push(Level.INFO, "DCP starting...");
		
		boolean isReady = false;
		if (run_model()) {
			if (run_server(port))
				isReady = true;
		}
		
		if (isReady) {
			QueuedLogger.push(Level.INFO, "DCP is ready.");
		}
		else {
			QueuedLogger.push(Level.FATAL, "DCP failed.");
			QueuedLogger.shutdown();
		}
	}

	private boolean run_server(int port) {
		QueuedLogger.push(Level.INFO, "DCP-Server is preparing...");
		
		ServerBootstrap bootstrap = new ServerBootstrap(new NioServerSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new ServerPipelineFactory());
		bootstrap.setOption("child.tcpNodelay", true);
		return false;
	}

	private boolean run_model() {
		QueuedLogger.push(Level.INFO, "DCP-Model is perparing...");
		
		try {
			TestDataBox.getInstance().init();
			
			QueuedLogger.push(Level.INFO, "DCP-Model is ready.");
		} 
		catch (Exception e) {
			QueuedLogger.push(Level.FATAL, "It's maybe not running dbms.");
			QueuedLogger.push(Level.FATAL, "DCP-Model failed.");
			return false;
		}
		return true;
	}
}
