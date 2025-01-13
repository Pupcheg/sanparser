package me.supcheg.sanparser.unchecked;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class GenericTypeResolver {

    public static <T> Class<T> obtainType(T[] genericTypeArray) {
        if (genericTypeArray.length != 0) {
            throw new IllegalArgumentException("genericTypeArray must be empty");
        }
        @SuppressWarnings("unchecked")
        Class<T> componentType = (Class<T>) genericTypeArray.getClass().getComponentType();
        if (componentType == Object.class) {
            throw new IllegalArgumentException("Got Object type");
        }
        return componentType;
    }

}
