package cn.jzyunqi.common.third.xunfei.client;

import cn.jzyunqi.common.exception.BusinessException;
import cn.jzyunqi.common.third.xunfei.model.HandWritingDto;
import cn.jzyunqi.common.third.xunfei.response.XunfeiBaseResponse;
import cn.jzyunqi.common.utils.CollectionUtilPlus;
import cn.jzyunqi.common.utils.DigestUtilPlus;
import cn.jzyunqi.common.utils.StringUtilPlus;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.core5.net.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author wiiyaya
 * @date 2020/9/18.
 */
@Slf4j
public class XunfeiOcrClient {

    /**
     * 手写文字识别
     */
    private static final String HAND_WRITING = "https://webapi.xfyun.cn/v1/service/v1/ocr/handwriting";

    /**
     * 第三方用户唯一凭证
     */
    private String appId;

    /**
     * 第三方用户唯一接口凭证
     */
    private String apiKey;

    private RestTemplate restTemplate;

    public XunfeiOcrClient(String appId, String apiKey, RestTemplate restTemplate) {
        this.appId = appId;
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;

        restTemplate.setMessageConverters(this.prepareFeatureConverter());
    }

    public HandWritingDto handWriting(String language, String base64File)throws BusinessException {
        XunfeiBaseResponse<HandWritingDto> schedulePageRsp;
        try {
            String currTime = String.valueOf(System.currentTimeMillis() / 1000L);
            String param = DigestUtilPlus.Base64.encodeBase64String(String.format("{\"language\":\"%s\"}", language).getBytes(StringUtilPlus.UTF_8));
            String checkSum = DigestUtilPlus.MD5.sign(apiKey + currTime + param, Boolean.FALSE);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(new MediaType(MediaType.APPLICATION_FORM_URLENCODED, StringUtilPlus.UTF_8));
            httpHeaders.set("X-Param", param);
            httpHeaders.set("X-CurTime", currTime);
            httpHeaders.set("X-CheckSum", checkSum);
            httpHeaders.set("X-Appid", appId);

            URI findScheduleUri = new URIBuilder(HAND_WRITING).build();

            MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
            params.add("image", base64File);

            RequestEntity<MultiValueMap<String, Object>> requestEntity = new RequestEntity<>(params, httpHeaders, HttpMethod.POST, findScheduleUri);
            ParameterizedTypeReference<XunfeiBaseResponse<HandWritingDto>> responseType = new ParameterizedTypeReference<XunfeiBaseResponse<HandWritingDto>>() {};
            ResponseEntity<XunfeiBaseResponse<HandWritingDto>> responseEntity  = restTemplate.exchange(requestEntity, responseType);
            schedulePageRsp = Optional.ofNullable(responseEntity.getBody()).orElseGet(XunfeiBaseResponse::new);
        }catch (Exception e) {
            log.error("======XunfeiOcrHelper handWriting error:", e);
            throw new BusinessException("common_error_xf_hand_writing_error");
        }

        if(schedulePageRsp.getCode().equals("0")){
            return schedulePageRsp.getData();
        }else{
            log.error("======XunfeiOcrHelper handWriting 200 error[{}][{}][{}]", schedulePageRsp.getSid(), schedulePageRsp.getCode(), schedulePageRsp.getDesc());
            throw new BusinessException("common_error_xf_hand_writing_failed");
        }
    }


    private List<HttpMessageConverter<?>> prepareFeatureConverter(){
        ObjectMapper om = Jackson2ObjectMapperBuilder.json().build();
        om.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, Boolean.TRUE);

        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter(om);
        jsonConverter.setSupportedMediaTypes(CollectionUtilPlus.Array.asList(MediaType.TEXT_PLAIN));

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        messageConverters.add(new AllEncompassingFormHttpMessageConverter()); //请求用
        messageConverters.add(jsonConverter); //返回用

        return messageConverters;
    }
}
