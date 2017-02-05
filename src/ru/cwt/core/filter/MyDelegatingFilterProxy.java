package ru.cwt.core.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.annotation.PostConstruct;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class MyDelegatingFilterProxy extends DelegatingFilterProxy {
	private static final Logger log = LoggerFactory.getLogger(MyDelegatingFilterProxy.class);

	@PostConstruct
	private void init() {
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
		try {
			if (request instanceof HttpServletRequest) {
				log.info("Request:" + ((HttpServletRequest) request).getRequestURL().toString());
			}
			super.doFilter(request, response, chain);
		} catch (IOException | ServletException e) {
			log.error("Exception", e);
		}
	}
}
