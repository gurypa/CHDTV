package androidx.media;

import androidx.versionedparcelable.VersionedParcel;

/* JADX INFO: loaded from: classes.dex */
public class AudioAttributesCompatParcelizer {
    public AudioAttributesCompatParcelizer() {
    }

    public static AudioAttributesCompat read(VersionedParcel r3) {
        AudioAttributesCompat r0 = new AudioAttributesCompat();
        r0.mImpl = (AudioAttributesImpl) r3.readVersionedParcelable(r0.mImpl, 1);
        return r0;
    }

    public static void write(AudioAttributesCompat r2, VersionedParcel r3) {
        r3.setSerializationFlags(false, false);
        r3.writeVersionedParcelable(r2.mImpl, 1);
    }
}
