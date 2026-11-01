package androidx.core.app;

import android.content.Intent;
import androidx.core.util.Consumer;

/* JADX INFO: loaded from: classes.dex */
public interface OnNewIntentProvider {
    void addOnNewIntentListener(Consumer<Intent> r1);

    void removeOnNewIntentListener(Consumer<Intent> r1);
}
