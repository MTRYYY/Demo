package com.stu.config;

import com.stu.filter.My2Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean setFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean( );
        filterRegistrationBean.setFilter(new My2Filter());
        filterRegistrationBean.setName("my2Filter");
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.setOrder(1);
        return  filterRegistrationBean;
    }
}
