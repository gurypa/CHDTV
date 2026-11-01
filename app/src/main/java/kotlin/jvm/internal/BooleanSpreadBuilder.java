package kotlin.jvm.internal;

import com.arthenica.ffmpegkit.MediaInformation;
import kotlin.Metadata;

/* JADX INFO: compiled from: PrimitiveSpreadBuilders.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0018\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\r\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nJ\u0006\u0010\u000b\u001a\u00020\u0002J\f\u0010\f\u001a\u00020\u0004*\u00020\u0002H\u0014R\u000e\u0010\u0006\u001a\u00020\u0002X\u0082\u0004¢\u0006\u0002\n\u0000¨\u0006\r"}, m421d2 = {"Lkotlin/jvm/internal/BooleanSpreadBuilder;", "Lkotlin/jvm/internal/PrimitiveSpreadBuilder;", "", MediaInformation.KEY_SIZE, "", "(I)V", "values", "add", "", "value", "", "toArray", "getSize", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class BooleanSpreadBuilder extends PrimitiveSpreadBuilder<boolean[]> {
    private final boolean[] values;

    public BooleanSpreadBuilder(int r2) {
        super(r2);
        this.values = new boolean[r2];
    }

    public final void add(boolean r4) {
        boolean[] r0 = this.values;
        int r1 = getPosition();
        setPosition(r1 + 1);
        r0[r1] = r4;
    }

    @Override // kotlin.jvm.internal.PrimitiveSpreadBuilder
    public /* bridge */ /* synthetic */ int getSize(boolean[] r2) {
        return getSize2(r2);
    }

    /* JADX INFO: renamed from: getSize, reason: avoid collision after fix types in other method */
    protected int getSize2(boolean[] r2) {
        Intrinsics.checkNotNullParameter(r2, "<this>");
        return r2.length;
    }

    public final boolean[] toArray() {
        return toArray(this.values, new boolean[size()]);
    }
}
