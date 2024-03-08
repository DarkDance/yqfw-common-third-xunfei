package cn.jzyunqi.common.third.xunfei.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * @author wiiyaya
 * @date 2020/9/18.
 */
@Getter
@Setter
public class HandWritingDto implements Serializable {
    private static final long serialVersionUID = 7297952987025908486L;

    /**
     * 区域块信息
     */
    private List<HwBlock> block;

    @Getter
    @Setter
    public static class HwBlock implements Serializable {
        private static final long serialVersionUID = 1972698971273361416L;

        /**
         * 区域块类型（text-文本，image-图片）
         */
        private String type;

        /**
         * 行信息
         */
        private List<HwLine> line;

    }

    @Getter
    @Setter
    public static class HwLine implements Serializable {
        private static final long serialVersionUID = 4826230611727647271L;

        /**
         * 后验概率
         */
        private Float confidence;

        private List<HwWord> word;
    }

    @Getter
    @Setter
    public static class HwWord implements Serializable {
        private static final long serialVersionUID = 6405321094971538508L;

        /**
         * 内容
         */
        private String content;
    }
}
