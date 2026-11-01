package androidx.media3.common.util;

import android.content.Context;
import android.opengl.GLES20;
import java.io.IOException;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public final class GlProgram {
    private static final int GL_SAMPLER_EXTERNAL_2D_Y2Y_EXT = 35815;
    private final Map<String, Attribute> attributeByName;
    private final Attribute[] attributes;
    private boolean externalTexturesRequireNearestSampling;
    private final int programId;
    private final Map<String, Uniform> uniformByName;
    private final Uniform[] uniforms;

    private static final class Attribute {
        private Buffer buffer;
        private final int location;
        public final String name;
        private int size;

        private Attribute(String r1, int r2) {
            this.name = r1;
            this.location = r2;
        }

        public static Attribute create(int r13, int r14) {
            int[] r11 = new int[1];
            GLES20.glGetProgramiv(r13, 35722, r11, 0);
            byte[] r9 = new byte[r11[0]];
            GLES20.glGetActiveAttrib(r13, r14, r11[0], new int[1], 0, new int[1], 0, new int[1], 0, r9, 0);
            String r1 = new String(r9, 0, GlProgram.access$000(r9));
            return new Attribute(r1, GlProgram.access$100(r13, r1));
        }

        public void bind() throws GlUtil.GlException {
            Buffer r6 = (Buffer) Assertions.checkNotNull(this.buffer, "call setBuffer before bind");
            GLES20.glBindBuffer(34962, 0);
            GLES20.glVertexAttribPointer(this.location, this.size, 5126, false, 0, r6);
            GLES20.glEnableVertexAttribArray(this.location);
            GlUtil.checkGlError();
        }

        public void setBuffer(float[] r2, int r3) {
            this.buffer = GlUtil.createBuffer(r2);
            this.size = r3;
        }
    }

    private static final class Uniform {
        private final float[] floatValue;
        private final int[] intValue;
        private final int location;
        public final String name;
        private int texIdValue;
        private int texUnitIndex;
        private final int type;

        private Uniform(String r2, int r3, int r4) {
            this.name = r2;
            this.location = r3;
            this.type = r4;
            this.floatValue = new float[16];
            this.intValue = new int[4];
        }

        public static Uniform create(int r13, int r14) {
            int[] r11 = new int[1];
            GLES20.glGetProgramiv(r13, 35719, r11, 0);
            int[] r7 = new int[1];
            byte[] r9 = new byte[r11[0]];
            GLES20.glGetActiveUniform(r13, r14, r11[0], new int[1], 0, new int[1], 0, r7, 0, r9, 0);
            String r1 = new String(r9, 0, GlProgram.access$000(r9));
            return new Uniform(r1, GlProgram.access$200(r13, r1), r7[0]);
        }

        public void bind(boolean r5) throws GlUtil.GlException {
            switch(this.type) {
                case 5124: goto L30;
                case 5126: goto L29;
                case 35664: goto L28;
                case 35665: goto L27;
                case 35666: goto L26;
                case 35667: goto L25;
                case 35668: goto L24;
                case 35669: goto L23;
                case 35675: goto L22;
                case 35676: goto L21;
                case 35678: goto L7;
                case 35815: goto L7;
                case 36198: goto L7;
                default: goto L5;
            };
        L21:
            GLES20.glUniformMatrix4fv(this.location, 1, false, this.floatValue, 0);
            GlUtil.checkGlError();
            return;
        L22:
            GLES20.glUniformMatrix3fv(this.location, 1, false, this.floatValue, 0);
            GlUtil.checkGlError();
            return;
        L23:
            GLES20.glUniform4iv(this.location, 1, this.intValue, 0);
            GlUtil.checkGlError();
            return;
        L24:
            GLES20.glUniform3iv(this.location, 1, this.intValue, 0);
            GlUtil.checkGlError();
            return;
        L25:
            GLES20.glUniform2iv(this.location, 1, this.intValue, 0);
            GlUtil.checkGlError();
            return;
        L26:
            GLES20.glUniform4fv(this.location, 1, this.floatValue, 0);
            GlUtil.checkGlError();
            return;
        L27:
            GLES20.glUniform3fv(this.location, 1, this.floatValue, 0);
            GlUtil.checkGlError();
            return;
        L28:
            GLES20.glUniform2fv(this.location, 1, this.floatValue, 0);
            GlUtil.checkGlError();
            return;
        L29:
            GLES20.glUniform1fv(this.location, 1, this.floatValue, 0);
            GlUtil.checkGlError();
            return;
        L30:
            GLES20.glUniform1iv(this.location, 1, this.intValue, 0);
            GlUtil.checkGlError();
            return;
        L5:
            throw new IllegalStateException("Unexpected uniform type: " + this.type);
        L7:
            if (this.texIdValue == 0) goto L20;
            GLES20.glActiveTexture(this.texUnitIndex + 33984);
            GlUtil.checkGlError();
            if (this.type != 35678) goto L11;
            int r0 = 3553;
        L12:
            int r2 = this.texIdValue;
            if (this.type == 35678) goto L17;
            if (r5 == false) goto L17;
            int r1 = 9728;
        L18:
            GlUtil.bindTexture(r0, r2, r1);
            GLES20.glUniform1i(this.location, this.texUnitIndex);
            GlUtil.checkGlError();
            return;
        L17:
            r1 = 9729;
            goto L18
        L11:
            r0 = 36197;
            goto L12
        L20:
            throw new IllegalStateException("No call to setSamplerTexId() before bind.");
        }

        public void setFloat(float r3) {
            this.floatValue[0] = r3;
        }

        public void setFloats(float[] r4) {
            System.arraycopy(r4, 0, this.floatValue, 0, r4.length);
        }

        public void setInt(int r3) {
            this.intValue[0] = r3;
        }

        public void setInts(int[] r4) {
            System.arraycopy(r4, 0, this.intValue, 0, r4.length);
        }

        public void setSamplerTexId(int r1, int r2) {
            this.texIdValue = r1;
            this.texUnitIndex = r2;
        }
    }

    public GlProgram(Context r3, String r4, String r5) throws IOException, GlUtil.GlException {
        this(Util.loadAsset(r3, r4), Util.loadAsset(r3, r5));
    }

    public GlProgram(String r9, String r10) throws GlUtil.GlException {
        this.programId = GLES20.glCreateProgram();
        GlUtil.checkGlError();
        addShader(this.programId, 35633, r9);
        addShader(this.programId, 35632, r10);
        GLES20.glLinkProgram(this.programId);
        int[] r1 = {0};
        GLES20.glGetProgramiv(this.programId, 35714, r1, 0);
        if (r1[0] != 1) goto L5;
        boolean r2 = true;
    L6:
        GlUtil.checkGlException(r2, "Unable to link shader program: \n" + GLES20.glGetProgramInfoLog(this.programId));
        GLES20.glUseProgram(this.programId);
        this.attributeByName = new HashMap();
        int[] r3 = new int[1];
        GLES20.glGetProgramiv(this.programId, 35721, r3, 0);
        this.attributes = new Attribute[r3[0]];
        int r4 = 0;
    L8:
        if (r4 >= r3[0]) goto L10;
        Attribute r5 = Attribute.create(this.programId, r4);
        this.attributes[r4] = r5;
        this.attributeByName.put(r5.name, r5);
        r4 = r4 + 1;
        goto L8
    L10:
        this.uniformByName = new HashMap();
        int[] r6 = new int[1];
        GLES20.glGetProgramiv(this.programId, 35718, r6, 0);
        this.uniforms = new Uniform[r6[0]];
        int r7 = 0;
    L12:
        if (r7 >= r6[0]) goto L14;
        Uniform r8 = Uniform.create(this.programId, r7);
        this.uniforms[r7] = r8;
        this.uniformByName.put(r8.name, r8);
        r7 = r7 + 1;
        goto L12
    L14:
        GlUtil.checkGlError();
        return;
    L5:
        r2 = false;
        goto L6
    }

    static /* synthetic */ int access$000(byte[] r1) {
        return getCStringLength(r1);
    }

    static /* synthetic */ int access$100(int r1, String r2) {
        return getAttributeLocation(r1, r2);
    }

    static /* synthetic */ int access$200(int r1, String r2) {
        return getUniformLocation(r1, r2);
    }

    private static void addShader(int r5, int r6, String r7) throws GlUtil.GlException {
        int r0 = GLES20.glCreateShader(r6);
        GLES20.glShaderSource(r0, r7);
        GLES20.glCompileShader(r0);
        boolean r1 = false;
        int[] r2 = {0};
        GLES20.glGetShaderiv(r0, 35713, r2, 0);
        if (r2[0] != 1) goto L5;
        r1 = true;
    L5:
        GlUtil.checkGlException(r1, GLES20.glGetShaderInfoLog(r0) + ", source: \n" + r7);
        GLES20.glAttachShader(r5, r0);
        GLES20.glDeleteShader(r0);
        GlUtil.checkGlError();
    }

    private static int getAttributeLocation(int r1, String r2) {
        return GLES20.glGetAttribLocation(r1, r2);
    }

    private int getAttributeLocation(String r2) {
        return getAttributeLocation(this.programId, r2);
    }

    private static int getCStringLength(byte[] r2) {
        int r0 = 0;
    L4:
        if (r0 >= r2.length) goto L10;
        if (r2[r0] == 0) goto L7;
        r0 = r0 + 1;
        goto L4
    L7:
        return r0;
    L10:
        return r2.length;
    }

    private static int getUniformLocation(int r1, String r2) {
        return GLES20.glGetUniformLocation(r1, r2);
    }

    public void bindAttributesAndUniforms() throws GlUtil.GlException {
        Attribute[] r0 = this.attributes;
        int r1 = r0.length;
        int r2 = 0;
        int r3 = 0;
    L3:
        if (r3 >= r1) goto L5;
        r0[r3].bind();
        r3 = r3 + 1;
        goto L3
    L5:
        Uniform[] r4 = this.uniforms;
        int r5 = r4.length;
    L6:
        if (r2 >= r5) goto L8;
        r4[r2].bind(this.externalTexturesRequireNearestSampling);
        r2 = r2 + 1;
        goto L6
    }

    public void delete() throws GlUtil.GlException {
        GLES20.glDeleteProgram(this.programId);
        GlUtil.checkGlError();
    }

    public int getAttributeArrayLocationAndEnable(String r2) throws GlUtil.GlException {
        int r0 = getAttributeLocation(r2);
        GLES20.glEnableVertexAttribArray(r0);
        GlUtil.checkGlError();
        return r0;
    }

    public int getUniformLocation(String r2) {
        return getUniformLocation(this.programId, r2);
    }

    public void setBufferAttribute(String r2, float[] r3, int r4) {
        ((Attribute) Assertions.checkNotNull(this.attributeByName.get(r2))).setBuffer(r3, r4);
    }

    public void setExternalTexturesRequireNearestSampling(boolean r1) {
        this.externalTexturesRequireNearestSampling = r1;
    }

    public void setFloatUniform(String r2, float r3) {
        ((Uniform) Assertions.checkNotNull(this.uniformByName.get(r2))).setFloat(r3);
    }

    public void setFloatsUniform(String r2, float[] r3) {
        ((Uniform) Assertions.checkNotNull(this.uniformByName.get(r2))).setFloats(r3);
    }

    public void setFloatsUniformIfPresent(String r2, float[] r3) {
        Uniform r0 = this.uniformByName.get(r2);
        if (r0 != null) goto L5;
        return;
    L5:
        r0.setFloats(r3);
    }

    public void setIntUniform(String r2, int r3) {
        ((Uniform) Assertions.checkNotNull(this.uniformByName.get(r2))).setInt(r3);
    }

    public void setIntsUniform(String r2, int[] r3) {
        ((Uniform) Assertions.checkNotNull(this.uniformByName.get(r2))).setInts(r3);
    }

    public void setSamplerTexIdUniform(String r2, int r3, int r4) {
        ((Uniform) Assertions.checkNotNull(this.uniformByName.get(r2))).setSamplerTexId(r3, r4);
    }

    public void use() throws GlUtil.GlException {
        GLES20.glUseProgram(this.programId);
        GlUtil.checkGlError();
    }
}
