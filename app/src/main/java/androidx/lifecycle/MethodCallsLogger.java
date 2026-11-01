package androidx.lifecycle;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class MethodCallsLogger {
    private Map<String, Integer> mCalledMethods;

    public MethodCallsLogger() {
        this.mCalledMethods = new HashMap();
    }

    public boolean approveCall(String r8, int r9) {
        Integer r0 = this.mCalledMethods.get(r8);
        if (r0 == null) goto L5;
        int r2 = r0.intValue();
    L7:
        if ((r2 & r9) == 0) goto L9;
        boolean r3 = true;
    L10:
        this.mCalledMethods.put(r8, Integer.valueOf(r2 | r9));
        if (r3 == false) goto L13;
        return false;
    L13:
        return true;
    L9:
        r3 = false;
        goto L10
    L5:
        r2 = 0;
        goto L7
    }
}
