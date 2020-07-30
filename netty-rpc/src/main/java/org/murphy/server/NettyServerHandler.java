package org.murphy.server;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import org.murphy.RpcParameter;

import java.lang.reflect.Method;
import java.util.logging.Logger;

/**
 * @author xuyuanpeng
 * @version 1.0
 * @date 2019-03-06 14:28
 */
//@Sharable 标识这类的实例之间可以在 channel 里面共享
@ChannelHandler.Sharable
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private static Logger logger = Logger.getLogger("EchoServerHandler");

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg){
        logger.info("channelRead");
        ByteBuf in= (ByteBuf) msg;
        //将所接收的数据返回给发送者。注意，这里还没有冲刷数据
        /**
         * 解析数据，处理数据，调用invoke
         * 将invoke数据返回
         */
        String resultStr="";
        try {
            String read= in.toString(CharsetUtil.UTF_8);
            System.out.println("从客户端读取了字符串：" + read);
            RpcParameter entity = JSON.parseObject(read,RpcParameter.class);
            Class clz= NettyServer.serviceRegistry.get(entity.getClzName());
            Method method=clz.getMethod(entity.getMethodName(),entity.getParamTypes());
            Object resultObj=method.invoke(clz.newInstance(),entity.getArguments());
            resultStr=JSON.toJSONString(resultObj);
        }catch (Exception e){
            logger.info("Exception>"+e.getMessage());
        }

        ByteBuf resultIn= Unpooled.buffer();
        resultIn.writeBytes(resultStr.getBytes());
        //必须写入ByteBuf字节流
        context.write(resultIn);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) throws Exception{
        //冲刷所有待审消息到远程节点。关闭通道后，操作完成
        logger.info("channelReadComplete");

        context.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
