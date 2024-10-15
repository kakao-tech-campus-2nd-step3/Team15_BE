package kakao.rebit.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import kakao.rebit.member.entity.Role;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface MemberInfo {
    Role[] allowedRoles() default {Role.ROLE_USER, Role.ROLE_ADMIN, Role.ROLE_EDITOR};
}
