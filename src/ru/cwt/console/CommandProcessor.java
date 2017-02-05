package ru.cwt.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

/**
 * CommandProcessor
 *
 * @author hexprobe <hexprobe@nbug.net>
 *
 * @license
 * This code is hereby placed in the public domain.
 *
 */
@Component
public class CommandProcessor implements OnCommandLineListener {
    private static final Logger log = LoggerFactory.getLogger(CommandProcessor.class);

    @Autowired
    private EasyShellServer shellServer;

    @Override
    public void OnCommandLine(EasyTerminal terminal, String commandLine) throws IOException {
        try {
            commandLine = commandLine.trim();
            String[] tokens = commandLine.split(" ");
            String name = tokens[0].toLowerCase(Locale.getDefault());

            if (shellServer == null)
                log.error("shell server is null");

            Command command = shellServer.getCommands().get(name);
            if (command != null) {
                command.execute(name, commandLine.substring(name.length()).trim(), terminal);
            } else {
                if (!name.isEmpty()) {
                    terminal.write("Command not found.\r\n");
                    terminal.flush();
                }
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception", e);
            terminal.write("CheckStatus: " + e.toString() + "\r\n");
            terminal.flush();
        }
    }
}