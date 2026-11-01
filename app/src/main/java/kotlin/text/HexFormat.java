package kotlin.text;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* JADX INFO: compiled from: HexFormat.kt */
/* JADX INFO: loaded from: classes.dex */
@Metadata(m420d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0010\u000e\n\u0002\b\u0005\b\u0007\u0018\u0000 \u00132\u00020\u0001:\u0004\u0011\u0012\u0013\u0014B\u001f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\b\u0010\u000f\u001a\u00020\u0010H\u0016R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u0015"}, m421d2 = {"Lkotlin/text/HexFormat;", "", "upperCase", "", "bytes", "Lkotlin/text/HexFormat$BytesHexFormat;", "number", "Lkotlin/text/HexFormat$NumberHexFormat;", "(ZLkotlin/text/HexFormat$BytesHexFormat;Lkotlin/text/HexFormat$NumberHexFormat;)V", "getBytes", "()Lkotlin/text/HexFormat$BytesHexFormat;", "getNumber", "()Lkotlin/text/HexFormat$NumberHexFormat;", "getUpperCase", "()Z", "toString", "", "Builder", "BytesHexFormat", "Companion", "NumberHexFormat", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
public final class HexFormat {
    public static final Companion Companion = null;
    private static final HexFormat Default = null;
    private static final HexFormat UpperCase = null;
    private final BytesHexFormat bytes;
    private final NumberHexFormat number;
    private final boolean upperCase;

    /* JADX INFO: compiled from: HexFormat.kt */
    @Metadata(m420d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0007\b\u0001¢\u0006\u0002\u0010\u0002J\b\u0010\u0013\u001a\u00020\u0014H\u0001J%\u0010\u0007\u001a\u00020\u00152\u0017\u0010\u0016\u001a\u0013\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u00150\u0017¢\u0006\u0002\b\u0018H\u0087\bø\u0001\u0000J%\u0010\n\u001a\u00020\u00152\u0017\u0010\u0016\u001a\u0013\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00150\u0017¢\u0006\u0002\b\u0018H\u0087\bø\u0001\u0000R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u0082\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0007\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0011\u0010\n\u001a\u00020\u00068F¢\u0006\u0006\u001a\u0004\b\u000b\u0010\fR\u001a\u0010\r\u001a\u00020\u000eX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000f\u0010\u0010\"\u0004\b\u0011\u0010\u0012\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\u0019"}, m421d2 = {"Lkotlin/text/HexFormat$Builder;", "", "()V", "_bytes", "Lkotlin/text/HexFormat$BytesHexFormat$Builder;", "_number", "Lkotlin/text/HexFormat$NumberHexFormat$Builder;", "bytes", "getBytes", "()Lkotlin/text/HexFormat$BytesHexFormat$Builder;", "number", "getNumber", "()Lkotlin/text/HexFormat$NumberHexFormat$Builder;", "upperCase", "", "getUpperCase", "()Z", "setUpperCase", "(Z)V", "build", "Lkotlin/text/HexFormat;", "", "builderAction", "Lkotlin/Function1;", "Lkotlin/ExtensionFunctionType;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Builder {
        private BytesHexFormat.Builder _bytes;
        private NumberHexFormat.Builder _number;
        private boolean upperCase;

        public Builder() {
            this.upperCase = HexFormat.Companion.getDefault().getUpperCase();
        }

        private final void bytes(Function1<? super BytesHexFormat.Builder, Unit> r2) {
            Intrinsics.checkNotNullParameter(r2, "builderAction");
            r2.invoke(getBytes());
        }

        private final void number(Function1<? super NumberHexFormat.Builder, Unit> r2) {
            Intrinsics.checkNotNullParameter(r2, "builderAction");
            r2.invoke(getNumber());
        }

        public final HexFormat build() {
            boolean r1 = this.upperCase;
            BytesHexFormat.Builder r2 = this._bytes;
            if (r2 == null) goto L6;
            BytesHexFormat r3 = r2.build$kotlin_stdlib();
            if (r3 == null) goto L6;
        L7:
            NumberHexFormat.Builder r4 = this._number;
            if (r4 == null) goto L11;
            NumberHexFormat r5 = r4.build$kotlin_stdlib();
            if (r5 == null) goto L11;
        L13:
            return new HexFormat(r1, r3, r5);
        L11:
            r5 = NumberHexFormat.Companion.getDefault$kotlin_stdlib();
        L6:
            r3 = BytesHexFormat.Companion.getDefault$kotlin_stdlib();
            goto L7
        }

        public final BytesHexFormat.Builder getBytes() {
            if (this._bytes != null) goto L5;
            this._bytes = new BytesHexFormat.Builder();
        L5:
            BytesHexFormat.Builder r0 = this._bytes;
            Intrinsics.checkNotNull(r0);
            return r0;
        }

        public final NumberHexFormat.Builder getNumber() {
            if (this._number != null) goto L5;
            this._number = new NumberHexFormat.Builder();
        L5:
            NumberHexFormat.Builder r0 = this._number;
            Intrinsics.checkNotNull(r0);
            return r0;
        }

        public final boolean getUpperCase() {
            return this.upperCase;
        }

        public final void setUpperCase(boolean r1) {
            this.upperCase = r1;
        }
    }

    /* JADX INFO: compiled from: HexFormat.kt */
    @Metadata(m420d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\r\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u001b2\u00020\u0001:\u0002\u001a\u001bB7\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006\u0012\u0006\u0010\b\u001a\u00020\u0006\u0012\u0006\u0010\t\u001a\u00020\u0006¢\u0006\u0002\u0010\nJ%\u0010\u0013\u001a\u00060\u0014j\u0002`\u00152\n\u0010\u0016\u001a\u00060\u0014j\u0002`\u00152\u0006\u0010\u0017\u001a\u00020\u0006H\u0000¢\u0006\u0002\b\u0018J\b\u0010\u0019\u001a\u00020\u0006H\u0016R\u0011\u0010\b\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\fR\u0011\u0010\t\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0010R\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\f¨\u0006\u001c"}, m421d2 = {"Lkotlin/text/HexFormat$BytesHexFormat;", "", "bytesPerLine", "", "bytesPerGroup", "groupSeparator", "", "byteSeparator", "bytePrefix", "byteSuffix", "(IILjava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", "getBytePrefix", "()Ljava/lang/String;", "getByteSeparator", "getByteSuffix", "getBytesPerGroup", "()I", "getBytesPerLine", "getGroupSeparator", "appendOptionsTo", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "sb", "indent", "appendOptionsTo$kotlin_stdlib", "toString", "Builder", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class BytesHexFormat {
        public static final Companion Companion = null;
        private static final BytesHexFormat Default = null;
        private final String bytePrefix;
        private final String byteSeparator;
        private final String byteSuffix;
        private final int bytesPerGroup;
        private final int bytesPerLine;
        private final String groupSeparator;

        /* JADX INFO: compiled from: HexFormat.kt */
        @Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u000b\n\u0002\u0010\b\n\u0002\b\f\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0000¢\u0006\u0002\u0010\u0002J\r\u0010\u001c\u001a\u00020\u001dH\u0000¢\u0006\u0002\b\u001eR$\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR$\u0010\n\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000b\u0010\u0007\"\u0004\b\f\u0010\tR$\u0010\r\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u0007\"\u0004\b\u000f\u0010\tR$\u0010\u0011\u001a\u00020\u00102\u0006\u0010\u0003\u001a\u00020\u0010@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0012\u0010\u0013\"\u0004\b\u0014\u0010\u0015R$\u0010\u0016\u001a\u00020\u00102\u0006\u0010\u0003\u001a\u00020\u0010@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0017\u0010\u0013\"\u0004\b\u0018\u0010\u0015R\u001a\u0010\u0019\u001a\u00020\u0004X\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u001a\u0010\u0007\"\u0004\b\u001b\u0010\t¨\u0006\u001f"}, m421d2 = {"Lkotlin/text/HexFormat$BytesHexFormat$Builder;", "", "()V", "value", "", "bytePrefix", "getBytePrefix", "()Ljava/lang/String;", "setBytePrefix", "(Ljava/lang/String;)V", "byteSeparator", "getByteSeparator", "setByteSeparator", "byteSuffix", "getByteSuffix", "setByteSuffix", "", "bytesPerGroup", "getBytesPerGroup", "()I", "setBytesPerGroup", "(I)V", "bytesPerLine", "getBytesPerLine", "setBytesPerLine", "groupSeparator", "getGroupSeparator", "setGroupSeparator", "build", "Lkotlin/text/HexFormat$BytesHexFormat;", "build$kotlin_stdlib", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
        public static final class Builder {
            private String bytePrefix;
            private String byteSeparator;
            private String byteSuffix;
            private int bytesPerGroup;
            private int bytesPerLine;
            private String groupSeparator;

            public Builder() {
                this.bytesPerLine = BytesHexFormat.Companion.getDefault$kotlin_stdlib().getBytesPerLine();
                this.bytesPerGroup = BytesHexFormat.Companion.getDefault$kotlin_stdlib().getBytesPerGroup();
                this.groupSeparator = BytesHexFormat.Companion.getDefault$kotlin_stdlib().getGroupSeparator();
                this.byteSeparator = BytesHexFormat.Companion.getDefault$kotlin_stdlib().getByteSeparator();
                this.bytePrefix = BytesHexFormat.Companion.getDefault$kotlin_stdlib().getBytePrefix();
                this.byteSuffix = BytesHexFormat.Companion.getDefault$kotlin_stdlib().getByteSuffix();
            }

            public final BytesHexFormat build$kotlin_stdlib() {
                return new BytesHexFormat(this.bytesPerLine, this.bytesPerGroup, this.groupSeparator, this.byteSeparator, this.bytePrefix, this.byteSuffix);
            }

            public final String getBytePrefix() {
                return this.bytePrefix;
            }

            public final String getByteSeparator() {
                return this.byteSeparator;
            }

            public final String getByteSuffix() {
                return this.byteSuffix;
            }

            public final int getBytesPerGroup() {
                return this.bytesPerGroup;
            }

            public final int getBytesPerLine() {
                return this.bytesPerLine;
            }

            public final String getGroupSeparator() {
                return this.groupSeparator;
            }

            public final void setBytePrefix(String r6) {
                Intrinsics.checkNotNullParameter(r6, "value");
                if (StringsKt.contains$default(r6, '\n', false, 2, null) == true) goto L9;
                if (StringsKt.contains$default(r6, '\r', false, 2, null) == true) goto L9;
                this.bytePrefix = r6;
                return;
            L9:
                throw new IllegalArgumentException("LF and CR characters are prohibited in bytePrefix, but was " + r6);
            }

            public final void setByteSeparator(String r6) {
                Intrinsics.checkNotNullParameter(r6, "value");
                if (StringsKt.contains$default(r6, '\n', false, 2, null) == true) goto L9;
                if (StringsKt.contains$default(r6, '\r', false, 2, null) == true) goto L9;
                this.byteSeparator = r6;
                return;
            L9:
                throw new IllegalArgumentException("LF and CR characters are prohibited in byteSeparator, but was " + r6);
            }

            public final void setByteSuffix(String r6) {
                Intrinsics.checkNotNullParameter(r6, "value");
                if (StringsKt.contains$default(r6, '\n', false, 2, null) == true) goto L9;
                if (StringsKt.contains$default(r6, '\r', false, 2, null) == true) goto L9;
                this.byteSuffix = r6;
                return;
            L9:
                throw new IllegalArgumentException("LF and CR characters are prohibited in byteSuffix, but was " + r6);
            }

            public final void setBytesPerGroup(int r4) {
                if (r4 <= 0) goto L6;
                this.bytesPerGroup = r4;
                return;
            L6:
                throw new IllegalArgumentException("Non-positive values are prohibited for bytesPerGroup, but was " + r4);
            }

            public final void setBytesPerLine(int r4) {
                if (r4 <= 0) goto L6;
                this.bytesPerLine = r4;
                return;
            L6:
                throw new IllegalArgumentException("Non-positive values are prohibited for bytesPerLine, but was " + r4);
            }

            public final void setGroupSeparator(String r2) {
                Intrinsics.checkNotNullParameter(r2, "<set-?>");
                this.groupSeparator = r2;
            }
        }

        /* JADX INFO: compiled from: HexFormat.kt */
        @Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, m421d2 = {"Lkotlin/text/HexFormat$BytesHexFormat$Companion;", "", "()V", "Default", "Lkotlin/text/HexFormat$BytesHexFormat;", "getDefault$kotlin_stdlib", "()Lkotlin/text/HexFormat$BytesHexFormat;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker r1) {
                this();
            }

            public final BytesHexFormat getDefault$kotlin_stdlib() {
                return BytesHexFormat.access$getDefault$cp();
            }
        }

        static {
            Companion = new Companion(null);
            Default = new BytesHexFormat(Integer.MAX_VALUE, Integer.MAX_VALUE, "  ", "", "", "");
        }

        public BytesHexFormat(int r2, int r3, String r4, String r5, String r6, String r7) {
            Intrinsics.checkNotNullParameter(r4, "groupSeparator");
            Intrinsics.checkNotNullParameter(r5, "byteSeparator");
            Intrinsics.checkNotNullParameter(r6, "bytePrefix");
            Intrinsics.checkNotNullParameter(r7, "byteSuffix");
            this.bytesPerLine = r2;
            this.bytesPerGroup = r3;
            this.groupSeparator = r4;
            this.byteSeparator = r5;
            this.bytePrefix = r6;
            this.byteSuffix = r7;
        }

        public static final /* synthetic */ BytesHexFormat access$getDefault$cp() {
            return Default;
        }

        public final StringBuilder appendOptionsTo$kotlin_stdlib(StringBuilder r6, String r7) {
            Intrinsics.checkNotNullParameter(r6, "sb");
            Intrinsics.checkNotNullParameter(r7, "indent");
            StringBuilder r0 = r6.append(r7).append("bytesPerLine = ").append(this.bytesPerLine);
            Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
            StringBuilder r1 = r0.append(",");
            Intrinsics.checkNotNullExpressionValue(r1, "append(...)");
            Intrinsics.checkNotNullExpressionValue(r1.append('\n'), "append(...)");
            StringBuilder r2 = r6.append(r7).append("bytesPerGroup = ").append(this.bytesPerGroup);
            Intrinsics.checkNotNullExpressionValue(r2, "append(...)");
            StringBuilder r3 = r2.append(",");
            Intrinsics.checkNotNullExpressionValue(r3, "append(...)");
            Intrinsics.checkNotNullExpressionValue(r3.append('\n'), "append(...)");
            StringBuilder r4 = r6.append(r7).append("groupSeparator = \"").append(this.groupSeparator);
            Intrinsics.checkNotNullExpressionValue(r4, "append(...)");
            StringBuilder r5 = r4.append("\",");
            Intrinsics.checkNotNullExpressionValue(r5, "append(...)");
            Intrinsics.checkNotNullExpressionValue(r5.append('\n'), "append(...)");
            StringBuilder r8 = r6.append(r7).append("byteSeparator = \"").append(this.byteSeparator);
            Intrinsics.checkNotNullExpressionValue(r8, "append(...)");
            StringBuilder r9 = r8.append("\",");
            Intrinsics.checkNotNullExpressionValue(r9, "append(...)");
            Intrinsics.checkNotNullExpressionValue(r9.append('\n'), "append(...)");
            StringBuilder r10 = r6.append(r7).append("bytePrefix = \"").append(this.bytePrefix);
            Intrinsics.checkNotNullExpressionValue(r10, "append(...)");
            StringBuilder r11 = r10.append("\",");
            Intrinsics.checkNotNullExpressionValue(r11, "append(...)");
            Intrinsics.checkNotNullExpressionValue(r11.append('\n'), "append(...)");
            r6.append(r7).append("byteSuffix = \"").append(this.byteSuffix).append("\"");
            return r6;
        }

        public final String getBytePrefix() {
            return this.bytePrefix;
        }

        public final String getByteSeparator() {
            return this.byteSeparator;
        }

        public final String getByteSuffix() {
            return this.byteSuffix;
        }

        public final int getBytesPerGroup() {
            return this.bytesPerGroup;
        }

        public final int getBytesPerLine() {
            return this.bytesPerLine;
        }

        public final String getGroupSeparator() {
            return this.groupSeparator;
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder();
            StringBuilder r3 = r0.append("BytesHexFormat(");
            Intrinsics.checkNotNullExpressionValue(r3, "append(...)");
            Intrinsics.checkNotNullExpressionValue(r3.append('\n'), "append(...)");
            Intrinsics.checkNotNullExpressionValue(appendOptionsTo$kotlin_stdlib(r0, "    ").append('\n'), "append(...)");
            r0.append(")");
            String r1 = r0.toString();
            Intrinsics.checkNotNullExpressionValue(r1, "toString(...)");
            return r1;
        }
    }

    /* JADX INFO: compiled from: HexFormat.kt */
    @Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0011\u0010\u0007\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006¨\u0006\t"}, m421d2 = {"Lkotlin/text/HexFormat$Companion;", "", "()V", "Default", "Lkotlin/text/HexFormat;", "getDefault", "()Lkotlin/text/HexFormat;", "UpperCase", "getUpperCase", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker r1) {
            this();
        }

        public final HexFormat getDefault() {
            return HexFormat.access$getDefault$cp();
        }

        public final HexFormat getUpperCase() {
            return HexFormat.access$getUpperCase$cp();
        }
    }

    /* JADX INFO: compiled from: HexFormat.kt */
    @Metadata(m420d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\u0018\u0000 \u00152\u00020\u0001:\u0002\u0014\u0015B\u001f\b\u0000\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006¢\u0006\u0002\u0010\u0007J%\u0010\r\u001a\u00060\u000ej\u0002`\u000f2\n\u0010\u0010\u001a\u00060\u000ej\u0002`\u000f2\u0006\u0010\u0011\u001a\u00020\u0003H\u0000¢\u0006\u0002\b\u0012J\b\u0010\u0013\u001a\u00020\u0003H\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\t¨\u0006\u0016"}, m421d2 = {"Lkotlin/text/HexFormat$NumberHexFormat;", "", "prefix", "", "suffix", "removeLeadingZeros", "", "(Ljava/lang/String;Ljava/lang/String;Z)V", "getPrefix", "()Ljava/lang/String;", "getRemoveLeadingZeros", "()Z", "getSuffix", "appendOptionsTo", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "sb", "indent", "appendOptionsTo$kotlin_stdlib", "toString", "Builder", "Companion", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
    public static final class NumberHexFormat {
        public static final Companion Companion = null;
        private static final NumberHexFormat Default = null;
        private final String prefix;
        private final boolean removeLeadingZeros;
        private final String suffix;

        /* JADX INFO: compiled from: HexFormat.kt */
        @Metadata(m420d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\u000b\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0000¢\u0006\u0002\u0010\u0002J\r\u0010\u0013\u001a\u00020\u0014H\u0000¢\u0006\u0002\b\u0015R$\u0010\u0005\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0006\u0010\u0007\"\u0004\b\b\u0010\tR\u001a\u0010\n\u001a\u00020\u000bX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\f\u0010\r\"\u0004\b\u000e\u0010\u000fR$\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0003\u001a\u00020\u0004@FX\u0086\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0011\u0010\u0007\"\u0004\b\u0012\u0010\t¨\u0006\u0016"}, m421d2 = {"Lkotlin/text/HexFormat$NumberHexFormat$Builder;", "", "()V", "value", "", "prefix", "getPrefix", "()Ljava/lang/String;", "setPrefix", "(Ljava/lang/String;)V", "removeLeadingZeros", "", "getRemoveLeadingZeros", "()Z", "setRemoveLeadingZeros", "(Z)V", "suffix", "getSuffix", "setSuffix", "build", "Lkotlin/text/HexFormat$NumberHexFormat;", "build$kotlin_stdlib", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
        public static final class Builder {
            private String prefix;
            private boolean removeLeadingZeros;
            private String suffix;

            public Builder() {
                this.prefix = NumberHexFormat.Companion.getDefault$kotlin_stdlib().getPrefix();
                this.suffix = NumberHexFormat.Companion.getDefault$kotlin_stdlib().getSuffix();
                this.removeLeadingZeros = NumberHexFormat.Companion.getDefault$kotlin_stdlib().getRemoveLeadingZeros();
            }

            public final NumberHexFormat build$kotlin_stdlib() {
                return new NumberHexFormat(this.prefix, this.suffix, this.removeLeadingZeros);
            }

            public final String getPrefix() {
                return this.prefix;
            }

            public final boolean getRemoveLeadingZeros() {
                return this.removeLeadingZeros;
            }

            public final String getSuffix() {
                return this.suffix;
            }

            public final void setPrefix(String r6) {
                Intrinsics.checkNotNullParameter(r6, "value");
                if (StringsKt.contains$default(r6, '\n', false, 2, null) == true) goto L9;
                if (StringsKt.contains$default(r6, '\r', false, 2, null) == true) goto L9;
                this.prefix = r6;
                return;
            L9:
                throw new IllegalArgumentException("LF and CR characters are prohibited in prefix, but was " + r6);
            }

            public final void setRemoveLeadingZeros(boolean r1) {
                this.removeLeadingZeros = r1;
            }

            public final void setSuffix(String r6) {
                Intrinsics.checkNotNullParameter(r6, "value");
                if (StringsKt.contains$default(r6, '\n', false, 2, null) == true) goto L9;
                if (StringsKt.contains$default(r6, '\r', false, 2, null) == true) goto L9;
                this.suffix = r6;
                return;
            L9:
                throw new IllegalArgumentException("LF and CR characters are prohibited in suffix, but was " + r6);
            }
        }

        /* JADX INFO: compiled from: HexFormat.kt */
        @Metadata(m420d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0080\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0014\u0010\u0003\u001a\u00020\u0004X\u0080\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0007"}, m421d2 = {"Lkotlin/text/HexFormat$NumberHexFormat$Companion;", "", "()V", "Default", "Lkotlin/text/HexFormat$NumberHexFormat;", "getDefault$kotlin_stdlib", "()Lkotlin/text/HexFormat$NumberHexFormat;", "kotlin-stdlib"}, m422k = 1, m423mv = {1, 9, 0}, m425xi = 48)
        public static final class Companion {
            private Companion() {
            }

            public /* synthetic */ Companion(DefaultConstructorMarker r1) {
                this();
            }

            public final NumberHexFormat getDefault$kotlin_stdlib() {
                return NumberHexFormat.access$getDefault$cp();
            }
        }

        static {
            Companion = new Companion(null);
            Default = new NumberHexFormat("", "", false);
        }

        public NumberHexFormat(String r2, String r3, boolean r4) {
            Intrinsics.checkNotNullParameter(r2, "prefix");
            Intrinsics.checkNotNullParameter(r3, "suffix");
            this.prefix = r2;
            this.suffix = r3;
            this.removeLeadingZeros = r4;
        }

        public static final /* synthetic */ NumberHexFormat access$getDefault$cp() {
            return Default;
        }

        public final StringBuilder appendOptionsTo$kotlin_stdlib(StringBuilder r6, String r7) {
            Intrinsics.checkNotNullParameter(r6, "sb");
            Intrinsics.checkNotNullParameter(r7, "indent");
            StringBuilder r0 = r6.append(r7).append("prefix = \"").append(this.prefix);
            Intrinsics.checkNotNullExpressionValue(r0, "append(...)");
            StringBuilder r1 = r0.append("\",");
            Intrinsics.checkNotNullExpressionValue(r1, "append(...)");
            Intrinsics.checkNotNullExpressionValue(r1.append('\n'), "append(...)");
            StringBuilder r2 = r6.append(r7).append("suffix = \"").append(this.suffix);
            Intrinsics.checkNotNullExpressionValue(r2, "append(...)");
            StringBuilder r3 = r2.append("\",");
            Intrinsics.checkNotNullExpressionValue(r3, "append(...)");
            Intrinsics.checkNotNullExpressionValue(r3.append('\n'), "append(...)");
            r6.append(r7).append("removeLeadingZeros = ").append(this.removeLeadingZeros);
            return r6;
        }

        public final String getPrefix() {
            return this.prefix;
        }

        public final boolean getRemoveLeadingZeros() {
            return this.removeLeadingZeros;
        }

        public final String getSuffix() {
            return this.suffix;
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder();
            StringBuilder r3 = r0.append("NumberHexFormat(");
            Intrinsics.checkNotNullExpressionValue(r3, "append(...)");
            Intrinsics.checkNotNullExpressionValue(r3.append('\n'), "append(...)");
            Intrinsics.checkNotNullExpressionValue(appendOptionsTo$kotlin_stdlib(r0, "    ").append('\n'), "append(...)");
            r0.append(")");
            String r1 = r0.toString();
            Intrinsics.checkNotNullExpressionValue(r1, "toString(...)");
            return r1;
        }
    }

    static {
        Companion = new Companion(null);
        Default = new HexFormat(false, BytesHexFormat.Companion.getDefault$kotlin_stdlib(), NumberHexFormat.Companion.getDefault$kotlin_stdlib());
        UpperCase = new HexFormat(true, BytesHexFormat.Companion.getDefault$kotlin_stdlib(), NumberHexFormat.Companion.getDefault$kotlin_stdlib());
    }

    public HexFormat(boolean r2, BytesHexFormat r3, NumberHexFormat r4) {
        Intrinsics.checkNotNullParameter(r3, "bytes");
        Intrinsics.checkNotNullParameter(r4, "number");
        this.upperCase = r2;
        this.bytes = r3;
        this.number = r4;
    }

    public static final /* synthetic */ HexFormat access$getDefault$cp() {
        return Default;
    }

    public static final /* synthetic */ HexFormat access$getUpperCase$cp() {
        return UpperCase;
    }

    public final BytesHexFormat getBytes() {
        return this.bytes;
    }

    public final NumberHexFormat getNumber() {
        return this.number;
    }

    public final boolean getUpperCase() {
        return this.upperCase;
    }

    public String toString() {
        StringBuilder r0 = new StringBuilder();
        StringBuilder r3 = r0.append("HexFormat(");
        Intrinsics.checkNotNullExpressionValue(r3, "append(...)");
        Intrinsics.checkNotNullExpressionValue(r3.append('\n'), "append(...)");
        StringBuilder r4 = r0.append("    upperCase = ").append(this.upperCase);
        Intrinsics.checkNotNullExpressionValue(r4, "append(...)");
        StringBuilder r5 = r4.append(",");
        Intrinsics.checkNotNullExpressionValue(r5, "append(...)");
        Intrinsics.checkNotNullExpressionValue(r5.append('\n'), "append(...)");
        StringBuilder r6 = r0.append("    bytes = BytesHexFormat(");
        Intrinsics.checkNotNullExpressionValue(r6, "append(...)");
        Intrinsics.checkNotNullExpressionValue(r6.append('\n'), "append(...)");
        Intrinsics.checkNotNullExpressionValue(this.bytes.appendOptionsTo$kotlin_stdlib(r0, "        ").append('\n'), "append(...)");
        StringBuilder r7 = r0.append("    ),");
        Intrinsics.checkNotNullExpressionValue(r7, "append(...)");
        Intrinsics.checkNotNullExpressionValue(r7.append('\n'), "append(...)");
        StringBuilder r8 = r0.append("    number = NumberHexFormat(");
        Intrinsics.checkNotNullExpressionValue(r8, "append(...)");
        Intrinsics.checkNotNullExpressionValue(r8.append('\n'), "append(...)");
        Intrinsics.checkNotNullExpressionValue(this.number.appendOptionsTo$kotlin_stdlib(r0, "        ").append('\n'), "append(...)");
        StringBuilder r9 = r0.append("    )");
        Intrinsics.checkNotNullExpressionValue(r9, "append(...)");
        Intrinsics.checkNotNullExpressionValue(r9.append('\n'), "append(...)");
        r0.append(")");
        String r1 = r0.toString();
        Intrinsics.checkNotNullExpressionValue(r1, "toString(...)");
        return r1;
    }
}
