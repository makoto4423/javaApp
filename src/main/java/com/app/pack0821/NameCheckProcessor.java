package com.app.pack0821;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class NameCheckProcessor extends AbstractProcessor {

    private NameChecker nameChecker;

    public void init(ProcessingEnvironment environment){
        super.init(environment);
        nameChecker = new NameChecker(environment);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(!roundEnv.processingOver()){
            for(Element element : roundEnv.getRootElements()){
                nameChecker.checkNames(element);
            }
        }
        return false;
    }
}
