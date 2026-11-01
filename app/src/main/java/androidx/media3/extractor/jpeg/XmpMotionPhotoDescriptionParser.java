package androidx.media3.extractor.jpeg;

import androidx.media3.common.C0180C;
import androidx.media3.common.MimeTypes;
import androidx.media3.common.ParserException;
import androidx.media3.common.util.Log;
import androidx.media3.common.util.XmlPullParserUtil;
import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.io.StringReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* JADX INFO: loaded from: classes.dex */
final class XmpMotionPhotoDescriptionParser {
    private static final String[] DESCRIPTION_MICRO_VIDEO_OFFSET_ATTRIBUTE_NAMES = null;
    private static final String[] DESCRIPTION_MOTION_PHOTO_PRESENTATION_TIMESTAMP_ATTRIBUTE_NAMES = null;
    private static final String[] MOTION_PHOTO_ATTRIBUTE_NAMES = null;
    private static final String TAG = "MotionPhotoXmpParser";

    static {
        MOTION_PHOTO_ATTRIBUTE_NAMES = new String[]{"Camera:MotionPhoto", "GCamera:MotionPhoto", "Camera:MicroVideo", "GCamera:MicroVideo"};
        DESCRIPTION_MOTION_PHOTO_PRESENTATION_TIMESTAMP_ATTRIBUTE_NAMES = new String[]{"Camera:MotionPhotoPresentationTimestampUs", "GCamera:MotionPhotoPresentationTimestampUs", "Camera:MicroVideoPresentationTimestampUs", "GCamera:MicroVideoPresentationTimestampUs"};
        DESCRIPTION_MICRO_VIDEO_OFFSET_ATTRIBUTE_NAMES = new String[]{"Camera:MicroVideoOffset", "GCamera:MicroVideoOffset"};
    }

    private XmpMotionPhotoDescriptionParser() {
    }

    public static MotionPhotoDescription parse(String r3) throws IOException {
        return parseInternal(r3);
    L9:
        Log.m48w(TAG, "Ignoring unexpected XMP metadata");
        return null;
    }

    private static MotionPhotoDescription parseInternal(String r9) throws XmlPullParserException, IOException {
        XmlPullParser r1 = XmlPullParserFactory.newInstance().newPullParser();
        r1.setInput(new StringReader(r9));
        r1.next();
        if (XmlPullParserUtil.isStartTag(r1, "x:xmpmeta") == false) goto L25;
        long r5 = C0180C.TIME_UNSET;
        ImmutableList<MotionPhotoDescription.ContainerItem> r3 = ImmutableList.m212of();
    L5:
        r1.next();
        if (XmlPullParserUtil.isStartTag(r1, "rdf:Description") == false) goto L12;
        if (parseMotionPhotoFlagFromDescription(r1) == false) goto L9;
        r5 = parseMotionPhotoPresentationTimestampUsFromDescription(r1);
        r3 = parseMicroVideoOffsetFromDescription(r1);
    L18:
        if (XmlPullParserUtil.isEndTag(r1, "x:xmpmeta") == false) goto L5;
        if (r3.isEmpty() == false) goto L23;
        return null;
    L23:
        return new MotionPhotoDescription(r5, r3);
    L9:
        return null;
    L12:
        if (XmlPullParserUtil.isStartTag(r1, "Container:Directory") == false) goto L15;
        r3 = parseMotionPhotoV1Directory(r1, "Container", "Item");
        goto L18
    L15:
        if (XmlPullParserUtil.isStartTag(r1, "GContainer:Directory") == false) goto L18;
        r3 = parseMotionPhotoV1Directory(r1, "GContainer", "GContainerItem");
        goto L18
    L25:
        throw ParserException.createForMalformedContainer("Couldn't find xmp metadata", null);
    }

    private static ImmutableList<MotionPhotoDescription.ContainerItem> parseMicroVideoOffsetFromDescription(XmlPullParser r18) {
        String[] r0 = DESCRIPTION_MICRO_VIDEO_OFFSET_ATTRIBUTE_NAMES;
        int r1 = r0.length;
        int r2 = 0;
    L3:
        if (r2 >= r1) goto L10;
        String r5 = XmlPullParserUtil.getAttributeValue(r18, r0[r2]);
        if (r5 != null) goto L6;
        r2 = r2 + 1;
        goto L3
    L6:
        long r9 = Long.parseLong(r5);
        return ImmutableList.m214of(new MotionPhotoDescription.ContainerItem(MimeTypes.IMAGE_JPEG, "Primary", 0, 0), new MotionPhotoDescription.ContainerItem(MimeTypes.VIDEO_MP4, "MotionPhoto", r9, 0));
    L10:
        return ImmutableList.m212of();
    }

    private static boolean parseMotionPhotoFlagFromDescription(XmlPullParser r6) {
        String[] r0 = MOTION_PHOTO_ATTRIBUTE_NAMES;
        int r1 = r0.length;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L11;
        String r5 = XmlPullParserUtil.getAttributeValue(r6, r0[r3]);
        if (r5 != null) goto L7;
        r3 = r3 + 1;
        goto L3
    L7:
        if (Integer.parseInt(r5) == 1) goto L9;
        return false;
    L9:
        return true;
    L11:
        return false;
    }

    private static long parseMotionPhotoPresentationTimestampUsFromDescription(XmlPullParser r9) {
        String[] r0 = DESCRIPTION_MOTION_PHOTO_PRESENTATION_TIMESTAMP_ATTRIBUTE_NAMES;
        int r1 = r0.length;
        int r2 = 0;
    L4:
        if (r2 >= r1) goto L13;
        String r6 = XmlPullParserUtil.getAttributeValue(r9, r0[r2]);
        if (r6 != null) goto L7;
        r2 = r2 + 1;
        goto L4
    L7:
        long r3 = Long.parseLong(r6);
        if (r3 != (-1)) goto L11;
        return C0180C.TIME_UNSET;
    L11:
        return r3;
    L13:
        return C0180C.TIME_UNSET;
    }

    private static ImmutableList<MotionPhotoDescription.ContainerItem> parseMotionPhotoV1Directory(XmlPullParser r21, String r22, String r23) throws XmlPullParserException, IOException {
        ImmutableList.Builder r3 = ImmutableList.builder();
        String r4 = r22 + ":Item";
        String r5 = r22 + ":Directory";
    L3:
        r21.next();
        if (XmlPullParserUtil.isStartTag(r21, r4) == false) goto L19;
        String r11 = XmlPullParserUtil.getAttributeValue(r21, r23 + ":Mime");
        String r12 = XmlPullParserUtil.getAttributeValue(r21, r23 + ":Semantic");
        String r17 = XmlPullParserUtil.getAttributeValue(r21, r23 + ":Length");
        String r18 = XmlPullParserUtil.getAttributeValue(r21, r23 + ":Padding");
        if (r11 == null) goto L17;
        if (r12 == null) goto L17;
        long r13 = 0;
        if (r17 == null) goto L12;
        long r15 = Long.parseLong(r17);
    L13:
        if (r18 == null) goto L15;
        r13 = Long.parseLong(r18);
    L15:
        r3.add(new MotionPhotoDescription.ContainerItem(r11, r12, r15, r13));
        goto L19
    L12:
        r15 = 0;
    L17:
        return ImmutableList.m212of();
    L19:
        if (XmlPullParserUtil.isEndTag(r21, r5) == false) goto L3;
        return r3.build();
    }
}
