/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import static org.springframework.util.Assert.notNull;

@Aspect
public class YouShallNotPassNullInParameters {
    @Before("it.haslearnt.aspects.Pointcuts.allPublicMethodsExceptSettersAndDtos()")
    public void throwExceptionIfAnyParameterIsNull(JoinPoint joinPoint) {
        for(Object argument : joinPoint.getArgs()) {
            notNull(argument);
        }
    }
}
