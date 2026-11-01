package es.antonio.m3u;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes3.dex */
public final class ClearKeyPairs {
    private static final Pattern PAIR = null;

    static {
        PAIR = Pattern.compile("\\s*[\"']?([a-fA-F0-9-]+)[\"']?\\s*:\\s*[\"']?([a-fA-F0-9]+)[\"']?\\s*");
    }

    private ClearKeyPairs() {
    }

    public static Map<String, String> parse(String r13) {
        if (r13 != null) goto L5;
        String r1 = "";
    L7:
        if (r1.startsWith("{") == false) goto L12;
        if (r1.endsWith("}") == false) goto L12;
        r1 = r1.substring(1, r1.length() - 1).trim();
    L12:
        if (r1.isEmpty() == true) goto L35;
        LinkedHashMap r2 = new LinkedHashMap();
        String[] r4 = r1.split("[,;]", -1);
        int r5 = r4.length;
        int r6 = 0;
    L14:
        if (r6 >= r5) goto L33;
        String r7 = r4[r6];
        Matcher r8 = PAIR.matcher(r7);
        if (r8.matches() == false) goto L32;
        String r9 = r8.group(1).replace("-", "").toLowerCase(Locale.ROOT);
        String r10 = r8.group(2).toLowerCase(Locale.ROOT);
        if (r9.matches("[a-f0-9]{32}") == false) goto L30;
        if (r10.matches("[a-f0-9]{32}") == false) goto L30;
        String r11 = (String) r2.put(r9, r10);
        if (r11 == null) goto L28;
        if (r11.equals(r10) == true) goto L28;
        throw new IllegalArgumentException("Hay dos claves diferentes para el mismo KID");
    L28:
        r6 = r6 + 1;
    L30:
        throw new IllegalArgumentException("KID y clave ClearKey deben tener 32 dígitos hexadecimales");
    L32:
        throw new IllegalArgumentException("Formato ClearKey no válido: se necesita KID:KEY o un objeto JWK");
    L33:
        return r2;
    L35:
        throw new IllegalArgumentException("Faltan claves ClearKey");
    L5:
        r1 = r13.trim();
        goto L7
    }
}
