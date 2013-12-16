package com.core.server;

import org.apache.log4j.Level;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import com.common.ProtocolCommon;
import com.core.protocol.HandlerClassLoader;
import com.core.protocol.ResponseClassLoader;
import com.core.protocol.handler.Handler;
import com.core.protocol.request.Request;
import com.core.protocol.response.Response;
import com.exception.DcpException;
import com.exception.ServerInternalErrorException;
import com.util.QueuedLogger.QueuedLogger;


public class ServerHandler extends SimpleChannelUpstreamHandler {
	private String lastRequestedTelegramNumber;
	
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		super.channelConnected(ctx, e);
		
		QueuedLogger.push(Level.INFO, "[channel:" + e.getChannel().getId() + "]" + " CONNECTED: " + e.getChannel().getRemoteAddress());
	}

	@Override
	public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        super.channelDisconnected(ctx, e);
        
		QueuedLogger.push(Level.INFO, "[channel:" + e.getChannel().getId() + "]" + " DISCONNECTED: " + e.getChannel().getRemoteAddress());
		
		lastRequestedTelegramNumber = null;
    }

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		if (e.getMessage() instanceof Request == false)
			throw new ServerInternalErrorException();
		
		Request request = (Request)e.getMessage();
		Response response = ResponseClassLoader.loadFrom(request.getTelegramNumber());
		Handler<Request, Response> handler = HandlerClassLoader.loadFrom(request.getTelegramNumber());
		lastRequestedTelegramNumber = request.getTelegramNumber();
		
		handler.setRequest(request);
		handler.setResponse(response);
		handler.handle();

		e.getChannel().write(handler.getResponse()).awaitUninterruptibly();
		e.getChannel().close();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
		DcpException exception = null;
		if (e.getCause() instanceof DcpException)
			exception = (DcpException)e.getCause();
		else
			exception = new ServerInternalErrorException();
		
		Response response = exception.createErrorResponse(lastRequestedTelegramNumber);
		e.getChannel().write(response).awaitUninterruptibly();
		e.getChannel().close();
		
		if (exception.getResultCode().equals(ProtocolCommon.RESULT_CODE_SEVER_INTERNAL_ERROR))
			QueuedLogger.push(Level.ERROR, "[channel:" + e.getChannel().getId() + "]" + " Internal system exception.", e.getCause());
	}
}
