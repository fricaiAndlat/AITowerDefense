package de.diavololoop.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Chloroplast on 30.05.2017.
 */
public class IOUtil {

    public static String getStringFromStream(InputStream stream) throws IOException {

        ByteArrayOutputStream result = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len;

        while(-1 != (len = stream.read(buffer))){
            result.write(buffer, 0, len);
        }

        stream.close();

        return new String(result.toByteArray(), StandardCharsets.UTF_8);

    }

    public static String getStringFromStreamNE(InputStream stream){
        try {
            return IOUtil.getStringFromStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        //for the compiler
        return null;
    }

}
