package rpc.common.serializer;

/**
 * @author fuqianzhong
 * @date 18/5/16
 * 序列化的接口类,定义待实现的方法
 */
public abstract class Serializer {
    public abstract <T> byte[] serialize(T obj);

    public abstract <T> T deserialize(byte[] bytes, Class<T> clazz);
}
