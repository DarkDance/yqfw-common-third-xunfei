package cn.jzyunqi.common.third.xunfei.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @date 2020/9/19.
 */
@Getter
@AllArgsConstructor
public enum AudioSampleRate {

    /**
     * 合成8K 的音频
     */
    RATE_8000("audio/L16;rate=8000"),

    /**
     * 合成16K 的音频
     */
    RATE_16000("audio/L16;rate=16000"),
    ;

    private String rate;
}
