package androidx.core.app;

import androidx.core.util.Consumer;

/* JADX INFO: loaded from: classes.dex */
public interface OnPictureInPictureModeChangedProvider {
    void addOnPictureInPictureModeChangedListener(Consumer<PictureInPictureModeChangedInfo> r1);

    void removeOnPictureInPictureModeChangedListener(Consumer<PictureInPictureModeChangedInfo> r1);
}
