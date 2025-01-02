package edu.tamu.scholars.discovery.view.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import edu.tamu.scholars.discovery.model.OpKey;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Embeddable
@JsonInclude(Include.NON_NULL)
public class Grouping implements Serializable {

    private static final long serialVersionUID = -987654321098765432L;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpKey opKey;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "index_options")
    private List<String> options;

    public Grouping() {
        super();
        this.opKey = OpKey.STARTS_WITH;
        this.options = new ArrayList<>();
    }

}
