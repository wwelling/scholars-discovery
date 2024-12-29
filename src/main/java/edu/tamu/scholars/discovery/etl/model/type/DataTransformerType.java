package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.component.Mapper;
import edu.tamu.scholars.discovery.component.json.JsonMapper;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.transform.DataTransformer;
import edu.tamu.scholars.discovery.etl.transform.FlatMapToNestedJsonNodeTransformer;

public enum DataTransformerType implements DataProcessorType<DataTransformer<?, ?>, Mapper> {

    FLAT_MAP_TO_NESTED_JSON_NODE(JsonMapper.class) {
        @Override
        public DataTransformer<?, ?> getDataProcessor(Data data, Mapper service) {
            return new FlatMapToNestedJsonNodeTransformer(data, service);
        }
    };

    private final Class<? extends Mapper> serviceType;

    private final String[] requiredAttributes;

    DataTransformerType(Class<? extends Mapper> serviceType, String... requiredAttributes) {
        this.serviceType = serviceType;
        this.requiredAttributes = requiredAttributes;
    }

    @Override
    public Class<? extends Mapper> getServiceType() {
        return serviceType;
    }

    @Override
    public String[] getRequiredAttributes() {
        return requiredAttributes;
    }

}
