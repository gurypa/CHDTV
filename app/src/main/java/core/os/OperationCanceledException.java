package androidx.core.os;

import androidx.core.util.ObjectsCompat;

/* JADX INFO: loaded from: classes.dex */
public class OperationCanceledException extends RuntimeException {
    public OperationCanceledException() {
        this(null);
    }

    public OperationCanceledException(String r2) {
        super(ObjectsCompat.toString(r2, "The operation has been canceled."));
    }
}
