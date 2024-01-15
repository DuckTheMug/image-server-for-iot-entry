package com.example.demo.service;

import lombok.Getter;
import lombok.NonNull;

import java.io.IOException;

@Getter
public class PythonService {
    private final String [] cmd;

    public PythonService(@NonNull String pathToScript, String @NonNull ... arg) {
        this.cmd = new String[arg.length];
        this.cmd[0] = "python";
        this.cmd[1] = pathToScript;
        for (String s : arg) {
            for (int j = 2; j < arg.length; j++)
                this.cmd[j] = s;
        }
    }

    public int exec() throws IOException, InterruptedException {
        return Runtime.getRuntime().exec(this.cmd).waitFor();
    }
}
