package com.iaoth.advcode16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day12Leonardo {

    private static Map<String, Integer> regs = new HashMap<>();

    static public void main(String... args) {
        List<String[]> app = new ArrayList<>();
        AdvCodeUtil.readResource("day12input.txt")
                .lines()
                .forEach(line -> app.add(line.split(" ")));

        int ip = 0;
        regs.put("a", 0);
        regs.put("b", 0);
        regs.put("c", 1);
        regs.put("d", 0);
        while (ip < app.size() && ip >= 0) {
            String[] ins = app.get(ip);
            switch (ins[0]) {
                case "cpy":
                    regs.put(ins[2], getValue(ins[1]));
                    ip++;
                    break;
                case "inc":
                    regs.compute(ins[1], (key, i) -> i + 1);
                    ip++;
                    break;
                case "dec":
                    regs.compute(ins[1], (key, i) -> i - 1);
                    ip++;
                    break;
                case "jnz":
                    if (getValue(ins[1]) != 0) {
                        ip += Integer.parseInt(ins[2]);
                    } else {
                        ip++;
                    }
                    break;
                default:
                    throw new RuntimeException("unknown instruction");
            }
        }

        System.out.println("a = " + regs.get("a"));
    }

    private static Integer getValue(String in) {
        if (in.matches("[a-d]")) {
            return regs.get(in);
        } else {
            return Integer.parseInt(in);
        }
    }
}
