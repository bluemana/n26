package eu.number26.challenge.connect;

import org.apache.log4j.Logger;

import eu.number26.challenge.core.Context;
import eu.number26.challenge.protocol.Configuration;
import eu.number26.challenge.protocol.Link;
import eu.number26.challenge.protocol.ProtocolHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class HttpRestBridge extends ChannelHandlerAdapter {

	private static final Logger LOGGER = Logger.getLogger(HttpRestBridge.class);
	
	private final Context context;
	
	public HttpRestBridge(Context context) {
		this.context = context;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		FullHttpRequest request = (FullHttpRequest) msg;
		LOGGER.info("Received request:\n" + msg);
		FullHttpResponse response = null;
		if (request.decoderResult().isSuccess()) {
			String uri = request.uri();
			HttpMethod method = request.method();
			Link link = new Link(method, uri);
			ProtocolHandler handler = Configuration.getHandler(link);
			if (handler != null) {
				try {
					String json = handler.handle(context, link, request.content().toString(CharsetUtil.UTF_8));
					response = createHttpResponse(HttpResponseStatus.OK, json);
				} catch (Exception e) {
					response = createHttpResponse(HttpResponseStatus.BAD_REQUEST, e.getMessage());
				}
			} else {
				response = createHttpResponse(HttpResponseStatus.NOT_FOUND, null);
			}
		} else {
			response = createHttpResponse(HttpResponseStatus.BAD_REQUEST,
				request.decoderResult().cause().getMessage());
		}
		response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8");
		LOGGER.info("Sending response:\n" + response);
		ctx.writeAndFlush(response);
		if (!HttpHeaderUtil.isKeepAlive(request)) {
			ctx.close();
		}
	}
	
	@Override
 	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LOGGER.error(cause.getMessage(), cause);
		ctx.close();
	}
	
	private static FullHttpResponse createHttpResponse(HttpResponseStatus status, String content) {
		return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,
				content == null ? Unpooled.buffer(0) : Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
	}
}
