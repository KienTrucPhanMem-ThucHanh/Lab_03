package org.example;

import org.example.examples.CommonOperations;
import org.example.examples.DirExplorer;
import org.example.examples.ListClassesExample;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        File projectDir = new File("E:\\HK2_Nam4\\Kien_Truc_Phan_Mem\\Thuc_Hanh\\Lab03");
//        new DirExplorer((level, path, file) -> path.endsWith(".java"), ((level, path, file) -> {
//            System.out.println(path);
//        })).explore(projectDir);
        ListClassesExample listClassesExample = new ListClassesExample();
        CommonOperations commonOperations = new CommonOperations();
        commonOperations.listMethodCalls(projectDir);
    }
}