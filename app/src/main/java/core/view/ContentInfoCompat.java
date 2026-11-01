package androidx.core.view;

import android.content.ClipData;
import android.content.ClipDescription;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.ContentInfo;
import androidx.core.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/* JADX INFO: loaded from: classes.dex */
public final class ContentInfoCompat {
    public static final int FLAG_CONVERT_TO_PLAIN_TEXT = 1;
    public static final int SOURCE_APP = 0;
    public static final int SOURCE_AUTOFILL = 4;
    public static final int SOURCE_CLIPBOARD = 1;
    public static final int SOURCE_DRAG_AND_DROP = 3;
    public static final int SOURCE_INPUT_METHOD = 2;
    public static final int SOURCE_PROCESS_TEXT = 5;
    private final Compat mCompat;

    private static final class Api31Impl {
        private Api31Impl() {
        }

        public static Pair<ContentInfo, ContentInfo> partition(ContentInfo r5, final Predicate<ClipData.Item> r6) {
            ClipData r0 = r5.getClip();
            ContentInfo r3 = null;
            if (r0.getItemCount() != 1) goto L13;
            boolean r1 = r6.test(r0.getItemAt(0));
            if (r1 == false) goto L7;
            ContentInfo r2 = r5;
        L8:
            if (r1 == true) goto L12;
            r3 = r5;
        L12:
            return Pair.create(r2, r3);
        L7:
            r2 = null;
            goto L8
        L13:
            Objects.requireNonNull(r6);
            Pair<ClipData, ClipData> r4 = ContentInfoCompat.partition(r0, new ContentInfoCompat$Api31Impl$$ExternalSyntheticLambda0(r6));
            if (r4.first != null) goto L18;
            return Pair.create(null, r5);
        L18:
            if (r4.second != null) goto L22;
            return Pair.create(r5, null);
        L22:
            return Pair.create(new ContentInfo.Builder(r5).setClip((ClipData) r4.first).build(), new ContentInfo.Builder(r5).setClip((ClipData) r4.second).build());
        }
    }

    public static final class Builder {
        private final BuilderCompat mBuilderCompat;

        public Builder(ClipData r3, int r4) {
            if (Build.VERSION.SDK_INT < 31) goto L5;
            this.mBuilderCompat = new BuilderCompat31Impl(r3, r4);
            return;
        L5:
            this.mBuilderCompat = new BuilderCompatImpl(r3, r4);
        }

        public Builder(ContentInfoCompat r3) {
            if (Build.VERSION.SDK_INT < 31) goto L5;
            this.mBuilderCompat = new BuilderCompat31Impl(r3);
            return;
        L5:
            this.mBuilderCompat = new BuilderCompatImpl(r3);
        }

        public ContentInfoCompat build() {
            return this.mBuilderCompat.build();
        }

        public Builder setClip(ClipData r2) {
            this.mBuilderCompat.setClip(r2);
            return this;
        }

        public Builder setExtras(Bundle r2) {
            this.mBuilderCompat.setExtras(r2);
            return this;
        }

        public Builder setFlags(int r2) {
            this.mBuilderCompat.setFlags(r2);
            return this;
        }

        public Builder setLinkUri(Uri r2) {
            this.mBuilderCompat.setLinkUri(r2);
            return this;
        }

        public Builder setSource(int r2) {
            this.mBuilderCompat.setSource(r2);
            return this;
        }
    }

    private interface BuilderCompat {
        ContentInfoCompat build();

        void setClip(ClipData r1);

        void setExtras(Bundle r1);

        void setFlags(int r1);

        void setLinkUri(Uri r1);

        void setSource(int r1);
    }

    private static final class BuilderCompat31Impl implements BuilderCompat {
        private final ContentInfo.Builder mPlatformBuilder;

        BuilderCompat31Impl(ClipData r2, int r3) {
            this.mPlatformBuilder = new ContentInfo.Builder(r2, r3);
        }

        BuilderCompat31Impl(ContentInfoCompat r3) {
            this.mPlatformBuilder = new ContentInfo.Builder(r3.toContentInfo());
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public ContentInfoCompat build() {
            return new ContentInfoCompat(new Compat31Impl(this.mPlatformBuilder.build()));
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public void setClip(ClipData r2) {
            this.mPlatformBuilder.setClip(r2);
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public void setExtras(Bundle r2) {
            this.mPlatformBuilder.setExtras(r2);
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public void setFlags(int r2) {
            this.mPlatformBuilder.setFlags(r2);
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public void setLinkUri(Uri r2) {
            this.mPlatformBuilder.setLinkUri(r2);
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public void setSource(int r2) {
            this.mPlatformBuilder.setSource(r2);
        }
    }

    private static final class BuilderCompatImpl implements BuilderCompat {
        ClipData mClip;
        Bundle mExtras;
        int mFlags;
        Uri mLinkUri;
        int mSource;

        BuilderCompatImpl(ClipData r1, int r2) {
            this.mClip = r1;
            this.mSource = r2;
        }

        BuilderCompatImpl(ContentInfoCompat r2) {
            this.mClip = r2.getClip();
            this.mSource = r2.getSource();
            this.mFlags = r2.getFlags();
            this.mLinkUri = r2.getLinkUri();
            this.mExtras = r2.getExtras();
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public ContentInfoCompat build() {
            return new ContentInfoCompat(new CompatImpl(this));
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public void setClip(ClipData r1) {
            this.mClip = r1;
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public void setExtras(Bundle r1) {
            this.mExtras = r1;
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public void setFlags(int r1) {
            this.mFlags = r1;
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public void setLinkUri(Uri r1) {
            this.mLinkUri = r1;
        }

        @Override // androidx.core.view.ContentInfoCompat.BuilderCompat
        public void setSource(int r1) {
            this.mSource = r1;
        }
    }

    private interface Compat {
        ClipData getClip();

        Bundle getExtras();

        int getFlags();

        Uri getLinkUri();

        int getSource();

        ContentInfo getWrapped();
    }

    private static final class Compat31Impl implements Compat {
        private final ContentInfo mWrapped;

        Compat31Impl(ContentInfo r2) {
            this.mWrapped = (ContentInfo) Preconditions.checkNotNull(r2);
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public ClipData getClip() {
            return this.mWrapped.getClip();
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public Bundle getExtras() {
            return this.mWrapped.getExtras();
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public int getFlags() {
            return this.mWrapped.getFlags();
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public Uri getLinkUri() {
            return this.mWrapped.getLinkUri();
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public int getSource() {
            return this.mWrapped.getSource();
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public ContentInfo getWrapped() {
            return this.mWrapped;
        }

        public String toString() {
            return "ContentInfoCompat{" + this.mWrapped + "}";
        }
    }

    private static final class CompatImpl implements Compat {
        private final ClipData mClip;
        private final Bundle mExtras;
        private final int mFlags;
        private final Uri mLinkUri;
        private final int mSource;

        CompatImpl(BuilderCompatImpl r5) {
            this.mClip = (ClipData) Preconditions.checkNotNull(r5.mClip);
            this.mSource = Preconditions.checkArgumentInRange(r5.mSource, 0, 5, "source");
            this.mFlags = Preconditions.checkFlagsArgument(r5.mFlags, 1);
            this.mLinkUri = r5.mLinkUri;
            this.mExtras = r5.mExtras;
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public ClipData getClip() {
            return this.mClip;
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public Bundle getExtras() {
            return this.mExtras;
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public int getFlags() {
            return this.mFlags;
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public Uri getLinkUri() {
            return this.mLinkUri;
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public int getSource() {
            return this.mSource;
        }

        @Override // androidx.core.view.ContentInfoCompat.Compat
        public ContentInfo getWrapped() {
            return null;
        }

        public String toString() {
            StringBuilder r0 = new StringBuilder().append("ContentInfoCompat{clip=").append(this.mClip.getDescription()).append(", source=").append(ContentInfoCompat.sourceToString(this.mSource)).append(", flags=").append(ContentInfoCompat.flagsToString(this.mFlags));
            String r2 = "";
            if (this.mLinkUri != null) goto L5;
            String r1 = "";
        L6:
            StringBuilder r3 = r0.append(r1);
            if (this.mExtras == null) goto L11;
            r2 = ", hasExtras";
        L11:
            return r3.append(r2).append("}").toString();
        L5:
            r1 = ", hasLinkUri(" + this.mLinkUri.toString().length() + ")";
            goto L6
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Flags {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Source {
    }

    ContentInfoCompat(Compat r1) {
        this.mCompat = r1;
    }

    static ClipData buildClipData(ClipDescription r3, List<ClipData.Item> r4) {
        ClipData r0 = new ClipData(new ClipDescription(r3), r4.get(0));
        int r1 = 1;
    L4:
        if (r1 >= r4.size()) goto L6;
        r0.addItem(r4.get(r1));
        r1 = r1 + 1;
        goto L4
    L6:
        return r0;
    }

    static String flagsToString(int r1) {
        if ((r1 & 1) == 0) goto L7;
        return "FLAG_CONVERT_TO_PLAIN_TEXT";
    L7:
        return String.valueOf(r1);
    }

    static Pair<ClipData, ClipData> partition(ClipData r5, androidx.core.util.Predicate<ClipData.Item> r6) {
        ArrayList r0 = null;
        ArrayList r1 = null;
        int r2 = 0;
    L4:
        if (r2 >= r5.getItemCount()) goto L17;
        ClipData.Item r3 = r5.getItemAt(r2);
        if (r6.test(r3) == false) goto L11;
        if (r0 != null) goto L9;
        ArrayList r4 = new ArrayList();
    L10:
        r4.add(r3);
        r0 = r4;
    L15:
        r2 = r2 + 1;
        goto L4
    L9:
        r4 = r0;
        goto L10
    L11:
        if (r1 != null) goto L13;
        ArrayList r7 = new ArrayList();
    L14:
        r7.add(r3);
        r1 = r7;
        goto L15
    L13:
        r7 = r1;
        goto L14
    L17:
        if (r0 == null) goto L19;
        if (r1 != null) goto L24;
        return Pair.create(r5, null);
    L24:
        return Pair.create(buildClipData(r5.getDescription(), r0), buildClipData(r5.getDescription(), r1));
    L19:
        return Pair.create(null, r5);
    }

    public static Pair<ContentInfo, ContentInfo> partition(ContentInfo r1, Predicate<ClipData.Item> r2) {
        return Api31Impl.partition(r1, r2);
    }

    static String sourceToString(int r1) {
        switch(r1) {
            case 0: goto L15;
            case 1: goto L13;
            case 2: goto L11;
            case 3: goto L9;
            case 4: goto L7;
            case 5: goto L5;
            default: goto L4;
        };
    L5:
        return "SOURCE_PROCESS_TEXT";
    L7:
        return "SOURCE_AUTOFILL";
    L9:
        return "SOURCE_DRAG_AND_DROP";
    L11:
        return "SOURCE_INPUT_METHOD";
    L13:
        return "SOURCE_CLIPBOARD";
    L15:
        return "SOURCE_APP";
    L4:
        return String.valueOf(r1);
    }

    public static ContentInfoCompat toContentInfoCompat(ContentInfo r2) {
        return new ContentInfoCompat(new Compat31Impl(r2));
    }

    public ClipData getClip() {
        return this.mCompat.getClip();
    }

    public Bundle getExtras() {
        return this.mCompat.getExtras();
    }

    public int getFlags() {
        return this.mCompat.getFlags();
    }

    public Uri getLinkUri() {
        return this.mCompat.getLinkUri();
    }

    public int getSource() {
        return this.mCompat.getSource();
    }

    public Pair<ContentInfoCompat, ContentInfoCompat> partition(androidx.core.util.Predicate<ClipData.Item> r6) {
        ClipData r0 = this.mCompat.getClip();
        ContentInfoCompat r3 = null;
        if (r0.getItemCount() != 1) goto L13;
        boolean r1 = r6.test(r0.getItemAt(0));
        if (r1 == false) goto L7;
        ContentInfoCompat r2 = this;
    L8:
        if (r1 == true) goto L12;
        r3 = this;
    L12:
        return Pair.create(r2, r3);
    L7:
        r2 = null;
        goto L8
    L13:
        Pair<ClipData, ClipData> r4 = partition(r0, r6);
        if (r4.first != null) goto L18;
        return Pair.create(null, this);
    L18:
        if (r4.second != null) goto L22;
        return Pair.create(this, null);
    L22:
        return Pair.create(new Builder(this).setClip((ClipData) r4.first).build(), new Builder(this).setClip((ClipData) r4.second).build());
    }

    public ContentInfo toContentInfo() {
        return (ContentInfo) Objects.requireNonNull(this.mCompat.getWrapped());
    }

    public String toString() {
        return this.mCompat.toString();
    }
}
