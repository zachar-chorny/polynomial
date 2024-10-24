package com.example.polynomialproccesingapp.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

@Service
public class PolynomialService {

    private static final String NUMERIC_REGEX = "^\\d+(\\.\\d+)?$";
    private static final String VARIABLE_REGEX = "^[a-zA-Z]+$";

    public String simplifyPolynomial(String polynomial) {
        List<String> tokens = tokenize(polynomial);
        List<String> postfix = infixToPostfix(tokens);
        List<Term> simplifiedTerms = evaluatePostfix(postfix);
        return formatPolynomial(simplifiedTerms);
    }

    public int evaluatePolynomial(String simplifiedPolynomial, int x) {
        List<String> tokens = tokenize(simplifiedPolynomial);
        List<String> postfix = infixToPostfix(tokens);
        List<Term> terms = evaluatePostfix(postfix);
        int result = 0;
        for (Term term : terms) {
            int termValue = term.coefficient;
            for (Map.Entry<String, Integer> entry : term.variables.entrySet()) {
                int exp = entry.getValue();
                termValue *= Math.pow(x, exp);
            }
            result += termValue;
        }
        return result;
    }

    private List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        int i = 0;
        while (i < expr.length()) {
            char c = expr.charAt(i);
            if (Character.isWhitespace(c)) {
                i++;
                continue;
            }
            if ("+-*/()^".indexOf(c) >= 0) {
                tokens.add(String.valueOf(c));
                i++;
            } else if (Character.isDigit(c) || c == '.') {
                StringBuilder num = new StringBuilder();
                num.append(c);
                i++;
                while (i < expr.length() && (Character.isDigit(expr.charAt(i)) || expr.charAt(i) == '.')) {
                    num.append(expr.charAt(i));
                    i++;
                }
                tokens.add(num.toString());
            } else if (Character.isLetter(c)) {
                StringBuilder var = new StringBuilder();
                var.append(c);
                i++;
                while (i < expr.length() && Character.isLetterOrDigit(expr.charAt(i))) {
                    var.append(expr.charAt(i));
                    i++;
                }
                tokens.add(var.toString());
            }
        }
        List<String> newTokens = new ArrayList<>();
        String prevToken = "";
        for (String token : tokens) {
            if ((isNumeric(prevToken) || isVariable(prevToken) || prevToken.equals(")")) &&
                    (isVariable(token) || token.equals("("))) {
                newTokens.add("*");
            }
            newTokens.add(token);
            prevToken = token;
        }
        return newTokens;
    }

    private List<String> infixToPostfix(List<String> tokens) {
        Map<String, Integer> precedence = new HashMap<>();
        precedence.put("^", 4);
        precedence.put("*", 3);
        precedence.put("/", 3);
        precedence.put("+", 2);
        precedence.put("-", 2);

        List<String> outputQueue = new ArrayList<>();
        Stack<String> operatorStack = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token) || isVariable(token)) {
                outputQueue.add(token);
            } else if (precedence.containsKey(token)) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(") &&
                        precedence.get(operatorStack.peek()) >= precedence.get(token)) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    outputQueue.add(operatorStack.pop());
                }
                operatorStack.pop();
            }
        }
        while (!operatorStack.isEmpty()) {
            outputQueue.add(operatorStack.pop());
        }
        return outputQueue;
    }

    private List<Term> evaluatePostfix(List<String> postfixTokens) {
        Stack<List<Term>> stack = new Stack<>();
        for (String token : postfixTokens) {
            if (isNumeric(token)) {
                int coeff = Integer.parseInt(token);
                Term term = new Term(coeff, new HashMap<>());
                stack.push(Collections.singletonList(term));
            } else if (isVariable(token)) {
                Map<String, Integer> vars = new HashMap<>();
                vars.put(token, 1);
                Term term = new Term(1, vars);
                stack.push(Collections.singletonList(term));
            } else if ("+-*/^".contains(token)) {
                List<Term> bTerms = stack.pop();
                List<Term> aTerms = stack.pop();
                List<Term> resultTerms = new ArrayList<>();
                switch (token) {
                    case "+":
                    case "-":
                        resultTerms.addAll(aTerms);
                        resultTerms.addAll(token.equals("+") ? bTerms : negateTerms(bTerms));
                        break;
                    case "*":
                        for (Term termA : aTerms) {
                            for (Term termB : bTerms) {
                                resultTerms.add(termA.multiply(termB));
                            }
                        }
                        break;
                    case "/":
                        int divisor = bTerms.get(0).coefficient;
                        for (Term term : aTerms) {
                            resultTerms.add(new Term(term.coefficient / divisor, term.variables));
                        }
                        break;
                    case "^":
                        int exponent = bTerms.get(0).coefficient;
                        resultTerms.addAll(aTerms); // Start with the base
                        for (int i = 1; i < exponent; i++) {
                            List<Term> tempResult = new ArrayList<>();
                            for (Term termA : resultTerms) {
                                for (Term termB : aTerms) {
                                    tempResult.add(termA.multiply(termB));
                                }
                            }
                            resultTerms = tempResult;
                        }
                        break;
                }
                stack.push(combineTerms(resultTerms));
            }
        }
        return stack.pop();
    }

    private List<Term> negateTerms(List<Term> terms) {
        List<Term> negated = new ArrayList<>();
        for (Term term : terms) {
            negated.add(new Term(-term.coefficient, term.variables));
        }
        return negated;
    }

    private List<Term> combineTerms(List<Term> terms) {
        Map<Map<String, Integer>, Term> termMap = new HashMap<>();
        for (Term term : terms) {
            Map<String, Integer> key = term.variables;
            Term existingTerm = termMap.get(key);
            if (existingTerm != null) {
                existingTerm.coefficient += term.coefficient;
            } else {
                termMap.put(key, new Term(term.coefficient, term.variables));
            }
        }
        return new ArrayList<>(termMap.values());
    }

    private String formatPolynomial(List<Term> terms) {
        terms.sort((t1, t2) -> {
            int degree1 = t1.variables.values().stream().mapToInt(Integer::intValue).sum();
            int degree2 = t2.variables.values().stream().mapToInt(Integer::intValue).sum();
            if (degree1 != degree2) {
                return Integer.compare(degree2, degree1);
            } else {
                String vars1 = String.join("", new TreeSet<>(t1.variables.keySet()));
                String vars2 = String.join("", new TreeSet<>(t2.variables.keySet()));
                return vars1.compareTo(vars2);
            }
        });
        StringBuilder result = new StringBuilder();
        for (Term term : terms) {
            double coeff = term.coefficient;
            if (coeff > 0 && result.length() > 0) {
                result.append("+");
            }
            result.append(term.toString());
        }
        return result.length() > 0 ? result.toString() : "0";
    }

    private boolean isNumeric(String str) {
        return str != null && str.matches(NUMERIC_REGEX);
    }

    private boolean isVariable(String str) {
        return str != null && str.matches(VARIABLE_REGEX);
    }

    private static class Term {
        private int coefficient;
        private final Map<String, Integer> variables;

        public Term(int coefficient, Map<String, Integer> variables) {
            this.coefficient = coefficient;
            this.variables = new TreeMap<>(variables);
        }

        public Term multiply(Term other) {
            int newCoefficient = this.coefficient * other.coefficient;
            Map<String, Integer> newVariables = new HashMap<>(this.variables);
            for (Map.Entry<String, Integer> entry : other.variables.entrySet()) {
                String var = entry.getKey();
                int exp = entry.getValue();
                newVariables.put(var, newVariables.getOrDefault(var, 0) + exp);
            }
            return new Term(newCoefficient, newVariables);
        }

        @Override
        public String toString() {
            StringBuilder result = new StringBuilder();
            if (coefficient != 0) {
                if (coefficient != 1 || variables.isEmpty()) {
                    result.append(coefficient);
                }
                for (Map.Entry<String, Integer> entry : variables.entrySet()) {
                    String var = entry.getKey();
                    int exp = entry.getValue();
                    if (exp == 1) {
                        result.append(var);
                    } else {
                        result.append(var).append("^").append(exp);
                    }
                }
            }
            return result.toString();
        }
    }
}
