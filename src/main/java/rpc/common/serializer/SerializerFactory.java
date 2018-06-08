package rpc.common.serializer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author fuqianzhong
 * @date 18/5/16
 * 序列化类的工厂,
 * 1. 单例模式: 采用懒汉模式,实例对象只有在方法被访问到时才实例化 ; 线程安全,double check(防止重复实例化)
 */
public class SerializerFactory {
    private static final ConcurrentHashMap<Byte,Serializer> serializerMap = new ConcurrentHashMap<Byte, Serializer>();
    private static volatile boolean isInitialized = false;

    private SerializerFactory(){
    }

    static {
        if(!isInitialized){
            synchronized (SerializerFactory.class){
                if(!isInitialized){
                    serializerMap.put(SerializerType.JAVA.getCode(), new JavaSerializer());
                    serializerMap.put(SerializerType.HESSIAN.getCode(), new HessianSerializer());
                    isInitialized = true;
                }
            }
        }
    }

    public static Serializer getSerializer(SerializerType serializerType){
        return serializerMap.get(serializerType.getCode());
    }
}
