package androidx.core.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import java.io.FileNotFoundException;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class DocumentsContractCompat {
    private static final String PATH_TREE = "tree";

    public static final class DocumentCompat {
        public static final int FLAG_VIRTUAL_DOCUMENT = 512;

        private DocumentCompat() {
        }
    }

    private static class DocumentsContractApi19Impl {
        private DocumentsContractApi19Impl() {
        }

        public static Uri buildDocumentUri(String r1, String r2) {
            return DocumentsContract.buildDocumentUri(r1, r2);
        }

        static boolean deleteDocument(ContentResolver r1, Uri r2) throws FileNotFoundException {
            return DocumentsContract.deleteDocument(r1, r2);
        }

        static String getDocumentId(Uri r1) {
            return DocumentsContract.getDocumentId(r1);
        }

        static boolean isDocumentUri(Context r1, Uri r2) {
            return DocumentsContract.isDocumentUri(r1, r2);
        }
    }

    private static class DocumentsContractApi21Impl {
        private DocumentsContractApi21Impl() {
        }

        static Uri buildChildDocumentsUri(String r1, String r2) {
            return DocumentsContract.buildChildDocumentsUri(r1, r2);
        }

        static Uri buildChildDocumentsUriUsingTree(Uri r1, String r2) {
            return DocumentsContract.buildChildDocumentsUriUsingTree(r1, r2);
        }

        static Uri buildDocumentUriUsingTree(Uri r1, String r2) {
            return DocumentsContract.buildDocumentUriUsingTree(r1, r2);
        }

        public static Uri buildTreeDocumentUri(String r1, String r2) {
            return DocumentsContract.buildTreeDocumentUri(r1, r2);
        }

        static Uri createDocument(ContentResolver r1, Uri r2, String r3, String r4) throws FileNotFoundException {
            return DocumentsContract.createDocument(r1, r2, r3, r4);
        }

        static String getTreeDocumentId(Uri r1) {
            return DocumentsContract.getTreeDocumentId(r1);
        }

        static Uri renameDocument(ContentResolver r1, Uri r2, String r3) throws FileNotFoundException {
            return DocumentsContract.renameDocument(r1, r2, r3);
        }
    }

    private static class DocumentsContractApi24Impl {
        private DocumentsContractApi24Impl() {
        }

        static boolean isTreeUri(Uri r1) {
            return DocumentsContract.isTreeUri(r1);
        }

        static boolean removeDocument(ContentResolver r1, Uri r2, Uri r3) throws FileNotFoundException {
            return DocumentsContract.removeDocument(r1, r2, r3);
        }
    }

    private DocumentsContractCompat() {
    }

    public static Uri buildChildDocumentsUri(String r1, String r2) {
        return DocumentsContractApi21Impl.buildChildDocumentsUri(r1, r2);
    }

    public static Uri buildChildDocumentsUriUsingTree(Uri r1, String r2) {
        return DocumentsContractApi21Impl.buildChildDocumentsUriUsingTree(r1, r2);
    }

    public static Uri buildDocumentUri(String r1, String r2) {
        return DocumentsContractApi19Impl.buildDocumentUri(r1, r2);
    }

    public static Uri buildDocumentUriUsingTree(Uri r1, String r2) {
        return DocumentsContractApi21Impl.buildDocumentUriUsingTree(r1, r2);
    }

    public static Uri buildTreeDocumentUri(String r1, String r2) {
        return DocumentsContractApi21Impl.buildTreeDocumentUri(r1, r2);
    }

    public static Uri createDocument(ContentResolver r1, Uri r2, String r3, String r4) throws FileNotFoundException {
        return DocumentsContractApi21Impl.createDocument(r1, r2, r3, r4);
    }

    public static String getDocumentId(Uri r1) {
        return DocumentsContractApi19Impl.getDocumentId(r1);
    }

    public static String getTreeDocumentId(Uri r1) {
        return DocumentsContractApi21Impl.getTreeDocumentId(r1);
    }

    public static boolean isDocumentUri(Context r1, Uri r2) {
        return DocumentsContractApi19Impl.isDocumentUri(r1, r2);
    }

    public static boolean isTreeUri(Uri r4) {
        if (Build.VERSION.SDK_INT >= 24) goto L11;
        List<String> r0 = r4.getPathSegments();
        if (r0.size() >= 2) goto L7;
        return false;
    L7:
        if (PATH_TREE.equals(r0.get(0)) == false) goto L13;
        return true;
    L13:
        return false;
    L11:
        return DocumentsContractApi24Impl.isTreeUri(r4);
    }

    public static boolean removeDocument(ContentResolver r2, Uri r3, Uri r4) throws FileNotFoundException {
        if (Build.VERSION.SDK_INT < 24) goto L7;
        return DocumentsContractApi24Impl.removeDocument(r2, r3, r4);
    L7:
        return DocumentsContractApi19Impl.deleteDocument(r2, r3);
    }

    public static Uri renameDocument(ContentResolver r1, Uri r2, String r3) throws FileNotFoundException {
        return DocumentsContractApi21Impl.renameDocument(r1, r2, r3);
    }
}
