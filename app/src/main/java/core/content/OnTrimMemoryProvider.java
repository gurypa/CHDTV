package androidx.core.content;

import androidx.core.util.Consumer;

/* JADX INFO: loaded from: classes.dex */
public interface OnTrimMemoryProvider {
    void addOnTrimMemoryListener(Consumer<Integer> r1);

    void removeOnTrimMemoryListener(Consumer<Integer> r1);
}
