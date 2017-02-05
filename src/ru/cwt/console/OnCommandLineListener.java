package ru.cwt.console;

import java.io.IOException;

/**
 * OnCommandLineListener
 *
 * @author hexprobe <hexprobe@nbug.net>
 *
 * @license
 * This code is hereby placed in the public domain.
 *
 */
public interface OnCommandLineListener {
    public void OnCommandLine(EasyTerminal terminal, String commandLine) throws IOException;
}
