package eu.number26.challenge.connect;

import eu.number26.challenge.core.Context;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class N26ChannelInitializer extends ChannelInitializer<Channel> {

	private final Context context;
	
	public N26ChannelInitializer(Context context) {
		this.context = context;
	}
	
	@Override
	protected void initChannel(Channel channel) throws Exception {
		channel.pipeline().addLast(
			new HttpRequestDecoder(),
			new HttpResponseEncoder(),
			new HttpObjectAggregator(1024 * 1024),
			new HttpRestBridge(context));
	}
}
