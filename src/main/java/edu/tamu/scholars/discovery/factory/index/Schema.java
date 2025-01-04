package edu.tamu.scholars.discovery.factory.index;

import java.util.List;

import edu.tamu.scholars.discovery.factory.index.dto.CopyField;
import edu.tamu.scholars.discovery.factory.index.dto.Field;

public interface Schema {

    List<Field> getFields();

    List<CopyField> getCopyFields();

    boolean addFields(List<Field> fields);

    boolean addCopyFields(List<CopyField> fields);

}
