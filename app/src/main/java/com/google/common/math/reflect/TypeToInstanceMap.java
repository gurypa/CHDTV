package com.google.common.reflect;

import com.google.errorprone.annotations.DoNotMock;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@DoNotMock("Use ImmutableTypeToInstanceMap or MutableTypeToInstanceMap")
@ElementTypesAreNonnullByDefault
public interface TypeToInstanceMap<B> extends Map<TypeToken<? extends B>, B> {
    @CheckForNull
    <T extends B> T getInstance(TypeToken<T> r1);

    @CheckForNull
    <T extends B> T getInstance(Class<T> r1);

    @CheckForNull
    <T extends B> T putInstance(TypeToken<T> r1, @ParametricNullness T r2);

    @CheckForNull
    <T extends B> T putInstance(Class<T> r1, @ParametricNullness T r2);
}
