package io.github.mat3e.controller;


import io.github.mat3e.TaskConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InfoController {

    private final DataSourceProperties dataSource;
    private final TaskConfiguration prop;

    public InfoController(DataSourceProperties dataSource, TaskConfiguration prop) {
        this.dataSource = dataSource;
        this.prop = prop;
    }

    @GetMapping("info/url")
    String redUrl()
    {
        return dataSource.getUrl();
    }

    @GetMapping("info/prop")
    boolean readProp()
    {
        return prop.getTemplate().isAllowMultipleTasks();
    }
}
