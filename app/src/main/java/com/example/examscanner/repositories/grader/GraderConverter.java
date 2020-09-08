package com.example.examscanner.repositories.grader;

import com.example.examscanner.communication.entities_interfaces.GraderEntityInterface;
import com.example.examscanner.repositories.Converter;

class GraderConverter implements Converter<GraderEntityInterface, Grader> {
    @Override
    public Grader convert(GraderEntityInterface graderEntityInterface) {
        return new Grader(graderEntityInterface.getEmail(),graderEntityInterface.getId());
    }
}
