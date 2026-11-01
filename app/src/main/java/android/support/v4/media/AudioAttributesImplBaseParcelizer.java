package android.support.v4.media;

import androidx.media.AudioAttributesImplBase;
import androidx.versionedparcelable.VersionedParcel;

/* JADX INFO: loaded from: classes.dex */
public final class AudioAttributesImplBaseParcelizer extends androidx.media.AudioAttributesImplBaseParcelizer {
    public AudioAttributesImplBaseParcelizer() {
    }

    public static AudioAttributesImplBase read(VersionedParcel r1) {
        return androidx.media.AudioAttributesImplBaseParcelizer.read(r1);
    }

    public static void write(AudioAttributesImplBase r0, VersionedParcel r1) {
        androidx.media.AudioAttributesImplBaseParcelizer.write(r0, r1);
    }
}
