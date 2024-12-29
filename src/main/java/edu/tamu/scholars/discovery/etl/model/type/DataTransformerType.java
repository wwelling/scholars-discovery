package edu.tamu.scholars.discovery.etl.model.type;

import edu.tamu.scholars.discovery.component.Mapper;
import edu.tamu.scholars.discovery.etl.model.Data;
import edu.tamu.scholars.discovery.etl.transform.DataTransformer;
import edu.tamu.scholars.discovery.etl.transform.FlatMapToNestedJsonNodeTransformer;

public enum DataTransformerType implements DataProcessorType<DataTransformer<?, ?>, Mapper> {

    // TODO: allow overriding required attributes from properties

    FLAT_MAP_TO_NESTED_JSON_NODE() {
        @Override
        public DataTransformer<?, ?> getDataProcessor(Data data, Mapper service) {
            return new FlatMapToNestedJsonNodeTransformer(data, service);
        }
    };

    private final String[] requiredAttributes;

    DataTransformerType(String... requiredAttributes) {
        this.requiredAttributes = requiredAttributes;
    }

    @Override
    public String[] getRequiredAttributes() {
        return requiredAttributes;
    }

}
