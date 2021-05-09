package com.itranswarp.learnjava.mail;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class OnSmtpEnvCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		return "true".equalsIgnoreCase(System.getenv("smtp"));
	}
}
