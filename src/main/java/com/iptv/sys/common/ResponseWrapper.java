/*     */
package com.iptv.sys.common;
/*     */ 
/*     */

import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletOutputStream;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpServletResponseWrapper;

/*     */
/*     */ public class ResponseWrapper
/*     */ extends HttpServletResponseWrapper
/*     */ {
    /*  16 */   private ByteArrayOutputStream buffer = null;
    /*  17 */   private ServletOutputStream out = null;
    /*  18 */   private PrintWriter writer = null;
    /*  19 */   private Map<String, String> headers = new HashMap();

    /*     */
/*     */
    public ResponseWrapper(HttpServletResponse response) throws IOException {
/*  22 */
        super(response);
/*     */     
/*  24 */
        this.buffer = new ByteArrayOutputStream();
/*  25 */
        this.out = new WapperedOutputStream(this.buffer);
/*  26 */
        this.writer = new PrintWriter(new OutputStreamWriter(this.buffer, getCharacterEncoding()));
/*     */
    }

    /*     */
/*     */
    public PrintWriter getWriter() throws IOException
/*     */ {
/*  31 */
        return this.writer;
/*     */
    }

    /*     */
/*     */
    public ServletOutputStream getOutputStream() throws IOException
/*     */ {
/*  36 */
        return this.out;
/*     */
    }

    /*     */
/*     */
    public void flushBuffer() throws IOException
/*     */ {
/*  41 */
        if (this.out != null) {
/*  42 */
            this.out.flush();
/*     */
        }
/*  44 */
        if (this.writer != null) {
/*  45 */
            this.writer.flush();
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public void reset()
/*     */ {
/*  51 */
        this.buffer.reset();
/*     */
    }

    /*     */
/*     */
    public Map getContent() {
/*     */
        try {
/*  56 */
            Map res = new HashMap();
/*  57 */
            res.put("ByteData", this.buffer.toByteArray());
/*  58 */
            res.put("StringData", new String(this.buffer.toByteArray(), "UTF-8"));
/*     */       
/*  60 */
            return res;
/*     */
        } catch (Exception e) {
/*  62 */
            e.printStackTrace();
/*     */
        }
/*     */     
/*  65 */
        return null;
/*     */
    }

    /*     */
/*     */   private class WapperedOutputStream extends ServletOutputStream {
        /*  69 */     private ByteArrayOutputStream bos = null;

        /*     */
/*     */
        public WapperedOutputStream(ByteArrayOutputStream stream) throws IOException {
/*  72 */
            this.bos = stream;
/*     */
        }

        /*     */
/*     */
        public void write(int b) throws IOException
/*     */ {
/*  77 */
            this.bos.write(b);
/*     */
        }

        /*     */
/*     */
        public void write(byte[] b) throws IOException
/*     */ {
/*  82 */
            super.write(b);
/*     */
        }

        /*     */
/*     */
        public void write(byte[] b, int off, int len) throws IOException
/*     */ {
/*  87 */
            super.write(b, off, len);
/*     */
        }
/*     */
    }

    /*     */
/*     */
    public String getHeader(String name)
/*     */ {
/*  93 */
        return (String) this.headers.get(name);
/*     */
    }

    /*     */
/*     */
    public void setHeader(String name, String value)
/*     */ {
/*  98 */
        this.headers.put(name, value);
/*     */
    }

    /*     */
/*     */
    public Map<String, String> getHeaders() {
/* 102 */
        return this.headers;
/*     */
    }
/*     */
}


/* Location:              C:\Users\宋羽翔\Desktop\com.iptv.core.jar!\com\iptv\sys\common\ResponseWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */