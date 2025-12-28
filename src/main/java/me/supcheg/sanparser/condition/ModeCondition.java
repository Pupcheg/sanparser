package me.supcheg.sanparser.condition;

import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
class ModeCondition implements Condition {
    @Override
    public boolean matches(ConditionContext ctx, AnnotatedTypeMetadata metadata) {
        return findMode(metadata)
                .map(mode -> isModeEnabled(mode, getArguments(ctx)))
                .orElse(false);
    }

    private static ApplicationArguments getArguments(ConditionContext ctx) {
        @Nullable BeanFactory beanFactory = ctx.getBeanFactory();
        Objects.requireNonNull(beanFactory, () -> "no bean factory in %s".formatted(ctx));

        return beanFactory.getBean(ApplicationArguments.class);
    }

    private static boolean isModeEnabled(String mode, ApplicationArguments arguments) {
        return arguments.containsOption("mode") && arguments.getOptionValues("mode").contains(mode);
    }

    private static Optional<String> findMode(AnnotatedTypeMetadata metadata) {
        @Nullable Map<String, @Nullable Object> attributes = metadata.getAnnotationAttributes(ConditionalOnMode.class.getName());
        if (attributes == null) {
            return Optional.empty();
        }

        return Optional.ofNullable(attributes.get("mode"))
                .filter(String.class::isInstance)
                .map(String.class::cast);
    }
}
