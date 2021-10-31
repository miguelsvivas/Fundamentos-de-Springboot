package com.fundamentos.springboot.fundamentos.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class textController {
    //requestmapping sirve para aceptar solicitudes http
    @RequestMapping
    @ResponseBody

    public ResponseEntity<String> function(){
        return new ResponseEntity<>("Hello from controller devtools", HttpStatus.OK);

    }


}
