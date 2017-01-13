package nl.gridshore.elastic.plugin.basic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

/**
 * Created by jettrocoenradie on 13/01/2017.
 */
public class JettroOnlyTokenFilter extends FilteringTokenFilter {
    protected final Logger logger = LogManager.getLogger(getClass());

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    /**
     * Create a new {@link FilteringTokenFilter}.
     *
     * @param in the {@link TokenStream} to consume
     */
    public JettroOnlyTokenFilter(TokenStream in) {
        super(in);
    }

    @Override
    protected boolean accept() throws IOException {
        logger.info("Term to check for 'jettro' {}", termAtt.toString());

        return termAtt.toString().equals("jettro");
    }
}
