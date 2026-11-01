package androidx.core.app;

import androidx.core.util.Consumer;

/* JADX INFO: loaded from: classes.dex */
public interface OnMultiWindowModeChangedProvider {
    void addOnMultiWindowModeChangedListener(Consumer<MultiWindowModeChangedInfo> r1);

    void removeOnMultiWindowModeChangedListener(Consumer<MultiWindowModeChangedInfo> r1);
}
