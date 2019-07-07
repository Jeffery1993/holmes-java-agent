package com.jeffery.holmes.server.view.impl;

import com.alibaba.fastjson.JSON;
import com.jeffery.holmes.server.view.FieldConfig;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TableFieldConfig implements FieldConfig {

    private Function function;
    private String as;

    private TableFieldConfig() {
    }

    public Function getFunction() {
        return function;
    }

    public String getAs() {
        return as;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static FieldConfig valueOf(Element field) {
        TableFieldConfig tableFieldConfig = new TableFieldConfig();
        tableFieldConfig.function = new Function(field.getAttribute("function"));
        tableFieldConfig.as = field.getAttribute("as");
        if (StringUtils.isEmpty(tableFieldConfig.as)) {
            tableFieldConfig.as = tableFieldConfig.function.expression;
        }
        return tableFieldConfig;
    }

    public static class Function {

        private final String expression;
        private final Operator operator;
        private final String field;

        private static final String REGEX = "^(MAX|MIN|SUM|AVG|LAST|COUNT)\\((.*?)\\)$";
        private static final Pattern PATTERN = Pattern.compile(REGEX, Pattern.CASE_INSENSITIVE);

        public Function(String expression) {
            Objects.requireNonNull(expression, "Expression must not be null!");
            this.expression = expression;
            Matcher matcher = PATTERN.matcher(expression);
            if (!matcher.matches()) {
                throw new IllegalArgumentException("Illegal expression: " + expression);
            }
            this.operator = Operator.valueOf(matcher.group(1));
            this.field = matcher.group(2);
        }

        public String getExpression() {
            return expression;
        }

        public Operator getOperator() {
            return operator;
        }

        public String getField() {
            return field;
        }

        public Object apply(List<Object> data) {
            switch (operator) {
                case MAX: {
                    return data.parallelStream()
                            .map(m -> Double.valueOf(m.toString()))
                            .max(Comparator.naturalOrder()).get();
                }
                case MIN: {
                    return data.parallelStream()
                            .map(m -> Double.valueOf(m.toString()))
                            .max(Comparator.naturalOrder()).get();
                }
                case SUM: {
                    return data.parallelStream()
                            .map(m -> Double.valueOf(m.toString()))
                            .reduce((a, b) -> a + b).get();
                }
                case AVG: {
                    return data.parallelStream()
                            .map(m -> Double.valueOf(m.toString()))
                            .reduce((a, b) -> a + b).get() / data.size();
                }
                case LAST: {
                    if (data.size() > 0) {
                        return data.get(data.size() - 1);
                    } else {
                        return null;
                    }
                }
                default: {
                    return null;
                }
            }
        }

        @Override
        public String toString() {
            return expression;
        }

    }

    public enum Operator {
        MAX,
        MIN,
        SUM,
        AVG,
        LAST;
    }

}
