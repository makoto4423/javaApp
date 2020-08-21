package com.app.pack0821;

import org.omg.CORBA.PUBLIC_MEMBER;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.*;
import javax.lang.model.util.ElementScanner8;
import javax.tools.Diagnostic;
import java.util.EnumSet;

public class NameChecker {

    private final Messager messager;
    NameCheckScanner nameCheckScanner = new NameCheckScanner();

    NameChecker(ProcessingEnvironment processingEnvironment) {
        this.messager = processingEnvironment.getMessager();
    }

    public void checkNames(Element element){
        nameCheckScanner.scan(element);
    }


    private class NameCheckScanner extends ElementScanner8<Void, Void> {
        public Void visitType(TypeElement e, Void p) {
            scan(e.getTypeParameters(), p);
            checkCamelCase(e, true);
            super.visitType(e, p);
            return null;
        }

        public Void visitExecutable(ExecutableElement e, Void p){
            if(e.getKind() == ElementKind.METHOD){
                Name name = e.getSimpleName();
                if(name.contentEquals(e.getEnclosingElement().getSimpleName())){
                    messager.printMessage(Diagnostic.Kind.WARNING, "method "+name+ " has the same class name",e);
                    checkCamelCase(e,false);
                }
            }
            super.visitExecutable(e,p);
            return null;
        }


        @Override
        public Void visitVariable(VariableElement e, Void unused) {
            if(e.getKind() == ElementKind.ENUM_CONSTANT || e.getConstantValue() != null || heuristicallyConstant(e)){
                checkAllCaps(e);
            }else{
                checkCamelCase(e, false);
            }
            return null;
        }

        private boolean heuristicallyConstant(VariableElement e){
            if(e.getEnclosingElement().getKind() == ElementKind.INTERFACE){
                return true;
            }else return e.getKind() == ElementKind.FIELD;
        }

        private void checkCamelCase(Element e, boolean initialCaps) {
            String name = e.getSimpleName().toString();
            boolean previousUpper = false;
            boolean conventional = true;
            int firstCodePoint = name.codePointAt(0);
            if (Character.isUpperCase(firstCodePoint)) {
                previousUpper = true;
                if (!initialCaps) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "name " + name + " should be head lower", e);
                    return;
                }
            } else if (Character.isLowerCase(firstCodePoint)) {
                if (initialCaps) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "name " + name + " should be head upper", e);
                    return;
                }
            } else {
                conventional = false;
            }

            if (conventional) {
                int cp = firstCodePoint;
                for (int i = Character.charCount(cp); i < name.length(); i += Character.charCount(cp)) {
                    cp = name.codePointAt(i);
                    if (Character.isUpperCase(cp)) {
                        if (previousUpper) {
                            conventional = false;
                            break;
                        }
                        previousUpper = true;
                    } else {
                        previousUpper = false;
                    }
                }
                if (!conventional) {
                    messager.printMessage(Diagnostic.Kind.WARNING, "name" + name + " should be config camel");
                }
            }
        }

        private void checkAllCaps(Element e){
            String name = e.getSimpleName().toString();
            boolean conventional = true;
            int firstCodePoint = name.codePointAt(0);
        }
    }

}
