package androidx.core.content;

import android.content.res.Configuration;
import androidx.core.util.Consumer;

/* JADX INFO: loaded from: classes.dex */
public interface OnConfigurationChangedProvider {
    void addOnConfigurationChangedListener(Consumer<Configuration> r1);

    void removeOnConfigurationChangedListener(Consumer<Configuration> r1);
}
