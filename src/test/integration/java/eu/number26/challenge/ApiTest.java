package eu.number26.challenge;

import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import eu.number26.challenge.connect.HttpRestBridge;
import eu.number26.challenge.core.Context;
import eu.number26.challenge.core.Transaction;
import eu.number26.challenge.protocol.transaction.PutTransactionRequest;
import eu.number26.challenge.protocol.transaction.PutTransactionResponse;
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
	
	private static final Gson GSON = new GsonBuilder().create();
	
	@Test
	public void baseResource_Get_200() {
		Context context = Mockito.mock(Context.class);
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
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
	public void transactionService_Get_200() {
		Context context = Mockito.mock(Context.class);
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
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
	public void invalidResource_Get_404() {
		Context context = Mockito.mock(Context.class);
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		HttpRequest request = createHttpRequest(HttpMethod.GET, "/something", null);
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.NOT_FOUND);
	}
	
	@Test
	public void transaction_Put_Created() {
		Context context = Mockito.mock(Context.class);
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		PutTransactionRequest requestBody = new PutTransactionRequest(null, "shopping", new BigDecimal(10.5));
		PutTransactionResponse expectedBody = new PutTransactionResponse(PutTransactionResponse.Status.OK);
		HttpRequest request = createHttpRequest(HttpMethod.PUT, "/transactionservice/transaction/1", GSON.toJson(requestBody));
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.OK);
		PutTransactionResponse responseBody = new GsonBuilder().create().fromJson(
			response.content().toString(Charset.forName("utf-8")),
			PutTransactionResponse.class);
		Assert.assertTrue(responseBody.equals(expectedBody));
		Mockito.verify(context).addTransaction(new Transaction(1, null, "shopping", new BigDecimal(10.5)));
	}
	
	@Test
	public void transaction_PutTransactionWithInvalidId_Error() {
		Context context = Mockito.mock(Context.class);
		Mockito.doThrow(new IllegalArgumentException("Invalid transaction ID: 1"))
			.when(context).addTransaction(new Transaction(1, null, "shopping", new BigDecimal(10.5)));;
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		PutTransactionRequest requestBody = new PutTransactionRequest(null, "shopping", new BigDecimal(10.5));
		HttpRequest request = createHttpRequest(HttpMethod.PUT, "/transactionservice/transaction/1", GSON.toJson(requestBody));
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.BAD_REQUEST);
	}
	
	private HttpRequest createHttpRequest(HttpMethod method, String path, String content) {
		return new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, method, path,
			content == null ? Unpooled.buffer(0) : Unpooled.copiedBuffer(content, CharsetUtil.UTF_8));
	}
}
