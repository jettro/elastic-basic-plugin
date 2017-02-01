package nl.gridshore.elastic.plugin.basic;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.BaseRestHandler;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestController;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.rest.action.RestBuilderListener;

import java.io.IOException;

import static org.elasticsearch.rest.RestRequest.Method.GET;

public class JettroRestAction extends BaseRestHandler {
    private final static Logger LOGGER = LogManager.getLogger(JettroRestAction.class);
    private static final String ACTION_EXISTS = "exists";
    private static final String NAME_INDEX_TO_CHECK = "jettro";

    @Inject
    public JettroRestAction(Settings settings, RestController controller) {
        super(settings);
        controller.registerHandler(GET, "_jettro/{action}", this);
        controller.registerHandler(GET, "_jettro", this);
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        LOGGER.info("Handle _jettro endpoint");

        String action = request.param("action");
        if (action != null && ACTION_EXISTS.equals(action)) {
            LOGGER.info("Handle index exists request");
            return createExistsResponse(request, client);
        } else {
            return createMessageResponse(request);
        }
    }

    private RestChannelConsumer createExistsResponse(final RestRequest request, NodeClient client) {
        return channel -> {
            client.admin().indices().prepareGetIndex()
                    .execute(new RestBuilderListener<GetIndexResponse>(channel) {
                        @Override
                        public RestResponse buildResponse(GetIndexResponse response, XContentBuilder builder) throws Exception {
                            String[] indices = response.getIndices();
                            if (indices == null || indices.length == 0) {
                                LOGGER.info("No indices are found");
                            }
                            boolean exists = false;
                            for (String index : indices) {
                                LOGGER.info("Index to check: {}", index);
                                if (index.startsWith(NAME_INDEX_TO_CHECK)) {
                                    exists = true;
                                    break;
                                }
                            }
                            Exists jettroExists = new Exists(exists);
                            builder.startObject();
                            jettroExists.toXContent(builder, request);
                            builder.endObject();
                            return new BytesRestResponse(RestStatus.OK,builder);
                        }
                    });
        };
    }

    private RestChannelConsumer createMessageResponse(RestRequest request) {
        return channel -> {
            Message message = new Message();
            XContentBuilder builder = channel.newBuilder();
            builder.startObject();
            message.toXContent(builder, request);
            builder.endObject();
            channel.sendResponse(new BytesRestResponse(RestStatus.OK, builder));
        };
    }

    /**
     * Class used to return a json object containing a message
     */
    class Message implements ToXContent {
        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            return builder.field("message", "Plugin to support the Jettro index.");
        }
    }

    /**
     * Class used to return a json object containing the exists property
     */
    class Exists implements ToXContent {
        private boolean exists;

        Exists(boolean exists) {
            this.exists = exists;
        }

        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            return builder.field("exists", exists);
        }
    }
}
