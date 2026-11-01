package es.antonio.m3u;

import android.net.Uri;
import androidx.media3.exoplayer.dash.manifest.DashManifest;
import androidx.media3.exoplayer.dash.manifest.DashManifestParser;
import androidx.media3.exoplayer.dash.manifest.SegmentBase;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* JADX INFO: loaded from: classes7.dex */
public final class DemandDashParser extends DashManifestParser {
    private boolean compact;
    private final boolean foldInput;

    public DemandDashParser() {
        this(true);
    }

    public DemandDashParser(boolean r1) {
        this.foldInput = r1;
    }

    @Override // androidx.media3.exoplayer.dash.manifest.DashManifestParser, androidx.media3.exoplayer.upstream.ParsingLoadable.Parser
    public DashManifest parse(Uri r6, InputStream r7) throws IOException {
        if (this.foldInput == false) goto L5;
        long r0 = System.nanoTime();
        DemandMpdInput r2 = new DemandMpdInput(r7);
        DashManifest r8 = super.parse(r6, r2);
        BlackBox.event("VOD_MPD_COMPACT", "input_bytes=" + r2.inputBytes + " parsed_bytes=" + r2.outputBytes + " s_elements=" + r2.elements + " runs=" + r2.runs + " peak_output_bytes=" + r2.peakOutput + " duration_ms=" + ((System.nanoTime() - r0) / 1000000));
        return r8;
    L5:
        return super.parse(r6, r7);
    }

    @Override // androidx.media3.exoplayer.dash.manifest.DashManifestParser
    protected DashManifest parseMediaPresentationDescription(XmlPullParser r5, Uri r6) throws XmlPullParserException, IOException {
        this.compact = !"dynamic".equals(r5.getAttributeValue(null, "type"));
        long r0 = System.nanoTime();
        DashManifest r7 = super.parseMediaPresentationDescription(r5, r6);
        BlackBox.event("VOD_DASH_PARSE", "duration_ms=" + ((System.nanoTime() - r0) / 1000000) + " static=" + this.compact);
        return r7;
    }

    @Override // androidx.media3.exoplayer.dash.manifest.DashManifestParser
    protected List<SegmentBase.SegmentTimelineElement> parseSegmentTimeline(XmlPullParser r2, long r3, long r5) throws XmlPullParserException, IOException {
        if (this.compact == false) goto L6;
        return DemandTimeline.read(r2, r3, r5);
    L6:
        return super.parseSegmentTimeline(r2, r3, r5);
    }
}
