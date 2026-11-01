package com.google.common.collect;

import com.google.common.base.Preconditions;
import java.util.NoSuchElementException;
import javax.annotation.CheckForNull;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public abstract class AbstractIterator<T> extends UnmodifiableIterator<T> {

    @CheckForNull
    private T next;
    private State state;

    private static final enum State extends Enum<State> {
        private static final /* synthetic */ State[] $VALUES = null;
        public static final State DONE = null;
        public static final State FAILED = null;
        public static final State NOT_READY = null;
        public static final State READY = null;

        private static /* synthetic */ State[] $values() {
            return new State[]{READY, NOT_READY, DONE, FAILED};
        }

        static {
            READY = new State("READY", 0);
            NOT_READY = new State("NOT_READY", 1);
            DONE = new State("DONE", 2);
            FAILED = new State("FAILED", 3);
            $VALUES = $values();
        }

        private State(String r1, int r2) {
            super(r1, r2);
        }

        public static State valueOf(String r1) {
            return (State) Enum.valueOf(State.class, r1);
        }

        public static State[] values() {
            return (State[]) $VALUES.clone();
        }
    }

    protected AbstractIterator() {
        this.state = State.NOT_READY;
    }

    private boolean tryToComputeNext() {
        this.state = State.FAILED;
        this.next = computeNext();
        if (this.state == State.DONE) goto L6;
        this.state = State.READY;
        return true;
    L6:
        return false;
    }

    @CheckForNull
    protected abstract T computeNext();

    @CheckForNull
    protected final T endOfData() {
        this.state = State.DONE;
        return null;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        if (this.state == State.FAILED) goto L5;
        boolean r0 = true;
    L6:
        Preconditions.checkState(r0);
        switch(this.state.ordinal()) {
            case 0: goto L11;
            case 1: goto L9;
            case 2: goto L10;
            default: goto L9;
        };
    L10:
        return false;
    L11:
        return true;
    L9:
        return tryToComputeNext();
    L5:
        r0 = false;
        goto L6
    }

    @Override // java.util.Iterator
    @ParametricNullness
    public final T next() {
        if (hasNext() == false) goto L7;
        this.state = State.NOT_READY;
        T r0 = (T) NullnessCasts.uncheckedCastNullableTToT(this.next);
        this.next = null;
        return r0;
    L7:
        throw new NoSuchElementException();
    }

    @ParametricNullness
    public final T peek() {
        if (hasNext() == false) goto L7;
        return (T) NullnessCasts.uncheckedCastNullableTToT(this.next);
    L7:
        throw new NoSuchElementException();
    }
}
