package kotlin;

import androidx.exifinterface.media.ExifInterface;
import java.io.Serializable;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: LazyJVM.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u00000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0002\u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00012\b\u0012\u0004\u0012\u0002H\u00010\u00022\u00060\u0003j\u0002`\u0004B\u001f\u0012\f\u0010\u0005\u001a\b\u0012\u0004\u0012\u00028\u00000\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00010\b¢\u0006\u0002\u0010\tJ\b\u0010\u000e\u001a\u00020\u000fH\u0016J\b\u0010\u0010\u001a\u00020\u0011H\u0016J\b\u0010\u0012\u001a\u00020\bH\u0002R\u0010\u0010\n\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n\u0012\u0004\u0012\u00028\u0000\u0018\u00010\u0006X\u0088\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\u00028\u00008VX\u0096\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\r¨\u0006\u0013"}, m421d2 = {"Lkotlin/SynchronizedLazyImpl;", ExifInterface.GPS_DIRECTION_TRUE, "Lkotlin/Lazy;", "Ljava/io/Serializable;", "Lkotlin/io/Serializable;", "initializer", "Lkotlin/Function0;", "lock", "", "(Lkotlin/jvm/functions/Function0;Ljava/lang/Object;)V", "_value", "value", "getValue", "()Ljava/lang/Object;", "isInitialized", "", "toString", "", "writeReplace", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
final class SynchronizedLazyImpl<T> implements Lazy<T>, Serializable {
    private volatile Object _value;
    private Function0<? extends T> initializer;
    private final Object lock;

    public SynchronizedLazyImpl(Function0<? extends T> r2, Object r3) {
        Intrinsics.checkNotNullParameter(r2, "initializer");
        this.initializer = r2;
        this._value = UNINITIALIZED_VALUE.INSTANCE;
        if (r3 != null) goto L5;
        Object r0 = this;
    L6:
        this.lock = r0;
        return;
    L5:
        r0 = r3;
        goto L6
    }

    public /* synthetic */ SynchronizedLazyImpl(Function0 r1, Object r2, int r3, DefaultConstructorMarker r4) {
        if ((r3 & 2) == 0) goto L5;
        r2 = null;
    L5:
        this(r1, r2);
    }

    private final Object writeReplace() {
        return new InitializedLazyImpl(getValue());
    }

    @Override // kotlin.Lazy
    public T getValue() {
        T r0 = (T) this._value;
        if (r0 == UNINITIALIZED_VALUE.INSTANCE) goto L5;
        return r0;
    L5:
        Object r1 = this.lock;
        monitor-enter(r1);
        Object r3 = this._value;     // Catch: Throwable -> L15
        if (r3 == UNINITIALIZED_VALUE.INSTANCE) goto L11;
        T r4 = (T) r3;
    L13:
        monitor-exit(r1);
        return r4;
    L11:
        Function0<? extends T> r5 = this.initializer;     // Catch: Throwable -> L15
        Intrinsics.checkNotNull(r5);     // Catch: Throwable -> L15
        r4 = r5.invoke();     // Catch: Throwable -> L15
        this._value = r4;     // Catch: Throwable -> L15
        this.initializer = null;     // Catch: Throwable -> L15
    L15:
        th = move-exception;
        throw th;
    }

    @Override // kotlin.Lazy
    public boolean isInitialized() {
        if (this._value == UNINITIALIZED_VALUE.INSTANCE) goto L5;
        return true;
    L5:
        return false;
    }

    public String toString() {
        if (isInitialized() == true) goto L7;
        return "Lazy value not initialized yet.";
    L7:
        return String.valueOf(getValue());
    }
}
