package edu.tamu.scholars.discovery.defaults;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;

import edu.tamu.scholars.discovery.theme.model.Theme;
import edu.tamu.scholars.discovery.theme.model.repo.ThemeRepo;


@Service
public class ThemeDefaults extends AbstractDefaults<Theme, ThemeRepo> {

    public ThemeDefaults() {
        super();
    }

    @Override
    public String path() {
        return "classpath:defaults/themes.yml";
    }

    @Override
    public List<Theme> read(InputStream is) throws IOException {
        return mapper.readValue(is, new TypeReference<List<Theme>>() {});
    }

}
