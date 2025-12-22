package cn.jzyunqi.common.third.xunfei.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.xunfei.enums.AudioEncode;
import cn.jzyunqi.common.third.xunfei.enums.AudioSampleRate;
import cn.jzyunqi.common.third.xunfei.enums.TextEncode;
import cn.jzyunqi.common.third.xunfei.enums.VoiceName;
import cn.jzyunqi.common.third.xunfei.model.XunfeiWssToken;
import cn.jzyunqi.common.third.xunfei.request.TtsRequest;
import cn.jzyunqi.common.third.xunfei.response.TtsResponse;
import cn.jzyunqi.common.utils.DateTimeUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.time.LocalDateTime;

/**
 * @author wiiyaya
 * @date 2020/9/19.
 */
@Slf4j
public class XunfeiTtsClient {

    private static final String HOST = "tts-api.xfyun.cn";
    private static final String PATH = "/v2/tts";
    private static final String SIGN_TEMPLATE = "host: %s\ndate: %s\nGET %s HTTP/1.1";
    private static final String AUTH_TEMPLATE = "api_key=\"%s\", algorithm=\"hmac-sha256\", headers=\"host date request-line\", signature=\"%s\"";

    /**
     * 第三方用户唯一凭证
     */
    private String appId;

    /**
     * 第三方用户唯一接口凭证
     */
    private String apiKey;

    /**
     * 第三方用户唯一接口凭证密钥
     */
    private String apiSecret;

    private JsonMapper objectMapper;

    public XunfeiTtsClient(String appId, String apiKey, String apiSecret) {
        this.appId = appId;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.objectMapper = JsonMapper.builder()
                .changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_NULL))
                .build();
    }

    /**
     * 获取WSS授权地址
     */
    public XunfeiWssToken getWebSocketURL() throws BusinessException {
        URI uri;
        try {
            String date = LocalDateTime.now(DateTimeUtilPlus.UTC0_ZONE_REGION).format(DateTimeUtilPlus.GMT0_DATE_FORMAT);

            String waitSign = String.format(SIGN_TEMPLATE, HOST, date, PATH);
            String sign = DigestUtilPlus.Mac.sign(waitSign, apiSecret, DigestUtilPlus.MacAlgo.H_SHA256, Boolean.TRUE);

            String authorization = String.format(AUTH_TEMPLATE, apiKey, sign);


            uri = new URIBuilder("wss://" + HOST + PATH)
                    .addParameter("authorization", DigestUtilPlus.Base64.encodeBase64String(authorization.getBytes(StringUtilPlus.UTF_8)))
                    .addParameter("date", date)
                    .addParameter("host", HOST)
                    .build();
        } catch (Exception e) {
            log.error("======XunfeiTtsHelper getWebSocketURL error:", e);
            throw new BusinessException("common_error_xf_get_web_socket_url_error");
        }

        XunfeiWssToken xunfeiWssToken = new XunfeiWssToken();
        xunfeiWssToken.setAppId(appId);
        xunfeiWssToken.setUrl(uri.toString());
        return xunfeiWssToken;
    }

    /**
     * 生成WSS请求对象
     *
     * @param word 带翻译字符串
     */
    public String generateRequestDto(String word) throws BusinessException {
        return generateRequestDto(AudioEncode.RAW, AudioSampleRate.RATE_16000, 14, 70, 18, Boolean.FALSE, word);
    }

    /**
     * 生成WSS请求对象
     *
     * @param encode 音频编码
     * @param rate 音频采样率
     * @param word 带翻译字符串
     */
    public String generateRequestDto(AudioEncode encode, AudioSampleRate rate, Integer speed, Integer volume, Integer pitch, boolean hasBgs, String word) throws BusinessException {
        TtsRequest.Common common = new TtsRequest.Common();
        common.setAppId(appId);

        TtsRequest.Business business = new TtsRequest.Business();
        business.setAue(encode.getMode());
        if(encode == AudioEncode.LAME){
            business.setSfl(1);
        }
        business.setAuf(rate.getRate());
        business.setVcn(VoiceName.xiaoyan);
        business.setSpeed(speed);
        business.setVolume(volume);
        business.setPitch(pitch);
        business.setBgs(hasBgs ? 1 : 0);
        business.setTte(TextEncode.UTF8);
        //business.setReg();
        //business.setRdn();

        TtsRequest.Data data = new TtsRequest.Data();
        data.setStatus(2);
        data.setText(DigestUtilPlus.Base64.encodeBase64String(word.getBytes(StringUtilPlus.UTF_8)));

        TtsRequest ttsRequest = new TtsRequest();
        ttsRequest.setCommon(common);
        ttsRequest.setBusiness(business);
        ttsRequest.setData(data);

        try {
            return objectMapper.writeValueAsString(ttsRequest);
        } catch (JacksonException e) {
            log.error("======XunfeiTtsHelper generateRequestDto error:", e);
            throw new BusinessException("common_error_xf_generate_request_dto_error");
        }
    }

    /**
     * 翻译返回值
     *
     * @param data 待翻译数据
     */
    public TtsResponse parserResponseDto(String data) throws BusinessException {
        try {
            return objectMapper.readValue(data, TtsResponse.class);
        } catch (JacksonException e) {
            log.error("======XunfeiTtsHelper parserResponseDto error:", e);
            throw new BusinessException("common_error_xf_parser_response_dto_error");
        }
    }
}
