package androidx.core.app;

import android.content.res.Configuration;

/* JADX INFO: loaded from: classes.dex */
public final class PictureInPictureModeChangedInfo {
    private final boolean mIsInPictureInPictureMode;
    private final Configuration mNewConfig;

    public PictureInPictureModeChangedInfo(boolean r2) {
        this.mIsInPictureInPictureMode = r2;
        this.mNewConfig = null;
    }

    public PictureInPictureModeChangedInfo(boolean r1, Configuration r2) {
        this.mIsInPictureInPictureMode = r1;
        this.mNewConfig = r2;
    }

    public Configuration getNewConfig() {
        if (this.mNewConfig == null) goto L7;
        return this.mNewConfig;
    L7:
        throw new IllegalStateException("PictureInPictureModeChangedInfo must be constructed with the constructor that takes a Configuration to call getNewConfig(). Are you running on an API 26 or higher device that makes this information available?");
    }

    public boolean isInPictureInPictureMode() {
        return this.mIsInPictureInPictureMode;
    }
}
