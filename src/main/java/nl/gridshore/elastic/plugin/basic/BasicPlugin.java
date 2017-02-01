package nl.gridshore.elastic.plugin.basic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.index.analysis.TokenFilterFactory;
import org.elasticsearch.indices.analysis.AnalysisModule;
import org.elasticsearch.plugins.ActionPlugin;
import org.elasticsearch.plugins.AnalysisPlugin;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.rest.RestHandler;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Main class to initialize the plugin. For additional functionality you usually implement one of the more
 * specific interfaces. The filter can be used with the name <strong>jettro</strong>
 *
 * ActionPlugin - To initialize Rest based extensions
 * AnalysisPlugin - To initialize analyzer extensions, among them the Filter extension.
 */
public class BasicPlugin extends Plugin implements ActionPlugin, AnalysisPlugin {
    private final static Logger LOGGER = LogManager.getLogger(BasicPlugin.class);
    private static final String FILTER_NAME = "jettro";

    public BasicPlugin() {
        super();
        LOGGER.warn("Create the Basic Plugin and installed it into elasticsearch");
    }

    @Override
    public List<Class<? extends RestHandler>> getRestHandlers() {
        return Collections.singletonList(JettroRestAction.class);
    }

    @Override
    public Map<String, AnalysisModule.AnalysisProvider<TokenFilterFactory>> getTokenFilters() {
        return Collections.singletonMap(FILTER_NAME, JettroTokenFilterFactory::new);
    }
}
