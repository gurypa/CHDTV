package androidx.core.content;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.core.util.Consumer;
import androidx.core.util.Preconditions;
import androidx.core.util.Predicate;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class IntentSanitizer {
    private static final String TAG = "IntentSanitizer";
    private boolean mAllowAnyComponent;
    private boolean mAllowClipDataText;
    private boolean mAllowIdentifier;
    private boolean mAllowSelector;
    private boolean mAllowSourceBounds;
    private Predicate<String> mAllowedActions;
    private Predicate<String> mAllowedCategories;
    private Predicate<ClipData> mAllowedClipData;
    private Predicate<Uri> mAllowedClipDataUri;
    private Predicate<ComponentName> mAllowedComponents;
    private Predicate<Uri> mAllowedData;
    private Map<String, Predicate<Object>> mAllowedExtras;
    private int mAllowedFlags;
    private Predicate<String> mAllowedPackages;
    private Predicate<String> mAllowedTypes;

    /* JADX INFO: renamed from: androidx.core.content.IntentSanitizer$1 */
    static /* synthetic */ class C00531 {
    }

    private static class Api15Impl {
        private Api15Impl() {
        }

        static Intent getSelector(Intent r1) {
            return r1.getSelector();
        }

        static void setSelector(Intent r0, Intent r1) {
            r0.setSelector(r1);
        }
    }

    private static class Api16Impl {

        private static class Api31Impl {
            private Api31Impl() {
            }

            static void checkOtherMembers(int r2, ClipData.Item r3, Consumer<String> r4) {
                if (r3.getHtmlText() == null) goto L5;
            L8:
                r4.accept("ClipData item at position " + r2 + " contains htmlText, textLinks or intent: " + r3);
                return;
            L5:
                if (r3.getIntent() != null) goto L8;
                if (r3.getTextLinks() != null) goto L8;
            }
        }

        private Api16Impl() {
        }

        private static void checkOtherMembers(int r2, ClipData.Item r3, Consumer<String> r4) {
            if (r3.getHtmlText() == null) goto L5;
        L6:
            r4.accept("ClipData item at position " + r2 + " contains htmlText, textLinks or intent: " + r3);
            return;
        L5:
            if (r3.getIntent() != null) goto L6;
        }

        static void sanitizeClipData(Intent r10, Intent r11, Predicate<ClipData> r12, boolean r13, Predicate<Uri> r14, Consumer<String> r15) {
            ClipData r0 = r10.getClipData();
            if (r0 != null) goto L5;
            return;
        L5:
            ClipData r1 = null;
            if (r12 != null) goto L8;
        L10:
            int r2 = 0;
        L12:
            if (r2 >= r0.getItemCount()) goto L42;
            ClipData.Item r3 = r0.getItemAt(r2);
            if (Build.VERSION.SDK_INT < 31) goto L16;
            Api31Impl.checkOtherMembers(r2, r3, r15);
        L17:
            CharSequence r4 = null;
            if (r13 == false) goto L21;
            r4 = r3.getText();
        L23:
            Uri r5 = null;
            if (r14 != null) goto L29;
            if (r3.getUri() == null) goto L35;
            r15.accept("Item URI is not allowed. Item position: " + r2 + ". URI: " + r3.getUri());
        L35:
            if (r4 != null) goto L38;
            if (r5 != null) goto L38;
        L41:
            r2 = r2 + 1;
        L38:
            if (r1 != null) goto L40;
            r1 = new ClipData(r0.getDescription(), new ClipData.Item(r4, null, r5));
            goto L41
        L40:
            r1.addItem(new ClipData.Item(r4, null, r5));
            goto L41
        L29:
            if (r3.getUri() != null) goto L31;
        L34:
            r5 = r3.getUri();
            goto L35
        L31:
            if (r14.test(r3.getUri()) == true) goto L34;
            r15.accept("Item URI is not allowed. Item position: " + r2 + ". URI: " + r3.getUri());
            goto L35
        L21:
            if (r3.getText() == null) goto L23;
            r15.accept("Item text cannot contain value. Item position: " + r2 + ". Text: " + r3.getText());
            goto L23
        L16:
            checkOtherMembers(r2, r3, r15);
            goto L17
        L42:
            if (r1 == null) goto L50;
            r11.setClipData(r1);
            return;
        L50:
            return;
        L8:
            if (r12.test(r0) == false) goto L10;
            r11.setClipData(r0);
        }
    }

    private static class Api29Impl {
        private Api29Impl() {
        }

        static String getIdentifier(Intent r1) {
            return r1.getIdentifier();
        }

        static Intent setIdentifier(Intent r1, String r2) {
            return r1.setIdentifier(r2);
        }
    }

    public static final class Builder {
        private static final int HISTORY_STACK_FLAGS = 2112614400;
        private static final int RECEIVER_FLAGS = 2015363072;
        private boolean mAllowAnyComponent;
        private boolean mAllowClipDataText;
        private boolean mAllowIdentifier;
        private boolean mAllowSelector;
        private boolean mAllowSomeComponents;
        private boolean mAllowSourceBounds;
        private Predicate<String> mAllowedActions;
        private Predicate<String> mAllowedCategories;
        private Predicate<ClipData> mAllowedClipData;
        private Predicate<Uri> mAllowedClipDataUri;
        private Predicate<ComponentName> mAllowedComponents;
        private Predicate<Uri> mAllowedData;
        private Map<String, Predicate<Object>> mAllowedExtras;
        private int mAllowedFlags;
        private Predicate<String> mAllowedPackages;
        private Predicate<String> mAllowedTypes;

        public Builder() {
            this.mAllowedActions = new IntentSanitizer$Builder$$ExternalSyntheticLambda10();
            this.mAllowedData = new IntentSanitizer$Builder$$ExternalSyntheticLambda11();
            this.mAllowedTypes = new IntentSanitizer$Builder$$ExternalSyntheticLambda12();
            this.mAllowedCategories = new IntentSanitizer$Builder$$ExternalSyntheticLambda13();
            this.mAllowedPackages = new IntentSanitizer$Builder$$ExternalSyntheticLambda14();
            this.mAllowedComponents = new IntentSanitizer$Builder$$ExternalSyntheticLambda15();
            this.mAllowedExtras = new HashMap();
            this.mAllowClipDataText = false;
            this.mAllowedClipDataUri = new IntentSanitizer$Builder$$ExternalSyntheticLambda16();
            this.mAllowedClipData = new IntentSanitizer$Builder$$ExternalSyntheticLambda17();
        }

        static /* synthetic */ boolean lambda$allowAnyComponent$10(ComponentName r1) {
            return true;
        }

        static /* synthetic */ boolean lambda$allowClipDataUriWithAuthority$11(String r1, Uri r2) {
            return r1.equals(r2.getAuthority());
        }

        static /* synthetic */ boolean lambda$allowComponentWithPackage$9(String r1, ComponentName r2) {
            return r1.equals(r2.getPackageName());
        }

        static /* synthetic */ boolean lambda$allowDataWithAuthority$8(String r1, Uri r2) {
            return r1.equals(r2.getAuthority());
        }

        static /* synthetic */ boolean lambda$allowExtra$12(Object r1) {
            return true;
        }

        static /* synthetic */ boolean lambda$allowExtra$13(Class r1, Predicate r2, Object r3) {
            if (r1.isInstance(r3) == true) goto L5;
        L7:
            return false;
        L5:
            if (r2.test(r1.cast(r3)) == false) goto L7;
            return true;
        }

        static /* synthetic */ boolean lambda$allowExtra$14(Object r1) {
            return false;
        }

        static /* synthetic */ boolean lambda$allowExtraOutput$16(String r1, Uri r2) {
            return r1.equals(r2.getAuthority());
        }

        static /* synthetic */ boolean lambda$allowExtraStreamUriWithAuthority$15(String r1, Uri r2) {
            return r1.equals(r2.getAuthority());
        }

        static /* synthetic */ boolean lambda$new$0(String r1) {
            return false;
        }

        static /* synthetic */ boolean lambda$new$1(Uri r1) {
            return false;
        }

        static /* synthetic */ boolean lambda$new$2(String r1) {
            return false;
        }

        static /* synthetic */ boolean lambda$new$3(String r1) {
            return false;
        }

        static /* synthetic */ boolean lambda$new$4(String r1) {
            return false;
        }

        static /* synthetic */ boolean lambda$new$5(ComponentName r1) {
            return false;
        }

        static /* synthetic */ boolean lambda$new$6(Uri r1) {
            return false;
        }

        static /* synthetic */ boolean lambda$new$7(ClipData r1) {
            return false;
        }

        public Builder allowAction(Predicate<String> r2) {
            Preconditions.checkNotNull(r2);
            this.mAllowedActions = this.mAllowedActions.mo5or(r2);
            return this;
        }

        public Builder allowAction(String r2) {
            Preconditions.checkNotNull(r2);
            Objects.requireNonNull(r2);
            allowAction(new IntentSanitizer$Builder$$ExternalSyntheticLambda1(r2));
            return this;
        }

        public Builder allowAnyComponent() {
            this.mAllowAnyComponent = true;
            this.mAllowedComponents = new IntentSanitizer$Builder$$ExternalSyntheticLambda6();
            return this;
        }

        public Builder allowCategory(Predicate<String> r2) {
            Preconditions.checkNotNull(r2);
            this.mAllowedCategories = this.mAllowedCategories.mo5or(r2);
            return this;
        }

        public Builder allowCategory(String r2) {
            Preconditions.checkNotNull(r2);
            Objects.requireNonNull(r2);
            return allowCategory(new IntentSanitizer$Builder$$ExternalSyntheticLambda1(r2));
        }

        public Builder allowClipData(Predicate<ClipData> r2) {
            Preconditions.checkNotNull(r2);
            this.mAllowedClipData = this.mAllowedClipData.mo5or(r2);
            return this;
        }

        public Builder allowClipDataText() {
            this.mAllowClipDataText = true;
            return this;
        }

        public Builder allowClipDataUri(Predicate<Uri> r2) {
            Preconditions.checkNotNull(r2);
            this.mAllowedClipDataUri = this.mAllowedClipDataUri.mo5or(r2);
            return this;
        }

        public Builder allowClipDataUriWithAuthority(final String r2) {
            Preconditions.checkNotNull(r2);
            return allowClipDataUri(new IntentSanitizer$Builder$$ExternalSyntheticLambda7(r2));
        }

        public Builder allowComponent(final ComponentName r2) {
            Preconditions.checkNotNull(r2);
            Objects.requireNonNull(r2);
            return allowComponent(new IntentSanitizer$Builder$$ExternalSyntheticLambda5(r2));
        }

        public Builder allowComponent(Predicate<ComponentName> r2) {
            Preconditions.checkNotNull(r2);
            this.mAllowSomeComponents = true;
            this.mAllowedComponents = this.mAllowedComponents.mo5or(r2);
            return this;
        }

        public Builder allowComponentWithPackage(final String r2) {
            Preconditions.checkNotNull(r2);
            return allowComponent(new IntentSanitizer$Builder$$ExternalSyntheticLambda2(r2));
        }

        public Builder allowData(Predicate<Uri> r2) {
            Preconditions.checkNotNull(r2);
            this.mAllowedData = this.mAllowedData.mo5or(r2);
            return this;
        }

        public Builder allowDataWithAuthority(final String r2) {
            Preconditions.checkNotNull(r2);
            allowData(new IntentSanitizer$Builder$$ExternalSyntheticLambda3(r2));
            return this;
        }

        public Builder allowExtra(String r3, Predicate<Object> r4) {
            Preconditions.checkNotNull(r3);
            Preconditions.checkNotNull(r4);
            Predicate<Object> r0 = this.mAllowedExtras.get(r3);
            if (r0 != null) goto L5;
            r0 = new IntentSanitizer$Builder$$ExternalSyntheticLambda8();
        L5:
            Predicate<Object> r1 = r0.mo5or(r4);
            this.mAllowedExtras.put(r3, r1);
            return this;
        }

        public Builder allowExtra(String r2, Class<?> r3) {
            return allowExtra(r2, r3, new IntentSanitizer$Builder$$ExternalSyntheticLambda18());
        }

        public <T> Builder allowExtra(String r2, final Class<T> r3, final Predicate<T> r4) {
            Preconditions.checkNotNull(r2);
            Preconditions.checkNotNull(r3);
            Preconditions.checkNotNull(r4);
            return allowExtra(r2, new IntentSanitizer$Builder$$ExternalSyntheticLambda4(r3, r4));
        }

        public Builder allowExtraOutput(Predicate<Uri> r3) {
            allowExtra("output", Uri.class, r3);
            return this;
        }

        public Builder allowExtraOutput(final String r4) {
            allowExtra("output", Uri.class, new IntentSanitizer$Builder$$ExternalSyntheticLambda0(r4));
            return this;
        }

        public Builder allowExtraStream(Predicate<Uri> r3) {
            allowExtra("android.intent.extra.STREAM", Uri.class, r3);
            return this;
        }

        public Builder allowExtraStreamUriWithAuthority(final String r4) {
            Preconditions.checkNotNull(r4);
            allowExtra("android.intent.extra.STREAM", Uri.class, new IntentSanitizer$Builder$$ExternalSyntheticLambda9(r4));
            return this;
        }

        public Builder allowFlags(int r2) {
            this.mAllowedFlags |= r2;
            return this;
        }

        public Builder allowHistoryStackFlags() {
            this.mAllowedFlags |= HISTORY_STACK_FLAGS;
            return this;
        }

        public Builder allowIdentifier() {
            this.mAllowIdentifier = true;
            return this;
        }

        public Builder allowPackage(Predicate<String> r2) {
            Preconditions.checkNotNull(r2);
            this.mAllowedPackages = this.mAllowedPackages.mo5or(r2);
            return this;
        }

        public Builder allowPackage(String r2) {
            Preconditions.checkNotNull(r2);
            Objects.requireNonNull(r2);
            return allowPackage(new IntentSanitizer$Builder$$ExternalSyntheticLambda1(r2));
        }

        public Builder allowReceiverFlags() {
            this.mAllowedFlags |= RECEIVER_FLAGS;
            return this;
        }

        public Builder allowSelector() {
            this.mAllowSelector = true;
            return this;
        }

        public Builder allowSourceBounds() {
            this.mAllowSourceBounds = true;
            return this;
        }

        public Builder allowType(Predicate<String> r2) {
            Preconditions.checkNotNull(r2);
            this.mAllowedTypes = this.mAllowedTypes.mo5or(r2);
            return this;
        }

        public Builder allowType(String r2) {
            Preconditions.checkNotNull(r2);
            Objects.requireNonNull(r2);
            return allowType(new IntentSanitizer$Builder$$ExternalSyntheticLambda1(r2));
        }

        public IntentSanitizer build() {
            if (this.mAllowAnyComponent == false) goto L7;
            if (this.mAllowSomeComponents == false) goto L7;
        L12:
            throw new SecurityException("You must call either allowAnyComponent or one or more of the allowComponent methods; but not both.");
        L7:
            if (this.mAllowAnyComponent == false) goto L9;
        L13:
            IntentSanitizer r0 = new IntentSanitizer(null);
            IntentSanitizer.access$102(r0, this.mAllowedFlags);
            IntentSanitizer.access$202(r0, this.mAllowedActions);
            IntentSanitizer.access$302(r0, this.mAllowedData);
            IntentSanitizer.access$402(r0, this.mAllowedTypes);
            IntentSanitizer.access$502(r0, this.mAllowedCategories);
            IntentSanitizer.access$602(r0, this.mAllowedPackages);
            IntentSanitizer.access$702(r0, this.mAllowAnyComponent);
            IntentSanitizer.access$802(r0, this.mAllowedComponents);
            IntentSanitizer.access$902(r0, this.mAllowedExtras);
            IntentSanitizer.access$1002(r0, this.mAllowClipDataText);
            IntentSanitizer.access$1102(r0, this.mAllowedClipDataUri);
            IntentSanitizer.access$1202(r0, this.mAllowedClipData);
            IntentSanitizer.access$1302(r0, this.mAllowIdentifier);
            IntentSanitizer.access$1402(r0, this.mAllowSelector);
            IntentSanitizer.access$1502(r0, this.mAllowSourceBounds);
            return r0;
        L9:
            if (this.mAllowSomeComponents == false) goto L12;
            goto L13
        }
    }

    private IntentSanitizer() {
    }

    /* synthetic */ IntentSanitizer(C00531 r1) {
        this();
    }

    static /* synthetic */ boolean access$1002(IntentSanitizer r0, boolean r1) {
        r0.mAllowClipDataText = r1;
        return r1;
    }

    static /* synthetic */ int access$102(IntentSanitizer r0, int r1) {
        r0.mAllowedFlags = r1;
        return r1;
    }

    static /* synthetic */ Predicate access$1102(IntentSanitizer r0, Predicate r1) {
        r0.mAllowedClipDataUri = r1;
        return r1;
    }

    static /* synthetic */ Predicate access$1202(IntentSanitizer r0, Predicate r1) {
        r0.mAllowedClipData = r1;
        return r1;
    }

    static /* synthetic */ boolean access$1302(IntentSanitizer r0, boolean r1) {
        r0.mAllowIdentifier = r1;
        return r1;
    }

    static /* synthetic */ boolean access$1402(IntentSanitizer r0, boolean r1) {
        r0.mAllowSelector = r1;
        return r1;
    }

    static /* synthetic */ boolean access$1502(IntentSanitizer r0, boolean r1) {
        r0.mAllowSourceBounds = r1;
        return r1;
    }

    static /* synthetic */ Predicate access$202(IntentSanitizer r0, Predicate r1) {
        r0.mAllowedActions = r1;
        return r1;
    }

    static /* synthetic */ Predicate access$302(IntentSanitizer r0, Predicate r1) {
        r0.mAllowedData = r1;
        return r1;
    }

    static /* synthetic */ Predicate access$402(IntentSanitizer r0, Predicate r1) {
        r0.mAllowedTypes = r1;
        return r1;
    }

    static /* synthetic */ Predicate access$502(IntentSanitizer r0, Predicate r1) {
        r0.mAllowedCategories = r1;
        return r1;
    }

    static /* synthetic */ Predicate access$602(IntentSanitizer r0, Predicate r1) {
        r0.mAllowedPackages = r1;
        return r1;
    }

    static /* synthetic */ boolean access$702(IntentSanitizer r0, boolean r1) {
        r0.mAllowAnyComponent = r1;
        return r1;
    }

    static /* synthetic */ Predicate access$802(IntentSanitizer r0, Predicate r1) {
        r0.mAllowedComponents = r1;
        return r1;
    }

    static /* synthetic */ Map access$902(IntentSanitizer r0, Map r1) {
        r0.mAllowedExtras = r1;
        return r1;
    }

    static /* synthetic */ void lambda$sanitizeByFiltering$0(String r0) {
    }

    static /* synthetic */ void lambda$sanitizeByThrowing$1(String r1) {
        throw new SecurityException(r1);
    }

    private void putExtra(Intent r4, String r5, Object r6) {
        if (r6 != null) goto L5;
        r4.getExtras().putString(r5, null);
        return;
    L5:
        if ((r6 instanceof Parcelable) == false) goto L8;
        r4.putExtra(r5, (Parcelable) r6);
        return;
    L8:
        if ((r6 instanceof Parcelable[]) == false) goto L11;
        r4.putExtra(r5, (Parcelable[]) r6);
        return;
    L11:
        if ((r6 instanceof Serializable) == false) goto L15;
        r4.putExtra(r5, (Serializable) r6);
        return;
    L15:
        throw new IllegalArgumentException("Unsupported type " + r6.getClass());
    }

    public Intent sanitize(Intent r17, Consumer<String> r18) {
        Intent r2 = new Intent();
        ComponentName r7 = r17.getComponent();
        if (this.mAllowAnyComponent == false) goto L6;
        if (r7 != null) goto L6;
    L7:
        r2.setComponent(r7);
    L9:
        String r8 = r17.getPackage();
        if (r8 != null) goto L12;
    L15:
        r2.setPackage(r8);
    L17:
        if ((this.mAllowedFlags | r17.getFlags()) != this.mAllowedFlags) goto L19;
        r2.setFlags(r17.getFlags());
    L20:
        String r9 = r17.getAction();
        if (r9 != null) goto L23;
    L26:
        r2.setAction(r9);
    L27:
        Uri r10 = r17.getData();
        if (r10 != null) goto L30;
    L33:
        r2.setData(r10);
    L34:
        String r11 = r17.getType();
        if (r11 != null) goto L37;
    L40:
        r2.setDataAndType(r2.getData(), r11);
    L41:
        Set<String> r12 = r17.getCategories();
        if (r12 == null) goto L51;
        Iterator<String> r1 = r12.iterator();
    L45:
        if (r1.hasNext() == false) goto L51;
        String r3 = r1.next();
        if (this.mAllowedCategories.test(r3) == true) goto L48;
        r18.accept("Category is not allowed: " + r3);
        goto L45
    L48:
        r2.addCategory(r3);
    L51:
        Bundle r13 = r17.getExtras();
        if (r13 == null) goto L73;
        Iterator<String> r4 = r13.keySet().iterator();
    L55:
        if (r4.hasNext() == false) goto L73;
        String r5 = r4.next();
        if (r5.equals("android.intent.extra.STREAM") == false) goto L62;
        if ((this.mAllowedFlags & 1) != 0) goto L62;
        r18.accept("Allowing Extra Stream requires also allowing at least  FLAG_GRANT_READ_URI_PERMISSION Flag.");
    L62:
        if (r5.equals("output") == false) goto L66;
        if (((~this.mAllowedFlags) & 3) == 0) goto L66;
        r18.accept("Allowing Extra Output requires also allowing FLAG_GRANT_READ_URI_PERMISSION and FLAG_GRANT_WRITE_URI_PERMISSION Flags.");
    L66:
        Object r6 = r13.get(r5);
        Predicate<Object> r14 = this.mAllowedExtras.get(r5);
        if (r14 == null) goto L71;
        if (r14.test(r6) == false) goto L71;
        putExtra(r2, r5, r6);
    L71:
        r18.accept("Extra is not allowed. Key: " + r5 + ". Value: " + r6);
    L73:
        Api16Impl.sanitizeClipData(r17, r2, this.mAllowedClipData, this.mAllowClipDataText, this.mAllowedClipDataUri, r18);
        if (Build.VERSION.SDK_INT < 29) goto L82;
        if (this.mAllowIdentifier == false) goto L79;
        Api29Impl.setIdentifier(r2, Api29Impl.getIdentifier(r17));
        goto L82
    L79:
        if (Api29Impl.getIdentifier(r17) == null) goto L82;
        r18.accept("Identifier is not allowed: " + Api29Impl.getIdentifier(r17));
    L82:
        if (this.mAllowSelector == false) goto L85;
        Api15Impl.setSelector(r2, Api15Impl.getSelector(r17));
    L88:
        if (this.mAllowSourceBounds == false) goto L91;
        r2.setSourceBounds(r17.getSourceBounds());
    L93:
        return r2;
    L91:
        if (r17.getSourceBounds() == null) goto L93;
        r18.accept("SourceBounds is not allowed: " + r17.getSourceBounds());
        goto L93
    L85:
        if (Api15Impl.getSelector(r17) == null) goto L88;
        r18.accept("Selector is not allowed: " + Api15Impl.getSelector(r17));
        goto L88
    L37:
        if (this.mAllowedTypes.test(r11) == true) goto L40;
        r18.accept("Type is not allowed: " + r11);
        goto L41
    L30:
        if (this.mAllowedData.test(r10) == true) goto L33;
        r18.accept("Data is not allowed: " + r10);
        goto L34
    L23:
        if (this.mAllowedActions.test(r9) == true) goto L26;
        r18.accept("Action is not allowed: " + r9);
        goto L27
    L19:
        r2.setFlags(this.mAllowedFlags & r17.getFlags());
        r18.accept("The intent contains flags that are not allowed: 0x" + Integer.toHexString(r17.getFlags() & (~this.mAllowedFlags)));
        goto L20
    L12:
        if (this.mAllowedPackages.test(r8) == true) goto L15;
        r18.accept("Package is not allowed: " + r8);
    L6:
        if (this.mAllowedComponents.test(r7) == true) goto L7;
        r18.accept("Component is not allowed: " + r7);
        r2.setComponent(new ComponentName("android", "java.lang.Void"));
        goto L9
    }

    public Intent sanitizeByFiltering(Intent r2) {
        return sanitize(r2, new IntentSanitizer$$ExternalSyntheticLambda1());
    }

    public Intent sanitizeByThrowing(Intent r2) {
        return sanitize(r2, new IntentSanitizer$$ExternalSyntheticLambda0());
    }
}
