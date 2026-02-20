package calculator;

import java.util.*;

public class Calculator {
    private final List<Integer> numbers = new LinkedList<>();

    private void checkBinaryOperation(){
        if (numbers.size() != 2){
            throw new IllegalStateException();
        }
    }

    public void enter(int i){
        numbers.add(i);
        if (numbers.size() > 2){
            throw new IllegalStateException();
        }
    }

    public void add(){
        checkBinaryOperation();
        numbers.set(0, numbers.get(0) + numbers.get(1));
        numbers.remove(1);
    }

    public void multiply(){
        checkBinaryOperation();
        numbers.set(0, numbers.get(0) * numbers.get(1));
        numbers.remove(1);
    }

    public int getResult(){
        if (numbers.size() != 1){
            throw new IllegalStateException();
        }
        return numbers.get(0);
    }
}
