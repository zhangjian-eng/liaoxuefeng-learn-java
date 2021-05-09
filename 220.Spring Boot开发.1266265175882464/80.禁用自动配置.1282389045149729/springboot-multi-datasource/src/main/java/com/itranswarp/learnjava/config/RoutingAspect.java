package com.itranswarp.learnjava.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RoutingAspect {
	@Around("@annotation(routingWithSlave)")
	public Object routingWithDataSource(ProceedingJoinPoint joinPoint, RoutingWithSlave routingWithSlave)
			throws Throwable {
		try (RoutingDataSourceContext ctx = new RoutingDataSourceContext(RoutingDataSourceContext.SLAVE_DATASOURCE)) {
			return joinPoint.proceed();
		}
	}
}
