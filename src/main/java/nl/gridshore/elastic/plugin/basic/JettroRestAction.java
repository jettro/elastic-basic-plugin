package nl.gridshore.elastic.plugin.basic;

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

    @Inject
    public JettroRestAction(Settings settings, RestController controller) {
        super(settings);
        controller.registerHandler(GET, "_jettro/{action}", this);
        controller.registerHandler(GET, "_jettro", this);

    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        String action = request.param("action");
        if (action != null && "exists".equals(action)) {
            return channel -> {
                client.admin().indices().prepareGetIndex()

                        .execute(new RestBuilderListener<GetIndexResponse>(channel) {
                            @Override
                            public RestResponse buildResponse(GetIndexResponse getIndexResponse, XContentBuilder builder) throws Exception {
                                boolean exists = false;
                                for (String index : getIndexResponse.getIndices()) {
                                    if (index.startsWith("jettro")) {
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
        } else {
            return channel -> {
                Message message = new Message();
                XContentBuilder builder = channel.newBuilder();
                builder.startObject();
                message.toXContent(builder, request);
                builder.endObject();
                channel.sendResponse(new BytesRestResponse(RestStatus.OK, builder));
            };
        }
    }


    class Message implements ToXContent {
        @Override
        public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
            return builder.field("message", "Plugin to support the Jettro index.");
        }
    }

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
