package edu.tamu.scholars.discovery.factory.index.solr;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.BaseHttpSolrClient.RemoteSolrException;
import org.apache.solr.client.solrj.impl.Http2SolrClient;
import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.apache.solr.common.SolrInputDocument;

import edu.tamu.scholars.discovery.factory.index.CopyField;
import edu.tamu.scholars.discovery.factory.index.Field;
import edu.tamu.scholars.discovery.factory.index.Index;

@Slf4j
public class SolrIndex implements Index<SolrInputDocument> {

    private final Http2SolrClient solrClient;

    private SolrIndex(String host, String collection) {
        this.solrClient = new Http2SolrClient.Builder(host)
            .withConnectionTimeout(1, TimeUnit.MINUTES)
            .withIdleTimeout(5, TimeUnit.MINUTES)
            .withMaxConnectionsPerHost(10)
            .withRequestTimeout(5, TimeUnit.MINUTES)
            .withDefaultCollection(collection)
            .build();
    }

    @Override
    public List<Field> getFields() {
        SchemaRequest.Fields request = new SchemaRequest.Fields();

        try {
            SchemaResponse.FieldsResponse response = request.process(this.solrClient);

            return response.getFields()
                .stream()
                .map(Field::of)
                .toList();

        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error requesting fields from Solr", e);
        }

        return List.of();
    }

    @Override
    public List<CopyField> getCopyFields() {
        SchemaRequest.CopyFields request = new SchemaRequest.CopyFields();

        try {
            SchemaResponse.CopyFieldsResponse response = request.process(this.solrClient);

            return response.getCopyFields()
                .stream()
                .map(CopyField::of)
                .toList();

        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error requesting copy fields from Solr", e);
        }

        return List.of();
    }

    @Override
    public boolean addFields(List<Field> fields) {
        List<SchemaRequest.Update> addFieldRequests = fields.stream()
            .map(Field::toMap)
            .map(attributes -> (SchemaRequest.Update) new SchemaRequest.AddField(attributes))
            .toList();

        return updateSchema(addFieldRequests);
    }

    @Override
    public boolean addCopyFields(List<CopyField> copyFields) {
        List<SchemaRequest.Update> addCopyFieldRequests = copyFields.stream()
            .map(field -> (SchemaRequest.Update) new SchemaRequest.AddCopyField(field.getSource(), field.getDest()))
            .toList();

        return updateSchema(addCopyFieldRequests);
    }

    @Override
    public void update(Collection<SolrInputDocument> documents) {
        try {
            this.solrClient.add(documents);
            this.solrClient.commit();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.warn("Error updating Solr documents", e);
            log.info("Attempting batch documents individually");
            documents.forEach(this::update);
        }
    }

    @Override
    public void update(SolrInputDocument document) {
        try {
            this.solrClient.add(document);
            this.solrClient.commit();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error updating Solr document", e);
        }
    }

    @Override
    public void optimize() {
        try {
            this.solrClient.optimize();
        } catch (RemoteSolrException | SolrServerException | IOException e) {
            log.error("Error optimizing Solr collection", e);
        }
    }

    @Override
    public void close() {
        this.solrClient.close();
    }

    private boolean updateSchema(List<SchemaRequest.Update> updates) {
        SchemaRequest.Update request = new SchemaRequest.MultiUpdate(updates);

        try {
            SchemaResponse.UpdateResponse response = request.process(this.solrClient);

            return response.getStatus() == 0;
        } catch (SolrServerException | IOException e) {
            log.error("Error updating Solr schema", e);
        }

        return false;
    }

    public static SolrIndex of(Map<String, String> attributes) {
        String host = attributes.getOrDefault("host", "http://localhost:8983/solr");
        String collection = attributes.getOrDefault("collection", "scholars-discovery");

        return new SolrIndex(host, collection);
    }

}
