package es.antonio.m3u;

import androidx.media3.common.C0180C;
import androidx.media3.common.PlaybackException;
import androidx.media3.datasource.HttpDataSource;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Set;
import javax.net.ssl.SSLException;

/* JADX INFO: loaded from: classes7.dex */
final class PlaybackReason222 {
    private static long lastAt;
    private static String lastCode;

    static final class Info {
        final String code;
        final String detail;
        final int http;
        final String stage;
        final String title;

        Info(String r7, String r8, String r9, String r10) {
            this(r7, r8, r9, r10, 0);
        }

        Info(String r1, String r2, String r3, String r4, int r5) {
            this.stage = r1;
            this.code = r2;
            this.title = r3;
            this.detail = r4;
            this.http = r5;
        }
    }

    static {
        lastCode = "";
    }

    private PlaybackReason222() {
    }

    static String details(Throwable r6) {
        Info r0 = info(r6);
        StringBuilder r1 = new StringBuilder("Etapa: ").append(r0.stage).append("\nMotivo: ").append(r0.title).append("\nCódigo: ").append(r0.code).append("\n\n").append(r0.detail);
        if ((r6 instanceof PlaybackException) == false) goto L5;
        r1.append("\nMedia3: ").append(((PlaybackException) r6).getErrorCodeName());
    L5:
        Set r2 = Collections.newSetFromMap(new IdentityHashMap());
        int r3 = 0;
    L6:
        if (r6 == null) goto L17;
        int r4 = r3 + 1;
        if (r3 >= 16) goto L17;
        if (r2.add(r6) == false) goto L17;
        String r5 = r6.getClass().getSimpleName();
        StringBuilder r7 = r1.append("\nCausa ").append(r4).append(": ");
        if (r5.matches("[A-Za-z0-9_$]{1,80}") == true) goto L15;
        r5 = "Throwable";
    L15:
        r7.append(r5);
        r6 = r6.getCause();
        r3 = r4;
    L17:
        return r1.toString();
    }

    static Info info(Throwable r4) {
        return inspect(r4);
    L6:
        return new Info("Diagnóstico", "DIAGNOSTIC_UNAVAILABLE", "No se pudo ampliar el diagnóstico", "El error original se conserva en los detalles técnicos.");
    }

    static Info inspect(Throwable r15) {
        ArrayList r0 = new ArrayList();
        Set r1 = Collections.newSetFromMap(new IdentityHashMap());
        Throwable r2 = r15;
    L3:
        if (r2 == null) goto L9;
        if (r0.size() >= 16) goto L9;
        if (r1.add(r2) == false) goto L9;
        r0.add(r2);
        r2 = r2.getCause();
    L9:
        Iterator r3 = r0.iterator();
    L11:
        if (r3.hasNext() == false) goto L44;
        Throwable r4 = (Throwable) r3.next();
        if ((r4 instanceof HttpDataSource.InvalidResponseCodeException) == false) goto L11;
        int r8 = ((HttpDataSource.InvalidResponseCodeException) r4).responseCode;
        if (r8 != 401) goto L18;
        String r16 = "El servidor exige autorización";
    L43:
        return new Info("Conexión HTTP", "HTTP_" + r8, "HTTP " + r8 + ": " + r16, "La respuesta HTTP está confirmada. El código no permite determinar por sí solo si el problema es un token, una restricción de acceso o el estado del servidor.", r8);
    L18:
        if (r8 != 403) goto L21;
        r16 = "El servidor rechaza el acceso";
        goto L43
    L21:
        if (r8 != 404) goto L24;
        r16 = "El recurso no se encuentra";
        goto L43
    L24:
        if (r8 != 408) goto L26;
    L41:
        r16 = "El servidor agotó el tiempo de espera";
        goto L43
    L26:
        if (r8 == 504) goto L41;
        if (r8 != 416) goto L32;
        r16 = "El servidor rechaza el rango solicitado";
        goto L43
    L32:
        if (r8 != 429) goto L35;
        r16 = "El servidor limita las peticiones";
        goto L43
    L35:
        if (r8 != 503) goto L38;
        r16 = "Servicio no disponible";
        goto L43
    L38:
        if (r8 < 500) goto L40;
        r16 = "Error del servidor";
        goto L43
    L40:
        r16 = "El servidor devolvió un error";
        goto L43
    L44:
        Iterator r5 = r0.iterator();
    L46:
        if (r5.hasNext() == false) goto L63;
        Throwable r6 = (Throwable) r5.next();
        if ((r6 instanceof UnknownHostException) == true) goto L50;
        if ((r6 instanceof SocketTimeoutException) == true) goto L54;
        if ((r6 instanceof SSLException) == true) goto L58;
        if ((r6 instanceof ConnectException) == false) goto L46;
        return new Info("Conexión", "CONNECT", "No se pudo conectar con el servidor", "Falló la apertura de la conexión.");
    L58:
        return new Info("Conexión segura", "TLS", "Falló la conexión HTTPS", "Se produjo un error TLS o de certificado; no confirma un problema de clave de vídeo.");
    L54:
        return new Info("Conexión", "TIMEOUT", "Se agotó el tiempo de conexión o lectura", "La conexión no completó una operación dentro del tiempo permitido.");
    L50:
        return new Info("Conexión", "DNS", "No se pudo localizar el servidor", "Falló la resolución DNS; no se ha llegado al descifrado.");
    L63:
        Iterator r7 = r0.iterator();
        boolean r9 = false;
        boolean r10 = false;
    L65:
        if (r7.hasNext() == false) goto L163;
        Throwable r11 = (Throwable) r7.next();
        StackTraceElement[] r12 = r11.getStackTrace();
        int r13 = r12.length;
        int r14 = 0;
    L67:
        if (r14 >= r13) goto L75;
        StackTraceElement r17 = r12[r14];
        if (r17.getClassName().equals("com.cineplex.sampleaes.NativeDecryptor") == false) goto L72;
        r9 = true;
    L72:
        if (r17.getClassName().startsWith("es.antonio.chdtv.playback.sampleaes.") == false) goto L74;
        r10 = true;
    L74:
        r14 = r14 + 1;
        goto L67
    L75:
        boolean r18 = r11 instanceof UnsatisfiedLinkError;
        if (r18 == true) goto L78;
        String r19 = r11.getMessage();
        if (r19 == null) goto L65;
        if (r19.equals("Sample-AES: no hay una clave local para el KID del manifiesto") == true) goto L162;
        if (r19.equals("Sample-AES: falta una clave para este segmento") == true) goto L162;
        if (r19.equals("Sample-AES: configuración de claves locales inválida") == true) goto L160;
        if (r19.equals("Sample-AES: clave o IV inválidos") == true) goto L160;
        if (r19.equals("Se necesitan una clave AES y un IV de 16 bytes") == true) goto L160;
        if (r19.equals("Sample-AES: metadatos SKD inválidos") == true) goto L158;
        if (r19.equals("Sample-AES: referencia SKD inválida") == true) goto L158;
        if (r19.equals("Sample-AES: falta el KID del manifiesto") == true) goto L158;
        if (r19.equals("Sample-AES: segmento incompleto") == true) goto L156;
        if (r19.equals("Sample-AES: descarga sin progreso") == true) goto L156;
        if (r19.equals("Sample-AES: segmento vacío") == true) goto L156;
        if (r19.equals("Sample-AES: este motor necesita TS o AAC, no fMP4") == true) goto L154;
        if (r19.equals("Sample-AES: las partes LL-HLS requieren un adaptador específico") == true) goto L154;
        if (r19.equals("Sample-AES: KEYFORMAT no compatible con el motor local") == true) goto L154;
        if (r19.equals("Sample-AES: CTR con KEYFORMAT identity requiere otro motor CENC") == true) goto L154;
        if (r19.startsWith("El segmento no está alineado a paquetes MPEG-TS") == true) goto L152;
        if (r19.startsWith("Sin sincronización MPEG-TS en el paquete") == true) goto L152;
        if (r19.startsWith("Sample-AES: salida nativa") == true) goto L125;
        if (r19.startsWith("Invalid encrypted MPEG-TS segment; native=") == true) goto L129;
        if (r19.equals("Invalid encrypted MPEG-TS segment") == true) goto L150;
        if (r19.startsWith("Sample-AES: fallo del motor nativo TS [MPEG_TS_REJECTED]") == true) goto L150;
        if (r19.equals("Native decryptor failed") == true) goto L148;
        if (r19.startsWith("Sample-AES: fallo del motor nativo TS [NATIVE_INTERNAL]") == true) goto L148;
        if (r19.equals("Sample-AES: motor nativo no disponible en este dispositivo") == true) goto L146;
        if (r18 == false) goto L65;
    L146:
        return new Info("Motor nativo", "NATIVE_UNAVAILABLE", "No se pudo cargar el motor nativo", "Comprueba que la APK corresponda a la arquitectura del dispositivo.");
    L148:
        return new Info("Motor Rust / SAMPLE-AES", "NATIVE_INTERNAL", "Falló internamente el motor Rust", "La llamada nativa terminó con un fallo interno; no se ha identificado una causa más concreta.");
    L150:
        return new Info("Motor Rust / SAMPLE-AES", "MPEG_TS_REJECTED", "Rust rechazó el segmento TS", "El motor nativo devolvió un rechazo genérico. La causa interna exacta no está disponible; este resultado no demuestra que el token o la clave sean incorrectos.");
    L129:
        return new Info("Motor Rust / SAMPLE-AES", "MPEG_TS_REJECTED", "Rust rechazó el segmento TS", "Diagnóstico nativo: " + HlsDiagnostics225.safeCause(r11) + ". Los detalles del segmento están en la caja negra.");
    L125:
        return new Info("Salida del motor Rust", "NATIVE_OUTPUT_INVALID", "Rust devolvió una salida vacía o inválida", "Falló la validación de los bytes devueltos por el motor nativo.");
    L152:
        return new Info("Formato del segmento", "TS_INVALID", "El segmento no tiene una estructura TS válida", "Falló la comprobación de tamaño o sincronización de paquetes MPEG-TS antes de completar el procesamiento.");
    L154:
        return new Info("Compatibilidad HLS", "ENCRYPTION_UNSUPPORTED", "Formato HLS o cifrado no compatible con este motor", "El adaptador rechazó expresamente un formato o esquema que no admite.");
    L156:
        return new Info("Descarga del segmento", "SEGMENT_INCOMPLETE", "El segmento llegó vacío o incompleto", "No se dispone de los bytes completos que necesita el procesamiento.");
    L158:
        return new Info("Manifiesto HLS", "SKD_INVALID", "La referencia de cifrado HLS no es válida", "No se pudieron interpretar los metadatos SKD o el identificador de clave.");
    L160:
        return new Info("Clave HLS", "KEY_INVALID", "La clave o el IV tienen un formato inválido", "Se ha rechazado la estructura de la configuración. Esto no determina si una clave con formato válido descifra correctamente.");
    L162:
        return new Info("Clave HLS", "KEY_MISSING", "Falta la clave que pide este canal", "El manifiesto solicita una clave que no está en la configuración local. No es un fallo de token Movistar.");
    L78:
        return new Info("Motor nativo", "NATIVE_UNAVAILABLE", "No se pudo cargar el motor nativo", "Comprueba que la APK corresponda a la arquitectura del dispositivo.");
    L163:
        if (r9 == false) goto L167;
        return new Info("Motor Rust / SAMPLE-AES", "NATIVE_IO", "Falló el procesamiento nativo del segmento", "La traza confirma una llamada a Rust, pero no proporciona un motivo interno más concreto.");
    L167:
        if ((r15 instanceof PlaybackException) == false) goto L184;
        String r110 = ((PlaybackException) r15).getErrorCodeName();
        if (r110.startsWith("ERROR_CODE_DECOD") == true) goto L182;
        if (r110.startsWith("ERROR_CODE_AUDIO_TRACK") == true) goto L182;
        if (r110.startsWith("ERROR_CODE_DRM") == false) goto L178;
        return new Info("Licencia / DRM", "DRM", "Falló la licencia o el sistema DRM", "Media3 notificó un fallo DRM. El código genérico no distingue por sí solo entre licencia, clave y compatibilidad.");
    L178:
        if (r110.startsWith("ERROR_CODE_PARSING") == false) goto L184;
        return new Info("Lectura del contenido", "PARSING", "No se pudo interpretar el contenido", "Media3 rechazó el formato de entrada o su estructura.");
    L182:
        return new Info("Decodificación de audio/vídeo", "DECODER", "Falló el decodificador del dispositivo", "Media3 notificó un error de decodificación o de salida de audio. No equivale a un fallo de descifrado.");
    L184:
        if (r10 == false) goto L186;
        Info r111 = new Info("Adaptador HLS / SAMPLE-AES", "HLS_PROCESSING", "Falló el procesamiento HLS", "La traza sitúa el fallo en el adaptador HLS, pero no identifica una causa exacta.");
    L187:
        return r111;
    L186:
        r111 = new Info("Reproducción", "UNKNOWN", "La reproducción ha fallado", "El registro no contiene evidencia suficiente para atribuir una causa concreta.");
        goto L187
    }

    static void record(String r7, Throwable r8) {
        if (BlackBox.active() == false) goto L27;
        if (r7 != null) goto L6;
        return;
    L6:
        if (r7.startsWith("PLAYER_ERROR_") == false) goto L8;
    L10:
        Info r9 = info(r8);
        long r0 = System.nanoTime();
        monitor-enter(PlaybackReason222.class);
    L24:
        th = move-exception;
        throw th;
    L13:
        if (r7.equals("MEDIA_LOAD_ERROR") == true) goto L15;
    L20:
        lastCode = r9.code;     // Catch: Throwable -> L24
        lastAt = r0;     // Catch: Throwable -> L24
        monitor-exit(PlaybackReason222.class);     // Catch: Throwable -> L24
        BlackBox.event("PLAYBACK_REASON", "code=" + r9.code + " http=" + r9.http + " stage=" + r9.stage);
        return;
    L15:
        if (r9.code.equals(lastCode) == false) goto L20;
        if ((r0 - lastAt) >= C0180C.NANOS_PER_SECOND) goto L20;
        monitor-exit(PlaybackReason222.class);     // Catch: Throwable -> L24
        return;
    L8:
        if (r7.equals("MEDIA_LOAD_ERROR") == true) goto L10;
        return;
    }

    static String summary(Throwable r2) {
        Info r3 = info(r2);
        return r3.title + " [" + r3.code + "] · Detalles en ⚙";
    }
}
