package org.murphy.client;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import org.murphy.RpcParameter;
import org.murphy.callback.INettyCallBack;

@ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    private RpcParameter rpcParameter;
    private INettyCallBack nettyCallBack;
    /**
     * 请求数据
     */
    @Override
    public void channelActive(ChannelHandlerContext context){
        System.out.println("客户端传到服务器的参数：" + JSON.toJSONString(rpcParameter));
        context.writeAndFlush(Unpooled.copiedBuffer(JSON.toJSONString(rpcParameter),
                CharsetUtil.UTF_8));
    }

    /**
     * 服务器返回数据
     */
    @Override
    protected void channelRead0(ChannelHandlerContext context, ByteBuf byteBuf) throws Exception {
        System.out.println("客户端从服务器读取的参数"+byteBuf.toString(CharsetUtil.UTF_8));
        nettyCallBack.calllBack(byteBuf.toString(CharsetUtil.UTF_8));
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }


    public void setRpcParameter(RpcParameter rpcParameter) {
        this.rpcParameter = rpcParameter;
    }


    public void setNettyCallBack(INettyCallBack nettyCallBack) {
        this.nettyCallBack = nettyCallBack;
    }
}
