package edu.tamu.scholars.discovery.controller;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.rdf.model.Model;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.tamu.scholars.discovery.triplestore.TdbTriplestore;

@Slf4j
@RestController
@RequestMapping("/sparql")
public class SparqlController {

    @PostMapping(value = "/construct", consumes = MediaType.TEXT_PLAIN_VALUE)
    public void construct(
            @RequestParam(defaultValue = "RDF_XML") RdfFormat format,
            @RequestBody String query,
            HttpServletResponse response) throws IOException {
        TdbTriplestore triplestore = TdbTriplestore.of("triplestore");
        try (QueryExecution qe = triplestore.createQueryExecution(query)) {
            Model model = qe.execConstruct();
            model.write(response.getWriter(), format.getFormat());
            response.setContentType(format.getContentType());
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
            triplestore.destroy();
        }
    }

    @Getter
    public enum RdfFormat {
        RDF_XML("RDF/XML", "application/rdf+xml"),
        RDF_XML_ABBREV("RDF/XML-ABBREV", "application/rdf+xml"),
        N_TRIPLE("N-TRIPLE", "application/n-triples"),
        TURTLE("TURTLE", "text/turtle"),
        TTL("TTL", "text/turtle"),
        N3("N3", "text/n3");

        private final String format;
        private final String contentType;

        RdfFormat(String format, String contentType) {
            this.format = format;
            this.contentType = contentType;
        }
    }

}
