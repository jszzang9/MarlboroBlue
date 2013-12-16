package com.core.server;

import java.io.UnsupportedEncodingException;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Level;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.common.ProtocolCommon;
import com.core.protocol.RequestClassLoader;
import com.core.protocol.request.Request;
import com.exception.DcpException;
import com.exception.InvalidJsonFormatException;
import com.util.JsonGenerator;
import com.util.QueuedLogger.QueuedLogger;
import com.util.QueuedLogger.QueuedTransactionLogs;

public class ServerDecoder extends FrameDecoder{

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws UnsupportedEncodingException, DcpException {
		if (buffer.readableBytes() < ProtocolCommon.BODY_LENGTH_SIZE + ProtocolCommon.TELEGRAM_NUMBER_SIZE)
			return null;
		
		buffer.markReaderIndex();

		int bodyLength = buffer.readInt();
		if (buffer.readableBytes() < ProtocolCommon.TELEGRAM_NUMBER_SIZE + bodyLength) {
			buffer.resetReaderIndex();
			return null;
		}
		
		byte[] telegramNumber = new byte[ProtocolCommon.TELEGRAM_NUMBER_SIZE];
		buffer.readBytes(telegramNumber);

		byte[] bodyData = new byte[bodyLength];
		buffer.readBytes(bodyData);

		String telegramNumberString = new String(telegramNumber, "UTF-8").toUpperCase();
		String bodyDataString = new String(bodyData, "UTF-8");
		
		QueuedTransactionLogs rawLogs = new QueuedTransactionLogs();
		rawLogs.add(Level.INFO, "[channel:" + channel.getId() + "]" + " ================== Incoming Info ================== ");
		rawLogs.add(Level.INFO, "[channel:" + channel.getId() + "]" + "  > Connection : " + channel.getRemoteAddress() + " ");
		rawLogs.add(Level.INFO, "[channel:" + channel.getId() + "]" + "  > Raw Data : " + bodyLength + telegramNumberString + bodyDataString + " ");
		rawLogs.add(Level.INFO, "[channel:" + channel.getId() + "]" + " =================================================== ");
		QueuedLogger.push(rawLogs);

		Request request = RequestClassLoader.loadFrom(telegramNumberString);
		if (JsonGenerator.mayBeJSON(bodyDataString) == false)
			throw new InvalidJsonFormatException(telegramNumberString);
		try {
			request.setBodyData(JSONObject.fromObject(bodyDataString));
		} catch (JSONException e) {
			throw new InvalidJsonFormatException(telegramNumberString);
		}
		
		QueuedTransactionLogs logs = new QueuedTransactionLogs();
		logs.add(Level.INFO, "[channel:" + channel.getId() + "]" + " ================== Incoming Info ================== ");
		logs.add(Level.INFO, "[channel:" + channel.getId() + "]" + "  > Connection : " + channel.getRemoteAddress() + " ");
		logs.add(Level.INFO, "[channel:" + channel.getId() + "]" + "  > Telegram Number : " + request.getTelegramNumber() + " ");
		logs.add(Level.INFO, "[channel:" + channel.getId() + "]" + "  > Description : " + request.getDesscription() + " ");
		logs.add(Level.INFO, "[channel:" + channel.getId() + "]" + "  > Body Data : " + bodyDataString + " ");
		logs.add(Level.INFO, "[channel:" + channel.getId() + "]" + " =================================================== ");
		QueuedLogger.push(logs);

		return request;
	}
}
