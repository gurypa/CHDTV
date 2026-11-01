package es.antonio.m3u;

import android.net.Uri;
import androidx.media3.common.MimeTypes;
import androidx.media3.datasource.ByteArrayDataSource;
import androidx.media3.datasource.DataSource;
import androidx.media3.datasource.DataSpec;
import androidx.media3.datasource.TransferListener;
import androidx.media3.exoplayer.hls.playlist.HlsMediaPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsMultivariantPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylist;
import androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory;
import androidx.media3.exoplayer.upstream.ParsingLoadable;
import androidx.media3.extractor.text.ttml.TtmlNode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* JADX INFO: loaded from: classes7.dex */
public final class TvHlsOverlap implements DataSource.Factory {
    private static final Map<Object, TvHlsOverlap> ACTIVE = null;
    private static final ThreadPoolExecutor POOL = null;
    private volatile boolean cancelled;
    private final boolean fastStartup;
    private final DataSource.Factory network;
    private volatile Ticket ticket;
    private final TvHlsTrace trace;

    public static final class ChannelChangeCancellation extends InterruptedIOException {
        public ChannelChangeCancellation() {
            super("HLS source released for channel change");
        }
    }

    private final class ParserFactory implements HlsPlaylistParserFactory {
        final HlsPlaylistParserFactory delegate;
        final /* synthetic */ TvHlsOverlap this$0;

        ParserFactory(TvHlsOverlap r1, HlsPlaylistParserFactory r2) {
            this.this$0 = r1;
            this.delegate = r2;
        }

        @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory
        public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser() {
            return wrap(this.delegate.createPlaylistParser());
        }

        @Override // androidx.media3.exoplayer.hls.playlist.HlsPlaylistParserFactory
        public ParsingLoadable.Parser<HlsPlaylist> createPlaylistParser(HlsMultivariantPlaylist r2, HlsMediaPlaylist r3) {
            return wrap(this.delegate.createPlaylistParser(r2, r3));
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX INFO: renamed from: lambda$0$es-antonio-m3u-TvHlsOverlap$ParserFactory, reason: not valid java name */
        /* synthetic */ HlsPlaylist m813lambda$0$esantoniom3uTvHlsOverlap$ParserFactory(ParsingLoadable.Parser r12, Uri r13, InputStream r14) throws IOException {
            long r0 = System.nanoTime();
            long[] r2 = TvHlsUrls.mark();
            HlsPlaylist r15 = (HlsPlaylist) r12.parse(r13, r14);
            String r16 = TvHlsUrls.report(r2);
            if (r16 == null) goto L5;
            BlackBox.event("HLS_METADATA_URLS", r16);
        L5:
            boolean r5 = false;
            if ((r15 instanceof HlsMultivariantPlaylist) == false) goto L59;
            HlsMultivariantPlaylist r17 = (HlsMultivariantPlaylist) r15;
            if (TvHlsOverlap.access$4(this.this$0) == false) goto L10;
            HlsPlaylist r18 = TvHlsStartup.align(r17);
            HlsMultivariantPlaylist r7 = r18;
        L11:
            Iterator<HlsMultivariantPlaylist.Variant> r8 = r7.variants.iterator();
        L13:
            if (r8.hasNext() == false) goto L14;
            TvHlsOverlap.access$3(this.this$0).register(r8.next().url.toString(), "playlist", "video_or_muxed");
            goto L13
        L14:
            Iterator<HlsMultivariantPlaylist.Rendition> r19 = r7.audios.iterator();
        L16:
            if (r19.hasNext() == false) goto L17;
            HlsMultivariantPlaylist.Rendition r9 = r19.next();
            if (r9.url == null) goto L16;
            TvHlsOverlap.access$3(this.this$0).register(r9.url.toString(), "playlist", MimeTypes.BASE_TYPE_AUDIO);
            goto L16
        L17:
            Iterator<HlsMultivariantPlaylist.Variant> r10 = r7.variants.iterator();
            int r110 = 0;
        L19:
            if (r10.hasNext() == false) goto L20;
            HlsMultivariantPlaylist.Variant r111 = r10.next();
            if (r111.format.codecs == null) goto L53;
            if (r111.format.codecs.isEmpty() == false) goto L19;
        L53:
            r110 = r110 + 1;
            goto L19
        L20:
            BlackBox.event("HLS_PLAYLIST_MASTER", "variants=" + r7.variants.size() + " audio=" + r7.audios.size() + " variants_without_codecs=" + r110 + " parse_ms=" + ((System.nanoTime() - r0) / 1000000));
            HlsMultivariantPlaylist.Rendition r112 = null;
            if (r7.variants.isEmpty() == false) goto L23;
            String r113 = null;
        L24:
            byte r1 = -1;
            if (r113 == null) goto L43;
            Iterator<HlsMultivariantPlaylist.Rendition> r3 = r7.audios.iterator();
        L28:
            if (r3.hasNext() == false) goto L43;
            HlsMultivariantPlaylist.Rendition r4 = r3.next();
            if (r4.url == null) goto L28;
            if (r113.equals(r4.groupId) == false) goto L28;
            if ((r4.format.selectionFlags & 1) == 0) goto L38;
            byte r6 = 2;
        L41:
            if (r6 <= r1) goto L28;
            r112 = r4;
            r1 = r6;
            goto L28
        L38:
            if ((r4.format.selectionFlags & 4) == 0) goto L40;
            r6 = 1;
            goto L41
        L40:
            r6 = 0;
        L43:
            if (r112 == null) goto L94;
            StringBuilder r114 = new StringBuilder("primary_variant_group=true default=");
            if (r1 != 2) goto L47;
            r5 = true;
        L47:
            BlackBox.event("HLS_AUDIO_OVERLAP_SELECTED", r114.append(r5).toString());
            this.this$0.prime(r112.url.toString());
        L94:
            return r18;
        L23:
            r113 = r7.variants.get(0).audioGroupId;
            goto L24
        L10:
            r18 = r15;
            r7 = r17;
            goto L11
        L59:
            if ((r15 instanceof HlsMediaPlaylist) == false) goto L95;
            HlsMediaPlaylist r115 = (HlsMediaPlaylist) r15;
            String r116 = TvHlsOverlap.access$3(this.this$0).role(r13.toString());
            URI r11 = new URI(r115.baseUri);     // Catch: URISyntaxException -> L67
            int r20 = Math.max(0, r115.segments.size() - 24);     // Catch: URISyntaxException -> L67
        L63:
            if (r20 >= r115.segments.size()) goto L68;
            TvHlsOverlap.access$3(this.this$0).register(r11.resolve(r115.segments.get(r20).url).toString(), "segment", r116);     // Catch: URISyntaxException -> L67
            r20 = r20 + 1;
        L68:
            BlackBox.event("HLS_PLAYLIST_MEDIA", "segments=" + r115.segments.size() + " live=" + (!r115.hasEndTag) + " parse_ms=" + ((System.nanoTime() - r0) / 1000000));
            return r15;
        L95:
            return r15;
        }

        ParsingLoadable.Parser<HlsPlaylist> wrap(final ParsingLoadable.Parser<HlsPlaylist> r2) {
            return new TvHlsOverlap$ParserFactory$$ExternalSyntheticLambda0(this, r2);
        }
    }

    static final class Result {
        final byte[] bytes;
        final long completed;
        final Map<String, List<String>> headers;
        final Uri uri;

        Result(byte[] r1, Uri r2, Map<String, List<String>> r3) {
            this.bytes = r1;
            this.uri = r2;
            this.headers = r3;
            this.completed = System.nanoTime();
        }
    }

    private final class Source implements DataSource {
        final List<TransferListener> listeners;
        Result result;
        DataSource selected;
        final /* synthetic */ TvHlsOverlap this$0;
        long traceBegin;
        long traceBytes;
        long traceFirst;
        long traceLast;
        long traceOpen;
        String traceUrl;
        final DataSource upstream;

        Source(TvHlsOverlap r3, DataSource r4) {
            this.this$0 = r3;
            this.listeners = new ArrayList();
            this.traceFirst = -1;
            this.upstream = r4;
        }

        @Override // androidx.media3.datasource.DataSource
        public void addTransferListener(TransferListener r2) {
            this.listeners.add(r2);
            this.upstream.addTransferListener(r2);
        }

        @Override // androidx.media3.datasource.DataSource
        public void close() throws IOException {
            long r5 = -1;
        L23:
            th = move-exception;
            if (this.traceUrl != null) goto L27;
        L39:
            this.selected = null;
            this.result = null;
            this.traceUrl = null;
            throw th;
        L27:
            if (this.traceOpen < this.traceBegin) goto L39;
            TvHlsTrace r18 = TvHlsOverlap.access$3(this.this$0);
            String r3 = this.traceUrl;
            long r20 = (this.traceOpen - this.traceBegin) / 1000000;
            if (this.traceFirst < 0) goto L32;
            r5 = (this.traceFirst - this.traceBegin) / 1000000;
        L32:
            long r22 = r5;
            long r24 = (this.traceLast - this.traceOpen) / 1000000;
            long r4 = this.traceBytes;
            if (this.result == null) goto L35;
            boolean r28 = true;
        L36:
            String r6 = r18.report(r3, r20, r22, r24, r4, r28);
            if (r6 == null) goto L39;
            BlackBox.event("HLS_TRANSFER_TRACK", r6);
            goto L39
        L35:
            r28 = false;
            goto L36
        L4:
            if (this.selected == null) goto L7;
            this.selected.close();     // Catch: Throwable -> L23
        L7:
            if (this.traceUrl != null) goto L9;
        L21:
            this.selected = null;
            this.result = null;
            this.traceUrl = null;
            return;
        L9:
            if (this.traceOpen < this.traceBegin) goto L21;
            TvHlsTrace r12 = TvHlsOverlap.access$3(this.this$0);
            String r13 = this.traceUrl;
            long r14 = (this.traceOpen - this.traceBegin) / 1000000;
            if (this.traceFirst < 0) goto L14;
            r5 = (this.traceFirst - this.traceBegin) / 1000000;
        L14:
            long r19 = (this.traceLast - this.traceOpen) / 1000000;
            long r7 = this.traceBytes;
            if (this.result == null) goto L17;
            boolean r23 = true;
        L18:
            String r0 = r12.report(r13, r14, r5, r19, r7, r23);
            if (r0 == null) goto L21;
            BlackBox.event("HLS_TRANSFER_TRACK", r0);
            goto L21
        L17:
            r23 = false;
            goto L18
        }

        @Override // androidx.media3.datasource.DataSource
        public Map<String, List<String>> getResponseHeaders() {
            if (this.result == null) goto L6;
            return this.result.headers;
        L6:
            if (this.selected != null) goto L9;
            return Collections.emptyMap();
        L9:
            return this.selected.getResponseHeaders();
        }

        @Override // androidx.media3.datasource.DataSource
        public Uri getUri() {
            if (this.result == null) goto L8;
            if (this.result.uri == null) goto L8;
            return this.result.uri;
        L8:
            if (this.selected != null) goto L11;
            return null;
        L11:
            return this.selected.getUri();
        }

        @Override // androidx.media3.datasource.DataSource
        public long open(DataSpec r9) throws IOException {
            this.traceUrl = r9.uri.toString();
            this.traceBegin = System.nanoTime();
            this.traceFirst = -1;
            this.traceBytes = 0;
            Ticket r0 = TvHlsOverlap.access$0(this.this$0, r9);
            if (r0 == null) goto L38;
            long r1 = System.nanoTime();
            Result r2 = r0.result.get();     // Catch: CancellationException -> L16 ExecutionException -> L23 InterruptedException -> L34
            if (TvHlsOverlap.access$1(this.this$0) == true) goto L38;
            if ((System.nanoTime() - r2.completed) > TimeUnit.SECONDS.toNanos(1)) goto L38;
            this.result = r2;     // Catch: CancellationException -> L16 ExecutionException -> L23 InterruptedException -> L34
            this.selected = new ByteArrayDataSource(r2.bytes);     // Catch: CancellationException -> L16 ExecutionException -> L23 InterruptedException -> L34
            Iterator<TransferListener> r3 = this.listeners.iterator();     // Catch: CancellationException -> L16 ExecutionException -> L23 InterruptedException -> L34
        L11:
            if (r3.hasNext() == false) goto L12;
            this.selected.addTransferListener(r3.next());     // Catch: CancellationException -> L16 ExecutionException -> L23 InterruptedException -> L34
            goto L11
        L12:
            BlackBox.event("HLS_AUDIO_OVERLAP_USED", "wait_ms=" + ((System.nanoTime() - r1) / 1000000) + " bytes=" + r2.bytes.length);     // Catch: CancellationException -> L16 ExecutionException -> L23 InterruptedException -> L34
            long r4 = this.selected.open(r9);     // Catch: CancellationException -> L16 ExecutionException -> L23 InterruptedException -> L34
            this.traceOpen = System.nanoTime();     // Catch: CancellationException -> L16 ExecutionException -> L23 InterruptedException -> L34
            this.traceLast = this.traceOpen;     // Catch: CancellationException -> L16 ExecutionException -> L23 InterruptedException -> L34
            return r4;
        L23:
            e = move-exception;
            if (TvHlsOverlap.access$1(this.this$0) == true) goto L33;
            Throwable r10 = e.getCause();
            BlackBox.event("HLS_AUDIO_OVERLAP_ERROR", "type=" + r10.getClass().getSimpleName());
            if ((r10 instanceof IOException) == false) goto L31;
            throw ((IOException) r10);
        L31:
            throw new IOException("HLS audio playlist", r10);
        L33:
            throw new ChannelChangeCancellation();
        L35:
            Thread.currentThread().interrupt();
            throw new InterruptedIOException();
        L18:
            if (TvHlsOverlap.access$1(this.this$0) == false) goto L22;
            throw new ChannelChangeCancellation();
        L22:
            throw new InterruptedIOException();
        L38:
            if (TvHlsOverlap.access$1(this.this$0) == true) goto L47;
            this.selected = this.upstream;
            long r5 = this.selected.open(r9);     // Catch: IOException -> L43
            this.traceOpen = System.nanoTime();
            this.traceLast = this.traceOpen;
            return r5;
        L43:
            e = move-exception;
            throw TvHlsOverlap.access$2(this.this$0, e, TtmlNode.TEXT_EMPHASIS_MARK_OPEN);
        L47:
            throw new ChannelChangeCancellation();
        }

        @Override // androidx.media3.common.DataReader
        public int read(byte[] r3, int r4, int r5) throws IOException {
            if (TvHlsOverlap.access$1(this.this$0) == true) goto L16;
            int r6 = this.selected.read(r3, r4, r5);     // Catch: IOException -> L12
            this.traceLast = System.nanoTime();
            if (r6 > 0) goto L8;
        L11:
            return r6;
        L8:
            if (this.traceFirst >= 0) goto L10;
            this.traceFirst = this.traceLast;
        L10:
            this.traceBytes += (long) r6;
        L12:
            e = move-exception;
            throw TvHlsOverlap.access$2(this.this$0, e, "read");
        L16:
            throw new ChannelChangeCancellation();
        }
    }

    static final class Ticket {
        boolean claimed;
        final FutureTask<Result> result;
        final String url;

        Ticket(String r1, FutureTask<Result> r2) {
            this.url = r1;
            this.result = r2;
        }
    }

    static {
        POOL = new ThreadPoolExecutor(0, 2, 20, TimeUnit.SECONDS, new SynchronousQueue(), new TvHlsOverlap$$ExternalSyntheticLambda1());
        ACTIVE = new WeakHashMap();
    }

    public TvHlsOverlap(DataSource.Factory r1) {
        this(r1, r1);
    }

    public TvHlsOverlap(Object r2, DataSource.Factory r3) {
        this(r2, r3, false);
    }

    public TvHlsOverlap(Object r2, DataSource.Factory r3, boolean r4) {
        this.trace = new TvHlsTrace();
        this.fastStartup = r4;
        this.network = r3;
        monitor-enter(TvHlsOverlap.class);
        cancel(r2);     // Catch: Throwable -> L7
        ACTIVE.put(r2, this);     // Catch: Throwable -> L7
        monitor-exit(TvHlsOverlap.class);     // Catch: Throwable -> L7
        return;
    L7:
        th = move-exception;
        throw th;
    }

    static /* synthetic */ Ticket access$0(TvHlsOverlap r0, DataSpec r1) {
        return r0.claim(r1);
    }

    static /* synthetic */ boolean access$1(TvHlsOverlap r0) {
        return r0.cancelled;
    }

    static /* synthetic */ IOException access$2(TvHlsOverlap r0, IOException r1, String r2) {
        return r0.sourceFailure(r1, r2);
    }

    static /* synthetic */ TvHlsTrace access$3(TvHlsOverlap r0) {
        return r0.trace;
    }

    static /* synthetic */ boolean access$4(TvHlsOverlap r0) {
        return r0.fastStartup;
    }

    public static synchronized void cancel(Object r2) {
        monitor-enter(TvHlsOverlap.class);
        TvHlsOverlap r3 = ACTIVE.remove(r2);     // Catch: Throwable -> L11
        if (r3 == null) goto L9;
        r3.cancelled = true;     // Catch: Throwable -> L11
        Ticket r4 = r3.ticket;     // Catch: Throwable -> L11
        if (r4 == null) goto L9;
        r4.result.cancel(true);     // Catch: Throwable -> L11
    L9:
        monitor-exit(TvHlsOverlap.class);
        return;
    L11:
        th = move-exception;
        throw th;
    }

    private synchronized Ticket claim(DataSpec r6) {
        monitor-enter(this);
        Ticket r0 = this.ticket;     // Catch: Throwable -> L27
        if (this.cancelled == true) goto L24;
        if (r0 == null) goto L24;
        if (r0.claimed == true) goto L24;
        if (r6.position != 0) goto L24;
        if (r6.length != (-1)) goto L24;
        if (r6.httpMethod != 1) goto L24;
        if (r6.httpBody != null) goto L24;
        if (r6.httpRequestHeaders.isEmpty() == false) goto L24;
        if (r0.url.equals(r6.uri.toString()) == false) goto L24;
        r0.claimed = true;     // Catch: Throwable -> L27
        monitor-exit(this);
        return r0;
    L24:
        monitor-exit(this);
        return null;
    L27:
        th = move-exception;
        throw th;
    }

    static boolean eligibleUrl(String r4) {
        URI r1 = new URI(r4);     // Catch: Exception -> L16
        if ("https".equalsIgnoreCase(r1.getScheme()) == true) goto L8;
        if ("http".equalsIgnoreCase(r1.getScheme()) == true) goto L8;
    L15:
        return false;
    L8:
        if (r1.getHost() == null) goto L15;
        if (r1.getUserInfo() != null) goto L15;
        if (r4.contains("_HLS_") == true) goto L15;
        return true;
    L17:
        return false;
    }

    public static boolean expectedCancellation(String r1, Throwable r2) {
        if ("MEDIA_LOAD_ERROR".equals(r1) == true) goto L5;
        return false;
    L5:
        if (r2 == null) goto L6;
        if ((r2 instanceof ChannelChangeCancellation) == true) goto L9;
        r2 = r2.getCause();
        goto L5
    L9:
        BlackBox.event("MEDIA_LOAD_CANCELLED", "reason=source_released");
        return true;
    L6:
        return false;
    }

    private Result fetch(String r11) throws IOException {
        long r0 = System.nanoTime();
        DataSource r2 = this.network.createDataSource();
    L34:
        th = move-exception;
        r2.close();
        throw th;
    L4:
        if (this.cancelled == true) goto L33;
        if (Thread.currentThread().isInterrupted() == true) goto L33;
        r2.open(new DataSpec.Builder().setUri(r11).setFlags(1).build());     // Catch: Throwable -> L34
        ByteArrayOutputStream r12 = new ByteArrayOutputStream();     // Catch: Throwable -> L34
        byte[] r5 = new byte[8192];     // Catch: Throwable -> L34
    L8:
        int r7 = r2.read(r5, 0, 8192);     // Catch: Throwable -> L34
        if (r7 == (-1)) goto L10;
        if (this.cancelled == true) goto L43;
        if (Thread.currentThread().isInterrupted() == true) goto L43;
        if (r7 == 0) goto L30;
        if ((r12.size() + r7) > 2097152) goto L30;
        r12.write(r5, 0, r7);     // Catch: Throwable -> L34
    L30:
        throw new IOException("Audio playlist size/progress");     // Catch: Throwable -> L34
    L43:
        throw new InterruptedIOException();     // Catch: Throwable -> L34
    L10:
        byte[] r13 = r12.toByteArray();     // Catch: Throwable -> L34
        if (r13.length < 7) goto L20;
        if (r13[0] != 35) goto L20;
        if (r13[1] != 69) goto L20;
        Result r3 = new Result(r13, r2.getUri(), r2.getResponseHeaders());     // Catch: Throwable -> L34
        BlackBox.event("HLS_AUDIO_OVERLAP_FETCH", "duration_ms=" + ((System.nanoTime() - r0) / 1000000) + " bytes=" + r13.length);     // Catch: Throwable -> L34
        r2.close();
        return r3;
    L20:
        throw new IOException("Not an HLS playlist");     // Catch: Throwable -> L34
    L33:
        throw new InterruptedIOException();     // Catch: Throwable -> L34
    }

    static /* synthetic */ Thread lambda$0(Runnable r2) {
        Thread r0 = new Thread(r2, "CHDTVHlsAudio");
        r0.setDaemon(true);
        return r0;
    }

    private IOException sourceFailure(IOException r4, String r5) {
        if (this.cancelled == true) goto L5;
        int r0 = 0;
        Throwable r1 = r4;
    L8:
        if (r0 >= 8) goto L16;
        if (r1.getCause() == null) goto L16;
        if (r1.getCause() == r1) goto L16;
        r1 = r1.getCause();
        r0 = r0 + 1;
    L16:
        if ((r1 instanceof UnknownHostException) == false) goto L19;
        String r2 = "DNS";
    L25:
        BlackBox.event("HLS_NETWORK_FAILURE", "phase=" + r5 + " kind=" + r2 + " source_active=true");
        return r4;
    L19:
        if ((r1 instanceof ProtocolException) == false) goto L22;
        r2 = "HTTP_PROTOCOL";
        goto L25
    L22:
        if ((r1 instanceof SocketTimeoutException) == false) goto L24;
        r2 = "TIMEOUT";
        goto L25
    L24:
        r2 = "IO";
        goto L25
    L5:
        return new ChannelChangeCancellation();
    }

    @Override // androidx.media3.datasource.DataSource.Factory
    public DataSource createDataSource() {
        return new Source(this, this.network.createDataSource());
    }

    /* JADX INFO: renamed from: lambda$1$es-antonio-m3u-TvHlsOverlap, reason: not valid java name */
    /* synthetic */ Result m812lambda$1$esantoniom3uTvHlsOverlap(String r1) throws Exception {
        return fetch(r1);
    }

    public HlsPlaylistParserFactory parser(HlsPlaylistParserFactory r2) {
        return new ParserFactory(this, r2);
    }

    synchronized void prime(final String r3) {
        monitor-enter(this);
    L20:
        th = move-exception;
        throw th;
    L4:
        if (this.cancelled == false) goto L6;
    L18:
        monitor-exit(this);
        return;
    L6:
        if (this.ticket != null) goto L18;
        if (eligibleUrl(r3) == false) goto L18;
        FutureTask r0 = new FutureTask(new TvHlsOverlap$$ExternalSyntheticLambda0(this, r3));     // Catch: Throwable -> L20
        this.ticket = new Ticket(r3, r0);     // Catch: Throwable -> L20
        POOL.execute(r0);     // Catch: RejectedExecutionException -> L13 Throwable -> L20
    L16:
        monitor-exit(this);
        return;
    L15:
        this.ticket = null;     // Catch: Throwable -> L20
        r0.cancel(false);     // Catch: Throwable -> L20
        BlackBox.event("HLS_AUDIO_OVERLAP_SKIPPED", "workers_busy=true");     // Catch: Throwable -> L20
        goto L16
    }
}
