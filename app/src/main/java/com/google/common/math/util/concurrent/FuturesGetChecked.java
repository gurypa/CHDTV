package com.google.common.util.concurrent;

import com.google.common.base.Preconditions;
import com.google.common.collect.Ordering;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
final class FuturesGetChecked {
    private static final Ordering<List<Class<?>>> ORDERING_BY_CONSTRUCTOR_PARAMETER_LIST = null;
    private static final Ordering<Constructor<?>> WITH_STRING_PARAM_THEN_WITH_THROWABLE_PARAM = null;

    interface GetCheckedTypeValidator {
        void validateClass(Class<? extends Exception> r1);
    }

    static class GetCheckedTypeValidatorHolder {
        static final GetCheckedTypeValidator BEST_VALIDATOR = null;

        static final enum WeakSetValidator extends Enum<WeakSetValidator> implements GetCheckedTypeValidator {
            private static final /* synthetic */ WeakSetValidator[] $VALUES = null;
            public static final WeakSetValidator INSTANCE = null;
            private static final Set<WeakReference<Class<? extends Exception>>> validClasses = null;

            private static /* synthetic */ WeakSetValidator[] $values() {
                return new WeakSetValidator[]{INSTANCE};
            }

            static {
                INSTANCE = new WeakSetValidator("INSTANCE", 0);
                $VALUES = $values();
                validClasses = new CopyOnWriteArraySet();
            }

            private WeakSetValidator(String r1, int r2) {
                super(r1, r2);
            }

            public static WeakSetValidator valueOf(String r1) {
                return (WeakSetValidator) Enum.valueOf(WeakSetValidator.class, r1);
            }

            public static WeakSetValidator[] values() {
                return (WeakSetValidator[]) $VALUES.clone();
            }

            @Override // com.google.common.util.concurrent.FuturesGetChecked.GetCheckedTypeValidator
            public void validateClass(Class<? extends Exception> r4) {
                Iterator<WeakReference<Class<? extends Exception>>> r0 = validClasses.iterator();
            L4:
                if (r0.hasNext() == false) goto L9;
                if (r4.equals(r0.next().get()) == false) goto L4;
                return;
            L9:
                FuturesGetChecked.checkExceptionClassValidity(r4);
                if (validClasses.size() <= 1000) goto L12;
                validClasses.clear();
            L12:
                validClasses.add(new WeakReference(r4));
            }
        }

        static {
            BEST_VALIDATOR = getBestValidator();
        }

        GetCheckedTypeValidatorHolder() {
        }

        static GetCheckedTypeValidator getBestValidator() {
            return FuturesGetChecked.weakSetValidator();
        }
    }

    static {
        ORDERING_BY_CONSTRUCTOR_PARAMETER_LIST = Ordering.natural().onResultOf(new FuturesGetChecked$$ExternalSyntheticLambda0()).compound(Ordering.natural().onResultOf(new FuturesGetChecked$$ExternalSyntheticLambda1())).reverse();
        WITH_STRING_PARAM_THEN_WITH_THROWABLE_PARAM = ORDERING_BY_CONSTRUCTOR_PARAMETER_LIST.onResultOf(new FuturesGetChecked$$ExternalSyntheticLambda2());
    }

    private FuturesGetChecked() {
    }

    private static GetCheckedTypeValidator bestGetCheckedTypeValidator() {
        return GetCheckedTypeValidatorHolder.BEST_VALIDATOR;
    }

    static void checkExceptionClassValidity(Class<? extends Exception> r2) {
        Preconditions.checkArgument(isCheckedException(r2), "Futures.getChecked exception type (%s) must not be a RuntimeException", r2);
        Preconditions.checkArgument(hasConstructorUsableByGetChecked(r2), "Futures.getChecked exception type (%s) must be an accessible class with an accessible constructor whose parameters (if any) must be of type String and/or Throwable", r2);
    }

    @ParametricNullness
    static <V, X extends Exception> V getChecked(GetCheckedTypeValidator r2, Future<V> r3, Class<X> r4) throws Exception {
        r2.validateClass(r4);
        return r3.get();
    L8:
        e = move-exception;
        Thread.currentThread().interrupt();
        throw newWithCause(r4, e);
    L5:
        e = move-exception;
        wrapAndThrowExceptionOrError(e.getCause(), r4);
        throw new AssertionError();
    }

    @ParametricNullness
    static <V, X extends Exception> V getChecked(Future<V> r1, Class<X> r2) throws Exception {
        return (V) getChecked(bestGetCheckedTypeValidator(), r1, r2);
    }

    @ParametricNullness
    static <V, X extends Exception> V getChecked(Future<V> r2, Class<X> r3, long r4, TimeUnit r6) throws Exception {
        bestGetCheckedTypeValidator().validateClass(r3);
        return r2.get(r4, r6);
    L11:
        e = move-exception;
        Thread.currentThread().interrupt();
        throw newWithCause(r3, e);
    L5:
        e = move-exception;
        wrapAndThrowExceptionOrError(e.getCause(), r3);
        throw new AssertionError();
    L8:
        e = move-exception;
        throw newWithCause(r3, e);
    }

    private static boolean hasConstructorUsableByGetChecked(Class<? extends Exception> r2) {
        newWithCause(r2, new Exception());     // Catch: Throwable -> L5
        return true;
    L6:
        return false;
    }

    static boolean isCheckedException(Class<? extends Exception> r1) {
        return !RuntimeException.class.isAssignableFrom(r1);
    }

    static /* synthetic */ Comparable lambda$static$0(List r1) {
        return Boolean.valueOf(r1.contains(String.class));
    }

    static /* synthetic */ Comparable lambda$static$1(List r1) {
        return Boolean.valueOf(r1.contains(Throwable.class));
    }

    static /* synthetic */ List lambda$static$2(Constructor r1) {
        return Arrays.asList(r1.getParameterTypes());
    }

    @CheckForNull
    private static <X> X newFromConstructor(Constructor<X> r6, Throwable r7) {
        Class<?>[] r0 = r6.getParameterTypes();
        Object[] r1 = new Object[r0.length];
        int r2 = 0;
    L4:
        if (r2 >= r0.length) goto L23;
        Class<?> r3 = r0[r2];
        if (r3.equals(String.class) == false) goto L9;
        r1[r2] = r7.toString();
    L11:
        r2 = r2 + 1;
        goto L4
    L9:
        if (r3.equals(Throwable.class) == false) goto L12;
        r1[r2] = r7;
        goto L11
    L12:
        return null;
    L23:
        return r6.newInstance(r1);
    L22:
        return null;
    }

    private static <X extends Exception> X newWithCause(Class<X> r4, Throwable r5) {
        Iterator r1 = preferringStringsThenThrowables(Arrays.asList(r4.getConstructors())).iterator();
    L4:
        if (r1.hasNext() == false) goto L13;
        X r3 = (X) newFromConstructor((Constructor) r1.next(), r5);
        if (r3 == null) goto L4;
        if (r3.getCause() != null) goto L10;
        r3.initCause(r5);
    L10:
        return r3;
    L13:
        throw new IllegalArgumentException("No appropriate constructor for exception of type " + r4 + " in response to chained exception", r5);
    }

    private static <X extends Exception> List<Constructor<X>> preferringStringsThenThrowables(List<Constructor<X>> r1) {
        return (List<Constructor<X>>) WITH_STRING_PARAM_THEN_WITH_THROWABLE_PARAM.sortedCopy(r1);
    }

    static GetCheckedTypeValidator weakSetValidator() {
        return GetCheckedTypeValidatorHolder.WeakSetValidator.INSTANCE;
    }

    private static <X extends Exception> void wrapAndThrowExceptionOrError(Throwable r2, Class<X> r3) throws Exception {
        if ((r2 instanceof Error) == true) goto L11;
        if ((r2 instanceof RuntimeException) == false) goto L9;
        throw new UncheckedExecutionException(r2);
    L9:
        throw newWithCause(r3, r2);
    L11:
        throw new ExecutionError((Error) r2);
    }
}
