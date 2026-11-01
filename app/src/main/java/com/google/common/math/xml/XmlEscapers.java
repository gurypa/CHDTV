package com.google.common.xml;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;
import kotlin.text.Typography;

/* JADX INFO: loaded from: classes.dex */
@ElementTypesAreNonnullByDefault
public class XmlEscapers {
    private static final char MAX_ASCII_CONTROL_CHAR = 31;
    private static final char MIN_ASCII_CONTROL_CHAR = 0;
    private static final Escaper XML_ATTRIBUTE_ESCAPER = null;
    private static final Escaper XML_CONTENT_ESCAPER = null;
    private static final Escaper XML_ESCAPER = null;

    static {
        Escapers.Builder r0 = Escapers.builder();
        r0.setSafeRange(0, 65533);
        r0.setUnsafeReplacement("�");
        char r2 = 0;
    L4:
        if (r2 > 31) goto L10;
        if (r2 == '\t') goto L9;
        if (r2 == '\n') goto L9;
        if (r2 == '\r') goto L9;
        r0.addEscape(r2, "�");
    L9:
        r2 = (char) (r2 + 1);
        goto L4
    L10:
        r0.addEscape(Typography.amp, "&amp;");
        r0.addEscape(Typography.less, "&lt;");
        r0.addEscape(Typography.greater, "&gt;");
        XML_CONTENT_ESCAPER = r0.build();
        r0.addEscape('\'', "&apos;");
        r0.addEscape(Typography.quote, "&quot;");
        XML_ESCAPER = r0.build();
        r0.addEscape('\t', "&#x9;");
        r0.addEscape('\n', "&#xA;");
        r0.addEscape('\r', "&#xD;");
        XML_ATTRIBUTE_ESCAPER = r0.build();
    }

    private XmlEscapers() {
    }

    public static Escaper xmlAttributeEscaper() {
        return XML_ATTRIBUTE_ESCAPER;
    }

    public static Escaper xmlContentEscaper() {
        return XML_CONTENT_ESCAPER;
    }
}
