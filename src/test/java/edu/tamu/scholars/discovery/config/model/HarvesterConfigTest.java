package edu.tamu.scholars.discovery.config.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.tamu.scholars.discovery.index.component.jena.TriplestoreHarvester;
import edu.tamu.scholars.discovery.index.model.AbstractIndexDocument;
import edu.tamu.scholars.discovery.index.model.Collection;
import edu.tamu.scholars.discovery.index.model.Concept;
import edu.tamu.scholars.discovery.index.model.Document;
import edu.tamu.scholars.discovery.index.model.Organization;
import edu.tamu.scholars.discovery.index.model.Person;
import edu.tamu.scholars.discovery.index.model.Process;
import edu.tamu.scholars.discovery.index.model.Relationship;

@ExtendWith(MockitoExtension.class)
public class HarvesterConfigTest {

    @Test
    public void testDefaultConstructor() {
        HarvesterConfig harvesterConfig = new HarvesterConfig();
        assertNotNull(harvesterConfig);
        assertEquals(TriplestoreHarvester.class, harvesterConfig.getType());
        assertNotNull(harvesterConfig.getDocumentTypes());
        assertEquals(0, harvesterConfig.getDocumentTypes().size());
    }

    @Test
    public void testTypeGetterSetter() {
        HarvesterConfig harvesterConfig = new HarvesterConfig();
        harvesterConfig.setType(TriplestoreHarvester.class);
        assertEquals(TriplestoreHarvester.class, harvesterConfig.getType());
    }

    @Test
    public void testDocumentTypesGetterSetter() {
        HarvesterConfig harvesterConfig = new HarvesterConfig();
        List<Class<? extends AbstractIndexDocument>> documentTypes = new ArrayList<Class<? extends AbstractIndexDocument>>();
        documentTypes.add(Collection.class);
        documentTypes.add(Concept.class);
        documentTypes.add(Document.class);
        documentTypes.add(Organization.class);
        documentTypes.add(Person.class);
        documentTypes.add(Process.class);
        documentTypes.add(Relationship.class);
        harvesterConfig.setDocumentTypes(documentTypes);
        assertEquals(7, harvesterConfig.getDocumentTypes().size());
    }

}
