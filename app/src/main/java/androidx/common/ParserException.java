package androidx.media3.common;

import java.io.IOException;

/* JADX INFO: loaded from: classes.dex */
public class ParserException extends IOException {
    public final boolean contentIsMalformed;
    public final int dataType;

    protected ParserException(String r1, Throwable r2, boolean r3, int r4) {
        super(r1, r2);
        this.contentIsMalformed = r3;
        this.dataType = r4;
    }

    public static ParserException createForMalformedContainer(String r2, Throwable r3) {
        return new ParserException(r2, r3, true, 1);
    }

    public static ParserException createForMalformedDataOfUnknownType(String r3, Throwable r4) {
        return new ParserException(r3, r4, true, 0);
    }

    public static ParserException createForMalformedManifest(String r3, Throwable r4) {
        return new ParserException(r3, r4, true, 4);
    }

    public static ParserException createForManifestWithUnsupportedFeature(String r3, Throwable r4) {
        return new ParserException(r3, r4, false, 4);
    }

    public static ParserException createForUnsupportedContainerFeature(String r4) {
        return new ParserException(r4, null, false, 1);
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        return super.getMessage() + " {contentIsMalformed=" + this.contentIsMalformed + ", dataType=" + this.dataType + "}";
    }
}
