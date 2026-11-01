package kotlin;

/* JADX INFO: compiled from: HashCode.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000\f\n\u0000\n\u0002\u0010\b\n\u0002\u0010\u0000\n\u0000\u001a\u000f\u0010\u0000\u001a\u00020\u0001*\u0004\u0018\u00010\u0002H\u0087\b¨\u0006\u0003"}, m421d2 = {"hashCode", "", "", "kotlin-stdlib"}, m422k = 2, m423mv = {1, 9, 0}, m425xi = 48)
public final class HashCodeKt {
    private static final int hashCode(Object r1) {
        if (r1 != null) goto L6;
        return 0;
    L6:
        return r1.hashCode();
    }
}
