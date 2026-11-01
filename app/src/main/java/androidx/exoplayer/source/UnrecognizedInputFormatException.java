package androidx.media3.exoplayer.source;

import android.net.Uri;
import androidx.media3.common.ParserException;
import androidx.media3.extractor.SniffFailure;
import com.google.common.collect.ImmutableList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class UnrecognizedInputFormatException extends ParserException {
    public final ImmutableList<SniffFailure> sniffFailures;
    public final Uri uri;

    @Deprecated
    public UnrecognizedInputFormatException(String r2, Uri r3) {
        this(r2, r3, ImmutableList.m212of());
    }

    public UnrecognizedInputFormatException(String r4, Uri r5, List<? extends SniffFailure> r6) {
        super(r4, null, false, 1);
        this.uri = r5;
        this.sniffFailures = ImmutableList.copyOf(r6);
    }
}
