/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.example.discard;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

/**
 * Handles a client-side channel.
 */
public class DiscardClientHandler extends ChannelHandlerAdapter {

private final ByteBuf message;
	
	// 초기화
	public DiscardClientHandler(){
		message = Unpooled.buffer(EchoClient.MESSAGE_SIZE);
		// 예제로 사용할 바이트 배열을 만듭니다.
		byte[] str = "abcefg".getBytes();
		// 예제 바이트 배열을 메시지에 씁니다.
		message.writeBytes(str);

	}
	
	// 채널이 활성화 되면 동작할 코드를 정의합니다.
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// 메시지를 쓴 후 플러쉬합니다.
		ctx.writeAndFlush(message);
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception {
		// 받은 메시지를 ByteBuf형으로 캐스팅합니다.
		ByteBuf byteBufMessage = (ByteBuf) msg;
		// 읽을 수 있는 바이트의 길이를 가져옵니다.
		int size = byteBufMessage.readableBytes();

		// 읽을 수 있는 바이트의 길이만큼 바이트 배열을 초기화합니다.
		byte [] byteMessage = new byte[size];
		// for문을 돌며 가져온 바이트 값을 연결합니다.
		for(int i = 0 ; i < size; i++){
			byteMessage[i] = byteBufMessage.getByte(i);
		}
		
		// 바이트를 String 형으로 변환합니다.
		String str = new String(byteMessage);
		
		// 결과를 콘솔에 출력합니다.
		System.out.println(str);

		// 그후 컨텍스트를 종료합니다.
		ctx.close();
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}