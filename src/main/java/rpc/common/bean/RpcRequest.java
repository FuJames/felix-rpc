package rpc.common.bean;

import java.util.Arrays;

/**
 * @author fuqianzhong
 * @date 18/6/1
 */
public class RpcRequest implements RpcSerializable {
    private static final long serialVersionUID = -6898431081897296544L;

    private String requestId;
    private String serviceKey;
    private long createMillisTime;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    private String serialize;
    private long timeout = 1000;

    public String getRequestId() {
        return requestId;
    }
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }
    public long getCreateMillisTime() {
        return createMillisTime;
    }
    public void setCreateMillisTime(long createMillisTime) {
        this.createMillisTime = createMillisTime;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getMethodName() {
        return methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }
    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }
    public Object[] getParameters() {
        return parameters;
    }
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public String getServiceKey() {
        return serviceKey;
    }

    public void setServiceKey(String serviceKey) {
        this.serviceKey = serviceKey;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    @Override
    public String getSerialize() {
        return serialize;
    }

    @Override
    public void setSerialize(String serialize) {
        this.serialize = serialize;
    }

    @Override
    public String toString() {
        return "NettyRequest [requestId=" + requestId + ", createMillisTime="
                + createMillisTime + ", className=" + className
                + ", methodName=" + methodName + ", serialize=" + serialize + ", parameterTypes="
                + Arrays.toString(parameterTypes) + ", parameters="
                + Arrays.toString(parameters) + "]";
    }
}
