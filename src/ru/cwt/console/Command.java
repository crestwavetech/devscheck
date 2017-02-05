package ru.cwt.console;

import java.io.IOException;

/**
 * Command
 *
 * @author hexprobe <hexprobe@nbug.net>
 *
 * @license
 * This code is hereby placed in the public domain.
 *
 */
public interface Command {
    public abstract void execute(String name, String argument, EasyTerminal terminal) throws IOException;
}
