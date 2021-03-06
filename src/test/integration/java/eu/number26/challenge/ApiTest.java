package eu.number26.challenge;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gson.Gson;

import eu.number26.challenge.connect.HttpRestBridge;
import eu.number26.challenge.core.Context;
import eu.number26.challenge.core.Transaction;
import eu.number26.challenge.protocol.Configuration;
import eu.number26.challenge.protocol.sum.GetSumResponse;
import eu.number26.challenge.protocol.transaction.GetTransactionResponse;
import eu.number26.challenge.protocol.transaction.PutTransactionRequest;
import eu.number26.challenge.protocol.transaction.PutTransactionResponse;
import eu.number26.challenge.protocol.type.GetTypeResponse;
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
	
	private static final Gson GSON = Configuration.GSON;
	
	@Test
	public void baseResource_Get_Resource() {
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
	public void transactionService_Get_Service() {
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
	public void invalidResource_Get_Error() {
		Context context = Mockito.mock(Context.class);
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		HttpRequest request = createHttpRequest(HttpMethod.GET, "/something", null);
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.NOT_FOUND);
	}
	
	@Test
	public void transaction_PutTransaction_Created() {
		Context context = Mockito.mock(Context.class);
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		PutTransactionRequest requestBody = new PutTransactionRequest(null, "book", new BigDecimal(15));
		HttpRequest request = createHttpRequest(HttpMethod.PUT, "/transactionservice/transaction/1", GSON.toJson(requestBody));
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.OK);
		PutTransactionResponse expectedResponseBody = new PutTransactionResponse(PutTransactionResponse.Status.OK);
		PutTransactionResponse responseBody = GSON.fromJson(response.content().toString(Charset.forName("utf-8")), PutTransactionResponse.class);
		Assert.assertTrue(responseBody.equals(expectedResponseBody));
		Mockito.verify(context).addTransaction(new Transaction(1, null, "book", new BigDecimal(15)));
	}
	
	@Test
	public void transaction_PutTransactionWithInvalidId_Error() {
		Context context = Mockito.mock(Context.class);
		Mockito.doThrow(new IllegalArgumentException("Invalid transaction ID: 1"))
			.when(context).addTransaction(new Transaction(1, null, "rocket", new BigDecimal(15000000000L)));;
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		PutTransactionRequest requestBody = new PutTransactionRequest(null, "rocket", new BigDecimal(15000000000L));
		HttpRequest request = createHttpRequest(HttpMethod.PUT, "/transactionservice/transaction/1", GSON.toJson(requestBody));
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.BAD_REQUEST);
	}
	
	@Test
	public void transaction_GetTransaction_Transaction() {
		Context context = Mockito.mock(Context.class);
		Mockito.when(context.getTransaction(1)).thenReturn(new Transaction(2, 1L, "sport", new BigDecimal(30)));
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		HttpRequest request = createHttpRequest(HttpMethod.GET, "/transactionservice/transaction/1", null);
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.OK);
		GetTransactionResponse expectedResponseBody = new GetTransactionResponse(1L, "sport", new BigDecimal(30));
		GetTransactionResponse responseBody = GSON.fromJson(response.content().toString(Charset.forName("utf-8")),
			GetTransactionResponse.class);
		Assert.assertTrue(responseBody.equals(expectedResponseBody));
	}
	
	@Test
	public void transaction_GetNonExistentTransaction_Error() {
		Context context = Mockito.mock(Context.class);
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		HttpRequest request = createHttpRequest(HttpMethod.GET, "/transactionservice/transaction/1", null);
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.BAD_REQUEST);
	}
	
	@Test
	public void transaction_GetTransactionWithWrongIdType_Error() {
		Context context = Mockito.mock(Context.class);
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		HttpRequest request = createHttpRequest(HttpMethod.GET, "/transactionservice/transaction/id1", null);
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.BAD_REQUEST);
	}
	
	@Test
	public void types_GetType_TransactionList() {
		Context context = Mockito.mock(Context.class);
		Set<Transaction> transactions = new HashSet<>();
		transactions.add(new Transaction(1, null, "fee", new BigDecimal(13)));
		transactions.add(new Transaction(2, 1L, "fee", new BigDecimal(20)));
		Mockito.when(context.getTransactions("fee")).thenReturn(transactions);
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		HttpRequest request = createHttpRequest(HttpMethod.GET, "/transactionservice/type/fee", null);
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.OK);
		GetTypeResponse expectedResponseBody = new GetTypeResponse();
		expectedResponseBody.add(1L);
		expectedResponseBody.add(2L);
		GetTypeResponse responseBody = GSON.fromJson(response.content().toString(Charset.forName("utf-8")),
			GetTypeResponse.class);
		Assert.assertTrue(responseBody.equals(expectedResponseBody));
	}
	
	@Test
	public void types_GetUnmatchingType_EmptyTransactionList() {
		Context context = Mockito.mock(Context.class);
		Mockito.when(context.getTransactions("fee")).thenReturn(Collections.emptySet());
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		HttpRequest request = createHttpRequest(HttpMethod.GET, "/transactionservice/type/fee", null);
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.OK);
		GetTypeResponse responseBody = GSON.fromJson(response.content().toString(Charset.forName("utf-8")),
			GetTypeResponse.class);
		Assert.assertTrue(responseBody.isEmpty());
	}
	
	@Test
	public void sum_GetSum_Sum() {
		Context context = Mockito.mock(Context.class);
		Mockito.when(context.transitiveSum(1)).thenReturn(new BigDecimal(100.4));
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		HttpRequest request = createHttpRequest(HttpMethod.GET, "/transactionservice/sum/1", null);
		channel.writeInbound(request);
		channel.checkException();
		FullHttpResponse response = channel.readOutbound();
		Assert.assertTrue(response.status() == HttpResponseStatus.OK);
		GetSumResponse expectedResponseBody = new GetSumResponse(new BigDecimal(100.4));
		GetSumResponse responseBody = GSON.fromJson(response.content().toString(Charset.forName("utf-8")), GetSumResponse.class);
		Assert.assertTrue(responseBody.equals(expectedResponseBody));
	}
	
	@Test
	public void sum_GetSumOfUnmatchingTransactionId_Error() {
		Context context = Mockito.mock(Context.class);
		Mockito.when(context.transitiveSum(1)).thenReturn(null);
		EmbeddedChannel channel = new EmbeddedChannel(new HttpRestBridge(context));
		HttpRequest request = createHttpRequest(HttpMethod.GET, "/transactionservice/sum/1", null);
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
