/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.aspects;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Pointcuts {
    @Pointcut("execution(public * it.haslearnt.*..*.*(..)) && !execution(public * it.haslearnt.*..*Dto.*(..)) && !execution(public * it.haslearnt..*..*.set*(..))\"")
    public void  allPublicMethodsExceptSettersAndDtos() {}
}
