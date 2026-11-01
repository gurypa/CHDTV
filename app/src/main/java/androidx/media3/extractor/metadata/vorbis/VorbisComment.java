package androidx.media3.extractor.metadata.vorbis;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public final class VorbisComment extends androidx.media3.extractor.metadata.flac.VorbisComment {
    public static final Parcelable.Creator<VorbisComment> CREATOR = null;

    static {
        CREATOR = new C04361();
    }

    VorbisComment(Parcel r1) {
        super(r1);
    }

    public VorbisComment(String r1, String r2) {
        super(r1, r2);
    }
}
