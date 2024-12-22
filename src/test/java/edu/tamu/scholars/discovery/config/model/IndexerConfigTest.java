package edu.tamu.scholars.discovery.config.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tamu.scholars.discovery.discovery.component.solr.SolrIndexer;
import edu.tamu.scholars.discovery.discovery.model.AbstractIndexDocument;
import edu.tamu.scholars.discovery.discovery.model.Collection;
import edu.tamu.scholars.discovery.discovery.model.Concept;
import edu.tamu.scholars.discovery.discovery.model.Document;
import edu.tamu.scholars.discovery.discovery.model.Organization;
import edu.tamu.scholars.discovery.discovery.model.Person;
import edu.tamu.scholars.discovery.discovery.model.Process;
import edu.tamu.scholars.discovery.discovery.model.Relationship;

@ExtendWith(MockitoExtension.class)
public class IndexerConfigTest {

    @Test
    public void testDefaultConstructor() {
        IndexerConfig indexerConfig = new IndexerConfig();
        assertNotNull(indexerConfig);
        assertEquals(SolrIndexer.class, indexerConfig.getType());
        assertNotNull(indexerConfig.getDocumentTypes());
        assertEquals(0, indexerConfig.getDocumentTypes().size());
    }

    @Test
    public void testTypeGetterSetter() {
        IndexerConfig indexerConfig = new IndexerConfig();
        indexerConfig.setType(SolrIndexer.class);
        assertEquals(SolrIndexer.class, indexerConfig.getType());
    }

    @Test
    public void testDocumentTypesGetterSetter() {
        IndexerConfig indexerConfig = new IndexerConfig();
        List<Class<? extends AbstractIndexDocument>> documentTypes = new ArrayList<Class<? extends AbstractIndexDocument>>();
        documentTypes.add(Collection.class);
        documentTypes.add(Concept.class);
        documentTypes.add(Document.class);
        documentTypes.add(Organization.class);
        documentTypes.add(Person.class);
        documentTypes.add(Process.class);
        documentTypes.add(Relationship.class);
        indexerConfig.setDocumentTypes(documentTypes);
        assertEquals(7, indexerConfig.getDocumentTypes().size());
    }

}