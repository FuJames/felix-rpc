package rpc.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import rpc.common.bean.RpcSerializable;
import rpc.common.serializer.Serializer;
import rpc.common.serializer.SerializerFactory;
import rpc.common.serializer.SerializerType;

/**
 * @author fuqianzhong
 * @date 18/6/1
 * encode:对象转成字节流
 */
public class NettyEncoder extends MessageToByteEncoder<Object> {
    private Class<?> msgClass;

    public NettyEncoder(Class<?> msgClass){
        this.msgClass = msgClass;
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if(msgClass.isInstance(msg)){
            RpcSerializable serializable = (RpcSerializable) msg;
            SerializerType serializerType = SerializerType.getSerializerType(serializable.getSerialize());
            Serializer serializer = SerializerFactory.getSerializer(serializerType);
            byte[] bytes = serializer.serialize(msg);
            out.writeByte(serializerType.getCode());
            out.writeInt(bytes.length);
//            out.writeInt(Integer.MAX_VALUE);
            out.writeBytes(bytes);
        }
    }
}
