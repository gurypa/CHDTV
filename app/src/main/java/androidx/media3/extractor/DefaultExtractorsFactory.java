package androidx.media3.extractor;

import android.net.Uri;
import androidx.media3.common.FileTypes;
import androidx.media3.common.Format;
import androidx.media3.common.util.TimestampAdjuster;
import androidx.media3.extractor.amr.AmrExtractor;
import androidx.media3.extractor.avi.AviExtractor;
import androidx.media3.extractor.avif.AvifExtractor;
import androidx.media3.extractor.bmp.BmpExtractor;
import androidx.media3.extractor.flac.FlacExtractor;
import androidx.media3.extractor.flv.FlvExtractor;
import androidx.media3.extractor.heif.HeifExtractor;
import androidx.media3.extractor.jpeg.JpegExtractor;
import androidx.media3.extractor.mkv.MatroskaExtractor;
import androidx.media3.extractor.mp3.Mp3Extractor;
import androidx.media3.extractor.mp4.FragmentedMp4Extractor;
import androidx.media3.extractor.mp4.Mp4Extractor;
import androidx.media3.extractor.ogg.OggExtractor;
import androidx.media3.extractor.p001ts.Ac3Extractor;
import androidx.media3.extractor.p001ts.Ac4Extractor;
import androidx.media3.extractor.p001ts.AdtsExtractor;
import androidx.media3.extractor.p001ts.DefaultTsPayloadReaderFactory;
import androidx.media3.extractor.p001ts.PsExtractor;
import androidx.media3.extractor.p001ts.TsExtractor;
import androidx.media3.extractor.png.PngExtractor;
import androidx.media3.extractor.text.DefaultSubtitleParserFactory;
import androidx.media3.extractor.text.SubtitleParser;
import androidx.media3.extractor.wav.WavExtractor;
import androidx.media3.extractor.webp.WebpExtractor;
import com.google.common.collect.ImmutableList;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultExtractorsFactory implements ExtractorsFactory {
    private static final int[] DEFAULT_EXTRACTOR_ORDER = null;
    private static final ExtensionLoader FLAC_EXTENSION_LOADER = null;
    private static final ExtensionLoader MIDI_EXTENSION_LOADER = null;
    private int adtsFlags;
    private int amrFlags;
    private boolean constantBitrateSeekingAlwaysEnabled;
    private boolean constantBitrateSeekingEnabled;
    private int flacFlags;
    private int fragmentedMp4Flags;
    private int jpegFlags;
    private int matroskaFlags;
    private int mp3Flags;
    private int mp4Flags;
    private SubtitleParser.Factory subtitleParserFactory;
    private boolean textTrackTranscodingEnabled;
    private int tsFlags;
    private int tsMode;
    private ImmutableList<Format> tsSubtitleFormats;
    private int tsTimestampSearchBytes;

    private static final class ExtensionLoader {
        private final ConstructorSupplier constructorSupplier;
        private final AtomicBoolean extensionLoaded;
        private Constructor<? extends Extractor> extractorConstructor;

        public interface ConstructorSupplier {
            Constructor<? extends Extractor> getConstructor() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException;
        }

        public ExtensionLoader(ConstructorSupplier r3) {
            this.constructorSupplier = r3;
            this.extensionLoaded = new AtomicBoolean(false);
        }

        private Constructor<? extends Extractor> maybeLoadExtractorConstructor() {
            AtomicBoolean r0 = this.extensionLoaded;
            monitor-enter(r0);
        L19:
            th = move-exception;
            throw th;
        L5:
            if (this.extensionLoaded.get() == false) goto L23;
            Constructor<? extends Extractor> r1 = this.extractorConstructor;     // Catch: Throwable -> L19
            monitor-exit(r0);     // Catch: Throwable -> L19
            return r1;
        L23:
            Constructor<? extends Extractor> r2 = this.constructorSupplier.getConstructor();     // Catch: Exception -> L12 ClassNotFoundException -> L15 Throwable -> L19
            monitor-exit(r0);     // Catch: Throwable -> L19
            return r2;
        L12:
            e = move-exception;
            throw new RuntimeException("Error instantiating extension", e);     // Catch: Throwable -> L19
        L16:
            this.extensionLoaded.set(true);     // Catch: Throwable -> L19
            return this.extractorConstructor;
        }

        public Extractor getExtractor(Object... r5) {
            Constructor<? extends Extractor> r0 = maybeLoadExtractorConstructor();
            if (r0 != null) goto L11;
            return null;
        L11:
            return r0.newInstance(r5);
        L8:
            e = move-exception;
            throw new IllegalStateException("Unexpected error creating extractor", e);
        }
    }

    public static /* synthetic */ Constructor $r8$lambda$F3_j6dVylL4MI0cE_5Js_FX16M4() {
        return getFlacExtractorConstructor();
    }

    /* JADX INFO: renamed from: $r8$lambda$f2WO-QnNuJkSXUlyLvKOvkcEmrY, reason: not valid java name */
    public static /* synthetic */ Constructor m482$r8$lambda$f2WOQnNuJkSXUlyLvKOvkcEmrY() {
        return getMidiExtractorConstructor();
    }

    static {
        DEFAULT_EXTRACTOR_ORDER = new int[]{5, 4, 12, 8, 3, 10, 9, 11, 6, 2, 0, 1, 7, 16, 15, 14, 17, 18, 19, 20, 21};
        FLAC_EXTENSION_LOADER = new ExtensionLoader(new DefaultExtractorsFactory$$ExternalSyntheticLambda0());
        MIDI_EXTENSION_LOADER = new ExtensionLoader(new DefaultExtractorsFactory$$ExternalSyntheticLambda1());
    }

    public DefaultExtractorsFactory() {
        this.tsMode = 1;
        this.tsTimestampSearchBytes = TsExtractor.DEFAULT_TIMESTAMP_SEARCH_BYTES;
        this.subtitleParserFactory = new DefaultSubtitleParserFactory();
        this.textTrackTranscodingEnabled = true;
    }

    private void addExtractorsForFileType(int r10, List<Extractor> r11) {
        int r0 = 2;
        int r1 = 1;
        int r2 = 0;
        switch(r10) {
            case 0: goto L73;
            case 1: goto L72;
            case 2: goto L63;
            case 3: goto L54;
            case 4: goto L50;
            case 5: goto L49;
            case 6: goto L44;
            case 7: goto L35;
            case 8: goto L26;
            case 9: goto L25;
            case 10: goto L24;
            case 11: goto L21;
            case 12: goto L19;
            case 13: goto L99;
            case 14: goto L18;
            case 15: goto L15;
            case 16: goto L14;
            case 17: goto L13;
            case 18: goto L12;
            case 19: goto L11;
            case 20: goto L7;
            case 21: goto L5;
            default: goto L99;
        };
    L5:
        r11.add(new AvifExtractor());
        return;
    L11:
        r11.add(new BmpExtractor());
        return;
    L12:
        r11.add(new WebpExtractor());
        return;
    L13:
        r11.add(new PngExtractor());
        return;
    L14:
        r11.add(new AviExtractor(1 ^ (this.textTrackTranscodingEnabled ? 1 : 0), this.subtitleParserFactory));
        return;
    L15:
        Extractor r3 = MIDI_EXTENSION_LOADER.getExtractor(new Object[0]);
        if (r3 == null) goto L83;
        r11.add(r3);
        return;
    L83:
        return;
    L18:
        r11.add(new JpegExtractor(this.jpegFlags));
        return;
    L19:
        r11.add(new WavExtractor());
        return;
    L24:
        r11.add(new PsExtractor());
        return;
    L25:
        r11.add(new OggExtractor());
        return;
    L26:
        SubtitleParser.Factory r4 = this.subtitleParserFactory;
        int r5 = this.fragmentedMp4Flags;
        if (this.textTrackTranscodingEnabled == false) goto L29;
        int r6 = 0;
    L30:
        r11.add(new FragmentedMp4Extractor(r4, r5 | r6));
        SubtitleParser.Factory r7 = this.subtitleParserFactory;
        int r8 = this.mp4Flags;
        if (this.textTrackTranscodingEnabled == true) goto L34;
        r2 = 16;
    L34:
        r11.add(new Mp4Extractor(r7, r2 | r8));
        return;
    L29:
        r6 = 32;
        goto L30
    L35:
        int r9 = this.mp3Flags;
        if (this.constantBitrateSeekingEnabled == true) goto L39;
        r1 = 0;
    L39:
        int r12 = r1 | r9;
        if (this.constantBitrateSeekingAlwaysEnabled == true) goto L43;
        r0 = 0;
    L43:
        r11.add(new Mp3Extractor(r0 | r12));
        return;
    L44:
        SubtitleParser.Factory r13 = this.subtitleParserFactory;
        int r14 = this.matroskaFlags;
        if (this.textTrackTranscodingEnabled == false) goto L48;
        r0 = 0;
    L48:
        r11.add(new MatroskaExtractor(r13, r0 | r14));
        return;
    L49:
        r11.add(new FlvExtractor());
        return;
    L50:
        Extractor r15 = FLAC_EXTENSION_LOADER.getExtractor(new Object[]{Integer.valueOf(this.flacFlags)});
        if (r15 == null) goto L53;
        r11.add(r15);
        return;
    L53:
        r11.add(new FlacExtractor(this.flacFlags));
        return;
    L54:
        int r16 = this.amrFlags;
        if (this.constantBitrateSeekingEnabled == true) goto L58;
        r1 = 0;
    L58:
        int r17 = r1 | r16;
        if (this.constantBitrateSeekingAlwaysEnabled == true) goto L62;
        r0 = 0;
    L62:
        r11.add(new AmrExtractor(r0 | r17));
        return;
    L63:
        int r18 = this.adtsFlags;
        if (this.constantBitrateSeekingEnabled == true) goto L67;
        r1 = 0;
    L67:
        int r19 = r1 | r18;
        if (this.constantBitrateSeekingAlwaysEnabled == true) goto L71;
        r0 = 0;
    L71:
        r11.add(new AdtsExtractor(r0 | r19));
        return;
    L72:
        r11.add(new Ac4Extractor());
        return;
    L73:
        r11.add(new Ac3Extractor());
        return;
    L99:
        return;
    L7:
        if ((2 & this.mp4Flags) == 0) goto L9;
        return;
    L9:
        if ((this.mp4Flags & 4) != 0) goto L77;
        r11.add(new HeifExtractor());
        return;
    L77:
        return;
    L21:
        if (this.tsSubtitleFormats != null) goto L23;
        this.tsSubtitleFormats = ImmutableList.m212of();
    L23:
        r11.add(new TsExtractor(this.tsMode, !this.textTrackTranscodingEnabled ? 1 : 0, this.subtitleParserFactory, new TimestampAdjuster(0), new DefaultTsPayloadReaderFactory(this.tsFlags, this.tsSubtitleFormats), this.tsTimestampSearchBytes));
    }

    private static Constructor<? extends Extractor> getFlacExtractorConstructor() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (Boolean.TRUE.equals(Class.forName("androidx.media3.decoder.flac.FlacLibrary").getMethod("isAvailable", new Class[0]).invoke(null, new Object[0])) == true) goto L5;
        return null;
    L5:
        return Class.forName("androidx.media3.decoder.flac.FlacExtractor").asSubclass(Extractor.class).getConstructor(new Class[]{Integer.TYPE});
    }

    private static Constructor<? extends Extractor> getMidiExtractorConstructor() throws ClassNotFoundException, NoSuchMethodException {
        return Class.forName("androidx.media3.decoder.midi.MidiExtractor").asSubclass(Extractor.class).getConstructor(new Class[0]);
    }

    @Override // androidx.media3.extractor.ExtractorsFactory
    public synchronized Extractor[] createExtractors() {
        monitor-enter(this);
        Extractor[] r0 = createExtractors(Uri.EMPTY, new HashMap());     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.extractor.ExtractorsFactory
    public synchronized Extractor[] createExtractors(Uri r8, Map<String, List<String>> r9) {
        monitor-enter(this);
        ArrayList r0 = new ArrayList(DEFAULT_EXTRACTOR_ORDER.length);     // Catch: Throwable -> L20
        int r1 = FileTypes.inferFileTypeFromResponseHeaders(r9);     // Catch: Throwable -> L20
        if (r1 == (-1)) goto L6;
        addExtractorsForFileType(r1, r0);     // Catch: Throwable -> L20
    L6:
        int r3 = FileTypes.inferFileTypeFromUri(r8);     // Catch: Throwable -> L20
        if (r3 == (-1)) goto L10;
        if (r3 == r1) goto L10;
        addExtractorsForFileType(r3, r0);     // Catch: Throwable -> L20
    L10:
        int[] r2 = DEFAULT_EXTRACTOR_ORDER;     // Catch: Throwable -> L20
        int r4 = r2.length;     // Catch: Throwable -> L20
        int r5 = 0;
    L11:
        if (r5 >= r4) goto L17;
        int r6 = r2[r5];     // Catch: Throwable -> L20
        if (r6 == r1) goto L16;
        if (r6 == r3) goto L16;
        addExtractorsForFileType(r6, r0);     // Catch: Throwable -> L20
    L16:
        r5 = r5 + 1;     // Catch: Throwable -> L20
        goto L11
    L17:
        Extractor[] r7 = (Extractor[]) r0.toArray(new Extractor[r0.size()]);     // Catch: Throwable -> L20
        monitor-exit(this);
        return r7;
    L20:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.extractor.ExtractorsFactory
    @Deprecated
    public synchronized DefaultExtractorsFactory experimentalSetTextTrackTranscodingEnabled(boolean r1) {
        monitor-enter(this);
        this.textTrackTranscodingEnabled = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.extractor.ExtractorsFactory
    @Deprecated
    public /* bridge */ /* synthetic */ ExtractorsFactory experimentalSetTextTrackTranscodingEnabled(boolean r1) {
        return experimentalSetTextTrackTranscodingEnabled(r1);
    }

    public synchronized DefaultExtractorsFactory setAdtsExtractorFlags(int r1) {
        monitor-enter(this);
        this.adtsFlags = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setAmrExtractorFlags(int r1) {
        monitor-enter(this);
        this.amrFlags = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setConstantBitrateSeekingAlwaysEnabled(boolean r1) {
        monitor-enter(this);
        this.constantBitrateSeekingAlwaysEnabled = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setConstantBitrateSeekingEnabled(boolean r1) {
        monitor-enter(this);
        this.constantBitrateSeekingEnabled = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setFlacExtractorFlags(int r1) {
        monitor-enter(this);
        this.flacFlags = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setFragmentedMp4ExtractorFlags(int r1) {
        monitor-enter(this);
        this.fragmentedMp4Flags = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setJpegExtractorFlags(int r1) {
        monitor-enter(this);
        this.jpegFlags = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setMatroskaExtractorFlags(int r1) {
        monitor-enter(this);
        this.matroskaFlags = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setMp3ExtractorFlags(int r1) {
        monitor-enter(this);
        this.mp3Flags = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setMp4ExtractorFlags(int r1) {
        monitor-enter(this);
        this.mp4Flags = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.extractor.ExtractorsFactory
    public synchronized DefaultExtractorsFactory setSubtitleParserFactory(SubtitleParser.Factory r1) {
        monitor-enter(this);
        this.subtitleParserFactory = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    @Override // androidx.media3.extractor.ExtractorsFactory
    public /* bridge */ /* synthetic */ ExtractorsFactory setSubtitleParserFactory(SubtitleParser.Factory r1) {
        return setSubtitleParserFactory(r1);
    }

    @Deprecated
    public synchronized DefaultExtractorsFactory setTextTrackTranscodingEnabled(boolean r2) {
        monitor-enter(this);
        DefaultExtractorsFactory r0 = experimentalSetTextTrackTranscodingEnabled(r2);     // Catch: Throwable -> L6
        monitor-exit(this);
        return r0;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setTsExtractorFlags(int r1) {
        monitor-enter(this);
        this.tsFlags = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setTsExtractorMode(int r1) {
        monitor-enter(this);
        this.tsMode = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setTsExtractorTimestampSearchBytes(int r1) {
        monitor-enter(this);
        this.tsTimestampSearchBytes = r1;     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }

    public synchronized DefaultExtractorsFactory setTsSubtitleFormats(List<Format> r2) {
        monitor-enter(this);
        this.tsSubtitleFormats = ImmutableList.copyOf(r2);     // Catch: Throwable -> L6
        monitor-exit(this);
        return this;
    L6:
        th = move-exception;
        throw th;
    }
}
