package pt.megsi.fwk.filters;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class CachedBodyHttpServletResponse extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream cachedContent = new ByteArrayOutputStream();
    private ServletOutputStream outputStream;
    private PrintWriter writer;
    private int httpStatus = SC_OK;

    public CachedBodyHttpServletResponse(HttpServletResponse response) {
        super(response);
    }

    @Override
    public void setStatus(int sc) {
        super.setStatus(sc);
        this.httpStatus = sc;
    }

    @Override
    public void sendError(int sc) throws IOException {
        super.sendError(sc);
        this.httpStatus = sc;
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
        super.sendError(sc, msg);
        this.httpStatus = sc;
    }

    public int getStatusCode() {
        return httpStatus;
    }

    @Override
    public ServletOutputStream getOutputStream() {
        if (this.outputStream == null) {
            this.outputStream = new ServletOutputStream() {
                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setWriteListener(WriteListener writeListener) {
                }

                @Override
                public void write(int b) throws IOException {
                    cachedContent.write(b);
                }
            };
        }
        return this.outputStream;
    }

    @Override
    public PrintWriter getWriter() {
        if (this.writer == null) {
            this.writer = new PrintWriter(new OutputStreamWriter(cachedContent));
        }
        return this.writer;
    }

    public byte[] getBody() {
        return cachedContent.toByteArray();
    }

    public String getBodyAsString() {
        return new String(getBody(), StandardCharsets.UTF_8);
    }
}