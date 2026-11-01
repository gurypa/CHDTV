package androidx.core.app;

import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.PersistableBundle;
import androidx.core.graphics.drawable.IconCompat;

/* JADX INFO: loaded from: classes.dex */
public class Person {
    private static final String ICON_KEY = "icon";
    private static final String IS_BOT_KEY = "isBot";
    private static final String IS_IMPORTANT_KEY = "isImportant";
    private static final String KEY_KEY = "key";
    private static final String NAME_KEY = "name";
    private static final String URI_KEY = "uri";
    IconCompat mIcon;
    boolean mIsBot;
    boolean mIsImportant;
    String mKey;
    CharSequence mName;
    String mUri;

    static class Api22Impl {
        private Api22Impl() {
        }

        static Person fromPersistableBundle(PersistableBundle r2) {
            return new Builder().setName(r2.getString(Person.NAME_KEY)).setUri(r2.getString(Person.URI_KEY)).setKey(r2.getString(Person.KEY_KEY)).setBot(r2.getBoolean(Person.IS_BOT_KEY)).setImportant(r2.getBoolean(Person.IS_IMPORTANT_KEY)).build();
        }

        static PersistableBundle toPersistableBundle(Person r3) {
            PersistableBundle r0 = new PersistableBundle();
            if (r3.mName == null) goto L5;
            String r1 = r3.mName.toString();
        L6:
            r0.putString(Person.NAME_KEY, r1);
            r0.putString(Person.URI_KEY, r3.mUri);
            r0.putString(Person.KEY_KEY, r3.mKey);
            r0.putBoolean(Person.IS_BOT_KEY, r3.mIsBot);
            r0.putBoolean(Person.IS_IMPORTANT_KEY, r3.mIsImportant);
            return r0;
        L5:
            r1 = null;
            goto L6
        }
    }

    static class Api28Impl {
        private Api28Impl() {
        }

        static Person fromAndroidPerson(android.app.Person r2) {
            Builder r0 = new Builder().setName(r2.getName());
            if (r2.getIcon() == null) goto L5;
            IconCompat r1 = IconCompat.createFromIcon(r2.getIcon());
        L7:
            return r0.setIcon(r1).setUri(r2.getUri()).setKey(r2.getKey()).setBot(r2.isBot()).setImportant(r2.isImportant()).build();
        L5:
            r1 = null;
            goto L7
        }

        static android.app.Person toAndroidPerson(Person r2) {
            android.app.Person.Builder r0 = new android.app.Person.Builder().setName(r2.getName());
            if (r2.getIcon() == null) goto L5;
            Icon r1 = r2.getIcon().toIcon();
        L7:
            return r0.setIcon(r1).setUri(r2.getUri()).setKey(r2.getKey()).setBot(r2.isBot()).setImportant(r2.isImportant()).build();
        L5:
            r1 = null;
            goto L7
        }
    }

    public static class Builder {
        IconCompat mIcon;
        boolean mIsBot;
        boolean mIsImportant;
        String mKey;
        CharSequence mName;
        String mUri;

        public Builder() {
        }

        Builder(Person r2) {
            this.mName = r2.mName;
            this.mIcon = r2.mIcon;
            this.mUri = r2.mUri;
            this.mKey = r2.mKey;
            this.mIsBot = r2.mIsBot;
            this.mIsImportant = r2.mIsImportant;
        }

        public Person build() {
            return new Person(this);
        }

        public Builder setBot(boolean r1) {
            this.mIsBot = r1;
            return this;
        }

        public Builder setIcon(IconCompat r1) {
            this.mIcon = r1;
            return this;
        }

        public Builder setImportant(boolean r1) {
            this.mIsImportant = r1;
            return this;
        }

        public Builder setKey(String r1) {
            this.mKey = r1;
            return this;
        }

        public Builder setName(CharSequence r1) {
            this.mName = r1;
            return this;
        }

        public Builder setUri(String r1) {
            this.mUri = r1;
            return this;
        }
    }

    Person(Builder r2) {
        this.mName = r2.mName;
        this.mIcon = r2.mIcon;
        this.mUri = r2.mUri;
        this.mKey = r2.mKey;
        this.mIsBot = r2.mIsBot;
        this.mIsImportant = r2.mIsImportant;
    }

    public static Person fromAndroidPerson(android.app.Person r1) {
        return Api28Impl.fromAndroidPerson(r1);
    }

    public static Person fromBundle(Bundle r3) {
        Bundle r0 = r3.getBundle(ICON_KEY);
        Builder r1 = new Builder().setName(r3.getCharSequence(NAME_KEY));
        if (r0 == null) goto L5;
        IconCompat r2 = IconCompat.createFromBundle(r0);
    L7:
        return r1.setIcon(r2).setUri(r3.getString(URI_KEY)).setKey(r3.getString(KEY_KEY)).setBot(r3.getBoolean(IS_BOT_KEY)).setImportant(r3.getBoolean(IS_IMPORTANT_KEY)).build();
    L5:
        r2 = null;
        goto L7
    }

    public static Person fromPersistableBundle(PersistableBundle r1) {
        return Api22Impl.fromPersistableBundle(r1);
    }

    public IconCompat getIcon() {
        return this.mIcon;
    }

    public String getKey() {
        return this.mKey;
    }

    public CharSequence getName() {
        return this.mName;
    }

    public String getUri() {
        return this.mUri;
    }

    public boolean isBot() {
        return this.mIsBot;
    }

    public boolean isImportant() {
        return this.mIsImportant;
    }

    public String resolveToLegacyUri() {
        if (this.mUri == null) goto L7;
        return this.mUri;
    L7:
        if (this.mName != null) goto L9;
        return "";
    L9:
        return "name:" + this.mName;
    }

    public android.app.Person toAndroidPerson() {
        return Api28Impl.toAndroidPerson(this);
    }

    public Builder toBuilder() {
        return new Builder(this);
    }

    public Bundle toBundle() {
        Bundle r0 = new Bundle();
        r0.putCharSequence(NAME_KEY, this.mName);
        if (this.mIcon == null) goto L5;
        Bundle r1 = this.mIcon.toBundle();
    L6:
        r0.putBundle(ICON_KEY, r1);
        r0.putString(URI_KEY, this.mUri);
        r0.putString(KEY_KEY, this.mKey);
        r0.putBoolean(IS_BOT_KEY, this.mIsBot);
        r0.putBoolean(IS_IMPORTANT_KEY, this.mIsImportant);
        return r0;
    L5:
        r1 = null;
        goto L6
    }

    public PersistableBundle toPersistableBundle() {
        return Api22Impl.toPersistableBundle(this);
    }
}
