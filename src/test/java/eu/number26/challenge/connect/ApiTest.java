package eu.number26.challenge.connect;

import org.junit.Assert;
import org.junit.Test;

import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

public class ApiTest {
	
	@Test
	public void http_GetBaseResource_200() {
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge());
		HttpRequest request = createHttpRequest(HttpMethod.GET, "", null);
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.OK);
		request = createHttpRequest(HttpMethod.GET, "/", null);
		channel.writeInbound(request);
		channel.checkException();
		response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.OK);
	}
	
	@Test
	public void http_GetTransactionRervice_200() {
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge());
		HttpRequest request = createHttpRequest(HttpMethod.GET, "/transactionservice", null);
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.OK);
		request = createHttpRequest(HttpMethod.GET, "/transactionservice/", null);
		channel.writeInbound(request);
		channel.checkException();
		response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.OK);
	}
	
	@Test
	public void http_GetInvalidResourse_404() {
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge());
		HttpRequest request = createHttpRequest(HttpMethod.GET, "/something", null);
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.NOT_FOUND);
	}
	
	private HttpRequest createHttpRequest(HttpMethod method, String path, String content) {
		return new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, path,
			content == null ? Unpooled.buffer(0) : Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
	}
}
