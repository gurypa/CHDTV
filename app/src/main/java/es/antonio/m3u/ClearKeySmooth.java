package es.antonio.m3u;

import android.net.Uri;
import androidx.media3.common.C0180C;
import androidx.media3.common.DrmInitData;
import androidx.media3.common.Format;
import androidx.media3.common.MimeTypes;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifest;
import androidx.media3.exoplayer.smoothstreaming.manifest.SsManifestParser;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONException;

/* JADX INFO: loaded from: classes3.dex */
final class ClearKeySmooth implements ParsingLoadable.Parser<SsManifest> {
    private final byte[] pssh;

    ClearKeySmooth(byte[] r2) throws JSONException {
        this.pssh = new ClearKeyHls(r2).pssh;
    }

    SsManifest convert(SsManifest r18) {
        if (r18.protectionElement != null) goto L5;
        return r18;
    L5:
        DrmInitData r2 = new DrmInitData(C0180C.CENC_TYPE_cenc, new DrmInitData.SchemeData[]{new DrmInitData.SchemeData(C0180C.CLEARKEY_UUID, MimeTypes.VIDEO_MP4, this.pssh)});
        SsManifest.StreamElement[] r3 = new SsManifest.StreamElement[r18.streamElements.length];
        int r4 = 0;
    L7:
        if (r4 >= r3.length) goto L13;
        SsManifest.StreamElement r5 = r18.streamElements[r4];
        Format[] r6 = new Format[r5.formats.length];
        int r7 = 0;
    L10:
        if (r7 >= r6.length) goto L12;
        r6[r7] = r5.formats[r7].buildUpon().setDrmInitData(r2).build();
        r7 = r7 + 1;
        goto L10
    L12:
        r3[r4] = r5.copy(r6);
        r4 = r4 + 1;
        goto L7
    L13:
        SsManifest.ProtectionElement r15 = new SsManifest.ProtectionElement(C0180C.CLEARKEY_UUID, this.pssh, r18.protectionElement.trackEncryptionBoxes);
        int r8 = r18.majorVersion;
        int r9 = r18.minorVersion;
        long r11 = 0;
        if (r18.durationUs != C0180C.TIME_UNSET) goto L16;
        long r10 = 0;
    L18:
        if (r18.dvrWindowLengthUs == C0180C.TIME_UNSET) goto L22;
        r11 = r18.dvrWindowLengthUs;
    L22:
        return new SsManifest(r8, r9, 1000000, r10, r11, r18.lookAheadCount, r18.isLive, r15, r3);
    L16:
        r10 = r18.durationUs;
        goto L18
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
    public SsManifest parse(Uri r2, InputStream r3) throws IOException {
        return convert(new SsManifestParser().parse(r2, r3));
    }

    @Override // androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
    public /* bridge */ /* synthetic */ SsManifest parse(Uri r1, InputStream r2) throws IOException {
        return parse(r1, r2);
    }
}
