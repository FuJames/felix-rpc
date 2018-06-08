package rpc.common.serializer;

import java.io.*;

/**
 * @author fuqianzhong
 * @date 18/5/16
 * java序列化类
 * 1. 序列化: 将对象转化为二进制字节流
 * 2. 反序列化: 将二进制字节流,转化为java对象
 * 3. 被序列化的java对象,必须实现Serializable接口,否则会报NotSerializableException错误
 */
public class JavaSerializer extends Serializer{
    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(obj);
            oos.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
        return os.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            ObjectInputStream is = new ObjectInputStream(bis);
            return (T) is.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void serialize(Object obj,OutputStream os){
        try {
            ObjectOutputStream oos = new ObjectOutputStream(os);
            try {
                oos.writeObject(obj);
                oos.flush();
            } finally {
                oos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object deserialize(InputStream is) {
        try {
            ObjectInputStream ois = new ObjectInputStream(is);
            try {
                return ois.readObject();
            } finally {
                ois.close();
            }
        } catch (Throwable t) {
            return null;
        }
    }

}
