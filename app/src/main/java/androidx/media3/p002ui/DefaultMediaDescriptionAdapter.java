package androidx.media3.p002ui;

import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import androidx.media3.common.Player;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultMediaDescriptionAdapter implements PlayerNotificationManager.MediaDescriptionAdapter {
    private final PendingIntent pendingIntent;

    public DefaultMediaDescriptionAdapter(PendingIntent r1) {
        this.pendingIntent = r1;
    }

    @Override // androidx.media3.ui.PlayerNotificationManager.MediaDescriptionAdapter
    public PendingIntent createCurrentContentIntent(Player r2) {
        return this.pendingIntent;
    }

    @Override // androidx.media3.ui.PlayerNotificationManager.MediaDescriptionAdapter
    public CharSequence getCurrentContentText(Player r3) {
        if (r3.isCommandAvailable(18) == true) goto L6;
        return null;
    L6:
        CharSequence r0 = r3.getMediaMetadata().artist;
        if (TextUtils.isEmpty(r0) == true) goto L10;
        return r0;
    L10:
        return r3.getMediaMetadata().albumArtist;
    }

    @Override // androidx.media3.ui.PlayerNotificationManager.MediaDescriptionAdapter
    public CharSequence getCurrentContentTitle(Player r4) {
        if (r4.isCommandAvailable(18) == true) goto L5;
        return "";
    L5:
        CharSequence r0 = r4.getMediaMetadata().displayTitle;
        if (TextUtils.isEmpty(r0) == true) goto L8;
        return r0;
    L8:
        CharSequence r2 = r4.getMediaMetadata().title;
        if (r2 != null) goto L11;
        return "";
    L11:
        return r2;
    }

    @Override // androidx.media3.ui.PlayerNotificationManager.MediaDescriptionAdapter
    public Bitmap getCurrentLargeIcon(Player r4, PlayerNotificationManager.BitmapCallback r5) {
        if (r4.isCommandAvailable(18) == true) goto L5;
        return null;
    L5:
        byte[] r0 = r4.getMediaMetadata().artworkData;
        if (r0 != null) goto L9;
        return null;
    L9:
        return BitmapFactory.decodeByteArray(r0, 0, r0.length);
    }

    @Override // androidx.media3.ui.PlayerNotificationManager.MediaDescriptionAdapter
    public /* synthetic */ CharSequence getCurrentSubText(Player r1) {
        return PlayerNotificationManager.MediaDescriptionAdapter.CC.$default$getCurrentSubText(this, r1);
    }
}
