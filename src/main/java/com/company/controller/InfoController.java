package com.company.controller;


import com.company.TaskConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/info")
public class InfoController {

    private final DataSourceProperties dataSource;
    private final TaskConfiguration prop;

    public InfoController(DataSourceProperties dataSource, TaskConfiguration prop) {
        this.dataSource = dataSource;
        this.prop = prop;
    }

    @GetMapping("/url")
    String redUrl()
    {
        return dataSource.getUrl();
    }

    @GetMapping("/prop")
    boolean readProp()
    {
        return prop.getTemplate().isAllowMultipleTasks();
    }
}
