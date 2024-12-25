package edu.tamu.scholars.discovery.view.model;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import edu.tamu.scholars.discovery.model.OpKey;

/**
 * {@link DirectoryView} embedded class `Index` to describe collection result
 * navigation.
 */
@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class Index implements Serializable {

    private static final long serialVersionUID = -987654321098765432L;

    @Column(nullable = false)
    private String field;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OpKey opKey = OpKey.STARTS_WITH;

    @ElementCollection
    private List<String> options;

}
