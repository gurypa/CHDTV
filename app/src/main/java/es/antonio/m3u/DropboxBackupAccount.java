package es.antonio.m3u;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.security.keystore.KeyGenParameterSpec;
import android.util.AtomicFile;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
final class DropboxBackupAccount {
    DropboxBackupAccount() {
    }

    static boolean configured(Context r1) {
        return file(r1).isFile();
    }

    static EditText field(Activity r2, LinearLayout r3, String r4, boolean r5) {
        EditText r0 = new EditText(r2);
        r0.setSingleLine();
        r0.setHint(r4);
        if (r5 == false) goto L5;
        int r1 = 128;
    L6:
        r0.setInputType(r1 | 1);
        r3.addView(r0);
        return r0;
    L5:
        r1 = 144;
        goto L6
    }

    static File file(Context r3) {
        return new File(r3.getNoBackupFilesDir(), "dropbox-backup.enc");
    }

    static SecretKey key() throws Exception {
        KeyStore r1 = KeyStore.getInstance("AndroidKeyStore");
        r1.load(null);
        if (r1.containsAlias("chdtv_dropbox_backup") == true) goto L6;
        KeyGenerator r0 = KeyGenerator.getInstance("AES", "AndroidKeyStore");
        r0.init(new KeyGenParameterSpec.Builder("chdtv_dropbox_backup", 3).setBlockModes(new String[]{"GCM"}).setEncryptionPaddings(new String[]{"NoPadding"}).setKeySize(256).build());
        r0.generateKey();
    L6:
        return (SecretKey) r1.getKey("chdtv_dropbox_backup", null);
    }

    static /* synthetic */ void lambda$settings$0(Activity r2, Runnable r3, DialogInterface r4, int r5) {
        new AtomicFile(file(r2)).delete();
        r3.run();
    }

    static /* synthetic */ void lambda$settings$1(EditText r6, EditText r7, EditText r8, Activity r9, EditText r10, AlertDialog r11, Runnable r12, View r13) {
        String r0 = r6.getText().toString().trim();     // Catch: Exception -> L9
        String r1 = r7.getText().toString().trim();     // Catch: Exception -> L9
        if (r0.isEmpty() == true) goto L8;
        if (r1.isEmpty() == true) goto L8;
        String r2 = DropboxBackupClient.path(r8.getText().toString().trim());     // Catch: Exception -> L9
        save(r9, new JSONObject().put("key", r0).put("secret", r10.getText().toString().trim()).put("refresh", r1).put("path", r2));     // Catch: Exception -> L9
        r11.dismiss();     // Catch: Exception -> L9
        r12.run();     // Catch: Exception -> L9
        return;
    L8:
        throw new IOException();     // Catch: Exception -> L9
    L10:
        r7.setError("Comprueba credenciales y una ruta como /CHDTV-copia.zip");
    }

    static /* synthetic */ void lambda$settings$2(final AlertDialog r9, final EditText r10, final EditText r11, final EditText r12, final Activity r13, final EditText r14, final Runnable r15, DialogInterface r16) {
        r9.getButton(-1).setOnClickListener(new DropboxBackupAccount$$ExternalSyntheticLambda0(r10, r11, r12, r13, r14, r9, r15));
    }

    static JSONObject load(Context r7) throws Exception {
        byte[] r0 = new AtomicFile(file(r7)).readFully();
        if (r0.length < 28) goto L9;
        if (r0.length > 65536) goto L9;
        Cipher r1 = Cipher.getInstance("AES/GCM/NoPadding");
        r1.init(2, key(), new GCMParameterSpec(128, Arrays.copyOfRange(r0, 0, 12)));
        return new JSONObject(new String(r1.doFinal(Arrays.copyOfRange(r0, 12, r0.length)), "UTF-8"));
    L9:
        throw new IOException("Configura Dropbox de nuevo");
    }

    static void save(Context r5, JSONObject r6) throws Exception {
        Cipher r0 = Cipher.getInstance("AES/GCM/NoPadding");
        r0.init(1, key());
        byte[] r1 = r0.doFinal(r6.toString().getBytes("UTF-8"));
        AtomicFile r2 = new AtomicFile(file(r5));
        FileOutputStream r3 = r2.startWrite();
        r3.write(r0.getIV());     // Catch: Exception -> L5
        r3.write(r1);     // Catch: Exception -> L5
        r2.finishWrite(r3);     // Catch: Exception -> L5
        return;
    L5:
        e = move-exception;
        r2.failWrite(r3);
        throw e;
    }

    static void settings(final Activity r14, final Runnable r15) {
        LinearLayout r0 = new LinearLayout(r14);
        r0.setOrientation(1);
        int r2 = (int) (r14.getResources().getDisplayMetrics().density * 16.0f);
        r0.setPadding(r2, r2, r2, r2);
        TextView r3 = new TextView(r14);
        r3.setText("Configura tu aplicación Dropbox. La ruta se sobrescribe en cada copia. Estas credenciales quedan cifradas en este dispositivo y no se exportan en las copias.");
        r0.addView(r3);
        final EditText r8 = field(r14, r0, "APP_KEY", false);
        final EditText r12 = field(r14, r0, "APP_SECRET (opcional para PKCE)", true);
        final EditText r9 = field(r14, r0, "REFRESH_TOKEN", true);
        final EditText r10 = field(r14, r0, "Ruta del ZIP en Dropbox", false);
        r10.setText("/CHDTV-copia-completa.zip");
        if (configured(r14) == true) goto L9;
    L7:
        ScrollView r1 = new ScrollView(r14);
        r1.addView(r0);
        final AlertDialog r7 = new AlertDialog.Builder(r14).setTitle("Dropbox · subida directa").setView(r1).setPositiveButton("Guardar", null).setNeutralButton("Desconectar", new DropboxBackupAccount$$ExternalSyntheticLambda1(r14, r15)).setNegativeButton("Cancelar", null).create();
        r7.setOnShowListener(new DropboxBackupAccount$$ExternalSyntheticLambda2(r7, r8, r9, r10, r14, r12, r15));
        r7.show();
        return;
    L9:
        JSONObject r4 = load(r14);     // Catch: Exception -> L6
        r8.setText(r4.getString("key"));     // Catch: Exception -> L6
        r12.setText(r4.optString("secret"));     // Catch: Exception -> L6
        r9.setText(r4.getString("refresh"));     // Catch: Exception -> L6
        r10.setText(r4.getString("path"));     // Catch: Exception -> L6
        goto L7
    }
}
