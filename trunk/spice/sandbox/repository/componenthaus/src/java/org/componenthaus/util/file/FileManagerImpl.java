package org.componenthaus.util.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;

public class FileManagerImpl implements FileManager {
    private static final int chunk = 1024;

    public void copy(File from, OutputStream to) throws IOException {
        assert from != null;
        assert to != null;
        final BufferedInputStream in = new BufferedInputStream(new FileInputStream(from));
        copy(in, to);
        in.close();
    }

    private void copy(InputStream from, OutputStream to) throws IOException {
        final byte[] buff = new byte[chunk];
        int bytesRead = -1;
        while (-1 != (bytesRead = from.read(buff, 0, buff.length))) {
            to.write(buff, 0, bytesRead);
        }
    }

    private void copy(File from, Writer to) throws IOException {
        assert from != null;
        assert to != null;
        final BufferedInputStream in = new BufferedInputStream(new FileInputStream(from));
        final byte[] buff = new byte[chunk];
        int bytesRead = -1;
        while (-1 != (bytesRead = in.read(buff, 0, buff.length))) {
            to.write(new String(buff,0,bytesRead));
        }
        in.close();
    }

    public void copy(InputStream from, File to) throws IOException {
        FileOutputStream os = new FileOutputStream(to);
        copy(from,os);
        os.close();
    }
}
