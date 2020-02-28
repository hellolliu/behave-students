package org.java.behave.admin.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.behave.admin.dao.WxTokenVO;
import org.java.behave.core.config.WxProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class WXTokenJob {
        private final Log logger = LogFactory.getLog(WXTokenJob.class);
        @Autowired
        private WxProperties properties;
        private String tokenUrl="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=";
        /**
         * 自动获取微信TOKEN
         */
        @Scheduled(fixedDelay = 3600 * 1000)
        public void checkOrderUnpaid() {
            {
                SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
                requestFactory.setConnectTimeout(1000);// 设置超时
                requestFactory.setReadTimeout(1000);
                RestTemplate restTemplate=new RestTemplate(requestFactory);
                HttpHeaders httpHeaders=new HttpHeaders();
                HttpMethod method=HttpMethod.GET;

                //以JSON的方式提交
                httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
                //将请求头部和参数合成一个请求
                HttpEntity<MultiValueMap<String,String>> entity=new HttpEntity<>(new LinkedMultiValueMap<>() ,httpHeaders);
                //执行HTTP请求，可将返回的结构使用WxTokenVO类格式化
                try{
                    ResponseEntity<WxTokenVO> responseEntity=restTemplate.exchange(tokenUrl+properties.getAppId()+"&secret="+properties.getAppSecret(),method,entity,WxTokenVO.class);
                    properties.setAccessToken(responseEntity.getBody().getAccess_token());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
}
