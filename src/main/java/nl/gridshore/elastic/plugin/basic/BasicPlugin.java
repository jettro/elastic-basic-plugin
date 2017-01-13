package nl.gridshore.elastic.plugin.basic;

import org.elasticsearch.index.analysis.ArabicStemTokenFilterFactory;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class BasicPlugin extends Plugin implements ActionPlugin, AnalysisPlugin {
    @Override
    public List<Class<? extends RestHandler>> getRestHandlers() {
        return Collections.singletonList(JettroRestAction.class);
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        return Collections.singletonMap("jettro", JettroTokenFilterFactory::new);
    }
}
