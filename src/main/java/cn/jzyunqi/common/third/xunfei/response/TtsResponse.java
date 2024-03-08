package cn.jzyunqi.common.third.xunfei.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2020/9/19.
 */
@Getter
@Setter
public class TtsResponse {

    /**
     * 本次会话的id，只在第一帧请求时返回
     */
    private String sid;

    /**
     * 返回码，0表示成功，其它表示异常
     */
    private Integer code;

    /**
     * 描述信息
     */
    private String message;

    /**
     * 合成详情
     */
    private Data data;

    @Getter
    @Setter
    public static class Data implements Serializable {
        private static final long serialVersionUID = 3716131523884092335L;

        /**
         * 当前音频流状态，0表示开始合成，1表示合成中，2表示合成结束
         */
        private Integer status;

        /**
         * 合成后的音频片段，采用base64编码
         */
        private String audio;

        /**
         * 合成进度，指当前合成文本的字节数
         */
        private String ced;
    }
}
