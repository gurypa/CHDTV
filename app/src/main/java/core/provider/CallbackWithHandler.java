package androidx.core.provider;

import android.graphics.Typeface;
import android.os.Handler;

/* JADX INFO: loaded from: classes.dex */
class CallbackWithHandler {
    private final FontsContractCompat.FontRequestCallback mCallback;
    private final Handler mCallbackHandler;

    CallbackWithHandler(FontsContractCompat.FontRequestCallback r2) {
        this.mCallback = r2;
        this.mCallbackHandler = CalleeHandler.create();
    }

    CallbackWithHandler(FontsContractCompat.FontRequestCallback r1, Handler r2) {
        this.mCallback = r1;
        this.mCallbackHandler = r2;
    }

    private void onTypefaceRequestFailed(final int r4) {
        final FontsContractCompat.FontRequestCallback r0 = this.mCallback;
        this.mCallbackHandler.post(new RunnableC00832(this, r0, r4));
    }

    private void onTypefaceRetrieved(final Typeface r4) {
        final FontsContractCompat.FontRequestCallback r0 = this.mCallback;
        this.mCallbackHandler.post(new RunnableC00821(this, r0, r4));
    }

    void onTypefaceResult(FontRequestWorker.TypefaceResult r2) {
        if (r2.isSuccess() == false) goto L5;
        onTypefaceRetrieved(r2.mTypeface);
        return;
    L5:
        onTypefaceRequestFailed(r2.mResult);
    }
}
