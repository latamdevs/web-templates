package cl.variacode.webtemplates;

import static com.google.common.base.MoreObjects.firstNonNull;
import static spark.Spark.halt;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;

import com.google.common.net.MediaType;
import spark.Filter;
import spark.Request;
import spark.Response;

public final class SparkWebJars implements Filter {

    private static final Logger logger = Logger.getLogger(SparkWebJars.class.getName());

    private interface LazyHolder {
        SparkWebJars INSTANCE = new SparkWebJars();
    }

    public static SparkWebJars getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override
    public void handle(final Request request, final Response response) throws Exception {
        final String contextPath = firstNonNull(request.contextPath(), "");
        final String webjarsResourceURI = "/META-INF/resources" + request.uri().replaceFirst(contextPath, "");

        logger.log(Level.FINE, "Webjars resource requested: {0}", webjarsResourceURI);

        final InputStream inputStream = this.getClass().getResourceAsStream(webjarsResourceURI);
        if (inputStream != null) {
            try {
                final String filename = getFileName(webjarsResourceURI);
                final String contentType = getMimeType(filename);

                final HttpServletResponse rawResponse = response.raw();
                rawResponse.setContentType(contentType);
                copy(inputStream, rawResponse.getOutputStream());

                halt();
            } finally {
                inputStream.close();
            }
        }
    }

    private String getMimeType(final String filename) {
        final String fileExtension = getFileExtension(filename);

        switch (fileExtension) {
            case "js": return MediaType.JSON_UTF_8.toString();
            case "css": return MediaType.CSS_UTF_8.toString();
        }

        return "application/octet-stream";
    }

    /**
     *
     * @param webjarsResourceURI
     * @return
     */
    private String getFileExtension(String filename) {
        final int index = filename.lastIndexOf('.');
        return filename.substring(index + 1);
    }

    /**
     *
     * @param webjarsResourceURI
     * @return
     */
    private String getFileName(String webjarsResourceURI) {
        String[] tokens = webjarsResourceURI.split("/");
        return tokens[tokens.length - 1];
    }


    /**
     * The default buffer size ({@value}) to use
     */
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    private static final int EOF = -1;

    // copy from InputStream
    //-----------------------------------------------------------------------
    /**
     * Copy bytes from an <code>InputStream</code> to an
     * <code>OutputStream</code>.
     * <p>
     * This method buffers the input internally, so there is no need to use a
     * <code>BufferedInputStream</code>.
     * <p>
     * Large streams (over 2GB) will return a bytes copied value of
     * <code>-1</code> after the copy has completed since the correct
     * number of bytes cannot be returned as an int. For large streams
     * use the <code>copyLarge(InputStream, OutputStream)</code> method.
     *
     * @param input  the <code>InputStream</code> to read from
     * @param output  the <code>OutputStream</code> to write to
     * @return the number of bytes copied, or -1 if &gt; Integer.MAX_VALUE
     * @throws NullPointerException if the input or output is null
     * @throws IOException if an I/O error occurs
     * @since 1.1
     */
    private static int copy(InputStream input, OutputStream output) throws IOException {
        long count = 0;
        int n = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        if (count > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) count;
    }

}
