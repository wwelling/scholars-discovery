package edu.tamu.scholars.discovery.etl.model.repo;

import java.util.Optional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import edu.tamu.scholars.discovery.etl.model.DataFieldDescriptor;
import edu.tamu.scholars.discovery.model.repo.NamedRepo;


@Repository
public interface DataFieldDescriptorRepo extends NamedRepo<DataFieldDescriptor> {

    public <S extends DataFieldDescriptor> Optional<S> findOne(Specification<S> example);

    @Override
    @EntityGraph(
        attributePaths = {
            "destination.copyTo",
            "source.cacheableSources",
            "nestedDescriptors"
        }
    )
    public Optional<DataFieldDescriptor> findById(Long id);
}
