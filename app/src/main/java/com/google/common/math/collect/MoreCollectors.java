package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collector;
import javax.annotation.CheckForNull;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public final class MoreCollectors {
    private static final Object NULL_PLACEHOLDER = null;
    private static final Collector<Object, ?, Object> ONLY_ELEMENT = null;
    private static final Collector<Object, ?, Optional<Object>> TO_OPTIONAL = null;

    private static final class ToOptionalState {
        static final int MAX_EXTRAS = 4;

        @CheckForNull
        Object element;
        List<Object> extras;

        ToOptionalState() {
            this.element = null;
            this.extras = Collections.emptyList();
        }

        void add(Object r3) {
            Preconditions.checkNotNull(r3);
            if (this.element != null) goto L6;
            this.element = r3;
            return;
        L6:
            if (this.extras.isEmpty() == false) goto L9;
            this.extras = new ArrayList(4);
            this.extras.add(r3);
            return;
        L9:
            if (this.extras.size() >= 4) goto L13;
            this.extras.add(r3);
            return;
        L13:
            throw multiples(true);
        }

        ToOptionalState combine(ToOptionalState r4) {
            if (this.element != null) goto L6;
            return r4;
        L6:
            if (r4.element != null) goto L9;
            return this;
        L9:
            if (this.extras.isEmpty() == false) goto L11;
            this.extras = new ArrayList();
        L11:
            this.extras.add(r4.element);
            this.extras.addAll(r4.extras);
            if (this.extras.size() > 4) goto L14;
            return this;
        L14:
            this.extras.subList(4, this.extras.size()).clear();
            throw multiples(true);
        }

        Object getElement() {
            if (this.element == null) goto L11;
            if (this.extras.isEmpty() == false) goto L9;
            return this.element;
        L9:
            throw multiples(false);
        L11:
            throw new NoSuchElementException();
        }

        Optional<Object> getOptional() {
            if (this.extras.isEmpty() == false) goto L7;
            return Optional.ofNullable(this.element);
        L7:
            throw multiples(false);
        }

        IllegalArgumentException multiples(boolean r5) {
            StringBuilder r0 = new StringBuilder().append("expected one element but was: <").append(this.element);
            Iterator<Object> r1 = this.extras.iterator();
        L4:
            if (r1.hasNext() == false) goto L6;
            Object r2 = r1.next();
            r0.append(", ").append(r2);
            goto L4
        L6:
            if (r5 == false) goto L8;
            r0.append(", ...");
        L8:
            r0.append(Typography.greater);
            throw new IllegalArgumentException(r0.toString());
        }
    }

    static {
        TO_OPTIONAL = Collector.of(new MoreCollectors$$ExternalSyntheticLambda0(), new MoreCollectors$$ExternalSyntheticLambda1(), new MoreCollectors$$ExternalSyntheticLambda2(), new MoreCollectors$$ExternalSyntheticLambda3(), new Collector.Characteristics[]{Collector.Characteristics.UNORDERED});
        NULL_PLACEHOLDER = new Object();
        ONLY_ELEMENT = Collector.of(new MoreCollectors$$ExternalSyntheticLambda0(), new MoreCollectors$$ExternalSyntheticLambda4(), new MoreCollectors$$ExternalSyntheticLambda2(), new MoreCollectors$$ExternalSyntheticLambda5(), new Collector.Characteristics[]{Collector.Characteristics.UNORDERED});
    }

    private MoreCollectors() {
    }

    static /* synthetic */ void lambda$static$0(ToOptionalState r1, Object r2) {
        if (r2 != null) goto L4;
        Object r0 = NULL_PLACEHOLDER;
    L5:
        r1.add(r0);
        return;
    L4:
        r0 = r2;
        goto L5
    }

    static /* synthetic */ Object lambda$static$1(ToOptionalState r2) {
        Object r0 = r2.getElement();
        if (r0 != NULL_PLACEHOLDER) goto L6;
        return null;
    L6:
        return r0;
    }

    public static <T> Collector<T, ?, T> onlyElement() {
        return (Collector<T, ?, T>) ONLY_ELEMENT;
    }

    public static <T> Collector<T, ?, Optional<T>> toOptional() {
        return (Collector<T, ?, Optional<T>>) TO_OPTIONAL;
    }
}
