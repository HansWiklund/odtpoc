package se.inera.odp;

import static net.logstash.logback.argument.StructuredArguments.keyValue;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Configuration
@EnableWebMvc
/**
 * Required for swagger because of spring.resources.add-mappings=false in application.properties
 * @author hanwik
 *
 */
public class WebConfig  implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/resources/**")
                .addResourceLocations("/resources/");
        registry
                .addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry
                .addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
       registry.addInterceptor(new HandlerInterceptorAdapter() {
          Logger logger = LoggerFactory.getLogger(WebConfig.class);

          @Override
          public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
             if (handler instanceof HandlerMethod) {
                HandlerMethod handlerMethod = (HandlerMethod) handler;
                Method method = handlerMethod.getMethod();
                
                Map<String, Object> req = new HashMap<>();
                req.put("method", request.getMethod());
                req.put("endpoint", request.getRequestURI());
                req.put("remote_address", request.getRemoteAddr());
                req.put("content-type", request.getContentType());
                if(request.getQueryString() != null)
                	req.put("query_string", request.getQueryString());

        		logger.info("request", keyValue("request", req));

        		/*
                logger.info("{} - {} - method '{}' on controller '{}'",
                      request.getMethod(), request.getRequestURI(), method.getName(),
                      handlerMethod.getBean().getClass()
                );
                */
             }
             return true;
          }
       });
    }
}
