package com.google.common.base;

import com.google.errorprone.annotations.CheckReturnValue;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.io.Serializable;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class Converter<A, B> implements Function<A, B> {
    private final boolean handleNullAutomatically;

    @CheckForNull
    @LazyInit
    private transient Converter<B, A> reverse;

    private static final class ConverterComposition<A, B, C> extends Converter<A, C> implements Serializable {
        private static final long serialVersionUID = 0;
        final Converter<A, B> first;
        final Converter<B, C> second;

        ConverterComposition(Converter<A, B> r1, Converter<B, C> r2) {
            this.first = r1;
            this.second = r2;
        }

        @Override // com.google.common.base.Converter
        @CheckForNull
        A correctedDoBackward(@CheckForNull C r3) {
            return (A) this.first.correctedDoBackward(this.second.correctedDoBackward(r3));
        }

        @Override // com.google.common.base.Converter
        @CheckForNull
        C correctedDoForward(@CheckForNull A r3) {
            return (C) this.second.correctedDoForward(this.first.correctedDoForward(r3));
        }

        @Override // com.google.common.base.Converter
        protected A doBackward(C r2) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        protected C doForward(A r2) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof ConverterComposition) == false) goto L10;
            ConverterComposition r0 = (ConverterComposition) r5;
            if (this.first.equals(r0.first) == true) goto L7;
            return false;
        L7:
            if (this.second.equals(r0.second) == false) goto L12;
            return true;
        L12:
            return false;
        L10:
            return false;
        }

        public int hashCode() {
            return (this.first.hashCode() * 31) + this.second.hashCode();
        }

        public String toString() {
            return this.first + ".andThen(" + this.second + ")";
        }
    }

    private static final class FunctionBasedConverter<A, B> extends Converter<A, B> implements Serializable {
        private final Function<? super B, ? extends A> backwardFunction;
        private final Function<? super A, ? extends B> forwardFunction;

        private FunctionBasedConverter(Function<? super A, ? extends B> r2, Function<? super B, ? extends A> r3) {
            this.forwardFunction = (Function) Preconditions.checkNotNull(r2);
            this.backwardFunction = (Function) Preconditions.checkNotNull(r3);
        }

        /* synthetic */ FunctionBasedConverter(Function r1, Function r2, C05361 r3) {
            this(r1, r2);
        }

        @Override // com.google.common.base.Converter
        protected A doBackward(B r2) {
            return this.backwardFunction.apply(r2);
        }

        @Override // com.google.common.base.Converter
        protected B doForward(A r2) {
            return this.forwardFunction.apply(r2);
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(@CheckForNull Object r5) {
            if ((r5 instanceof FunctionBasedConverter) == false) goto L11;
            FunctionBasedConverter r0 = (FunctionBasedConverter) r5;
            if (this.forwardFunction.equals(r0.forwardFunction) == true) goto L7;
            return false;
        L7:
            if (this.backwardFunction.equals(r0.backwardFunction) == false) goto L13;
            return true;
        L13:
            return false;
        L11:
            return false;
        }

        public int hashCode() {
            return (this.forwardFunction.hashCode() * 31) + this.backwardFunction.hashCode();
        }

        public String toString() {
            return "Converter.from(" + this.forwardFunction + ", " + this.backwardFunction + ")";
        }
    }

    private static final class IdentityConverter<T> extends Converter<T, T> implements Serializable {
        static final Converter<?, ?> INSTANCE = null;
        private static final long serialVersionUID = 0;

        static {
            INSTANCE = new IdentityConverter();
        }

        private IdentityConverter() {
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.base.Converter
        <S> Converter<T, S> doAndThen(Converter<T, S> r2) {
            return (Converter) Preconditions.checkNotNull(r2, "otherConverter");
        }

        @Override // com.google.common.base.Converter
        protected T doBackward(T r1) {
            return r1;
        }

        @Override // com.google.common.base.Converter
        protected T doForward(T r1) {
            return r1;
        }

        @Override // com.google.common.base.Converter
        public IdentityConverter<T> reverse() {
            return this;
        }

        @Override // com.google.common.base.Converter
        public /* bridge */ /* synthetic */ Converter reverse() {
            return reverse();
        }

        public String toString() {
            return "Converter.identity()";
        }
    }

    private static final class ReverseConverter<A, B> extends Converter<B, A> implements Serializable {
        private static final long serialVersionUID = 0;
        final Converter<A, B> original;

        ReverseConverter(Converter<A, B> r1) {
            this.original = r1;
        }

        @Override // com.google.common.base.Converter
        @CheckForNull
        B correctedDoBackward(@CheckForNull A r2) {
            return this.original.correctedDoForward(r2);
        }

        @Override // com.google.common.base.Converter
        @CheckForNull
        A correctedDoForward(@CheckForNull B r2) {
            return this.original.correctedDoBackward(r2);
        }

        @Override // com.google.common.base.Converter
        protected B doBackward(A r2) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter
        protected A doForward(B r2) {
            throw new AssertionError();
        }

        @Override // com.google.common.base.Converter, com.google.common.base.Function
        public boolean equals(@CheckForNull Object r4) {
            if ((r4 instanceof ReverseConverter) == true) goto L5;
            return false;
        L5:
            return this.original.equals(((ReverseConverter) r4).original);
        }

        public int hashCode() {
            return ~this.original.hashCode();
        }

        @Override // com.google.common.base.Converter
        public Converter<A, B> reverse() {
            return this.original;
        }

        public String toString() {
            return this.original + ".reverse()";
        }
    }

    protected Converter() {
        this(true);
    }

    Converter(boolean r1) {
        this.handleNullAutomatically = r1;
    }

    public static <A, B> Converter<A, B> from(Function<? super A, ? extends B> r2, Function<? super B, ? extends A> r3) {
        return new FunctionBasedConverter(r2, r3, null);
    }

    public static <T> Converter<T, T> identity() {
        return (IdentityConverter) IdentityConverter.INSTANCE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @CheckForNull
    private A unsafeDoBackward(@CheckForNull B r2) {
        return (A) doBackward(NullnessCasts.uncheckedCastNullableTToT(r2));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @CheckForNull
    private B unsafeDoForward(@CheckForNull A r2) {
        return (B) doForward(NullnessCasts.uncheckedCastNullableTToT(r2));
    }

    public final <C> Converter<A, C> andThen(Converter<B, C> r2) {
        return doAndThen(r2);
    }

    @Override // com.google.common.base.Function
    @Deprecated
    public final B apply(A r2) {
        return convert(r2);
    }

    @CheckForNull
    public final B convert(@CheckForNull A r2) {
        return correctedDoForward(r2);
    }

    public Iterable<B> convertAll(final Iterable<? extends A> r2) {
        Preconditions.checkNotNull(r2, "fromIterable");
        return new C05361(this, r2);
    }

    @CheckForNull
    A correctedDoBackward(@CheckForNull B r2) {
        if (this.handleNullAutomatically == false) goto L9;
        if (r2 != null) goto L7;
        return null;
    L7:
        return (A) Preconditions.checkNotNull(doBackward(r2));
    L9:
        return unsafeDoBackward(r2);
    }

    @CheckForNull
    B correctedDoForward(@CheckForNull A r2) {
        if (this.handleNullAutomatically == false) goto L9;
        if (r2 != null) goto L7;
        return null;
    L7:
        return (B) Preconditions.checkNotNull(doForward(r2));
    L9:
        return unsafeDoForward(r2);
    }

    <C> Converter<A, C> doAndThen(Converter<B, C> r3) {
        return new ConverterComposition(this, (Converter) Preconditions.checkNotNull(r3));
    }

    protected abstract A doBackward(B r1);

    protected abstract B doForward(A r1);

    @Override // com.google.common.base.Function
    public boolean equals(@CheckForNull Object r2) {
        return super.equals(r2);
    }

    @CheckReturnValue
    public Converter<B, A> reverse() {
        Converter<B, A> r0 = this.reverse;
        if (r0 != null) goto L6;
        ReverseConverter r1 = new ReverseConverter(this);
        this.reverse = r1;
        return r1;
    L6:
        return r0;
    }
}
