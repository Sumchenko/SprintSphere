package ru.sfedu.sprintspherepk.cli;

import picocli.CommandLine.Command;
import ru.sfedu.sprintspherepk.cli.commands.UserCommand;
import ru.sfedu.sprintspherepk.cli.commands.ProjectCommand;

@Command(
        name = "sprintsphere",
        subcommands = {UserCommand.class, ProjectCommand.class},
        description = "CLI для управления пользователями и проектами"
)
public class MainCommand implements Runnable {
    @Override
    public void run() {
        System.out.println("Добро пожаловать в CLI SprintSphere. Используйте команды для управления.");
    }
}
