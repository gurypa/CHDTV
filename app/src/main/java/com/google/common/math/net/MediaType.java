package com.google.common.net;

import com.google.common.base.Ascii;
import com.google.common.base.CharMatcher;
import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.Immutable;
import com.google.errorprone.annotations.concurrent.LazyInit;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import javax.annotation.CheckForNull;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes.dex */
@Immutable
@ElementTypesAreNonnullByDefault
public final class MediaType {
    public static final MediaType AAC_AUDIO = null;
    public static final MediaType ANY_APPLICATION_TYPE = null;
    public static final MediaType ANY_AUDIO_TYPE = null;
    public static final MediaType ANY_FONT_TYPE = null;
    public static final MediaType ANY_IMAGE_TYPE = null;
    public static final MediaType ANY_TEXT_TYPE = null;
    public static final MediaType ANY_TYPE = null;
    public static final MediaType ANY_VIDEO_TYPE = null;
    public static final MediaType APPLE_MOBILE_CONFIG = null;
    public static final MediaType APPLE_PASSBOOK = null;
    public static final MediaType APPLICATION_BINARY = null;
    private static final String APPLICATION_TYPE = "application";
    public static final MediaType APPLICATION_XML_UTF_8 = null;
    public static final MediaType ATOM_UTF_8 = null;
    private static final String AUDIO_TYPE = "audio";
    public static final MediaType BASIC_AUDIO = null;
    public static final MediaType BMP = null;
    public static final MediaType BZIP2 = null;
    public static final MediaType CACHE_MANIFEST_UTF_8 = null;
    private static final String CHARSET_ATTRIBUTE = "charset";
    public static final MediaType CRW = null;
    public static final MediaType CSS_UTF_8 = null;
    public static final MediaType CSV_UTF_8 = null;
    public static final MediaType DART_UTF_8 = null;
    public static final MediaType EOT = null;
    public static final MediaType EPUB = null;
    public static final MediaType FLV_VIDEO = null;
    public static final MediaType FONT_COLLECTION = null;
    public static final MediaType FONT_OTF = null;
    public static final MediaType FONT_SFNT = null;
    public static final MediaType FONT_TTF = null;
    private static final String FONT_TYPE = "font";
    public static final MediaType FONT_WOFF = null;
    public static final MediaType FONT_WOFF2 = null;
    public static final MediaType FORM_DATA = null;
    public static final MediaType GEO_JSON = null;
    public static final MediaType GIF = null;
    public static final MediaType GZIP = null;
    public static final MediaType HAL_JSON = null;
    public static final MediaType HEIF = null;
    public static final MediaType HTML_UTF_8 = null;
    public static final MediaType ICO = null;
    private static final String IMAGE_TYPE = "image";
    public static final MediaType I_CALENDAR_UTF_8 = null;
    public static final MediaType JAVASCRIPT_UTF_8 = null;
    public static final MediaType JOSE = null;
    public static final MediaType JOSE_JSON = null;
    public static final MediaType JP2K = null;
    public static final MediaType JPEG = null;
    public static final MediaType JSON_UTF_8 = null;
    public static final MediaType JWT = null;
    public static final MediaType KEY_ARCHIVE = null;
    public static final MediaType KML = null;
    public static final MediaType KMZ = null;
    private static final Map<MediaType, MediaType> KNOWN_TYPES = null;
    public static final MediaType L16_AUDIO = null;
    public static final MediaType L24_AUDIO = null;
    private static final CharMatcher LINEAR_WHITE_SPACE = null;
    public static final MediaType MANIFEST_JSON_UTF_8 = null;
    public static final MediaType MBOX = null;
    public static final MediaType MD_UTF_8 = null;
    public static final MediaType MEDIA_PRESENTATION_DESCRIPTION = null;
    public static final MediaType MICROSOFT_EXCEL = null;
    public static final MediaType MICROSOFT_OUTLOOK = null;
    public static final MediaType MICROSOFT_POWERPOINT = null;
    public static final MediaType MICROSOFT_WORD = null;
    public static final MediaType MP4_AUDIO = null;
    public static final MediaType MP4_VIDEO = null;
    public static final MediaType MPEG_AUDIO = null;
    public static final MediaType MPEG_VIDEO = null;
    public static final MediaType NACL_APPLICATION = null;
    public static final MediaType NACL_PORTABLE_APPLICATION = null;
    public static final MediaType OCTET_STREAM = null;
    public static final MediaType OGG_AUDIO = null;
    public static final MediaType OGG_CONTAINER = null;
    public static final MediaType OGG_VIDEO = null;
    public static final MediaType OOXML_DOCUMENT = null;
    public static final MediaType OOXML_PRESENTATION = null;
    public static final MediaType OOXML_SHEET = null;
    public static final MediaType OPENDOCUMENT_GRAPHICS = null;
    public static final MediaType OPENDOCUMENT_PRESENTATION = null;
    public static final MediaType OPENDOCUMENT_SPREADSHEET = null;
    public static final MediaType OPENDOCUMENT_TEXT = null;
    public static final MediaType OPENSEARCH_DESCRIPTION_UTF_8 = null;
    private static final Joiner.MapJoiner PARAMETER_JOINER = null;
    public static final MediaType PDF = null;
    public static final MediaType PLAIN_TEXT_UTF_8 = null;
    public static final MediaType PNG = null;
    public static final MediaType POSTSCRIPT = null;
    public static final MediaType PROTOBUF = null;
    public static final MediaType PSD = null;
    public static final MediaType QUICKTIME = null;
    private static final CharMatcher QUOTED_TEXT_MATCHER = null;
    public static final MediaType RDF_XML_UTF_8 = null;
    public static final MediaType RTF_UTF_8 = null;
    public static final MediaType SFNT = null;
    public static final MediaType SHOCKWAVE_FLASH = null;
    public static final MediaType SKETCHUP = null;
    public static final MediaType SOAP_XML_UTF_8 = null;
    public static final MediaType SVG_UTF_8 = null;
    public static final MediaType TAR = null;
    public static final MediaType TEXT_JAVASCRIPT_UTF_8 = null;
    private static final String TEXT_TYPE = "text";
    public static final MediaType THREE_GPP2_VIDEO = null;
    public static final MediaType THREE_GPP_VIDEO = null;
    public static final MediaType TIFF = null;
    private static final CharMatcher TOKEN_MATCHER = null;
    public static final MediaType TSV_UTF_8 = null;
    private static final ImmutableListMultimap<String, String> UTF_8_CONSTANT_PARAMETERS = null;
    public static final MediaType VCARD_UTF_8 = null;
    private static final String VIDEO_TYPE = "video";
    public static final MediaType VND_REAL_AUDIO = null;
    public static final MediaType VND_WAVE_AUDIO = null;
    public static final MediaType VORBIS_AUDIO = null;
    public static final MediaType VTT_UTF_8 = null;
    public static final MediaType WASM_APPLICATION = null;
    public static final MediaType WAX_AUDIO = null;
    public static final MediaType WEBM_AUDIO = null;
    public static final MediaType WEBM_VIDEO = null;
    public static final MediaType WEBP = null;
    private static final String WILDCARD = "*";
    public static final MediaType WMA_AUDIO = null;
    public static final MediaType WML_UTF_8 = null;
    public static final MediaType WMV = null;
    public static final MediaType WOFF = null;
    public static final MediaType WOFF2 = null;
    public static final MediaType XHTML_UTF_8 = null;
    public static final MediaType XML_UTF_8 = null;
    public static final MediaType XRD_UTF_8 = null;
    public static final MediaType ZIP = null;

    @LazyInit
    private int hashCode;
    private final ImmutableListMultimap<String, String> parameters;

    @CheckForNull
    @LazyInit
    private Optional<Charset> parsedCharset;
    private final String subtype;

    @CheckForNull
    @LazyInit
    private String toString;
    private final String type;

    private static final class Tokenizer {
        final String input;
        int position;

        Tokenizer(String r2) {
            this.position = 0;
            this.input = r2;
        }

        char consumeCharacter(char r3) {
            Preconditions.checkState(hasMore());
            if (previewChar() != r3) goto L5;
            boolean r0 = true;
        L6:
            Preconditions.checkState(r0);
            this.position++;
            return r3;
        L5:
            r0 = false;
            goto L6
        }

        char consumeCharacter(CharMatcher r3) {
            Preconditions.checkState(hasMore());
            char r0 = previewChar();
            Preconditions.checkState(r3.matches(r0));
            this.position++;
            return r0;
        }

        String consumeToken(CharMatcher r4) {
            int r0 = this.position;
            String r1 = consumeTokenIfPresent(r4);
            if (this.position == r0) goto L5;
            boolean r2 = true;
        L6:
            Preconditions.checkState(r2);
            return r1;
        L5:
            r2 = false;
            goto L6
        }

        String consumeTokenIfPresent(CharMatcher r4) {
            Preconditions.checkState(hasMore());
            int r0 = this.position;
            this.position = r4.negate().indexIn(this.input, r0);
            if (hasMore() == false) goto L6;
            return this.input.substring(r0, this.position);
        L6:
            return this.input.substring(r0);
        }

        boolean hasMore() {
            if (this.position >= 0) goto L5;
        L7:
            return false;
        L5:
            if (this.position >= this.input.length()) goto L7;
            return true;
        }

        char previewChar() {
            Preconditions.checkState(hasMore());
            return this.input.charAt(this.position);
        }
    }

    static {
        UTF_8_CONSTANT_PARAMETERS = ImmutableListMultimap.m226of(CHARSET_ATTRIBUTE, Ascii.toLowerCase(StandardCharsets.UTF_8.name()));
        TOKEN_MATCHER = CharMatcher.ascii().and(CharMatcher.javaIsoControl().negate()).and(CharMatcher.isNot(' ')).and(CharMatcher.noneOf("()<>@,;:\\\"/[]?="));
        QUOTED_TEXT_MATCHER = CharMatcher.ascii().and(CharMatcher.noneOf("\"\\\r"));
        LINEAR_WHITE_SPACE = CharMatcher.anyOf(" \t\r\n");
        KNOWN_TYPES = Maps.newHashMap();
        ANY_TYPE = createConstant(WILDCARD, WILDCARD);
        ANY_TEXT_TYPE = createConstant("text", WILDCARD);
        ANY_IMAGE_TYPE = createConstant("image", WILDCARD);
        ANY_AUDIO_TYPE = createConstant("audio", WILDCARD);
        ANY_VIDEO_TYPE = createConstant("video", WILDCARD);
        ANY_APPLICATION_TYPE = createConstant("application", WILDCARD);
        ANY_FONT_TYPE = createConstant(FONT_TYPE, WILDCARD);
        CACHE_MANIFEST_UTF_8 = createConstantUtf8("text", "cache-manifest");
        CSS_UTF_8 = createConstantUtf8("text", "css");
        CSV_UTF_8 = createConstantUtf8("text", "csv");
        HTML_UTF_8 = createConstantUtf8("text", "html");
        I_CALENDAR_UTF_8 = createConstantUtf8("text", "calendar");
        MD_UTF_8 = createConstantUtf8("text", "markdown");
        PLAIN_TEXT_UTF_8 = createConstantUtf8("text", "plain");
        TEXT_JAVASCRIPT_UTF_8 = createConstantUtf8("text", "javascript");
        TSV_UTF_8 = createConstantUtf8("text", "tab-separated-values");
        VCARD_UTF_8 = createConstantUtf8("text", "vcard");
        WML_UTF_8 = createConstantUtf8("text", "vnd.wap.wml");
        XML_UTF_8 = createConstantUtf8("text", "xml");
        VTT_UTF_8 = createConstantUtf8("text", "vtt");
        BMP = createConstant("image", "bmp");
        CRW = createConstant("image", "x-canon-crw");
        GIF = createConstant("image", "gif");
        ICO = createConstant("image", "vnd.microsoft.icon");
        JPEG = createConstant("image", "jpeg");
        PNG = createConstant("image", "png");
        PSD = createConstant("image", "vnd.adobe.photoshop");
        SVG_UTF_8 = createConstantUtf8("image", "svg+xml");
        TIFF = createConstant("image", "tiff");
        WEBP = createConstant("image", "webp");
        HEIF = createConstant("image", "heif");
        JP2K = createConstant("image", "jp2");
        MP4_AUDIO = createConstant("audio", "mp4");
        MPEG_AUDIO = createConstant("audio", "mpeg");
        OGG_AUDIO = createConstant("audio", "ogg");
        WEBM_AUDIO = createConstant("audio", "webm");
        L16_AUDIO = createConstant("audio", "l16");
        L24_AUDIO = createConstant("audio", "l24");
        BASIC_AUDIO = createConstant("audio", "basic");
        AAC_AUDIO = createConstant("audio", "aac");
        VORBIS_AUDIO = createConstant("audio", "vorbis");
        WMA_AUDIO = createConstant("audio", "x-ms-wma");
        WAX_AUDIO = createConstant("audio", "x-ms-wax");
        VND_REAL_AUDIO = createConstant("audio", "vnd.rn-realaudio");
        VND_WAVE_AUDIO = createConstant("audio", "vnd.wave");
        MP4_VIDEO = createConstant("video", "mp4");
        MPEG_VIDEO = createConstant("video", "mpeg");
        OGG_VIDEO = createConstant("video", "ogg");
        QUICKTIME = createConstant("video", "quicktime");
        WEBM_VIDEO = createConstant("video", "webm");
        WMV = createConstant("video", "x-ms-wmv");
        FLV_VIDEO = createConstant("video", "x-flv");
        THREE_GPP_VIDEO = createConstant("video", "3gpp");
        THREE_GPP2_VIDEO = createConstant("video", "3gpp2");
        APPLICATION_XML_UTF_8 = createConstantUtf8("application", "xml");
        ATOM_UTF_8 = createConstantUtf8("application", "atom+xml");
        BZIP2 = createConstant("application", "x-bzip2");
        DART_UTF_8 = createConstantUtf8("application", "dart");
        APPLE_PASSBOOK = createConstant("application", "vnd.apple.pkpass");
        EOT = createConstant("application", "vnd.ms-fontobject");
        EPUB = createConstant("application", "epub+zip");
        FORM_DATA = createConstant("application", "x-www-form-urlencoded");
        KEY_ARCHIVE = createConstant("application", "pkcs12");
        APPLICATION_BINARY = createConstant("application", "binary");
        GEO_JSON = createConstant("application", "geo+json");
        GZIP = createConstant("application", "x-gzip");
        HAL_JSON = createConstant("application", "hal+json");
        JAVASCRIPT_UTF_8 = createConstantUtf8("application", "javascript");
        JOSE = createConstant("application", "jose");
        JOSE_JSON = createConstant("application", "jose+json");
        JSON_UTF_8 = createConstantUtf8("application", "json");
        JWT = createConstant("application", "jwt");
        MANIFEST_JSON_UTF_8 = createConstantUtf8("application", "manifest+json");
        KML = createConstant("application", "vnd.google-earth.kml+xml");
        KMZ = createConstant("application", "vnd.google-earth.kmz");
        MBOX = createConstant("application", "mbox");
        APPLE_MOBILE_CONFIG = createConstant("application", "x-apple-aspen-config");
        MICROSOFT_EXCEL = createConstant("application", "vnd.ms-excel");
        MICROSOFT_OUTLOOK = createConstant("application", "vnd.ms-outlook");
        MICROSOFT_POWERPOINT = createConstant("application", "vnd.ms-powerpoint");
        MICROSOFT_WORD = createConstant("application", "msword");
        MEDIA_PRESENTATION_DESCRIPTION = createConstant("application", "dash+xml");
        WASM_APPLICATION = createConstant("application", "wasm");
        NACL_APPLICATION = createConstant("application", "x-nacl");
        NACL_PORTABLE_APPLICATION = createConstant("application", "x-pnacl");
        OCTET_STREAM = createConstant("application", "octet-stream");
        OGG_CONTAINER = createConstant("application", "ogg");
        OOXML_DOCUMENT = createConstant("application", "vnd.openxmlformats-officedocument.wordprocessingml.document");
        OOXML_PRESENTATION = createConstant("application", "vnd.openxmlformats-officedocument.presentationml.presentation");
        OOXML_SHEET = createConstant("application", "vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        OPENDOCUMENT_GRAPHICS = createConstant("application", "vnd.oasis.opendocument.graphics");
        OPENDOCUMENT_PRESENTATION = createConstant("application", "vnd.oasis.opendocument.presentation");
        OPENDOCUMENT_SPREADSHEET = createConstant("application", "vnd.oasis.opendocument.spreadsheet");
        OPENDOCUMENT_TEXT = createConstant("application", "vnd.oasis.opendocument.text");
        OPENSEARCH_DESCRIPTION_UTF_8 = createConstantUtf8("application", "opensearchdescription+xml");
        PDF = createConstant("application", "pdf");
        POSTSCRIPT = createConstant("application", "postscript");
        PROTOBUF = createConstant("application", "protobuf");
        RDF_XML_UTF_8 = createConstantUtf8("application", "rdf+xml");
        RTF_UTF_8 = createConstantUtf8("application", "rtf");
        SFNT = createConstant("application", "font-sfnt");
        SHOCKWAVE_FLASH = createConstant("application", "x-shockwave-flash");
        SKETCHUP = createConstant("application", "vnd.sketchup.skp");
        SOAP_XML_UTF_8 = createConstantUtf8("application", "soap+xml");
        TAR = createConstant("application", "x-tar");
        WOFF = createConstant("application", "font-woff");
        WOFF2 = createConstant("application", "font-woff2");
        XHTML_UTF_8 = createConstantUtf8("application", "xhtml+xml");
        XRD_UTF_8 = createConstantUtf8("application", "xrd+xml");
        ZIP = createConstant("application", "zip");
        FONT_COLLECTION = createConstant(FONT_TYPE, "collection");
        FONT_OTF = createConstant(FONT_TYPE, "otf");
        FONT_SFNT = createConstant(FONT_TYPE, "sfnt");
        FONT_TTF = createConstant(FONT_TYPE, "ttf");
        FONT_WOFF = createConstant(FONT_TYPE, "woff");
        FONT_WOFF2 = createConstant(FONT_TYPE, "woff2");
        PARAMETER_JOINER = Joiner.m184on("; ").withKeyValueSeparator("=");
    }

    private MediaType(String r1, String r2, ImmutableListMultimap<String, String> r3) {
        this.type = r1;
        this.subtype = r2;
        this.parameters = r3;
    }

    private static MediaType addKnownType(MediaType r1) {
        KNOWN_TYPES.put(r1, r1);
        return r1;
    }

    private String computeToString() {
        StringBuilder r0 = new StringBuilder().append(this.type).append('/').append(this.subtype);
        if (this.parameters.isEmpty() == true) goto L6;
        r0.append("; ");
        ListMultimap r1 = Multimaps.transformValues(this.parameters, new MediaType$$ExternalSyntheticLambda0());
        PARAMETER_JOINER.appendTo(r0, r1.entries());
    L6:
        return r0.toString();
    }

    private static void consumeSeparator(Tokenizer r1, char r2) {
        r1.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
        r1.consumeCharacter(r2);
        r1.consumeTokenIfPresent(LINEAR_WHITE_SPACE);
    }

    public static MediaType create(String r2, String r3) {
        MediaType r0 = create(r2, r3, ImmutableListMultimap.m225of());
        r0.parsedCharset = Optional.absent();
        return r0;
    }

    private static MediaType create(String r7, String r8, Multimap<String, String> r9) {
        Preconditions.checkNotNull(r7);
        Preconditions.checkNotNull(r8);
        Preconditions.checkNotNull(r9);
        String r0 = normalizeToken(r7);
        String r1 = normalizeToken(r8);
        if (WILDCARD.equals(r0) == true) goto L5;
    L8:
        boolean r2 = true;
    L9:
        Preconditions.checkArgument(r2, "A wildcard type cannot be used with a non-wildcard subtype");
        ImmutableListMultimap.Builder r3 = ImmutableListMultimap.builder();
        Iterator<Map.Entry<String, String>> r4 = r9.entries().iterator();
    L11:
        if (r4.hasNext() == false) goto L13;
        Map.Entry<String, String> r5 = r4.next();
        String r6 = normalizeToken(r5.getKey());
        r3.put(r6, normalizeParameterValue(r6, r5.getValue()));
        goto L11
    L13:
        MediaType r10 = new MediaType(r0, r1, r3.build());
        return (MediaType) MoreObjects.firstNonNull(KNOWN_TYPES.get(r10), r10);
    L5:
        if (WILDCARD.equals(r1) == true) goto L8;
        r2 = false;
        goto L9
    }

    static MediaType createApplicationType(String r1) {
        return create("application", r1);
    }

    static MediaType createAudioType(String r1) {
        return create("audio", r1);
    }

    private static MediaType createConstant(String r2, String r3) {
        MediaType r0 = addKnownType(new MediaType(r2, r3, ImmutableListMultimap.m225of()));
        r0.parsedCharset = Optional.absent();
        return r0;
    }

    private static MediaType createConstantUtf8(String r2, String r3) {
        MediaType r0 = addKnownType(new MediaType(r2, r3, UTF_8_CONSTANT_PARAMETERS));
        r0.parsedCharset = Optional.m185of(StandardCharsets.UTF_8);
        return r0;
    }

    static MediaType createFontType(String r1) {
        return create(FONT_TYPE, r1);
    }

    static MediaType createImageType(String r1) {
        return create("image", r1);
    }

    static MediaType createTextType(String r1) {
        return create("text", r1);
    }

    static MediaType createVideoType(String r1) {
        return create("video", r1);
    }

    private static String escapeAndQuote(String r6) {
        StringBuilder r0 = new StringBuilder(r6.length() + 16).append(Typography.quote);
        int r2 = 0;
    L4:
        if (r2 >= r6.length()) goto L12;
        char r3 = r6.charAt(r2);
        if (r3 == '\r') goto L9;
        if (r3 == '\\') goto L9;
        if (r3 == '\"') goto L9;
    L10:
        r0.append(r3);
        r2 = r2 + 1;
    L9:
        r0.append('\\');
        goto L10
    L12:
        return r0.append(Typography.quote).toString();
    }

    static /* synthetic */ String lambda$computeToString$0(String r1) {
        if (TOKEN_MATCHER.matchesAllOf(r1) == false) goto L8;
        if (r1.isEmpty() == true) goto L8;
        return r1;
    L8:
        return escapeAndQuote(r1);
    }

    private static String normalizeParameterValue(String r2, String r3) {
        Preconditions.checkNotNull(r3);
        Preconditions.checkArgument(CharMatcher.ascii().matchesAllOf(r3), "parameter values must be ASCII: %s", r3);
        if (CHARSET_ATTRIBUTE.equals(r2) == false) goto L6;
        return Ascii.toLowerCase(r3);
    L6:
        return r3;
    }

    private static String normalizeToken(String r1) {
        Preconditions.checkArgument(TOKEN_MATCHER.matchesAllOf(r1));
        Preconditions.checkArgument(!r1.isEmpty());
        return Ascii.toLowerCase(r1);
    }

    private Map<String, ImmutableMultiset<String>> parametersAsMap() {
        return Maps.transformValues(this.parameters.asMap(), new MediaType$$ExternalSyntheticLambda1());
    }

    public static MediaType parse(String r9) {
        Preconditions.checkNotNull(r9);
        Tokenizer r0 = new Tokenizer(r9);
        String r1 = r0.consumeToken(TOKEN_MATCHER);     // Catch: IllegalStateException -> L20
        consumeSeparator(r0, '/');     // Catch: IllegalStateException -> L20
        String r2 = r0.consumeToken(TOKEN_MATCHER);     // Catch: IllegalStateException -> L20
        ImmutableListMultimap.Builder r3 = ImmutableListMultimap.builder();     // Catch: IllegalStateException -> L20
    L5:
        if (r0.hasMore() == false) goto L18;
        consumeSeparator(r0, ';');     // Catch: IllegalStateException -> L20
        String r4 = r0.consumeToken(TOKEN_MATCHER);     // Catch: IllegalStateException -> L20
        consumeSeparator(r0, '=');     // Catch: IllegalStateException -> L20
        if ('\"' != r0.previewChar()) goto L16;
        r0.consumeCharacter(Typography.quote);     // Catch: IllegalStateException -> L20
        StringBuilder r5 = new StringBuilder();     // Catch: IllegalStateException -> L20
    L10:
        if ('\"' == r0.previewChar()) goto L15;
        if ('\\' == r0.previewChar()) goto L13;
        r5.append(r0.consumeToken(QUOTED_TEXT_MATCHER));     // Catch: IllegalStateException -> L20
        goto L10
    L13:
        r0.consumeCharacter('\\');     // Catch: IllegalStateException -> L20
        r5.append(r0.consumeCharacter(CharMatcher.ascii()));     // Catch: IllegalStateException -> L20
        goto L10
    L15:
        String r7 = r5.toString();     // Catch: IllegalStateException -> L20
        r0.consumeCharacter(Typography.quote);     // Catch: IllegalStateException -> L20
    L17:
        r3.put(r4, r7);     // Catch: IllegalStateException -> L20
        goto L5
    L16:
        r7 = r0.consumeToken(TOKEN_MATCHER);     // Catch: IllegalStateException -> L20
        goto L17
    L18:
        return create(r1, r2, r3.build());
    L20:
        e = move-exception;
        throw new IllegalArgumentException("Could not parse '" + r9 + "'", e);
    }

    public Optional<Charset> charset() {
        Optional<Charset> r0 = this.parsedCharset;
        if (r0 != null) goto L16;
        String r1 = null;
        r0 = Optional.absent();
        UnmodifiableIterator<String> r2 = this.parameters.get(CHARSET_ATTRIBUTE).iterator();
    L6:
        if (r2.hasNext() == false) goto L15;
        String r3 = r2.next();
        if (r1 == null) goto L9;
        if (r1.equals(r3) == true) goto L6;
        throw new IllegalStateException("Multiple charset values defined: " + r1 + ", " + r3);
    L9:
        r1 = r3;
        r0 = Optional.m185of(Charset.forName(r1));
        goto L6
    L15:
        this.parsedCharset = r0;
    L16:
        return r0;
    }

    public boolean equals(@CheckForNull Object r6) {
        if (r6 != this) goto L6;
        return true;
    L6:
        if ((r6 instanceof MediaType) == false) goto L16;
        MediaType r1 = (MediaType) r6;
        if (this.type.equals(r1.type) == false) goto L15;
        if (this.subtype.equals(r1.subtype) == false) goto L15;
        if (parametersAsMap().equals(r1.parametersAsMap()) == false) goto L15;
        return true;
    L15:
        return false;
    L16:
        return false;
    }

    public boolean hasWildcard() {
        if (WILDCARD.equals(this.type) == false) goto L5;
    L8:
        return true;
    L5:
        if (WILDCARD.equals(this.subtype) == true) goto L8;
        return false;
    }

    public int hashCode() {
        int r0 = this.hashCode;
        if (r0 != 0) goto L6;
        int r1 = Objects.hashCode(new Object[]{this.type, this.subtype, parametersAsMap()});
        this.hashCode = r1;
        return r1;
    L6:
        return r0;
    }

    /* JADX INFO: renamed from: is */
    public boolean m346is(MediaType r4) {
        if (r4.type.equals(WILDCARD) == true) goto L7;
        if (r4.type.equals(this.type) == true) goto L7;
    L13:
        return false;
    L7:
        if (r4.subtype.equals(WILDCARD) == true) goto L11;
        if (r4.subtype.equals(this.subtype) == false) goto L13;
    L11:
        if (this.parameters.entries().containsAll(r4.parameters.entries()) == false) goto L13;
        return true;
    }

    public ImmutableListMultimap<String, String> parameters() {
        return this.parameters;
    }

    public String subtype() {
        return this.subtype;
    }

    public String toString() {
        String r0 = this.toString;
        if (r0 != null) goto L6;
        String r1 = computeToString();
        this.toString = r1;
        return r1;
    L6:
        return r0;
    }

    public String type() {
        return this.type;
    }

    public MediaType withCharset(Charset r3) {
        Preconditions.checkNotNull(r3);
        MediaType r0 = withParameter(CHARSET_ATTRIBUTE, r3.name());
        r0.parsedCharset = Optional.m185of(r3);
        return r0;
    }

    public MediaType withParameter(String r2, String r3) {
        return withParameters(r2, ImmutableSet.m260of(r3));
    }

    public MediaType withParameters(Multimap<String, String> r3) {
        return create(this.type, this.subtype, r3);
    }

    public MediaType withParameters(String r7, Iterable<String> r8) {
        Preconditions.checkNotNull(r7);
        Preconditions.checkNotNull(r8);
        String r0 = normalizeToken(r7);
        ImmutableListMultimap.Builder r1 = ImmutableListMultimap.builder();
        UnmodifiableIterator<Map.Entry<String, String>> r2 = this.parameters.entries().iterator();
    L4:
        if (r2.hasNext() == false) goto L9;
        Map.Entry<String, String> r3 = r2.next();
        String r4 = r3.getKey();
        if (r0.equals(r4) == true) goto L4;
        r1.put(r4, r3.getValue());
        goto L4
    L9:
        Iterator<String> r5 = r8.iterator();
    L11:
        if (r5.hasNext() == false) goto L13;
        r1.put(r0, normalizeParameterValue(r0, r5.next()));
        goto L11
    L13:
        MediaType r6 = new MediaType(this.type, this.subtype, r1.build());
        if (r0.equals(CHARSET_ATTRIBUTE) == true) goto L17;
        r6.parsedCharset = this.parsedCharset;
    L17:
        return (MediaType) MoreObjects.firstNonNull(KNOWN_TYPES.get(r6), r6);
    }

    public MediaType withoutParameters() {
        if (this.parameters.isEmpty() == false) goto L6;
        return this;
    L6:
        return create(this.type, this.subtype);
    }
}
