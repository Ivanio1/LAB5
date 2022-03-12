package utility;

import exceptions.ScriptRecursionException;
import exceptions.WrongAmountOfElementsException;

import java.io.*;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.*;

public class Commander {

    private Collector manager;
    private String userCommand;
    private String[] finalUserCommand;
    private List<String> scriptStack = new ArrayList<>();

    {
        userCommand = "";
    }

    public Commander(Collector manager) {
        this.manager = manager;
    }

    /**
     * Mode for catching commands from user input.
     */
    public void interactiveMod() throws IOException {
        try (Scanner commandReader = new Scanner(System.in)) {
            if (commandReader.hasNextLine()) {
            while (!userCommand.equals("exit")) {
                userCommand = commandReader.nextLine();
                finalUserCommand = userCommand.trim().split(" ", 2);
                try {
                    switch (finalUserCommand[0]) {
                        case "":
                            break;
                        case "execute_script":
                            scriptmode(manager.execute((userCommand.trim().split(" ", 2))[1]));
                            break;
                        case "remove_first":
                            manager.remove_first();
                            break;
                        case "add":
                            manager.add(manager.readWork());
                            break;
                        case "remove_greater":
                            manager.remove_at(Integer.valueOf(finalUserCommand[1]));
                            break;
                        case "show":
                            manager.show();
                            break;
                        case "clear":
                            manager.clear();
                            break;
                        case "info":
                            manager.info();
                            break;
                        case "remove_by_id":
                            manager.remove_by_id(Integer.parseInt(finalUserCommand[1]));
                            break;
                        case "remove_at":
                            manager.remove_at(Integer.parseInt(finalUserCommand[1]));
                            break;
                        case "update_id":
                            manager.update_id();
                            break;
                        case "add_if_max":
                            manager.add_if_max();
                            break;
                        case "help":
                            manager.help();
                            break;
                        case "exit":
                            System.out.println("\nПроцесс завершён.");
                            System.exit(0);
                            break;

                        case "max_by_author":
                            manager.max_by_author();
                            break;
                        case "man":
                            manager.man(finalUserCommand[1]);
                            break;
                        case "save":
                            manager.save();
                            break;
                        case "count_by_difficulty":
                            manager.count_by_difficulty(finalUserCommand[1]);
                            break;
                        case "filter_greater_than_minimal_point":
                            manager.filter_greater_than_minimal_point(Double.parseDouble(finalUserCommand[1]));
                            break;
                        default:
                            System.out.println("Неопознанная команда:" + finalUserCommand[0] + "\nНаберите 'help' для справки.");
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Отсутствует аргумент.");
                } catch (ParseException | WrongAmountOfElementsException e) {
                    e.printStackTrace();
                }
            }
        }}
    }

    /**
     * Mode for catching commands from user input.
     */
    public void scriptmode(List<String> arr) {

        while (!userCommand.equals("exit")) {
            for (String command : arr) {
                userCommand = command;
                finalUserCommand = userCommand.split(" ", 2);

                // System.out.println(finalUserCommand);
                try {
                    switch (finalUserCommand[0]) {
                        case "":
                            break;
                        case "remove_first":
                            manager.remove_first();
                            break;
                        case "add":
                            manager.add(manager.script_add(finalUserCommand[1]));
                            break;
                        case "save":
                            manager.save();
                            break;
                        case "remove_greater":
                            manager.remove_at(Integer.parseInt(finalUserCommand[1]));
                            break;
                        case "show":
                            manager.show();
                            break;
                        case "clear":
                            manager.clear();
                            break;
                        case "info":
                            manager.info();
                            break;
                        case "remove_by_id":
                            manager.remove_by_id(Integer.parseInt(finalUserCommand[1]));
                            break;
                        case "update_id":
                            manager.update_id_script(finalUserCommand[1]);
                            break;
                        case "add_if_max":
                            manager.script_add_if_max(manager.script_add(finalUserCommand[1]));
                            break;
                        case "help":
                            manager.help();
                            break;
                        case "exit":
                            System.out.println("\nПроцесс завершён.");
                            System.exit(0);

                            break;
                        case "max_by_author":
                            manager.max_by_author();
                            break;
                        case "man":
                            manager.man(finalUserCommand[1]);
                            break;
                        case "count_by_difficulty":
                            manager.count_by_difficulty(finalUserCommand[1]);
                            break;
                        case "filter_greater_than_minimal_point":
                            manager.filter_greater_than_minimal_point(Double.parseDouble(finalUserCommand[1]));
                            break;
                        default:
                            System.out.println("Неопознанная команда. Наберите 'help' для справки.");
                    }
                } catch (ArrayIndexOutOfBoundsException ex) {
                    System.out.println("Отсутствует аргумент.");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}