package eu.number26.challenge;

import eu.number26.challenge.connect.N26ChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class N26Server {

	private final int port;
	
	public N26Server(int port) {
		this.port = port;
	}
	
	public void start() throws Exception {
		EventLoopGroup incomingConnectionGroup = new NioEventLoopGroup();
		EventLoopGroup acceptedConnectionGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(incomingConnectionGroup, acceptedConnectionGroup);
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.childHandler(new N26ChannelInitializer());
			bootstrap.option(ChannelOption.SO_BACKLOG, 128);
			bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture channelFuture = bootstrap.bind(port).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
			incomingConnectionGroup.shutdownGracefully();
			acceptedConnectionGroup.shutdownGracefully();
		}
	}
}
