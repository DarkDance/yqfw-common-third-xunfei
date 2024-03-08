package cn.jzyunqi.common.third.xunfei.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author wiiyaya
 * @date 2020/9/19.
 */
@Getter
@AllArgsConstructor
public enum AudioEncode {

    /**
     * 未压缩的pcm
     */
    RAW("raw"),

    /**
     * mp3 (当aue=lame时需传参sfl=1)
     */
    LAME("lame"),

    /**
     * 标准开源speex（for speex_wideband，即16k）数字代表指定压缩等级（默认等级为8）
     */
    SPEEX_ORG_WB7("speex-org-wb;7"),

    /**
     * 标准开源speex（for speex_narrowband，即8k）数字代表指定压缩等级（默认等级为8）
     */
    SPEEX_ORG_NB7("speex-org-nb;7"),

    /**
     * 压缩格式，压缩等级1~10，默认为7（8k讯飞定制speex）
     */
    SPEEX7("speex;7"),

    /**
     * 压缩格式，压缩等级1~10，默认为7（16k讯飞定制speex）
     */
    SPEEX_WB7("speex-wb;7"),
    ;

    private String mode;

}
