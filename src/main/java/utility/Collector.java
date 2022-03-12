package utility;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import com.google.gson.*;
import com.google.gson.reflect.*;
import data.*;
import exceptions.MinimalpointIsLessThenZero;
import exceptions.ScriptRecursionException;
import exceptions.WrongAmountOfElementsException;
import exceptions.WrongDate;


public class Collector {
    private Vector<LabWork> works = new Vector<>();
    private File jsonCollection;
    private File outPut;
    private Date initDate;
    private Gson gson = new Gson();
    protected static HashMap<String, String> manual;
    private List<String> scriptStack = new ArrayList<>();

    {
        Gson gson = new Gson();
        works = new Vector<>();
        manual = new HashMap<>();
        manual.put("remove_first", "удалить первый элемент из коллекции.");
        manual.put("add", "Добавить новый элемент в коллекцию.");
        manual.put("remove_greater", "Удалить из коллекции все элементы, превышающие заданный.");
        manual.put("show", "Вывести в стандартный поток вывода все элементы коллекции в строковом представлении.");
        manual.put("clear", "Очистить коллекцию.");
        manual.put("update_id", "обновить значение элемента коллекции, id которого равен заданному.");
        manual.put("info", "Вывести в стандартный поток вывода информацию о коллекции.");
        manual.put("remove_at_index", "удалить элемент, находящийся в заданной позиции коллекции.");
        manual.put("remove_by_id", "удалить элемент из коллекции по его id.");
        manual.put("add_if_max", " добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции.");
        manual.put("exit", "Сохранить коллекцию в файл и завершить работу программы.");
        manual.put("max_by_author", "вывести любой объект из коллекции, значение поля author которого является максимальным.");
        manual.put("count_by_difficulty", "вывести количество элементов, значение поля difficulty которых равно заданному.");
        manual.put("filter_greater_than_minimal_point", "вывести элементы, значение поля minimalPoint которых больше заданного.");

    }

    public Collector(String inPath, String outPath) throws IOException {
        this.jsonCollection = new File(inPath);
        this.outPut = new File(outPath);
        this.load();
        this.initDate = new Date();

        for (LabWork p : works) {
            if (p.getMinimalPoint() < 0.0) {
                throw new MinimalpointIsLessThenZero();
            }
        }
    }

    public Collector() throws IOException {
        System.out.println("Не указан путь к файлу!");
        System.exit(1);
    }

    public Collector(String inPath) {
        System.out.println("Не указан путь к файлу!");
        System.exit(1);
    }


    Scanner scanner = new Scanner(System.in);

    /**
     * Десериализует коллекцию из файла json.
     *
     * @throws IOException если файл пуст или защищён.
     */
    public void load() throws IOException {
        int beginSize = works.size();
        try {
            if (!jsonCollection.exists()) throw new FileNotFoundException();
        } catch (FileNotFoundException ex) {
            System.out.println("Файла по указанному пути не существует.");
            System.exit(1);
        }
        try {
            if ((!jsonCollection.canRead() || !jsonCollection.canWrite())) throw new SecurityException();
        } catch (SecurityException ex) {
            System.out.println("Файл защищён от чтения и/или записи. Для работы программы нужны оба разрешения.");
            System.exit(1);
        }
        try {
            if (jsonCollection.length() == 0) throw new JsonSyntaxException("");
        } catch (JsonSyntaxException ex) {
            System.out.println("Файл пуст.");
            System.exit(1);

        }
        try (BufferedReader inputStreamReader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonCollection)))) {
            System.out.println("Идёт загрузка коллекции " + jsonCollection.getAbsolutePath());
            String nextLine;
            StringBuilder result = new StringBuilder();
            while ((nextLine = inputStreamReader.readLine()) != null) {
                result.append(nextLine);

            }


            //System.out.println(result);

            Type collectionType = new TypeToken<Vector<LabWork>>() {
            }.getType();
            try {
                works = gson.fromJson(result.toString(), collectionType);
                // System.out.println(works);
                for (LabWork element : works) {
                    int id = create_id();
                    element.setId(id);
                    if (element.getDate() != null) {
                        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.ENGLISH);
                        Date cdate = formatter.parse(element.getDate());
                        element.setCreationDate(cdate);

                    }
                    if (element.getCreationDate() == null) {
                        Date date = create_date();
                        element.setCreationDate(date);
                    }
                }

            } catch (JsonSyntaxException ex) {
                System.out.println("Ошибка синтаксиса Json. Коллекция не может быть загружена.");
                System.exit(1);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            System.out.println("Коллекция успешно загружена. Добавлено " + (works.size() - beginSize) + " элементов.");
        }
    }


    /**
     * Выводит все элементы коллекции.
     */
    public void show() {
        if (works.size() != 0) works.forEach(p -> System.out.println(p.toString()));
        else System.out.println("Коллекция пуста.");
    }

    /**
     * Выводит на экран список доступных пользователю команд.
     */
    public void help() {
        System.out.println("Данные коллекции сохраняются автоматически после каждой успешной модификации.");
        System.out.println("Команды: " + manual.keySet() + "\nman {команда} для справки.\n");

    }

    /**
     * Выводит справку для конкретной команды.
     *
     * @param arg : Имя команды.
     */
    public void man(String arg) {
        System.out.println(arg + ": " + manual.get(arg));
    }

    /**
     * Выводит информацию о коллекции.
     */
    public void info() {
        System.out.println("Тип коллекции: " + works.getClass() + "\nДата инициализации: " + initDate + "\nКоличество элементов: " + works.size());
    }


    /**
     * Метод для чтения объекта LabWork из командной строки
     *
     * @return возвращает объект класса LabWork
     */
    public LabWork readWork() throws ParseException {
        LabWork W = null;
        int id = create_id();
        String name = readWorkName();
        Coordinates coordinates = readCoordinates();
        java.util.Date creationDate = java.util.Date.from(Instant.now());
        Double minimalPoint = readMinimalPoint();
        Difficulty difficulty = readDifficulty();
        Person author = readPerson();
        //System.out.println(coordinates);
        if (difficulty != null) {
            LabWork work = new LabWork(id, name, coordinates, creationDate, minimalPoint, difficulty, author);
            W = work;
        }
        if (difficulty == null) {
            LabWork work = new LabWork(id, name, coordinates, creationDate, minimalPoint, author);
            W = work;
        }
        return W;
    }

    /**
     * Добавляет новый элемент в коллекцию
     *
     * @param work: объект класса LabWork
     */
    public void add(LabWork work) {
        works.addElement(work);
        System.out.println("Объект успешно добавлен");
    }

    /**
     * Вспомогательный метод для чтения объекта Coordinates из командной строки
     *
     * @return возвращает объект класса Coordinates
     */
    public Coordinates readCoordinates() {
        Scanner scanner = new Scanner(System.in);
        Long x = null;
        Long y = null;
        do {
            System.out.println("Введите x, не может быть null.");
            String s = scanner.nextLine();
            if (s.equals("")) {
                x = null;

            } else {
                try {
                    x = Long.parseLong(s);
                } catch (IllegalArgumentException e) {
                    System.out.println("x - обязано быть числом без каких-либо разделителей.");
                }
            }
        } while (x == null);

        do {
            System.out.println("Введите y, не может быть null.");
            String s = scanner.nextLine();
            if (s.equals("")) {
                y = null;

            } else {
                try {
                    y = Long.parseLong(s);
                } catch (IllegalArgumentException e) {
                    System.out.println("y - обязано быть числом без каких-либо разделителей.");
                }
            }
        } while (y == null);

        return new Coordinates(x, y);
    }

    /**
     * Вспомогательный метод для чтения строки из командной строки
     *
     * @return возвращает объект класса String
     */
    public String readWorkName() {
        String name = "";
        while (name.equals("")) {
            System.out.println("Введите название лабораторной работы, поле не может быть пустой строкой");
            name = scanner.nextLine();
        }
        // System.out.println(name);
        return name;
    }

    /**
     * Вспомогательный метод для чтения строки из командной строки
     *
     * @return возвращает объект класса String
     */
    public String readPersonName() {
        String name = "";
        while (name.equals("")) {
            System.out.println("Введите имя автора, поле не может быть пустой строкой");
            name = scanner.nextLine();
        }
        // System.out.println(name);
        return name;
    }

    /**
     * Вспомогательный метод для чтения цены из командной строки
     *
     * @return возвращает цену Double
     */
    public Double readMinimalPoint() {
        Double point = null;
        do {
            System.out.println("Введите MinimalPoint, поле не может быть null, Значение поля должно быть больше 0");
            String s = scanner.nextLine();
            try {
                point = Double.parseDouble(s);
                if (point <= 0) {
                    System.out.println("MinimalPoint - обязано быть вещественным числом > 0. Формат ввода: 100.0 или 100");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("MinimalPoint - обязано быть вещественным числом > 0. Формат ввода: 100.0 или 100");
            }


        } while ((point == null) || (point <= 0));
        return point;
    }

    /**
     * Вспомогательный метод для чтения строки из командной строки
     *
     * @return возвращает объект класса String
     */
    public Date readBirth() throws ParseException, WrongDate {
        String name = "";
        String[] arr = new String[0];
        boolean flag = Boolean.TRUE;
        Date date = null;
        while (flag) {
            try {
                System.out.println("Введите день рождения автора(Формат ввода: DD.MM.YYYY,HH.MM.SS). Можно не заполнять - Enter." + "\nВнимательно вводите данные, чтобы избежать ошибки(Например: часы не могут быть больше 23.");
                String s = scanner.nextLine();
                if (s.equals("")) {
                    name = null;
                    date = null;
                    flag = Boolean.FALSE;
                } else {
                    name = s;
                    String[] date_test = name.split(",");
                    String[] date_1 = date_test[0].split("\\.");
                    String[] date_2 = date_test[1].split("\\.");
                    if (Integer.parseInt(date_1[0]) <= 0 || Integer.parseInt(date_1[0]) > 31 || Integer.parseInt(date_1[1]) <= 0 || Integer.parseInt(date_1[1]) > 12 || Integer.parseInt(date_2[0]) < 0 || Integer.parseInt(date_2[0]) > 23 || Integer.parseInt(date_2[1]) < 0 || Integer.parseInt(date_2[1]) > 59 || Integer.parseInt(date_2[2]) < 0 || Integer.parseInt(date_2[2]) > 59) {
                        System.out.println("Неверный формат даты!");
                        flag = Boolean.TRUE;
                    } else {
                        date = new SimpleDateFormat("dd.MM.yyyy,hh.mm.ss").parse(name);
                        flag = Boolean.FALSE;
                    }
                }

            } catch (IllegalArgumentException e) {
                System.out.println("ERROR! Неправильный формат даты");
                flag = Boolean.TRUE;
            }
        }


        return date;
    }

    /**
     * Вспомогательный метод для чтения объекта UnitOfMeasure из командной строки
     *
     * @return возвращает объект класса UnitOfMeasure
     */
    public Difficulty readDifficulty() {
        Difficulty diff = null;
        boolean flag = Boolean.TRUE;
        while (flag) {
            try {
                System.out.println("Введите Difficulty, значение поля может быть равно: EASY, HARD, VERY_HARD, HOPELESS");
                String s = scanner.nextLine();
                if (s.equals("")) {
                    diff = null;
                    flag = Boolean.FALSE;
                } else {
                    diff = Difficulty.valueOf(s);
                    flag = Boolean.FALSE;
                }

            } catch (IllegalArgumentException e) {
                System.out.println("ERROR! Значение поля может быть равно: EASY, HARD, VERY_HARD, HOPELESS");
                flag = Boolean.TRUE;
            }
        }

        return diff;
    }

    /**
     * Вспомогательный метод для чтения объекта Person из командной строки
     *
     * @return возвращает объект класса Person
     */
    public Person readPerson() throws ParseException {
        String name = readPersonName();
        Color color = readColor();
        Country nationality = readNationality();
        Date birth = readBirth();
        Person p = null;
        if (nationality != null && birth != null) {
            p = new Person(name, birth, color, nationality);
        }
        if (nationality == null && birth != null) {
            p = new Person(name, color, birth);
        }
        if (nationality == null && birth == null) {
            p = new Person(name, color);
        }
        //System.out.println(p);
        return p;

    }

    /**
     * Вспомогательный метод для чтения Color из командной строки
     *
     * @return возвращает объект Color
     */
    public Color readColor() {
        Color color = null;
        while (color == null) {
            try {
                System.out.println("Введите цвет глаз автора, значение поля может быть равно: RED, GREEN, ORANGE,BLACK, BROWN");
                color = Color.valueOf(scanner.nextLine());
            } catch (IllegalArgumentException e) {
                System.out.println("значение поля может быть равно: RED, GREEN, ORANGE,BLACK, BROWN");
            }
        }
        return color;
    }

    /**
     * Вспомогательный метод для чтения объекта Location из командной строки
     *
     * @return возвращает объект класса Location
     */
    public Country readNationality() {
        Country c = null;
        boolean flag = Boolean.TRUE;
        while (flag) {
            try {
                System.out.println("Введите национальность автора, значение поля может быть равно: USA, GERMANY, INDIA,VATICAN, SOUTH_KOREA. Поле может быть пустым - ENTER");
                String s = scanner.nextLine();
                if (s.equals("")) {
                    c = null;
                    flag = Boolean.FALSE;
                } else {
                    c = Country.valueOf(s);
                    flag = Boolean.FALSE;
                }

            } catch (IllegalArgumentException e) {
                System.out.println("ERROR! Значение поля может быть равно: USA, GERMANY, INDIA,VATICAN, SOUTH_KOREA");
                flag = Boolean.TRUE;
            }
        }


        return c;
    }

    /**
     * /**
     * Обновляет id
     */
    public void update_id() {
            if (works.size() != 0) {
                String addCommand = null;
                while (addCommand == null) {
                    try {
                        System.out.println("Введите Id");
                        addCommand = (scanner.nextLine());
                        int new_id = Integer.parseInt(addCommand);
                        works.forEach(p -> {
                            if (p != null && p.getId() == new_id) {
                                int id = create_id();
                                p.setId(id);
                            }
                        });
                        System.out.println("Id обновлено.");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Нет такого id.");
                    }
                }} else {
                System.out.println("Элемент не с чем сравнивать. Коллекция пуста.");

            }
    }

    /**
     * /**
     * Обновляет id
     */
    public void update_id_script(String t) {
        if (works.size() != 0) {
            try {
                System.out.println("Введите значение id:");

                int new_id = Integer.parseInt(t);
                works.forEach(p -> {
                    if (p != null && p.getId() == new_id) {
                        int id = create_id();
                        p.setId(id);
                    }
                });
            } catch (NoSuchElementException ex) {
                System.out.println("Ошибка! Id не найдено.");
            }
        } else System.out.println("Элемент не с чем сравнивать. Коллекция пуста.");

    }

    /**
     * Удаляет элемент коллекции по id
     *
     * @param id : Число, по которому необходимо удалить элемент
     */
    public void remove_by_id(int id) {
        if (works.size() != 0) {
            try {
                works.removeIf(p -> ((p.getId()) == id));
                System.out.println("Элемент коллекции удалён.");
            } catch (NoSuchElementException ex) {
                System.out.println("Ошибка! Id не найдено.");
            }
        } else System.out.println("Элемент не с чем сравнивать. Коллекция пуста.");
    }


    /**
     * Удаляет первый элемент коллекции.
     */
    public void remove_first() {
        try {
            works.remove(0);
            System.out.println("Первый элемент коллекции удалён.");
        } catch (NoSuchElementException ex) {
            System.out.println("Ошибка! Коллекция пуста.");
        }
    }

    /**
     * Удаляет элемент коллекции по индексу
     *
     * @param index : Число, по которому необходимо удалить элемент
     */
    public void remove_at(int index) {
        try {
            works.remove(index - 1);
            System.out.println("Элемент коллекции удалён.");
        } catch (NoSuchElementException ex) {
            System.out.println("Ошибка! Элемента по индексу не существует.");
        }
    }

    /**
     * Добавляет в коллекцию элемент переданный в качестве параметра, если он больше максимального элемента коллекции.
     */
    public void add_if_max() throws ParseException {

        System.out.println("Введите значение LabWork:");
        LabWork W = null;
        int id = create_id();
        String name = readWorkName();
        Coordinates coordinates = readCoordinates();
        java.util.Date creationDate = java.util.Date.from(Instant.now());
        Double minimalPoint = readMinimalPoint();
        Difficulty difficulty = readDifficulty();
        Person author = readPerson();

        if (difficulty != null) {
            W = new LabWork(id, name, coordinates, creationDate, minimalPoint, difficulty, author);
        }
        if (difficulty == null) {
            W = new LabWork(id, name, coordinates, creationDate, minimalPoint, author);
        }

        if (works.size() != 0) {
            LabWork competitor = Collections.max(works);

            if (competitor.getName().length() < W.getName().length()) {
                works.add(W);
                System.out.println("Элемент успешно добавлен.");
            } else System.out.println("Не удалось добавить элемент. Он меньше максимального.");
        } else System.out.println("Элемент не с чем сравнивать. Коллекция пуста.");
    }

    /**
     * Выводит любой объект из коллекции, значение поля author которого является максимальным
     */
    public void max_by_author() {
        if (works.size() != 0) {
            try {
                ArrayList names = new ArrayList<>();
                for (LabWork work : works) {
                    String a = work.getAuthor().getName();
                    names.add(a);
                }

                Comparable name = Collections.max(names);
                String Max_name = name.toString();
                for (LabWork work : works) {
                    if (work.getAuthor().getName() == Max_name) {
                        System.out.println(work.toString());
                    }
                }
            } catch (NoSuchElementException e) {
                System.out.println("Элемент не с чем сравнивать. Коллекция пуста.");
            }
        } else System.out.println("Элемент не с чем сравнивать. Коллекция пуста.");
    }

    /**
     * Считает количество элементов, значение поля difficulty которых равно заданному
     */
    public void count_by_difficulty(String addCommand) {
        if (works.size() != 0) {
            int c = 0;
            int n = 0;
            for (LabWork work : works) {
                if (work.getDifficulty() != null) {

                    if (Objects.equals(work.getDifficulty().toString(), addCommand) && work.getDifficulty() != null) {

                        c += 1;
                    }
                } else {
                    n += 1;
                    System.out.println("У " + n + " элементов коллекции сложности нет.");
                }


            }
            System.out.println("Количество элементов со сложностью " + addCommand + "=" + c);
        } else System.out.println("Элемент не с чем сравнивать. Коллекция пуста.");
    }

    /**
     * Выводит элементы, значение поля minimalPoint которых больше заданного
     *
     * @param point
     */
    public void filter_greater_than_minimal_point(double point) {
        if (works.size() != 0) {
            for (LabWork work : works) {
                if (work.getMinimalPoint() == point) {
                    System.out.println(work.toString());
                }
            }
        } else System.out.println("Элемент не с чем сравнивать. Коллекция пуста.");
    }

    public void script_add(List<String> commands) {

    }

    /**
     * Выполняет скрипт
     *
     * @return
     */
    public List<String> execute(String argument) throws WrongAmountOfElementsException {
        List<String> commands = new ArrayList<>();
        try {
            if (argument.isEmpty()) throw new WrongAmountOfElementsException();
            System.out.println("Выполняю скрипт '" + argument + "'...");
            String[] userCommand = {"", ""};
            try (Scanner scriptScanner = new Scanner(new File(argument))) {
                if (!scriptScanner.hasNext()) throw new NoSuchElementException();
                do {
                    userCommand = (scriptScanner.nextLine().trim() + " ").split(" ", 2);

                    //System.out.println(userCommand[0]);

                    if (userCommand[0].equals("update_id")) {
                        userCommand[1] = scriptScanner.nextLine();
                    }
                    if (userCommand[0].equals("add")) {
                        StringBuilder command = new StringBuilder();
                        String[] comands = new String[]{"save", "remove_first", "add", "remove_greater", "show", "clear", "update_id", "info", "help", "man", "remove_at_index", "remove_by_id", "add_if_max", "exit", "max_by_author", "count_by_difficulty", "filter_greater_than_minimal_point"};
                        String line = (scriptScanner.nextLine().trim() + " ").split(" ")[0];
                        while (!(Arrays.asList(comands)).contains(line)) {
                            command.append(line + " ");
                            line = scriptScanner.nextLine();
                        }
                        // System.out.println(command.toString());
                        userCommand[1] = command.toString();
                    }
                    if (userCommand[0].equals("execute_script")) {
                        throw new ScriptRecursionException();
                    }
                    //System.out.println(userCommand);
                    String out = userCommand[0] + " " + userCommand[1];
                    commands.add(out);
                    //System.out.println(commands);
                } while (scriptScanner.hasNextLine());
            } catch (FileNotFoundException exception) {
                System.out.println("Файл со скриптом не найден!");
            } catch (NoSuchElementException exception) {
                System.out.println("Файл со скриптом пуст!");
            } catch (ScriptRecursionException exception) {
                System.out.println("Скрипты не могут вызываться рекурсивно!");
            } catch (IllegalStateException exception) {
                System.out.println("Непредвиденная ошибка!");
                System.exit(0);
            }
        } catch (WrongAmountOfElementsException exception) {
            System.out.println("Некорректные команды в скрипте!");
        }
        // System.out.println(commands);
        return commands;
    }

    public LabWork script_add(String line) throws ParseException {
        String[] args = line.split(" ");

        LabWork W = null;
        try {
            int id = create_id();
            String name = args[0];
            String pname = null;
            Color color = null;
            Country country = null;
            Date birth = null;
            Coordinates coordinates = new Coordinates(Long.parseLong(args[1]), Long.parseLong(args[2]));
            java.util.Date creationDate = java.util.Date.from(Instant.now());
            Double minimalPoint = Double.parseDouble(args[3]);
            // System.out.println(args[0]+args[1]+args[2]+args[3]);
            if (Objects.equals(args[4], "EASY") || Objects.equals(args[4], "HARD") || Objects.equals(args[4], "VERY_HARD") || Objects.equals(args[4], "HOPELESS")) {
                Difficulty diff = Difficulty.valueOf(args[4]);
                pname = args[5];
                color = Color.valueOf(args[6]);
                if (args.length > 7) {
                    country = Country.valueOf(args[7]);
                    if (args.length > 8) {
                        birth = new SimpleDateFormat("dd.MM.yyyy").parse(args[8]);
                        Person p = new Person(pname, birth, color, country);
                        W = new LabWork(id, name, coordinates, creationDate, minimalPoint, diff, p);

                    } else {
                        Person p = new Person(pname, color, country);
                        W = new LabWork(id, name, coordinates, creationDate, minimalPoint, diff, p);
                    }
                } else {
                    Person p = new Person(pname, color);
                    W = new LabWork(id, name, coordinates, creationDate, minimalPoint, diff, p);
                }
            } else {
                pname = args[4];
                color = Color.valueOf(args[5]);
                if (args.length > 6) {
                    country = Country.valueOf(args[6]);
                    if (args.length > 7) {
                        birth = new SimpleDateFormat("dd.MM.yyyy").parse(args[7]);
                        Person p = new Person(pname, birth, color, country);
                        W = new LabWork(id, name, coordinates, creationDate, minimalPoint, p);
                    } else {
                        Person p = new Person(pname, color, country);
                        W = new LabWork(id, name, coordinates, creationDate, minimalPoint, p);
                    }
                } else {
                    Person p = new Person(pname, color);
                    W = new LabWork(id, name, coordinates, creationDate, minimalPoint, p);
                }
            }

        } catch (
                IllegalArgumentException e) {
            System.out.println("ERROR! Значение поля неверно");
        }
        return W;
    }

    public void script_add_if_max(LabWork W) {
        if (!works.isEmpty()) {
            LabWork competitor = Collections.max(works);

            if (competitor.getName().length() < W.getName().length()) {
                works.add(W);
                System.out.println("Элемент успешно добавлен.");
            } else System.out.println("Не удалось добавить элемент. Он меньше максимального.");
        } else System.out.println("Элемент не с чем сравнивать. Коллекция пуста.");
    }


    /**
     * Удаляет все элементы коллекции.
     */
    public void clear() {
        works.clear();
        System.out.println("Коллекция очищена.");
    }

    /**
     * Сериализует коллекцию в файл json.
     */
    public void save() {
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream((outPut))))) {
            writer.write(gson.toJson(works));
            System.out.println("Коллекция успешно сохранена.");
        } catch (Exception ex) {
            System.out.println("Возникла непредвиденная ошибка. Коллекция не может быть записана в файл.");
        }
    }


    /**
     * @return unique number.
     */
    public static int create_id() {
        return (int) Math.round(Math.random() * 32767 * 10);
    }

    /**
     * @return current date.
     */
    public static Date create_date() {

        Date date = new Date();
        return date;
    }

    public HashMap<String, String> getManual() {
        return manual;
    }

    @Override
    public String toString() {
        if (works.isEmpty()) return "Коллекция пуста!";

        String info = "";
        for (LabWork work : works) {
            info += work;
            info += "\n\n";
        }
        System.out.println(info);
        return info;

    }
}
