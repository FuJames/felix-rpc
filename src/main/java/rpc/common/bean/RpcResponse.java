package rpc.common.bean;

/**
 * @author fuqianzhong
 * @date 18/6/1
 */
public class RpcResponse implements RpcSerializable {

    private static final long serialVersionUID = -1186899064204492333L;

    private String requestId;
    private Throwable error;
    private Object result;
    private String serialize;


    public boolean isError() {
        return error != null;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
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
        return "NettyResponse [requestId=" + requestId + ", error=" + error+ ", serialize=" + serialize
                + ", result=" + result + "]";
    }
}
