package androidx.core.app;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.os.Build;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class NotificationChannelGroupCompat {
    private boolean mBlocked;
    private List<NotificationChannelCompat> mChannels;
    String mDescription;
    final String mId;
    CharSequence mName;

    public static class Builder {
        final NotificationChannelGroupCompat mGroup;

        public Builder(String r2) {
            this.mGroup = new NotificationChannelGroupCompat(r2);
        }

        public NotificationChannelGroupCompat build() {
            return this.mGroup;
        }

        public Builder setDescription(String r2) {
            this.mGroup.mDescription = r2;
            return this;
        }

        public Builder setName(CharSequence r2) {
            this.mGroup.mName = r2;
            return this;
        }
    }

    NotificationChannelGroupCompat(NotificationChannelGroup r2) {
        this(r2, Collections.emptyList());
    }

    NotificationChannelGroupCompat(NotificationChannelGroup r3, List<NotificationChannel> r4) {
        this(r3.getId());
        this.mName = r3.getName();
        if (Build.VERSION.SDK_INT < 28) goto L6;
        this.mDescription = r3.getDescription();
    L6:
        if (Build.VERSION.SDK_INT < 28) goto L8;
        this.mBlocked = r3.isBlocked();
        this.mChannels = getChannelsCompat(r3.getChannels());
        return;
    L8:
        this.mChannels = getChannelsCompat(r4);
    }

    NotificationChannelGroupCompat(String r2) {
        this.mChannels = Collections.emptyList();
        this.mId = (String) Preconditions.checkNotNull(r2);
    }

    private List<NotificationChannelCompat> getChannelsCompat(List<NotificationChannel> r6) {
        ArrayList r0 = new ArrayList();
        Iterator<NotificationChannel> r1 = r6.iterator();
    L4:
        if (r1.hasNext() == false) goto L9;
        NotificationChannel r2 = r1.next();
        if (this.mId.equals(r2.getGroup()) == false) goto L4;
        r0.add(new NotificationChannelCompat(r2));
        goto L4
    L9:
        return r0;
    }

    public List<NotificationChannelCompat> getChannels() {
        return this.mChannels;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String getId() {
        return this.mId;
    }

    public CharSequence getName() {
        return this.mName;
    }

    NotificationChannelGroup getNotificationChannelGroup() {
        if (Build.VERSION.SDK_INT >= 26) goto L6;
        return null;
    L6:
        NotificationChannelGroup r0 = new NotificationChannelGroup(this.mId, this.mName);
        if (Build.VERSION.SDK_INT < 28) goto L9;
        r0.setDescription(this.mDescription);
    L9:
        return r0;
    }

    public boolean isBlocked() {
        return this.mBlocked;
    }

    public Builder toBuilder() {
        return new Builder(this.mId).setName(this.mName).setDescription(this.mDescription);
    }
}
