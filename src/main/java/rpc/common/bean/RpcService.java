package rpc.common.bean;

import java.lang.annotation.*;

/**
 * @author fuqianzhong
 * @date 18/6/6
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface RpcService {
    String serviceKey();
}
