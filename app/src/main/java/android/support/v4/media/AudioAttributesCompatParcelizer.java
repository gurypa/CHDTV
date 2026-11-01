package android.support.v4.media;

import androidx.media.AudioAttributesCompat;
import androidx.versionedparcelable.VersionedParcel;

/* JADX INFO: loaded from: classes.dex */
public final class AudioAttributesCompatParcelizer extends androidx.media.AudioAttributesCompatParcelizer {
    public AudioAttributesCompatParcelizer() {
    }

    public static AudioAttributesCompat read(VersionedParcel r1) {
        return androidx.media.AudioAttributesCompatParcelizer.read(r1);
    }

    public static void write(AudioAttributesCompat r0, VersionedParcel r1) {
        androidx.media.AudioAttributesCompatParcelizer.write(r0, r1);
    }
}
