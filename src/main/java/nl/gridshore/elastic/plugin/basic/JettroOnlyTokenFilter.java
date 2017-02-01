package nl.gridshore.elastic.plugin.basic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.lucene.analysis.FilteringTokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import java.io.IOException;

/**
 * Filters that removes all token accept for <em>jettro</em>. The filter is even case sensitive. So using
 * it in combination with a lowercase filter might be good.
 */
public class JettroOnlyTokenFilter extends FilteringTokenFilter {
    private final Logger LOGGER = LogManager.getLogger(getClass());

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);

    /**
     * Create a new {@link JettroOnlyTokenFilter}.
     *
     * @param in the {@link TokenStream} to consume
     */
    public JettroOnlyTokenFilter(TokenStream in) {
        super(in);
    }

    @Override
    protected boolean accept() throws IOException {
        LOGGER.info("Term to check for 'jettro' {}", termAtt.toString());

        return termAtt.toString().equals("jettro");
    }
}
