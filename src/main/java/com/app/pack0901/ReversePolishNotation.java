package com.app.pack0901;

import java.util.*;

/**
 * a*b-c*d+e
 * <p>
 * a*(b-c*d)+e
 * <p>
 * (a*b+c)-(d+e)
 * <p>
 * ((a+b)/c+d)/e
 */
public class ReversePolishNotation {

    public enum Operator {
        ADD('+', 1), SUBTRACT('-', 1),
        MULTIPLY('*', 2), DIVIDE('/', 2),
        LEFT_BRACKET('(', -1), RIGHT_BRACKET(')', -1);


        public char op;
        public int priority;

        Operator(char op, int priority) {
            this.op = op;
            this.priority = priority;
        }

        public static Operator getOperator(char ch) {
            for (Operator op : Operator.values()) {
                if (op.op == ch) {
                    return op;
                }
            }
            return null;
        }

        public static List<Character> getOperator() {
            List<Character> res = new ArrayList<>();
            for (Operator op : Operator.values()) {
                res.add(op.op);
            }
            return res;
        }
    }

    public static Set<Character> set = new HashSet<>(Operator.getOperator());
    public static final String EMPTY_SPACE = " ";

    public String parse(String s) {
        StringBuilder sb = new StringBuilder();
        Stack<Operator> stack = new Stack<>();
        for (char ch : s.toCharArray()) {
            if (set.contains(ch)) {
                Operator now = Operator.getOperator(ch);
                if (now == null) {
                    throw new RuntimeException("");
                }
                if (now == Operator.LEFT_BRACKET) {
                    stack.push(Operator.LEFT_BRACKET);
                    continue;
                }
                if (now == Operator.RIGHT_BRACKET) {
                    while (!stack.isEmpty()) {
                        Operator op = stack.pop();
                        if (op != Operator.LEFT_BRACKET) {
                            sb.append(EMPTY_SPACE).append(op.op);
                        } else {
                            break;
                        }
                    }
                } else {
                    while (!stack.isEmpty() && stack.peek() != Operator.LEFT_BRACKET
                            && stack.peek().priority >= now.priority) {
                        sb.append(EMPTY_SPACE).append(stack.pop().op);
                    }
                    stack.push(now);
                    sb.append(EMPTY_SPACE);
                }
            } else {
                sb.append(ch);
            }
        }
        while (!stack.isEmpty()) {
            sb.append(EMPTY_SPACE).append(stack.pop().op);
        }
        return sb.toString();
    }


    public boolean isNotation(String s) {
        Stack<String> stack = new Stack<>();
        for (String a : s.split(EMPTY_SPACE)) {
            if(a.length() == 0) continue;
            if (a.length() == 1) {
                if (a.charAt(0) == Operator.LEFT_BRACKET.op || a.charAt(0) == Operator.RIGHT_BRACKET.op) {
                    return false;
                }
                if (set.contains(a.charAt(0))) {
                    if (stack.size() < 2) {
                        return false;
                    }
                    stack.pop();
                } else {
                    stack.push(a);
                }
            } else {
                stack.push(a);
            }
        }
        return stack.size() == 1;
    }

    public double calculate(String notation){
        Stack<Double> stack = new Stack<>();
        for(String a : notation.split(EMPTY_SPACE)){
            if(a.length() == 0) continue;
            if (a.length() == 1) {
                if(set.contains(a.charAt(0))){
                    double n = stack.pop();
                    double m = stack.pop();
                    if(a.charAt(0) == Operator.ADD.op){
                        stack.push(m+n);
                    }else if(a.charAt(0) == Operator.SUBTRACT.op){
                        stack.push(m-n);
                    }else if(a.charAt(0) == Operator.MULTIPLY.op){
                        stack.push(m*n);
                    }else if(a.charAt(0) == Operator.DIVIDE.op){
                        if(n == 0){
                            throw new RuntimeException("");
                        }
                        stack.push(m/n);
                    }
                }
            } else {
                stack.push(Double.parseDouble(a));
            }
        }
        return stack.pop();
    }
}
