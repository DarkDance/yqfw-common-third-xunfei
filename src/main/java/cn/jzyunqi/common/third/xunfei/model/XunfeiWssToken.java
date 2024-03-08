package cn.jzyunqi.common.third.xunfei.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2020/9/19.
 */
@Getter
@Setter
public class XunfeiWssToken implements Serializable {
    private static final long serialVersionUID = 2895492477683884307L;

    /**
     * 第三方用户唯一凭证
     */
    private String appId;

    /**
     * 访问web socket地址
     */
    private String url;
}
