package cn.jzyunqi.common.third.xunfei.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author wiiyaya
 * @date 2018/12/10.
 */
@Getter
@Setter
public class XunfeiBaseResponse<T> implements Serializable {
	private static final long serialVersionUID = -4636245150653020118L;

	/**
	 * 会话ID
	 */
	private String sid;

	/**
	 * 返回结果状态值， 成功返回0
	 */
	private String code;

	/**
	 * 返回结果说明
	 */
	private String desc;

	/**
	 * 返回结果
	 */
	private T data;
}
