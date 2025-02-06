package dev.lysmux.lab5.handler;

/**
 * Class that stores request information
 *
 * @param commandName requested command name
 * @param args        provided arguments
 */
public record Request(String commandName, String[] args) {
}
