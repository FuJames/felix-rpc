package rpc.common.serializer;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import java.io.*;

/**
 * @author fuqianzhong
 * @date 18/5/16
 * Hessian 序列化,相对于java序列化,有几个优点,
 * 1. 性能更佳 2. 占用空间更小
 * 适合rpc中的序列化层
 */
public class HessianSerializer extends Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(os);
        try {
            ho.writeObject(obj);
            ho.flush();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            try {
                ho.close();
            } catch (IOException e) {
            }
        }
        return os.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream is = new ByteArrayInputStream(bytes);
        HessianInput hi = new HessianInput(is);
        try {
            return (T) hi.readObject();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }finally {
            hi.close();
        }
    }

    public void serialize(Object obj, OutputStream os) {
        try {
            Hessian2Output ho = new Hessian2Output(os);
            try {
                ho.writeObject(obj);
                ho.flush();
            } finally {
                ho.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public Object deserialize(InputStream is) {
        try {
            Hessian2Input hi = new Hessian2Input(is);
            try {
                return hi.readObject();
            } finally {
                hi.close();
            }
        } catch (Throwable t) {
            throw new IllegalStateException(t.getMessage(), t);
        }
    }
}
