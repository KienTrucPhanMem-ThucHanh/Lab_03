package org.example.examples;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.base.Strings;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class CommonOperations {
    public void listMethodCalls(File projectDir) {
        new DirExplorer((level, path, file) -> path.endsWith(".java"), (level,
                                                                        path, file) -> {

            System.out.println(Strings.repeat("=", path.length()));
            System.out.println(path);
            try {
                new VoidVisitorAdapter<Object>() {
//                    @Override
//                    public void visit(PackageDeclaration n, Object arg) {
//                        super.visit(n, arg);
//                        // câu 1
//                        String packageName = n.getNameAsString();
//                        if(!packageName.matches("com\\.companyname\\..*")){
//                            System.out.println("Tên package "+ packageName +" không đúng cấu trúc tại : "+ n.getBegin());
//                        }
//                    }

//                    @Override
//                    public void visit(ClassOrInterfaceDeclaration n, Object arg) {
//                        super.visit(n, arg);
//                        String className = n.getNameAsString();

//                        if (!isValidClassName(className)) {
//                            System.out.println("WARNING: Invalid class name - " + className);
//                        }

//                        if (isCommented(n)) {
//                            System.out.println("Class: " + className + " has comment");
//                        } else {
//                            System.out.println("Class: " + className + " does not have a valid comment");
//                        }
//                    }

                    //                    @Override
//                    public void visit(FieldDeclaration n, Object arg) {
//                        super.visit(n, arg);
//                        // cau4
////                        List<VariableDeclarator> variables = n.getVariables();
////                        for (VariableDeclarator variableDeclarator : variables) {
////                            String fieldName = variableDeclarator.getNameAsString();
////
////                            if (!isValidFieldName(fieldName)) {
////                                System.out.println("WARNING: Invalid field name - " + fieldName);
////                            }
////                        }
//
//                        // cau5
//                        // Kiểm tra xem trường có phải là hằng số không
////                        if (n.isFinal() && n.isStatic()) {
////                            // Kiểm tra xem trường có phải là hằng số viết hoa trong một interface không
////                            if (isValidConstantField(n)) {
////                                System.out.println("Constant field is valid: " + n.getVariables().get(0).getNameAsString());
////                            } else {
////                                System.out.println("WARNING: Invalid constant field: " + n.getVariables().get(0).getNameAsString());
////                            }
////                        }
//                    }
//
                    @Override
                    public void visit(MethodDeclaration n, Object arg) {
                        super.visit(n, arg);
                        String methodName = n.getNameAsString();
//                        // cau 6
//                        // Kiểm tra xem tên method có bắt đầu bằng một động từ và là chữ thường hay không
//                        if (!isValidMethodName(methodName)) {
//                            System.out.println("WARNING: Invalid method name - " + methodName);
//                        }
                        // cau 7
                        // Kiểm tra xem phương thức có nằm trong danh sách các phương thức cần bỏ qua hay không
                        if (!isExcludedMethod(n.getNameAsString())) {
                            // Kiểm tra xem phương thức có ghi chú mô tả hay không
                            if (!hasDescriptionComment(n)) {
                                System.out.println("WARNING: Method " + n.getNameAsString() + " is missing a description comment.");
                            }
                        }
                    }
                }.visit(StaticJavaParser.parse(file), null);
            } catch (Exception e) {
                new RuntimeException(e);
            }
        }).explore(projectDir);

    }

    //Check cau 2
    private boolean isValidClassName(String className) {
        // Kiểm tra xem tên lớp có bắt đầu bằng chữ hoa và là một danh từ hoặc cụm danh ngữ hay không
        return className.matches("[A-Z][a-zA-Z]*");
    }

    // cau3
    private boolean isCommented(ClassOrInterfaceDeclaration n) {
        Optional<Comment> optionalComment = n.getComment();
        if (optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            String commentContent = comment.getContent();

            // Kiểm tra xem comment có chứa "(created-date" và "author" hay không
            return commentContent.contains("created-date") && commentContent.contains("author");
        }
        return false;
    }

    // cau 4
    private boolean isValidFieldName(String fieldName) {
        // Kiểm tra xem tên trường có bắt đầu bằng chữ thường và là danh từ hoặc cụm danh ngữ hay không
        return fieldName.matches("[a-z][a-zA-Z]*");
    }

    // cau 5

    private boolean isValidConstantField(FieldDeclaration n) {
        // Kiểm tra xem trường có phải là hằng số viết hoa và nằm trong một interface không
        return isUpperCaseConstant(n) && isInsideInterface(n);
    }

    // Kiểm tra xem trường có phải là hằng số viết hoa không
    private boolean isUpperCaseConstant(FieldDeclaration n) {
        List<VariableDeclarator> variables = n.getVariables();
        for (VariableDeclarator variable : variables) {
            String constantName = variable.getNameAsString();
            if (!constantName.equals(constantName.toUpperCase())) {
                return false;
            }
        }
        return true;
    }

    // Kiểm tra xem trường có nằm trong một interface không
    private boolean isInsideInterface(FieldDeclaration n) {
        Node parentNode = n.getParentNode().orElse(null);
        return parentNode instanceof ClassOrInterfaceDeclaration && ((ClassOrInterfaceDeclaration) parentNode).isInterface();
    }

    // cau 6
    private boolean isValidMethodName(String methodName) {
        // Kiểm tra xem tên method có bắt đầu bằng một động từ và là chữ thường hay không
        return methodName.matches("[a-z][a-zA-Z]*");
    }
    // cau 7

    // Kiểm tra xem phương thức có nằm trong danh sách các phương thức cần bỏ qua hay không
    private boolean isExcludedMethod(String methodName) {
        String[] excludedMethods = {"<init>", "hashCode", "equals", "toString", "get", "set"}; // Thêm các phương thức cần bỏ qua vào đây

        for (String excludedMethod : excludedMethods) {
            if (methodName.startsWith(excludedMethod)) {
                return true;
            }
        }
        return false;
    }
    // Kiểm tra xem phương thức có ghi chú mô tả hay không
    private boolean hasDescriptionComment(MethodDeclaration n) {
        // Lấy tất cả các comment trong file
        List<Comment> allComments = n.findCompilationUnit().get().getAllComments();

        for (Comment comment : allComments) {
            // Kiểm tra xem comment có bắt đầu bằng "/***" không
            if (comment instanceof BlockComment && comment.getContent().startsWith("/***")) {
                // Kiểm tra xem comment có nằm trực tiếp trước phương thức không
                if (comment.getRange().get().end.line + 1 == n.getRange().get().begin.line) {
                    System.out.println("Method: " + n.getNameAsString());
                    System.out.println("Description: " + comment.getContent());
                    return true;
                }
            }

            // Kiểm tra xem comment có nằm trực tiếp trước phương thức không
            // và có chứa "Description" không
            if (comment instanceof LineComment && comment.getRange().get().end.line + 1 == n.getRange().get().begin.line
                    && comment.getContent().contains("/***")) {
                System.out.println("Method: " + n.getNameAsString());
                System.out.println("Description: " + comment.getContent());
                return true;
            }
        }

        return false;
    }
}
