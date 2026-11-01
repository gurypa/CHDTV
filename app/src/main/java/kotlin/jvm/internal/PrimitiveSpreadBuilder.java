package kotlin.jvm.internal;

import androidx.exifinterface.media.ExifInterface;
import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Metadata;
import kotlin.ranges.IntRange;

/* JADX INFO: compiled from: PrimitiveSpreadBuilders.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\t\b&\u0018\u0000*\b\b\u0000\u0010\u0001*\u00020\u00022\u00020\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0013\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00028\u0000¢\u0006\u0002\u0010\u0012J\b\u0010\u0003\u001a\u00020\u0004H\u0004J\u001d\u0010\u0013\u001a\u00028\u00002\u0006\u0010\u0014\u001a\u00028\u00002\u0006\u0010\u0015\u001a\u00028\u0000H\u0004¢\u0006\u0002\u0010\u0016J\u0011\u0010\u0017\u001a\u00020\u0004*\u00028\u0000H$¢\u0006\u0002\u0010\u0018R\u001a\u0010\u0006\u001a\u00020\u0004X\u0084\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\u0005R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004¢\u0006\u0002\n\u0000R\u001e\u0010\n\u001a\n\u0012\u0006\u0012\u0004\u0018\u00018\u00000\u000bX\u0082\u0004¢\u0006\n\n\u0002\u0010\u000e\u0012\u0004\b\f\u0010\r¨\u0006\u0019"}, m421d2 = {"Lkotlin/jvm/internal/PrimitiveSpreadBuilder;", ExifInterface.GPS_DIRECTION_TRUE, "", MediaInformation.KEY_SIZE, "", "(I)V", "position", "getPosition", "()I", "setPosition", "spreads", "", "getSpreads$annotations", "()V", "[Ljava/lang/Object;", "addSpread", "", "spreadArgument", "(Ljava/lang/Object;)V", "toArray", "values", "result", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", "getSize", "(Ljava/lang/Object;)I", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public abstract class PrimitiveSpreadBuilder<T> {
    private int position;
    private final int size;
    private final T[] spreads;

    public PrimitiveSpreadBuilder(int r2) {
        this.size = r2;
        this.spreads = (T[]) new Object[this.size];
    }

    private static /* synthetic */ void getSpreads$annotations() {
    }

    public final void addSpread(T r4) {
        Intrinsics.checkNotNullParameter(r4, "spreadArgument");
        T[] r0 = this.spreads;
        int r1 = this.position;
        this.position = r1 + 1;
        r0[r1] = r4;
    }

    protected final int getPosition() {
        return this.position;
    }

    protected abstract int getSize(T r1);

    protected final void setPosition(int r1) {
        this.position = r1;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [kotlin.collections.IntIterator] */
    protected final int size() {
        int r0 = 0;
        ?? r1 = new IntRange(0, this.size - 1).iterator();
    L4:
        if (r1.hasNext() == false) goto L10;
        T r4 = this.spreads[r1.nextInt()];
        if (r4 == null) goto L8;
        int r5 = getSize(r4);
    L9:
        r0 = r0 + r5;
        goto L4
    L8:
        r5 = 1;
        goto L9
    L10:
        return r0;
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [kotlin.collections.IntIterator] */
    protected final T toArray(T r8, T r9) {
        Intrinsics.checkNotNullParameter(r8, "values");
        Intrinsics.checkNotNullParameter(r9, "result");
        int r0 = 0;
        int r1 = 0;
        ?? r2 = new IntRange(0, this.size - 1).iterator();
    L4:
        if (r2.hasNext() == false) goto L11;
        int r3 = r2.nextInt();
        T r5 = this.spreads[r3];
        if (r5 == null) goto L4;
        if (r1 >= r3) goto L9;
        System.arraycopy(r8, r1, r9, r0, r3 - r1);
        r0 = r0 + (r3 - r1);
    L9:
        int r6 = getSize(r5);
        System.arraycopy(r5, 0, r9, r0, r6);
        r0 = r0 + r6;
        r1 = r3 + 1;
        goto L4
    L11:
        if (r1 >= this.size) goto L13;
        System.arraycopy(r8, r1, r9, r0, this.size - r1);
    L13:
        return r9;
    }
}
