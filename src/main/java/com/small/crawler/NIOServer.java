package com.small.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Set;

/**
 * @author xinheng
 * @date 2018年11月15日
 * @introduce:
 */
public class NIOServer extends Thread {
	public void run() {
		try (Selector selector = Selector.open(); ServerSocketChannel serverSocket = ServerSocketChannel.open();) {// 创建
																													// Selector
			serverSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 8888));
			serverSocket.configureBlocking(false);
			// 注册到 Selector，并说明关注点
			serverSocket.register(selector, SelectionKey.OP_ACCEPT);
			while (true) {
				selector.select();// 阻塞等待就绪的 Channel，这是关键点之一
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iter = selectedKeys.iterator();
				while (iter.hasNext()) {
					SelectionKey key = iter.next();
					// 生产系统中一般会额外进行就绪状态检查
					sayHelloWorld((ServerSocketChannel) key.channel());
					iter.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void sayHelloWorld(ServerSocketChannel server) throws IOException {
		try (SocketChannel client = server.accept();) {
			client.write(Charset.defaultCharset().encode("Hello world!"));
		}
	}

	public static void main(String[] args) throws IOException {
		NIOServer server = new NIOServer();
		server.start();
		try (Socket client = new Socket(InetAddress.getLocalHost(), 8888)) {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
			bufferedReader.lines().forEach(s -> System.out.println("ab " + s));
		}
	}
}
