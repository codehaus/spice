package testdata.poms;

import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.IOException;

public class PomLoader {

    public static final InputSource loadPom(final String pomName) throws IOException {
        final InputStream in = PomLoader.class.getResourceAsStream(pomName);
        return new InputSource(in);
    }
}
