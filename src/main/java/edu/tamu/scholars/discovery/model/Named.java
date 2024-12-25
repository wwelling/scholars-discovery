package edu.tamu.scholars.discovery.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(onlyExplicitlyIncluded = true)
@MappedSuperclass
public abstract class Named implements Serializable {

    @Id
    @JsonInclude(Include.NON_EMPTY)
    @GeneratedValue(strategy = IDENTITY)
    @ToString.Include
    private Long id;

    @Version
    @Column(name = "version")
    @ToString.Include
    private Long version;

    @Size(min = 2, max = 64, message = "${Named.nameSize}")
    @Column(nullable = false, unique = true)
    @ToString.Include
    protected String name;

    @NotNull(message = "${Named.nameRequired}")
    public String getName() {
        return name;
    }

}
