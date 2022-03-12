package exceptions;

public class WrongDate extends RuntimeException{
    public WrongDate(){
        super("Неверная дата!");
    }
}
