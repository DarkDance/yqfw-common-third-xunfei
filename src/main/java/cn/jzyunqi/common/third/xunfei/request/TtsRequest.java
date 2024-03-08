package cn.jzyunqi.common.third.xunfei.request;

import cn.jzyunqi.common.third.xunfei.enums.TextEncode;
import cn.jzyunqi.common.third.xunfei.enums.VoiceName;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2020/9/19.
 */
@Getter
@Setter
public class TtsRequest implements Serializable {
    private static final long serialVersionUID = -1237238869158649671L;

    private Common common;

    private Business business;

    private Data data;

    @Getter
    @Setter
    public static class Common implements Serializable {
        private static final long serialVersionUID = -3903524867362908079L;

        /**
         * 第三方用户唯一凭证
         */
        @JsonProperty("app_id")
        private String appId;
    }

    @Getter
    @Setter
    public static class Business implements Serializable {
        private static final long serialVersionUID = -4462211428342501483L;

        /**
         * 音频编码
         */
        private String aue;

        /**
         * 是否开启流式返回
         */
        private Integer sfl;

        /**
         * 音频采样率
         */
        private String auf;

        /**
         * 发音人
         */
        private VoiceName vcn;

        /**
         * 语速，可选值：[0-100]，默认为50
         */
        private Integer speed;

        /**
         * 音量，可选值：[0-100]，默认为50
         */
        private Integer volume;

        /**
         * 音高，可选值：[0-100]，默认为50
         */
        private Integer pitch;

        /**
         * 是否有合成音频的背景音
         * 0:无背景音（默认值）
         * 1:有背景音
         */
        private Integer bgs;

        /**
         * 文本编码格式
         */
        private TextEncode tte;

        /**
         * 英文发音方式
         * 0：自动判断处理，如果不确定将按照英文词语拼写处理（缺省）
         * 1：所有英文按字母发音
         * 2：自动判断处理，如果不确定将按照字母朗读
         */
        private String reg;

        /**
         * 合成音频数字发音方式
         * 0：自动判断（默认值）
         * 1：完全数值
         * 2：完全字符串
         * 3：字符串优先
         */
        private String rdn;

    }

    @Getter
    @Setter
    public static class Data implements Serializable {
        private static final long serialVersionUID = 9200332793815220612L;

        /**
         * 数据状态，固定为2
         */
        private Integer status;

        /**
         * 文本内容，需进行base64编码；
         * base64编码前最大长度需小于8000字节，约2000汉字
         */
        private String text;
    }
}
