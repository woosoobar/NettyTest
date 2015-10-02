package io.netty.example.discard;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {
	// 채널을 읽을 때 동작할 코드를 정의 합니다.
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		
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
		str =  str + " from echo";
		
		String json = "{\"name\": \"kim\", \"age\": \"20\"}";
		
		// 결과를 콘솔에 출력합니다.
		System.out.println("============================================ 111  ");
		System.out.println(str);
		
		Thread.sleep(30000);
		byteBufMessage.writeBytes(json.getBytes());
		ctx.write(byteBufMessage); // 메시지를 그대로 다시 write 합니다.
	}

	// 채널 읽는 것을 완료했을 때 동작할 코드를 정의 합니다.
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		System.out.println("============================================ 222 ");
		ctx.flush(); // 컨텍스트의 내용을 플러쉬합니다.
	};

	// 예외가 발생할 때 동작할 코드를 정의 합니다.
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace(); // 쌓여있는 트레이스를 출력합니다.
		ctx.close(); // 컨텍스트를 종료시킵니다.
	}
}