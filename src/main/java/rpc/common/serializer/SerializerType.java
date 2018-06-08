package rpc.common.serializer;

import org.springframework.util.StringUtils;

/**
 * @author fuqianzhong
 * @date 18/5/16
 */
public enum SerializerType {
    JAVA((byte) 1, "java"),
    HESSIAN((byte) 2, "hessian"),
    PROTOSTUFF((byte) 3, "protostuff"),
    JACKSON((byte) 4, "jackson")
    ;

    private byte code;
    private String name;

    SerializerType(byte code, String name) {
        this.code = code;
        this.name = name;
    }

    public static SerializerType getSerializerType(String name){
        SerializerType type = null;
        if(!StringUtils.isEmpty(name)) {
            for (SerializerType serializerType : SerializerType.values()) {
                if (name.equals(serializerType.getName())) {
                    type = serializerType;
                }
            }
        }
        return type;
    }

    public static SerializerType getSerializerType(byte code) {
        switch (code) {
            case 1:
                return JAVA;
            case 2:
                return HESSIAN;
            default:
                throw new IllegalArgumentException("invalid serializerType code: " + code);

        }
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
