package ru.cwt.devscheck.status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.cwt.devscheck.status.model.Status;

/**
 * @author e.chertikhin
 * @date 26/01/2017
 * <p>
 * Copyright (c) 2017 CrestWave technologies LLC. All right reserved.
 */
@Controller
@RequestMapping("/status")
public class StatusEndpoint {
    private static final Logger log = LoggerFactory.getLogger(StatusEndpoint.class);

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public
    @ResponseBody
    Status getStatus() {
        Status status = new Status();

        status.setRes(0);

        return status;
    }

}
