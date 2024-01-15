package com.example.demo.service;

import lombok.Getter;

import java.io.IOException;

@Getter
public class PythonService {
    private final String [] cmd = new String[3];

    public PythonService(String pathToScript, String arg) {
        this.cmd[0] = "python";
        this.cmd[1] = pathToScript;
        this.cmd[2] = arg;
    }

    public int exec() throws IOException, InterruptedException {
        return Runtime.getRuntime().exec(this.cmd).waitFor();
    }
}
