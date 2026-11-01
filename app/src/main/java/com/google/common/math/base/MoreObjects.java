package com.google.common.base;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class MoreObjects {

    /* JADX INFO: renamed from: com.google.common.base.MoreObjects$1 */
    static /* synthetic */ class C05421 {
    }

    public static final class ToStringHelper {
        private final String className;
        private final ValueHolder holderHead;
        private ValueHolder holderTail;
        private boolean omitEmptyValues;
        private boolean omitNullValues;

        private static final class UnconditionalValueHolder extends ValueHolder {
            private UnconditionalValueHolder() {
            }

            /* synthetic */ UnconditionalValueHolder(C05421 r1) {
                this();
            }
        }

        static class ValueHolder {

            @CheckForNull
            String name;

            @CheckForNull
            ValueHolder next;

            @CheckForNull
            Object value;

            ValueHolder() {
            }
        }

        private ToStringHelper(String r2) {
            this.holderHead = new ValueHolder();
            this.holderTail = this.holderHead;
            this.omitNullValues = false;
            this.omitEmptyValues = false;
            this.className = (String) Preconditions.checkNotNull(r2);
        }

        /* synthetic */ ToStringHelper(String r1, C05421 r2) {
            this(r1);
        }

        private ValueHolder addHolder() {
            ValueHolder r0 = new ValueHolder();
            this.holderTail.next = r0;
            this.holderTail = r0;
            return r0;
        }

        private ToStringHelper addHolder(@CheckForNull Object r2) {
            addHolder().value = r2;
            return this;
        }

        private ToStringHelper addHolder(String r3, @CheckForNull Object r4) {
            ValueHolder r0 = addHolder();
            r0.value = r4;
            r0.name = (String) Preconditions.checkNotNull(r3);
            return this;
        }

        private UnconditionalValueHolder addUnconditionalHolder() {
            UnconditionalValueHolder r0 = new UnconditionalValueHolder(null);
            this.holderTail.next = r0;
            this.holderTail = r0;
            return r0;
        }

        private ToStringHelper addUnconditionalHolder(Object r2) {
            addUnconditionalHolder().value = r2;
            return this;
        }

        private ToStringHelper addUnconditionalHolder(String r3, Object r4) {
            UnconditionalValueHolder r0 = addUnconditionalHolder();
            r0.value = r4;
            r0.name = (String) Preconditions.checkNotNull(r3);
            return this;
        }

        private static boolean isEmpty(Object r3) {
            if ((r3 instanceof CharSequence) == false) goto L10;
            if (((CharSequence) r3).length() != 0) goto L8;
            return true;
        L8:
            return false;
        L10:
            if ((r3 instanceof Collection) == false) goto L14;
            return ((Collection) r3).isEmpty();
        L14:
            if ((r3 instanceof Map) == false) goto L18;
            return ((Map) r3).isEmpty();
        L18:
            if ((r3 instanceof Optional) == false) goto L22;
            return !((Optional) r3).isPresent();
        L22:
            if (r3.getClass().isArray() == true) goto L24;
            return false;
        L24:
            if (Array.getLength(r3) != 0) goto L27;
            return true;
        L27:
            return false;
        }

        public ToStringHelper add(String r2, char r3) {
            return addUnconditionalHolder(r2, String.valueOf(r3));
        }

        public ToStringHelper add(String r2, double r3) {
            return addUnconditionalHolder(r2, String.valueOf(r3));
        }

        public ToStringHelper add(String r2, float r3) {
            return addUnconditionalHolder(r2, String.valueOf(r3));
        }

        public ToStringHelper add(String r2, int r3) {
            return addUnconditionalHolder(r2, String.valueOf(r3));
        }

        public ToStringHelper add(String r2, long r3) {
            return addUnconditionalHolder(r2, String.valueOf(r3));
        }

        public ToStringHelper add(String r2, @CheckForNull Object r3) {
            return addHolder(r2, r3);
        }

        public ToStringHelper add(String r2, boolean r3) {
            return addUnconditionalHolder(r2, String.valueOf(r3));
        }

        public ToStringHelper addValue(char r2) {
            return addUnconditionalHolder(String.valueOf(r2));
        }

        public ToStringHelper addValue(double r2) {
            return addUnconditionalHolder(String.valueOf(r2));
        }

        public ToStringHelper addValue(float r2) {
            return addUnconditionalHolder(String.valueOf(r2));
        }

        public ToStringHelper addValue(int r2) {
            return addUnconditionalHolder(String.valueOf(r2));
        }

        public ToStringHelper addValue(long r2) {
            return addUnconditionalHolder(String.valueOf(r2));
        }

        public ToStringHelper addValue(@CheckForNull Object r2) {
            return addHolder(r2);
        }

        public ToStringHelper addValue(boolean r2) {
            return addUnconditionalHolder(String.valueOf(r2));
        }

        public ToStringHelper omitNullValues() {
            this.omitNullValues = true;
            return this;
        }

        public String toString() {
            boolean r0 = this.omitNullValues;
            boolean r1 = this.omitEmptyValues;
            String r2 = "";
            StringBuilder r3 = new StringBuilder(32).append(this.className).append('{');
            ValueHolder r4 = this.holderHead.next;
        L3:
            if (r4 == null) goto L22;
            Object r5 = r4.value;
            if ((r4 instanceof UnconditionalValueHolder) == true) goto L12;
            if (r5 != null) goto L9;
            if (r0 == false) goto L12;
        L20:
            r4 = r4.next;
            goto L3
        L9:
            if (r1 == false) goto L12;
            if (isEmpty(r5) == true) goto L20;
        L12:
            r3.append(r2);
            r2 = ", ";
            if (r4.name == null) goto L15;
            r3.append(r4.name).append('=');
        L15:
            if (r5 != null) goto L17;
        L19:
            r3.append(r5);
            goto L20
        L17:
            if (r5.getClass().isArray() == false) goto L19;
            String r8 = Arrays.deepToString(new Object[]{r5});
            r3.append(r8, 1, r8.length() - 1);
            goto L20
        L22:
            return r3.append('}').toString();
        }
    }

    private MoreObjects() {
    }

    public static <T> T firstNonNull(@CheckForNull T r2, @CheckForNull T r3) {
        if (r2 == null) goto L4;
        return r2;
    L4:
        if (r3 == null) goto L7;
        return r3;
    L7:
        throw new NullPointerException("Both parameters are null");
    }

    public static ToStringHelper toStringHelper(Class<?> r3) {
        return new ToStringHelper(r3.getSimpleName(), null);
    }

    public static ToStringHelper toStringHelper(Object r3) {
        return new ToStringHelper(r3.getClass().getSimpleName(), null);
    }

    public static ToStringHelper toStringHelper(String r2) {
        return new ToStringHelper(r2, null);
    }
}
