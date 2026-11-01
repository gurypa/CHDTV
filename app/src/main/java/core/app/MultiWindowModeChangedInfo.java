package androidx.core.app;

import android.content.res.Configuration;

/* JADX INFO: loaded from: classes.dex */
public final class MultiWindowModeChangedInfo {
    private final boolean mIsInMultiWindowMode;
    private final Configuration mNewConfig;

    public MultiWindowModeChangedInfo(boolean r2) {
        this.mIsInMultiWindowMode = r2;
        this.mNewConfig = null;
    }

    public MultiWindowModeChangedInfo(boolean r1, Configuration r2) {
        this.mIsInMultiWindowMode = r1;
        this.mNewConfig = r2;
    }

    public Configuration getNewConfig() {
        if (this.mNewConfig == null) goto L7;
        return this.mNewConfig;
    L7:
        throw new IllegalStateException("MultiWindowModeChangedInfo must be constructed with the constructor that takes a Configuration to call getNewConfig(). Are you running on an API 26 or higher device that makes this information available?");
    }

    public boolean isInMultiWindowMode() {
        return this.mIsInMultiWindowMode;
    }
}
